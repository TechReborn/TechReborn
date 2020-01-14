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

package techreborn.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import reborncore.api.tile.IUpgrade;
import reborncore.api.tile.IUpgradeable;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.recipes.IUpgradeHandler;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import techreborn.init.ModItems;
import techreborn.lib.ModInfo;
import techreborn.tiles.storage.TileAdjustableSU;

import javax.annotation.Nullable;
import java.security.InvalidParameterException;
import java.util.List;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class ItemUpgrades extends ItemTR implements IUpgrade {

	@ConfigRegistry(config = "items", category = "upgrades", key = "overclocker_speed", comment = "Overclocker upgrade speed multipiler")
	public static double overclockerSpeed = 0.25;

	@ConfigRegistry(config = "items", category = "upgrades", key = "overclocker_power", comment = "Overclocker upgrade power multipiler")
	public static double overclockerPower = 0.75;

	@ConfigRegistry(config = "items", category = "upgrades", key = "energy_storage", comment = "Energy storage upgrade extra power")
	public static double energyStoragePower = 40_000;

	public static final String[] types = new String[] { "overclock", "transformer", "energy_storage", "superconductor"};

	public ItemUpgrades() {
		setTranslationKey("techreborn.upgrade");
		setHasSubtypes(true);
		setMaxStackSize(16);
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

	// Item
	@Override
	public String getTranslationKey(ItemStack itemStack) {
		int meta = itemStack.getItemDamage();
		if (meta < 0 || meta >= types.length) {
			meta = 0;
		}

		return super.getTranslationKey() + "." + types[meta];
	}

	@Override
	public void getSubItems(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
		if (!isInCreativeTab(creativeTabs)) {
			return;
		}
		for (int meta = 0; meta < types.length; ++meta) {
			list.add(new ItemStack(this, 1, meta));
		}
	}

	// IUpgrade
	@Override
	public void process(IUpgradeHandler handler, ItemStack stack) {

		if (stack.getItemDamage() == 0) {
			handler.addSpeedMulti(overclockerSpeed);
			handler.addPowerMulti(overclockerPower);
			if(handler instanceof TilePowerAcceptor) {
				TilePowerAcceptor powerAcceptor = (TilePowerAcceptor) handler;
				powerAcceptor.extraPowerInput += powerAcceptor.getMaxInput();
				powerAcceptor.extraPowerStoage += powerAcceptor.getBaseMaxPower();
			}
		}
		if (handler instanceof TilePowerAcceptor) {
			if (stack.getItemDamage() == 2) {
				TilePowerAcceptor acceptor = (TilePowerAcceptor) handler;
				acceptor.extraPowerStoage += energyStoragePower;
			}
			if (stack.getItemDamage() == 1) {
				TilePowerAcceptor acceptor = (TilePowerAcceptor) handler;
				acceptor.extraTeir += 1;
			}
		}
		if(handler instanceof TileAdjustableSU){

		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleRightClick(TileEntity tile, ItemStack stack, Container container, int slotID) {

	}

	@Override
	public void addInformation(ItemStack stack,
	                           @Nullable
		                           World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if(stack.getItemDamage() == 3){
			tooltip.add(TextFormatting.LIGHT_PURPLE + "Increases the max transfer of the Adjustable SU");
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
				tooltip.add(TextFormatting.GOLD + "Blame obstinate_3 for this");
			}

		}
	}

	@Override
	public boolean isValidForInventory(IUpgradeable upgradeable, ItemStack stack) {
		if(stack.getItemDamage() == 3){
			return upgradeable instanceof TileAdjustableSU;
		}
		return true;
	}
}
