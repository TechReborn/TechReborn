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

package techreborn.blockentity.machine.multiblock;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;
import reborncore.client.screen.BuiltScreenHandlerProvider;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.client.screen.builder.ScreenHandlerBuilder;
import reborncore.common.blockentity.MultiblockWriter;
import reborncore.common.fluid.FluidValue;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.IInventoryAccess;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.Tank;
import techreborn.blockentity.machine.GenericMachineBlockEntity;
import techreborn.config.TechRebornConfig;
import techreborn.init.ModRecipes;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;
import techreborn.utils.FluidUtils;

import org.jetbrains.annotations.Nullable;

/**
 * @author drcrazy
 */
public class FluidReplicatorBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider {

	public static final FluidValue TANK_CAPACITY = FluidValue.BUCKET.multiply(16);
	public Tank tank;
	int ticksSinceLastChange;

	public FluidReplicatorBlockEntity() {
		super(TRBlockEntities.FLUID_REPLICATOR, "FluidReplicator", TechRebornConfig.fluidReplicatorMaxInput, TechRebornConfig.fluidReplicatorMaxEnergy, TRContent.Machine.FLUID_REPLICATOR.block, 3);
		this.inventory = new RebornInventory<>(4, "FluidReplicatorBlockEntity", 64, this, getInventoryAccess());
		this.crafter = new RecipeCrafter(ModRecipes.FLUID_REPLICATOR, this, 1, 0, this.inventory, new int[]{0}, null);
		this.tank = new Tank("FluidReplicatorBlockEntity", FluidReplicatorBlockEntity.TANK_CAPACITY, this);
	}

	@Override
	public void writeMultiblock(MultiblockWriter writer) {
		BlockState state = TRContent.MachineBlocks.ADVANCED.getCasing().getDefaultState();
		writer.translate(1, 0, -1)
				.ring(Direction.Axis.Y, 3, 0, 3, (v, p) -> v.getBlockState(p) == state, state, null, null);
	}

	// TileGenericMachine
	@Override
	public void tick() {
		if (world == null){
			return;
		}
		ticksSinceLastChange++;
		// Check cells input slot 2 time per second
		if (!world.isClient && ticksSinceLastChange >= 10) {
			if (!inventory.getStack(1).isEmpty()) {
				FluidUtils.fillContainers(tank, inventory, 1, 2);
				if (tank.isEmpty()){
					// need to set to empty fluid due to #2352
					tank.setFluid(Fluids.EMPTY);
				}
			}
			ticksSinceLastChange = 0;
		}

		super.tick();
	}

	@Override
	public RecipeCrafter getRecipeCrafter() {
		return crafter;
	}

	// TilePowerAcceptor
	@Override
	public void fromTag(BlockState blockState, final CompoundTag tagCompound) {
		super.fromTag(blockState, tagCompound);
		tank.read(tagCompound);
	}

	@Override
	public CompoundTag toTag(final CompoundTag tagCompound) {
		super.toTag(tagCompound);
		tank.write(tagCompound);
		return tagCompound;
	}

	private static IInventoryAccess<FluidReplicatorBlockEntity> getInventoryAccess() {
		return (slotID, stack, face, direction, blockEntity) -> {
			if (slotID == 0) {
				return stack.isItemEqualIgnoreDamage(TRContent.Parts.UU_MATTER.getStack());
			}
			return true;
		};
	}

	// TileMachineBase
	@Nullable
	@Override
	public Tank getTank() {
		return tank;
	}

	// IContainerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
		return new ScreenHandlerBuilder("fluidreplicator").player(player.inventory).inventory().hotbar().addInventory()
				.blockEntity(this).fluidSlot(1, 124, 35).filterSlot(0, 55, 45, stack -> stack.isItemEqualIgnoreDamage(TRContent.Parts.UU_MATTER.getStack()))
				.outputSlot(2, 124, 55).energySlot(3, 8, 72).sync(tank).syncEnergyValue().syncCrafterValue().addInventory()
				.create(this, syncID);
	}
}
