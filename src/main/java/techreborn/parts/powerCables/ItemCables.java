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

package techreborn.parts.powerCables;

import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.RebornCore;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.StringUtils;
import reborncore.mcmultipart.item.ItemMultiPart;
import reborncore.mcmultipart.multipart.IMultipart;
import techreborn.client.TechRebornCreativeTab;
import techreborn.lib.ModInfo;
import techreborn.parts.TechRebornParts;

import java.util.List;

/**
 * Created by modmuss50 on 27/02/2016.
 */
public class ItemCables extends ItemMultiPart implements ITexturedItem {

	public ItemCables() {
		setCreativeTab(TechRebornCreativeTab.instance);
		setHasSubtypes(true);
		setUnlocalizedName("techreborn.cable");
		setNoRepair();
		RebornCore.jsonDestroyer.registerObject(this);
		ItemStandaloneCables.mcPartCable = this;
	}

	@Override
	public IMultipart createPart(World world, BlockPos pos, EnumFacing side, Vec3d hit, ItemStack stack,
	                             EntityPlayer player) {
		try {
			return TechRebornParts.multipartHashMap.get(EnumCableType.values()[stack.getItemDamage()]).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	// gets Unlocalized Name depending on meta data
	public String getUnlocalizedName(ItemStack itemStack) {
		int meta = itemStack.getItemDamage();
		if (meta < 0 || meta >= EnumCableType.values().length) {
			meta = 0;
		}

		return super.getUnlocalizedName() + "." + EnumCableType.values()[meta];
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		for (int meta = 0; meta < EnumCableType.values().length; ++meta) {
			subItems.add(new ItemStack(this, 1, meta));
		}
	}

	@Override
	public String getTextureName(int damage) {
		return ModInfo.MOD_ID + ":items/cables/" + EnumCableType.values()[damage].getName();
	}

	@Override
	public int getMaxMeta() {
		return EnumCableType.values().length;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining) {
		return new ModelResourceLocation(ModInfo.MOD_ID + ":" + getUnlocalizedName(stack).substring(5), "inventory");
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		EnumCableType type = EnumCableType.values()[stack.getItemDamage()];
		tooltip.add(TextFormatting.GRAY + I18n.translateToLocal("desc.transfer") + " " + TextFormatting.GOLD + PowerSystem.getLocaliszedPowerFormatted(type.transferRate));
		tooltip.add(TextFormatting.GRAY + I18n.translateToLocal("desc.tier") + " " + TextFormatting.GOLD + StringUtils.toFirstCapitalAllLowercase(type.tier.toString()));
		if (type.canKill) {
			tooltip.add(TextFormatting.RED + I18n.translateToLocal("desc.uninsulatedCable"));
		}
	}
}