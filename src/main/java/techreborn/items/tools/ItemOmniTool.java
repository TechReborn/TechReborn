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

package techreborn.items.tools;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.ExternalPowerSystems;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.PoweredItemContainerProvider;
import reborncore.common.powerSystem.forge.ForgePowerItemManager;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.TorchHelper;
import techreborn.utils.TechRebornCreativeTab;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModItems;

import javax.annotation.Nullable;
import java.util.List;

public class ItemOmniTool extends ItemPickaxe implements IEnergyItemInfo {

	public static final int maxCharge = ConfigTechReborn.OmniToolCharge;
	public int cost = 100;
	public int hitCost = 125;

	public ItemOmniTool() {
		super(ToolMaterial.DIAMOND);
		efficiency = 13F;
		setCreativeTab(TechRebornCreativeTab.instance);
		setMaxStackSize(1);
		setMaxDamage(200);
		setTranslationKey("techreborn.omniTool");
	}
	
	// ItemPickaxe
	@Override
	public boolean canHarvestBlock(IBlockState state) {
		return Items.DIAMOND_AXE.canHarvestBlock(state) || Items.DIAMOND_SWORD.canHarvestBlock(state)
				|| Items.DIAMOND_PICKAXE.canHarvestBlock(state) || Items.DIAMOND_SHOVEL.canHarvestBlock(state)
				|| Items.SHEARS.canHarvestBlock(state);
	}

	// ItemTool
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos, EntityLivingBase entityLiving) {
		ForgePowerItemManager capEnergy = new ForgePowerItemManager(stack);

		capEnergy.extractEnergy(cost, false);
		ExternalPowerSystems.requestEnergyFromArmor(capEnergy, entityLiving);

		return true;
	}



	// @Override
	// public float getDigSpeed(ItemStack stack, IBlockState state) {
	// IEnergyStorage capEnergy = new ForgePowerItemManager(stack);
	// if (capEnergy.getEnergyStored() >= cost) {
	// capEnergy.extractEnergy(cost, false);
	// return 5.0F;
	// }
	//
	// if (Items.wooden_axe.getDigSpeed(stack, state) > 1.0F
	// || Items.wooden_sword.getDigSpeed(stack, state) > 1.0F
	// || Items.wooden_pickaxe.getDigSpeed(stack, state) > 1.0F
	// || Items.wooden_shovel.getDigSpeed(stack, state) > 1.0F
	// || Items.shears.getDigSpeed(stack, state) > 1.0F) {
	// return efficiencyOnProperMaterial;
	// } else {
	// return super.getDigSpeed(stack, state);
	// }
	// }

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase entityliving, EntityLivingBase attacker) {
		ForgePowerItemManager capEnergy = new ForgePowerItemManager(stack);
		if (capEnergy.getEnergyStored() >= hitCost) {
			capEnergy.extractEnergy(hitCost, false);
			ExternalPowerSystems.requestEnergyFromArmor(capEnergy, entityliving);

			entityliving.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) attacker), 8F);
		}
		return false;
	}

	// Item
	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos,
	                                  EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return TorchHelper.placeTorch(playerIn.getHeldItem(hand), playerIn, worldIn, pos, facing, hitX, hitY, hitZ, hand);
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		tooltip.add(TextFormatting.RED + "WIP Coming Soon");
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1 - ItemUtils.getPowerForDurabilityBar(stack);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return PowerSystem.getDisplayPower().colour;
	}

	@Override
	@Nullable
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return new PoweredItemContainerProvider(stack);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(
		CreativeTabs par2CreativeTabs, NonNullList<ItemStack> itemList) {
		if (!isInCreativeTab(par2CreativeTabs)) {
			return;
		}
		ItemStack uncharged = new ItemStack(ModItems.OMNI_TOOL);
		ItemStack charged = new ItemStack(ModItems.OMNI_TOOL);
		ForgePowerItemManager capEnergy = new ForgePowerItemManager(charged);
		capEnergy.setEnergyStored(capEnergy.getMaxEnergyStored());

		itemList.add(uncharged);
		itemList.add(charged);
	}
	
	// IEnergyItemInfo
	@Override
	public double getMaxPower(ItemStack stack) {
		return maxCharge;
	}

	@Override
	public boolean canAcceptEnergy(ItemStack stack) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(ItemStack itemStack) {
		return false;
	}

	@Override
	public double getMaxTransfer(ItemStack stack) {
		return 1000;
	}
}
