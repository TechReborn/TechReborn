/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.tiles.machine.multiblock;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.registration.RebornRegister;
import reborncore.common.registration.config.ConfigRegistry;
import reborncore.common.util.IInventoryAccess;
import reborncore.common.util.Inventory;
import reborncore.common.util.Tank;
import techreborn.TechReborn;
import techreborn.api.fluidreplicator.FluidReplicatorRecipeCrafter;
import techreborn.init.TRContent;
import techreborn.init.TRTileEntities;
import techreborn.tiles.TileGenericMachine;
import techreborn.utils.FluidUtils;

import javax.annotation.Nullable;

/**
 * @author drcrazy
 *
 */

@RebornRegister(TechReborn.MOD_ID)
public class TileFluidReplicator extends TileGenericMachine implements IContainerProvider {

	@ConfigRegistry(config = "machines", category = "fluidreplicator", key = "FluidReplicatorMaxInput", comment = "Fluid Replicator Max Input (Value in EU)")
	public static int maxInput = 256;
	@ConfigRegistry(config = "machines", category = "fluidreplicator", key = "FluidReplicatorMaxEnergy", comment = "Fluid Replicator Max Energy (Value in EU)")
	public static int maxEnergy = 400_000;

	public MultiblockChecker multiblockChecker;
	public static final int TANK_CAPACITY = 16_000;
	public Tank tank;
	int ticksSinceLastChange;

	public TileFluidReplicator() {
		super(TRTileEntities.FLUID_REPLICATOR, "FluidReplicator", maxInput, maxEnergy, TRContent.Machine.FLUID_REPLICATOR.block, 3);
		final int[] inputs = new int[] { 0 };
		this.inventory = new Inventory<>(4, "TileFluidReplicator", 64, this, getInventoryAccess());
		this.crafter = new FluidReplicatorRecipeCrafter(this, this.inventory, inputs, null);
		this.tank = new Tank("TileFluidReplicator", TileFluidReplicator.TANK_CAPACITY, this);
	}

	public boolean getMultiBlock() {
		if (multiblockChecker == null) {
			return false;
		}
		final boolean ring = multiblockChecker.checkRingY(1, 1, MultiblockChecker.REINFORCED_CASING,
				MultiblockChecker.ZERO_OFFSET);
		return ring;
	}

	// TileGenericMachine
	@Override
	public void tick() {
		if (multiblockChecker == null) {
			final BlockPos downCenter = pos.offset(getFacing().getOpposite(), 2);
			multiblockChecker = new MultiblockChecker(world, downCenter);
		}

		ticksSinceLastChange++;
		// Check cells input slot 2 time per second
		if (!world.isClient && ticksSinceLastChange >= 10) {
			if (!inventory.getInvStack(1).isEmpty()) {
				FluidUtils.fillContainers(tank, inventory, 1, 2, tank.getFluidType());
			}
			ticksSinceLastChange = 0;
		}

		if (getMultiBlock()) {
			super.tick();
		}

		tank.compareAndUpdate();
	}
	
	@Override
	public RecipeCrafter getRecipeCrafter() {
		return (RecipeCrafter) crafter;
	}

	// TilePowerAcceptor
	@Override
	public void fromTag(final CompoundTag tagCompound) {
		super.fromTag(tagCompound);
		tank.read(tagCompound);
	}

	@Override
	public CompoundTag toTag(final CompoundTag tagCompound) {
		super.toTag(tagCompound);
		tank.write(tagCompound);
		return tagCompound;
	}

	private static IInventoryAccess<TileFluidReplicator> getInventoryAccess(){
		return (slotID, stack, face, direction, tile) -> {
			if(slotID == 0){
				return stack.isEqualIgnoreTags(TRContent.Parts.UU_MATTER.getStack());
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
	public BuiltContainer createContainer(PlayerEntity player) {
		return new ContainerBuilder("fluidreplicator").player(player.inventory).inventory().hotbar().addInventory()
				.tile(this).fluidSlot(1, 124, 35).filterSlot(0, 55, 45, stack -> stack.isEqualIgnoreTags(TRContent.Parts.UU_MATTER.getStack()))
				.outputSlot(2, 124, 55).energySlot(3, 8, 72).syncEnergyValue().syncCrafterValue().addInventory()
				.create(this);
	}
}
