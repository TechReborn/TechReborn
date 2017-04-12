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

package techreborn.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import reborncore.api.tile.IUpgrade;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.tile.TileLegacyMachineBase;
import reborncore.common.util.InventoryHelper;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.init.ModItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.security.InvalidParameterException;
import java.util.List;

public class ItemUpgrades extends ItemTRNoDestroy implements IUpgrade {

	public static final String[] types = new String[] { "overclock", "transformer", "energy_storage", "range", "ejection", "injection" };

	public ItemUpgrades() {
		setUnlocalizedName("techreborn.upgrade");
		setHasSubtypes(true);
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		setMaxStackSize(1);
	}

	public static ItemStack getUpgradeByName(String name, int count) {
		for (int i = 0; i < types.length; i++) {
			if (types[i].equalsIgnoreCase(name)) {
				return new ItemStack(ModItems.UPGRADES, count, i);
			}
		}
		throw new InvalidParameterException("The upgrade " + name + " could not be found.");
	}

	public static ItemStack getUpgradeByName(String name) {
		return getUpgradeByName(name, 1);
	}

	@Override
	// gets Unlocalized Name depending on meta data
	public String getUnlocalizedName(ItemStack itemStack) {
		int meta = itemStack.getItemDamage();
		if (meta < 0 || meta >= types.length) {
			meta = 0;
		}

		return super.getUnlocalizedName() + "." + types[meta];
	}

	// Adds Dusts SubItems To Creative Tab
	@Override
	public void getSubItems(Item item, CreativeTabs creativeTabs, NonNullList list) {
		for (int meta = 0; meta < types.length; ++meta) {
			list.add(new ItemStack(item, 1, meta));
		}
	}

	@Override
	public void process(
		@Nonnull
			TileLegacyMachineBase machineBase,
		@Nullable
			RecipeCrafter crafter,
		@Nonnull
			ItemStack stack) {
		if(crafter != null){
			if (stack.getItemDamage() == 0) {
				crafter.addSpeedMulti(0.25);
				crafter.addPowerMulti(0.5);
			} else if(stack.getItemDamage() == 4){
				EnumFacing dir = EnumFacing.NORTH;
				TileEntity tileEntity = machineBase.getWorld().getTileEntity(machineBase.getPos().offset(dir));
				if(tileEntity instanceof IInventory){
					for(Integer outSlot : crafter.outputSlots){
						ItemStack outputStack = crafter.inventory.getStackInSlot(outSlot);
						if(!outputStack.isEmpty()){
							int amount = InventoryHelper.testInventoryInsertion((IInventory) tileEntity, outputStack, dir);
							if(amount > 0){
								InventoryHelper.insertItemIntoInventory((IInventory) tileEntity,outputStack);
								crafter.inventory.decrStackSize(outSlot, amount);
							}
						}
					}
				}
			} else if (stack.getItemDamage() == 5){
				if(machineBase.getWorld().getTotalWorldTime() % 10 != 0){
					return;
				}
				EnumFacing dir = EnumFacing.UP;
				TileEntity tileEntity = machineBase.getWorld().getTileEntity(machineBase.getPos().offset(dir));
				if(tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir.getOpposite())){
					IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir.getOpposite());
					for (int i = 0; i < itemHandler.getSlots(); i++) {
						ItemStack stack1 = itemHandler.getStackInSlot(i);
						if(crafter.isStackValidInput(stack1)){
							ItemStack extractedStack = itemHandler.extractItem(i, 1, true);
							int amount = InventoryHelper.testInventoryInsertion(machineBase, extractedStack, null);
							if(amount > 0){
								extractedStack = itemHandler.extractItem(i, 1, false);
								extractedStack.setCount(1);
								InventoryHelper.insertItemIntoInventory(machineBase , extractedStack);
							}
						}
					}
				}
			}
		}
		if(machineBase instanceof TilePowerAcceptor){
			if (stack.getItemDamage() == 2) {
				TilePowerAcceptor acceptor = (TilePowerAcceptor) machineBase;
				acceptor.extraPowerStoage += acceptor.getBaseMaxPower();
			}
		}
	}

	@Override
	public void handleRightClick(TileEntity tile, ItemStack stack, BuiltContainer container) {
		if(stack.getItemDamage() == 4){
			System.out.println("open a gui here");
		} else if(stack.getItemDamage() == 5){
			System.out.println("open a gui here");
		}
	}
}