/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

package techreborn.tiles.multiblock;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import reborncore.api.IToolDrop;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.Inventory;
import techreborn.api.Reference;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileDistillationTower extends TilePowerAcceptor
		implements IToolDrop, IRecipeCrafterProvider, IInventoryProvider, IContainerProvider {

	@ConfigRegistry(config = "machines", category = "distillation_tower", key = "DistillationTowerMaxInput", comment = "Distillation Tower Max Input (Value in EU)")
	public static int maxInput = 128;
	@ConfigRegistry(config = "machines", category = "distillation_tower", key = "DistillationTowerMaxEnergy", comment = "Distillation Tower Max Energy (Value in EU)")
	public static int maxEnergy = 10000;
	
	public Inventory inventory = new Inventory(11, "TileDistillationTower", 64, this);
	public MultiblockChecker multiblockChecker;
	public RecipeCrafter crafter;

	public TileDistillationTower() {
		super();
		final int[] inputs = new int[] { 0, 1 };
		final int[] outputs = new int[] { 2, 3, 4, 5 };
		this.crafter = new RecipeCrafter(Reference.distillationTowerRecipe, this, 2, 4, this.inventory, inputs, outputs);
	}
	
	public boolean getMutliBlock() {
		if (this.multiblockChecker == null) {
			return false;
		}
		final boolean layer0 = this.multiblockChecker.checkRectY(1, 1, MultiblockChecker.STANDARD_CASING, MultiblockChecker.ZERO_OFFSET);
		final boolean layer1 = this.multiblockChecker.checkRingY(1, 1, MultiblockChecker.ADVANCED_CASING, new BlockPos(0, 1, 0));
		final boolean layer2 = this.multiblockChecker.checkRingY(1, 1, MultiblockChecker.STANDARD_CASING, new BlockPos(0, 2, 0));
		final boolean layer3 = this.multiblockChecker.checkRectY(1, 1, MultiblockChecker.ADVANCED_CASING, new BlockPos(0, 3, 0));
		final Block centerBlock1 = this.multiblockChecker.getBlock(0, 1, 0).getBlock();
		final Block centerBlock2 = this.multiblockChecker.getBlock(0, 2, 0).getBlock();
		final boolean center1 = (centerBlock1 == Blocks.AIR);
		final boolean center2 = (centerBlock2 == Blocks.AIR);
		return layer0 && layer1 && layer2 && layer3 && center1 && center2;
	}
	
	public int getProgressScaled(final int scale) {
		if (this.crafter.currentTickTime != 0) {
			return this.crafter.currentTickTime * scale / this.crafter.currentNeededTicks;
		}
		return 0;
	}
	
	@Override
	public void update() {
		if (this.multiblockChecker == null) {
			final BlockPos pos = this.getPos().offset(this.getFacing().getOpposite(), 2);
			this.multiblockChecker = new MultiblockChecker(this.world, pos);
		}
		
		if (world.isRemote){ return; }
		
		if (this.getMutliBlock()) {
			super.update();
			this.charge(6);
		}	
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing direction) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing direction) {
		return false;
	}

	@Override
	public double getBaseMaxPower() {
		return maxEnergy;
	}

	@Override
	public double getBaseMaxOutput() {
		return 0;
	}

	@Override
	public double getBaseMaxInput() {
		return maxInput;
	}

	@Override
	public void readFromNBT(final NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		this.crafter.readFromNBT(tagCompound);
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		this.crafter.writeToNBT(tagCompound);
		return tagCompound;
	}
	
	// IToolDrop
	@Override
	public ItemStack getToolDrop(EntityPlayer p0) {
		return new ItemStack(ModBlocks.DISTILLATION_TOWER, 1);
	}

	// IRecipeCrafterProvider
	@Override
	public RecipeCrafter getRecipeCrafter() {
		return this.crafter;
	}

	// IInventoryProvider
	@Override
	public IInventory getInventory() {
		return this.inventory;
	}

	// IContainerProvider
	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("distillationtower").player(player.inventory).inventory().hotbar().addInventory()
				.tile(this).slot(0, 35, 27).slot(1, 35, 47).outputSlot(2, 79, 37).outputSlot(3, 99, 37)
				.outputSlot(4, 119, 37).outputSlot(5, 139, 37).energySlot(6, 8, 72).syncEnergyValue().syncCrafterValue()
				.addInventory().create(this);
	}

}
