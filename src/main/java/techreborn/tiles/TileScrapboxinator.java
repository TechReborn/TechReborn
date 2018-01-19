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

package techreborn.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import reborncore.api.IToolDrop;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.Inventory;
import techreborn.api.recipe.ScrapboxRecipeCrafter;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.lib.ModInfo;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileScrapboxinator extends TilePowerAcceptor
	implements IToolDrop, IInventoryProvider, IContainerProvider, IRecipeCrafterProvider {

	@ConfigRegistry(config = "machines", category = "scrapboxinator", key = "ScrapboxinatorMaxInput", comment = "Scrapboxinator Max Input (Value in EU)")
	public static int maxInput = 32;
	@ConfigRegistry(config = "machines", category = "scrapboxinator", key = "ScrapboxinatorMaxEnergy", comment = "Scrapboxinator Max Energy (Value in EU)")
	public static int maxEnergy = 1000;

	public Inventory inventory = new Inventory(3, "TileScrapboxinator", 64, this);
	public RecipeCrafter crafter;

	public TileScrapboxinator() {
		super();
		final int[] inputs = new int[] { 0 };
		final int[] outputs = new int[] { 1 };
		this.crafter = new ScrapboxRecipeCrafter(this, this.inventory, inputs, outputs);
	}
	
	public int getProgressScaled(final int scale) {
		if (this.crafter.currentTickTime != 0 && this.crafter.currentNeededTicks > 0) {
			return this.crafter.currentTickTime * scale / this.crafter.currentNeededTicks;
		}
		return 0;
	}
	
	// TilePowerAcceptor
	@Override
	public void update() {
		if (!this.world.isRemote) {
			super.update();
			this.charge(2);
		}	
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
	public boolean canAcceptEnergy(final EnumFacing direction) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(final EnumFacing direction) {
		return false;
	}
	
	// TileLegacyMachineBase
	@Override
	public boolean canBeUpgraded() {
		return false;
	}
	
	// IToolDrop
	@Override
	public ItemStack getToolDrop(final EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.SCRAPBOXINATOR, 1);
	}

	//IInvetoryProvider
	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

	// IContainerProvider
	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("scrapboxinator").player(player.inventory).inventory().hotbar().addInventory()
				.tile(this).filterSlot(0, 55, 45, stack -> stack.getItem() == ModItems.SCRAP_BOX).outputSlot(1, 101, 45)
				.energySlot(2, 8, 72).syncEnergyValue().syncCrafterValue().addInventory().create(this);
	}

	// IRecipeCrafterProvider
	@Override
	public RecipeCrafter getRecipeCrafter() {
		return this.crafter;
	}
}