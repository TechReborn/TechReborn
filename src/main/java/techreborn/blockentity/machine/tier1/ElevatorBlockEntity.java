/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.blockentity.machine.tier1;

import org.jspecify.annotations.Nullable;
import reborncore.api.IToolDrop;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.RedstoneConfiguration;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ElevatorBlockEntity extends PowerAcceptorBlockEntity implements IToolDrop, BuiltScreenHandlerProvider {

	public ElevatorBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.ELEVATOR, pos, state);
	}

	/**
	 * @param targetPos the position of another elevator
	 */
	public boolean isRunning(final BlockPos targetPos) {
		// null-safe because of the following instanceof
		final BlockEntity entity = getLevel().getBlockEntity(targetPos);
		if (!(entity instanceof ElevatorBlockEntity)) {
			return false;
		}
		return ((ElevatorBlockEntity)entity).getStored() > 0;
	}

	/**
	 * @param targetPos the position will be checked to be an elevator or air
	 */
	public boolean isAirOrElevator(final BlockPos targetPos) {
		return getLevel().isEmptyBlock(targetPos) || getLevel().getBlockEntity(targetPos) instanceof ElevatorBlockEntity;
	}

	/**
	 * @param targetPos the position of another elevator
	 */
	public boolean isFree(final BlockPos targetPos) {
		return getLevel().getBlockState(targetPos.above()).isAir() && getLevel().getBlockState(targetPos.above().above()).isAir();
	}

	/**
	 * @param targetPos the position of another elevator
	 */
	public boolean isValidTarget(final BlockPos targetPos) {
		return isRunning(targetPos) && isFree(targetPos);
	}

	public Optional<BlockPos> nextUpElevator() {
		BlockPos upPos = getBlockPos().above().above();
		if (!TechRebornConfig.allowElevatingThroughBlocks.get() && (!isAirOrElevator(getBlockPos().above()) || !isAirOrElevator(getBlockPos().above().above()))) {
			return Optional.empty();
		}
		do {
			upPos = upPos.above();
			if (!TechRebornConfig.allowElevatingThroughBlocks.get() && !isAirOrElevator(upPos)) {
				return Optional.empty();
			}
		} while (upPos.getY() <= getLevel().getMaxY() && !isValidTarget(upPos));
		if (upPos.getY() < getLevel().getMaxY() || isValidTarget(upPos)) {
			return Optional.of(upPos);
		}
		return Optional.empty();
	}

	public Optional<BlockPos> nextDownElevator() {
		BlockPos downPos = getBlockPos().below().below();
		if (!TechRebornConfig.allowElevatingThroughBlocks.get() && (!isAirOrElevator(getBlockPos().below()) || !isAirOrElevator(getBlockPos().below().below()))) {
			return Optional.empty();
		}
		do {
			downPos = downPos.below();
			if (!TechRebornConfig.allowElevatingThroughBlocks.get() && !isAirOrElevator(downPos)) {
				return Optional.empty();
			}
		} while (downPos.getY() >= getLevel().getMinY() && !isValidTarget(downPos));
		if (downPos.getY() > getLevel().getMinY() || isValidTarget(downPos)) {
			return Optional.of(downPos);
		}
		return Optional.empty();
	}

	/**
	 * @param targetPos the position of another elevator
	 */
	public int energyCost(final BlockPos targetPos) {
		return Math.max(Math.abs(targetPos.getY()-getBlockPos().getY())*TechRebornConfig.elevatorEnergyPerBlock.get(),0);
	}

	/**
	 * @param targetPos the position <strong>over</strong> another elevator
	 */
	protected boolean teleport(final Player player, final BlockPos targetPos) {
		if (!(getLevel() instanceof ServerLevel)) {
			return false;
		}
		final int energy = energyCost(targetPos);
		if (getStored() < energy) {
			return false;
		}
		playTeleportSoundAt(getBlockPos());
		player.teleport(new TeleportTransition(
			(ServerLevel)getLevel(),
			Vec3.atBottomCenterOf(new Vec3i(targetPos.getX(), targetPos.getY(), targetPos.getZ())),
			Vec3.ZERO,
			player.getYRot(),
			player.getXRot(),
			TeleportTransition.DO_NOTHING
		));

		useEnergy(energy);
		playTeleportSoundAt(targetPos);
		return true;
	}

	protected void playTeleportSoundAt(final BlockPos targetPos) {
		getLevel().playSound(null, targetPos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1f, 1f);
	}

	public void teleportUp(final Player player) {
		if (!this.worldPosition.closerToCenterThan(player.position(), 5) && player.level() == this.level) {
			// Ensure the player is close to the elevator and in the same world.
			return;
		}

		Optional<BlockPos> upTarget = nextUpElevator();
		if (upTarget.isEmpty()) {
			return;
		}
		if (teleport(player, upTarget.get().above())) {
			player.setJumping(false);
		}
	}

	// PowerAcceptorBlockEntity
	@Override
	public void tick(Level level, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(level, pos, state, blockEntity);
		if (!(level instanceof ServerLevel serverLevel) || getStored() <= 0 || !isActive(RedstoneConfiguration.Element.POWER_IO)) {
			return;
		}

		// teleporting up must be done via mixin for now
		Optional<BlockPos> downTarget = null;

		List<Player> players = serverLevel.getEntitiesOfClass(Player.class, new AABB(0d,1d,0d,1d,2d,1d).move(pos));
		if (players.size() == 0) {
			return;
		}
		for (Player player : players) {
			if (player.isShiftKeyDown()) {
				if (downTarget == null) {
					downTarget = nextDownElevator();
				}
				if (downTarget.isEmpty()) {
					continue;
				}
				if (teleport(player, downTarget.get().above())) {
					player.setShiftKeyDown(false);
				}
			}
		}
	}
	@Override
	public long getBaseMaxPower() {
		return TechRebornConfig.elevatorMaxEnergy.get();
	}

	@Override
	public boolean canProvideEnergy(@Nullable Direction side) {
		return false;
	}

	@Override
	public long getBaseMaxOutput() {
		return 0;
	}

	@Override
	public long getBaseMaxInput() {
		return TechRebornConfig.elevatorMaxInput.get();
	}

	// MachineBaseBlockEntity
	@Override
	public boolean hasSlotConfig() {
		return false;
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(Player p0) {
		return TRContent.Machine.ELEVATOR.getStack();
	}

	// BuiltScreenHandlerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, Player player) {
		return new ScreenHandlerBuilder("elevator")
				.player(player.getInventory())
				.inventory().hotbar().addInventory()
				.blockEntity(this)
				.syncEnergyValue()
				.addInventory().create(this, syncID);
	}
}
