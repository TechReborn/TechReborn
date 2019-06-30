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

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.Material;
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
import reborncore.common.util.RebornInventory;
import reborncore.common.util.Tank;
import techreborn.TechReborn;
import techreborn.init.ModRecipes;
import techreborn.init.TRContent;
import techreborn.init.TRTileEntities;
import techreborn.tiles.TileGenericMachine;
import techreborn.utils.FluidUtils;

import javax.annotation.Nullable;

@RebornRegister(TechReborn.MOD_ID)
public class TileIndustrialGrinder extends TileGenericMachine implements IContainerProvider{
	
	@ConfigRegistry(config = "machines", category = "industrial_grinder", key = "IndustrialGrinderMaxInput", comment = "Industrial Grinder Max Input (Value in EU)")
	public static int maxInput = 128;
	@ConfigRegistry(config = "machines", category = "industrial_grinder", key = "IndustrialGrinderMaxEnergy", comment = "Industrial Grinder Max Energy (Value in EU)")
	public static int maxEnergy = 10_000;
	
	public static final int TANK_CAPACITY = 16_000;
	public Tank tank;
	public MultiblockChecker multiblockChecker;
	int ticksSinceLastChange;

	public TileIndustrialGrinder() {
		super(TRTileEntities.INDUSTRIAL_GRINDER, "IndustrialGrinder", maxInput, maxEnergy, TRContent.Machine.INDUSTRIAL_GRINDER.block, 7);
		final int[] inputs = new int[] { 0, 1 };
		final int[] outputs = new int[] {2, 3, 4, 5};
		this.inventory = new RebornInventory<>(8, "TileIndustrialGrinder", 64, this, getInventoryAccess());
		this.crafter = new RecipeCrafter(ModRecipes.INDUSTRIAL_GRINDER, this, 1, 4, this.inventory, inputs, outputs);
		this.tank = new Tank("TileIndustrialGrinder", TileIndustrialGrinder.TANK_CAPACITY, this);
		this.ticksSinceLastChange = 0;
	}

	public boolean getMultiBlock() {
		if (multiblockChecker == null) {
			return false;
		}
		final boolean down = multiblockChecker.checkRectY(1, 1, MultiblockChecker.STANDARD_CASING, MultiblockChecker.ZERO_OFFSET);
		final boolean up = multiblockChecker.checkRectY(1, 1, MultiblockChecker.STANDARD_CASING, new BlockPos(0, 2, 0));
		final boolean blade = multiblockChecker.checkRingY(1, 1, MultiblockChecker.REINFORCED_CASING, new BlockPos(0, 1, 0));
		final BlockState centerBlock = multiblockChecker.getBlock(0, 1, 0);
		final boolean center = ((centerBlock.getBlock() instanceof FluidBlock
				|| centerBlock.getBlock() instanceof FluidBlock)
				&& centerBlock.getMaterial() == Material.WATER);
		return down && center && blade && up;
	}
	
	private static IInventoryAccess<TileIndustrialGrinder> getInventoryAccess(){
		return (slotID, stack, face, direction, tile) -> {
			if(slotID == 1){
				//TODO check if the item has fluid in it
				//return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).isPresent();
			}
			return true;
		};
	}

	// TilePowerAcceptor
	@Override
	public void tick() {
		if (multiblockChecker == null) {
			final BlockPos downCenter = pos.offset(getFacing().getOpposite(), 2).down();
			multiblockChecker = new MultiblockChecker(world, downCenter);
		}
		
		ticksSinceLastChange++;
		// Check cells input slot 2 time per second
		if (!world.isClient && ticksSinceLastChange >= 10) {
			if (!inventory.getInvStack(1).isEmpty()) {
				FluidUtils.drainContainers(tank, inventory, 1, 6);
				FluidUtils.fillContainers(tank, inventory, 1, 6, tank.getFluidType());
			}
			ticksSinceLastChange = 0;
		}
		
		if (!world.isClient && getMultiBlock()) {
			super.tick();
		}

		tank.compareAndUpdate();
	}
	
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

	// TileMachineBase
	@Nullable
	@Override
	public Tank getTank() {
		return tank;
	}

	// IContainerProvider
	@Override
	public BuiltContainer createContainer(final PlayerEntity player) {
		// fluidSlot first to support automation and shift-click
		return new ContainerBuilder("industrialgrinder").player(player.inventory).inventory().hotbar().addInventory()
				.tile(this).fluidSlot(1, 34, 35).slot(0, 84, 43).outputSlot(2, 126, 18).outputSlot(3, 126, 36)
				.outputSlot(4, 126, 54).outputSlot(5, 126, 72).outputSlot(6, 34, 55).energySlot(7, 8, 72)
				.syncEnergyValue().syncCrafterValue().addInventory().create(this);
	}

}