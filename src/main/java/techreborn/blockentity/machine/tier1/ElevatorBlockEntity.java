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

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
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

public class ElevatorBlockEntity extends PowerAcceptorBlockEntity implements IToolDrop, BuiltScreenHandlerProvider {

	public ElevatorBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.ELEVATOR, pos, state);
	}

	/**
	 * @param targetPos the position of another elevator
	 */
	public boolean isRunning(final BlockPos targetPos) {
		// null-safe because of the following instanceof
		final BlockEntity entity = getWorld().getBlockEntity(targetPos);
		if (!(entity instanceof ElevatorBlockEntity)) {
			return false;
		}
		return ((ElevatorBlockEntity)entity).getStored() > 0;
	}

	/**
	 * @param targetPos the position will be checked to be an elevator or air
	 */
	public boolean isAirOrElevator(final BlockPos targetPos) {
		return getWorld().isAir(targetPos) || getWorld().getBlockEntity(targetPos) instanceof ElevatorBlockEntity;
	}

	/**
	 * @param targetPos the position of another elevator
	 */
	public boolean isFree(final BlockPos targetPos) {
		return getWorld().getBlockState(targetPos.up()).isAir() && getWorld().getBlockState(targetPos.up().up()).isAir();
	}

	/**
	 * @param targetPos the position of another elevator
	 */
	public boolean isValidTarget(final BlockPos targetPos) {
		return isRunning(targetPos) && isFree(targetPos);
	}

	public Optional<BlockPos> nextUpElevator() {
		BlockPos upPos = getPos().up().up();
		if (!TechRebornConfig.allowElevatingThroughBlocks && (!isAirOrElevator(getPos().up()) || !isAirOrElevator(getPos().up().up()))) {
			return Optional.empty();
		}
		do {
			upPos = upPos.up();
			if (!TechRebornConfig.allowElevatingThroughBlocks && !isAirOrElevator(upPos)) {
				return Optional.empty();
			}
		} while (upPos.getY() <= getWorld().getTopY() && !isValidTarget(upPos));
		if (upPos.getY() < getWorld().getTopY() || isValidTarget(upPos)) {
			return Optional.of(upPos);
		}
		return Optional.empty();
	}

	public Optional<BlockPos> nextDownElevator() {
		BlockPos downPos = getPos().down().down();
		if (!TechRebornConfig.allowElevatingThroughBlocks && (!isAirOrElevator(getPos().down()) || !isAirOrElevator(getPos().down().down()))) {
			return Optional.empty();
		}
		do {
			downPos = downPos.down();
			if (!TechRebornConfig.allowElevatingThroughBlocks && !isAirOrElevator(downPos)) {
				return Optional.empty();
			}
		} while (downPos.getY() >= getWorld().getBottomY() && !isValidTarget(downPos));
		if (downPos.getY() > getWorld().getBottomY() || isValidTarget(downPos)) {
			return Optional.of(downPos);
		}
		return Optional.empty();
	}

	/**
	 * @param targetPos the position of another elevator
	 */
	public int energyCost(final BlockPos targetPos) {
		return Math.max(Math.abs(targetPos.getY()-getPos().getY())*TechRebornConfig.elevatorEnergyPerBlock,0);
	}

	/**
	 * @param targetPos the position <strong>over</strong> another elevator
	 */
	protected boolean teleport(final PlayerEntity player, final BlockPos targetPos) {
		if (!(getWorld() instanceof ServerWorld)) {
			return false;
		}
		final int energy = energyCost(targetPos);
		if (getStored() < energy) {
			return false;
		}
		playTeleportSoundAt(getPos());
		FabricDimensions.teleport(player, (ServerWorld)getWorld(),
			new TeleportTarget(Vec3d.ofBottomCenter(new Vec3i(targetPos.getX(), targetPos.getY(), targetPos.getZ())), Vec3d.ZERO, player.getYaw(), player.getPitch()));
		useEnergy(energy);
		playTeleportSoundAt(targetPos);
		return true;
	}

	protected void playTeleportSoundAt(final BlockPos targetPos) {
		getWorld().playSound(null, targetPos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1f, 1f);
	}

	public void teleportUp(final PlayerEntity player) {
		if (!this.pos.isWithinDistance(player.getPos(), 5) && player.world == this.world) {
			// Ensure the player is close to the elevator and in the same world.
			return;
		}

		Optional<BlockPos> upTarget = nextUpElevator();
		if (upTarget.isEmpty()) {
			return;
		}
		if (teleport(player, upTarget.get().up())) {
			player.setJumping(false);
		}
	}

	// PowerAcceptorBlockEntity
	@Override
	public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);
		if (!(world instanceof ServerWorld) || getStored() <= 0 || !isActive(RedstoneConfiguration.POWER_IO)) {
			return;
		}

		// teleporting up must be done via mixin for now
		Optional<BlockPos> downTarget = null;

		List<PlayerEntity> players = world.getNonSpectatingEntities(PlayerEntity.class, new Box(0d,1d,0d,1d,2d,1d).offset(pos));
		if (players.size() == 0) {
			return;
		}
		for (PlayerEntity player : players) {
			if (player.isSneaking()) {
				if (downTarget == null) {
					downTarget = nextDownElevator();
				}
				if (downTarget.isEmpty()) {
					continue;
				}
				if (teleport(player, downTarget.get().up())) {
					player.setSneaking(false);
				}
			}
		}
	}
	@Override
	public long getBaseMaxPower() {
		return TechRebornConfig.elevatorMaxEnergy;
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
		return TechRebornConfig.elevatorMaxInput;
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
	public ItemStack getToolDrop(PlayerEntity p0) {
		return TRContent.Machine.ELEVATOR.getStack();
	}

	// BuiltScreenHandlerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
		return new ScreenHandlerBuilder("elevator")
				.player(player.getInventory())
				.inventory().hotbar().addInventory()
				.blockEntity(this)
				.syncEnergyValue()
				.addInventory().create(this, syncID);
	}
}
