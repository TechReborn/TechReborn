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

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.api.tile.IUpgrade;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.recipes.IUpgradeHandler;
import reborncore.common.tile.TileLegacyMachineBase;
import reborncore.common.util.ItemNBTHelper;
import techreborn.Core;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.client.gui.upgrades.GuiSideConfig;
import techreborn.init.ModItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.security.InvalidParameterException;
import java.util.List;

public class ItemUpgrades extends ItemTR implements IUpgrade {

	public static final String[] types = new String[] { "overclock", "transformer", "energy_storage", "range" };

	public ItemUpgrades() {
		setUnlocalizedName("techreborn.upgrade");
		setHasSubtypes(true);
		setCreativeTab(TechRebornCreativeTabMisc.instance);
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
	public void getSubItems(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
		if (!isInCreativeTab(creativeTabs)) {
			return;
		}
		for (int meta = 0; meta < types.length; ++meta) {
			list.add(new ItemStack(this, 1, meta));
		}
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		super.addInformation(stack, world, tooltip, flag);
		if (stack.getItemDamage() == 4 || stack.getItemDamage() == 5) {
			tooltip.add("Facing: " + getFacing(stack).getName());
			String text = Core.proxy.getUpgradeConfigText();
			if (!text.isEmpty()) {
				tooltip.add(text);
			}
		}
	}

	@Override
	public void process(
		@Nonnull
			TileLegacyMachineBase machineBase,
		@Nullable
			IUpgradeHandler handler,
		@Nonnull
			ItemStack stack) {

		if (stack.getItemDamage() == 0) {
			handler.addSpeedMulti(0.25);
			handler.addPowerMulti(0.75);

		}
		if (machineBase instanceof TilePowerAcceptor) {
			if (stack.getItemDamage() == 2) {
				TilePowerAcceptor acceptor = (TilePowerAcceptor) machineBase;
				acceptor.extraPowerStoage += 40000;
			}
			if (stack.getItemDamage() == 1) {
				TilePowerAcceptor acceptor = (TilePowerAcceptor) machineBase;
				acceptor.extraTeir += 1;
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleRightClick(TileEntity tile, ItemStack stack, Container container, int slotID) {
		if (tile.getWorld().isRemote) {
			if (stack.getItemDamage() == 4 || stack.getItemDamage() == 5) {
				//TODO use the full gui handler
				Minecraft.getMinecraft().displayGuiScreen(new GuiSideConfig(Minecraft.getMinecraft().player, tile, getContainer(Minecraft.getMinecraft().player), slotID));
			}
		}
	}

	@SuppressWarnings("deprecation")
	@SideOnly(Side.CLIENT)
	public BuiltContainer getContainer(EntityPlayer player) {
		return new ContainerBuilder("sides").create();
	}

	public EnumFacing getFacing(ItemStack stack) {
		return EnumFacing.VALUES[ItemNBTHelper.getInt(stack, "side", 0)];
	}
}
