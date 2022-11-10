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

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.api.IToolDrop;
import reborncore.common.blockentity.RedstoneConfiguration;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import java.util.function.BiFunction;

public class ElevatorBlockEntity extends PowerAcceptorBlockEntity implements IToolDrop, BuiltScreenHandlerProvider {

	public static final int MIN_HEIGHT = -64;
	public static final int MAX_HEIGHT = 319;

	public ElevatorBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.ELEVATOR, pos, state);
	}

	public static boolean isRunning(final World world, final BlockPos pos) {
		final BlockEntity entity = world.getBlockEntity(pos);
		if (!(entity instanceof ElevatorBlockEntity)) {
			return false;
		}
		return ((ElevatorBlockEntity)entity).getStored() > 0;
	}

	public static boolean isFree(final World world, final BlockPos pos) {
		return world.getBlockState(pos.up()).isAir() && world.getBlockState(pos.up().up()).isAir();
	}

	public static boolean isValidTarget(final World world, final BlockPos pos) {
		return isRunning(world, pos) && isFree(world, pos);
	}

	public static BlockPos nextUpElevator(final World world, final BlockPos pos) {
		BlockPos upPos = pos.up().up();
		do {
			upPos = upPos.up();
		} while (upPos.getY() <= MAX_HEIGHT && !isValidTarget(world, upPos));
		if (upPos.getY() < MAX_HEIGHT || isValidTarget(world, upPos)) {
			return upPos;
		}
		return null;
	}

	public static BlockPos nextDownElevator(final World world, final BlockPos pos) {
		BlockPos downPos = pos.down().down();
		do {
			downPos = downPos.down();
		} while (downPos.getY() >= MIN_HEIGHT && !isValidTarget(world, downPos));
		if (downPos.getY() > MIN_HEIGHT || isValidTarget(world, downPos)) {
			return downPos;
		}
		return null;
	}

	public static int energyCost(BlockPos startPos, BlockPos endPos) {
		return Math.min(Math.abs(endPos.getY()-startPos.getY())*TechRebornConfig.elevatorEnergyPerBlock,0);
	}

	public void teleportUp(World world, BlockPos pos, PlayerEntity player) {
		teleport(world, pos, player, ElevatorBlockEntity::nextUpElevator);
	}

	public void teleportDown(World world, BlockPos pos, PlayerEntity player) {
		teleport(world, pos, player, ElevatorBlockEntity::nextDownElevator);
	}

	protected void teleport(World world, BlockPos pos, PlayerEntity player, BiFunction<World, BlockPos, BlockPos> targetCalculator) {
		if (world == null || !isActive(RedstoneConfiguration.POWER_IO)) {
			return;
		}
		BlockPos targetPos = targetCalculator.apply(world, pos);
		if (targetPos == null) {
			return;
		}
		final int energy = energyCost(pos, targetPos);
		if (getStored() < energy) {
			return;
		}
		world.playSound(null, pos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1f, 1f);
		player.teleport(targetPos.getX(), targetPos.getY(), targetPos.getZ(), true);
		useEnergy(energy);
	}
	// world.playSound(null, pos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1f, 1f);

	// PowerAcceptorBlockEntity
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
