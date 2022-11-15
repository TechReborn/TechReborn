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

	public static final int MIN_HEIGHT = -64;
	public static final int MAX_HEIGHT = 319;

	public ElevatorBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.ELEVATOR, pos, state);
	}

	public boolean isRunning(final World world, final BlockPos targetPos) {
		final BlockEntity entity = world.getBlockEntity(targetPos);
		if (!(entity instanceof ElevatorBlockEntity)) {
			return false;
		}
		return ((ElevatorBlockEntity)entity).getStored() > 0;
	}

	public boolean isFree(final World world, final BlockPos targetPos) {
		return world.getBlockState(targetPos.up()).isAir() && world.getBlockState(targetPos.up().up()).isAir();
	}

	public boolean isValidTarget(final World world, final BlockPos targetPos) {
		return isRunning(world, targetPos) && isFree(world, targetPos);
	}

	public Optional<BlockPos> nextUpElevator(final World world) {
		BlockPos upPos = getPos().up().up();
		do {
			upPos = upPos.up();
		} while (upPos.getY() <= MAX_HEIGHT && !isValidTarget(world, upPos));
		if (upPos.getY() < MAX_HEIGHT || isValidTarget(world, upPos)) {
			return Optional.of(upPos);
		}
		return Optional.empty();
	}

	public Optional<BlockPos> nextDownElevator(final World world) {
		BlockPos downPos = getPos().down().down();
		do {
			downPos = downPos.down();
		} while (downPos.getY() >= MIN_HEIGHT && !isValidTarget(world, downPos));
		if (downPos.getY() > MIN_HEIGHT || isValidTarget(world, downPos)) {
			return Optional.of(downPos);
		}
		return Optional.empty();
	}

	public int energyCost(BlockPos targetPos) {
		return Math.max(Math.abs(targetPos.getY()-getPos().getY())*TechRebornConfig.elevatorEnergyPerBlock,0);
	}

	protected boolean teleport(World world, BlockPos pos, PlayerEntity player, BlockPos targetPos) {
		if (!(world instanceof ServerWorld)) {
			return false;
		}
		final int energy = energyCost(targetPos);
		if (getStored() < energy) {
			return false;
		}
		world.playSound(null, getPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1f, 1f);
		FabricDimensions.teleport(player, (ServerWorld)world,
			new TeleportTarget(Vec3d.ofBottomCenter(new Vec3i(targetPos.getX(), targetPos.getY(), targetPos.getZ())), Vec3d.ZERO, player.getYaw(), player.getPitch()));
		useEnergy(energy);
		return true;
	}

	public void teleportUp(World world, BlockPos pos, PlayerEntity player) {
		Optional<BlockPos> upTarget = nextUpElevator(world);
		if (upTarget.isEmpty()) {
			return;
		}
		if (teleport(world, pos, player, upTarget.get().up())) {
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
					downTarget = nextDownElevator(world);
				}
				if (downTarget.isEmpty()) {
					continue;
				}
				if (teleport(world, pos, player, downTarget.get().up())) {
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
