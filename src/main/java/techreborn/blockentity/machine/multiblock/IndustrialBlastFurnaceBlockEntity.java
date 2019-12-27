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

package techreborn.blockentity.machine.multiblock;

import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.multiblock.IMultiblockPart;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.RebornInventory;
import techreborn.blockentity.machine.GenericMachineBlockEntity;
import techreborn.blockentity.machine.multiblock.casing.MachineCasingBlockEntity;
import techreborn.blocks.BlockMachineCasing;
import techreborn.config.TechRebornConfig;
import techreborn.init.ModRecipes;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;
import techreborn.multiblocks.MultiBlockCasing;

public class IndustrialBlastFurnaceBlockEntity extends GenericMachineBlockEntity implements IContainerProvider {

	public MultiblockChecker multiblockChecker;
	private int cachedHeat;

	public IndustrialBlastFurnaceBlockEntity() {
		super(TRBlockEntities.INDUSTRIAL_BLAST_FURNACE, "IndustrialBlastFurnace", TechRebornConfig.industrialBlastFurnaceMaxInput, TechRebornConfig.industrialBlastFurnaceMaxEnergy, TRContent.Machine.INDUSTRIAL_BLAST_FURNACE.block, 4);
		final int[] inputs = new int[] { 0, 1 };
		final int[] outputs = new int[] { 2, 3 };
		this.inventory = new RebornInventory<>(5, "IndustrialBlastFurnaceBlockEntity", 64, this);
		this.crafter = new RecipeCrafter(ModRecipes.BLAST_FURNACE, this, 2, 2, this.inventory, inputs, outputs);
	}
	
	public int getHeat() {
		if (!getMutliBlock()){
			return 0;
		}
		
		// Bottom center of multiblock
		final BlockPos location = pos.offset(getFacing().getOpposite(), 2);
		final BlockEntity blockEntity = world.getBlockEntity(location);

		if (blockEntity instanceof MachineCasingBlockEntity) {
			if (((MachineCasingBlockEntity) blockEntity).isConnected()
					&& ((MachineCasingBlockEntity) blockEntity).getMultiblockController().isAssembled()) {
				final MultiBlockCasing casing = ((MachineCasingBlockEntity) blockEntity).getMultiblockController();

				int heat = 0;

				// Bottom center shouldn't have any blockEntity entities below it
				if (world.getBlockState(new BlockPos(location.getX(), location.getY() - 1, location.getZ()))
						.getBlock() == blockEntity.getWorld().getBlockState(blockEntity.getPos()).getBlock()) {
					return 0;
				}

				for (final IMultiblockPart part : casing.connectedParts) {
					heat += BlockMachineCasing.getHeatFromState(part.getCachedState());
				}

				if (world.getBlockState(location.offset(Direction.UP, 1)).getMaterial().equals(Material.LAVA)
						&& world.getBlockState(location.offset(Direction.UP, 2)).getMaterial().equals(Material.LAVA)) {
					heat += 500;
				}
				return heat;
			}
		}

		return 0;
	}
	
	public boolean getMutliBlock() {
		if(multiblockChecker == null){
			return false;
		}
		final boolean layer0 = multiblockChecker.checkRectY(1, 1, MultiblockChecker.CASING_ANY, MultiblockChecker.ZERO_OFFSET);
		final boolean layer1 = multiblockChecker.checkRingY(1, 1, MultiblockChecker.CASING_ANY, new BlockPos(0, 1, 0));
		final boolean layer2 = multiblockChecker.checkRingY(1, 1, MultiblockChecker.CASING_ANY, new BlockPos(0, 2, 0));
		final boolean layer3 = multiblockChecker.checkRectY(1, 1, MultiblockChecker.CASING_ANY, new BlockPos(0, 3, 0));
		final Material centerBlock1 = multiblockChecker.getBlock(0, 1, 0).getMaterial();
		final Material centerBlock2 = multiblockChecker.getBlock(0, 2, 0).getMaterial();
		final boolean center1 = (centerBlock1 == Material.AIR || centerBlock1 == Material.LAVA);
		final boolean center2 = (centerBlock2 == Material.AIR || centerBlock2 == Material.LAVA);
		return layer0 && layer1 && layer2 && layer3 && center1 && center2;
	}
	
	public void setHeat(final int heat) {
		cachedHeat = heat;
	}

	public int getCachedHeat() {
		return cachedHeat;
	}
	
	// TileGenericMachine
	@Override
	public void tick() {
		if (multiblockChecker == null) {
			final BlockPos downCenter = pos.offset(getFacing().getOpposite(), 2);
			multiblockChecker = new MultiblockChecker(world, downCenter);
		}
		super.tick();
	}
	
	// IContainerProvider
	@Override
	public BuiltContainer createContainer(int syncID, final PlayerEntity player) {
		return new ContainerBuilder("blastfurnace").player(player.inventory).inventory().hotbar().addInventory()
				.blockEntity(this).slot(0, 50, 27).slot(1, 50, 47).outputSlot(2, 93, 37).outputSlot(3, 113, 37)
				.energySlot(4, 8, 72).syncEnergyValue().syncCrafterValue()
				.sync(this::getHeat, this::setHeat).addInventory().create(this, syncID);
	}

}
