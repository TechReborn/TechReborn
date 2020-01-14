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
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
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
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModItems;
import techreborn.utils.TechRebornCreativeTab;

import javax.annotation.Nullable;
import java.util.Random;

public class ItemRockCutter extends ItemPickaxe implements IEnergyItemInfo {

	public static final int maxCharge = ConfigTechReborn.RockCutterCharge;
	public int cost = 500;

	public ItemRockCutter() {
		super(ToolMaterial.DIAMOND);
		setTranslationKey("techreborn.rockcutter");
		setCreativeTab(TechRebornCreativeTab.instance);
		setMaxStackSize(1);
		efficiency = 16F;
	}

	// ItemPickaxe
	@Override
	public boolean canHarvestBlock(IBlockState state) {
		if (Items.DIAMOND_PICKAXE.canHarvestBlock(state)) {
			return true;
		}
		return false;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		if (new ForgePowerItemManager(stack).getEnergyStored() < cost) {
			return 2F;
		} else {
			return Items.DIAMOND_PICKAXE.getDestroySpeed(stack, state);
		}
	}

	// ItemTool
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos, EntityLivingBase entityLiving) {
		Random rand = new Random();
		if (rand.nextInt(EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack) + 1) == 0) {
			ForgePowerItemManager capEnergy = new ForgePowerItemManager(stack);

			capEnergy.extractEnergy(cost, false);
			ExternalPowerSystems.requestEnergyFromArmor(capEnergy, entityLiving);
		}
		return true;
	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {
		if (!stack.isItemEnchanted()) {
			stack.addEnchantment(Enchantments.SILK_TOUCH, 1);
		}
		return super.getHarvestLevel(stack, toolClass, player, blockState);
	}

	// Item
	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		if (!stack.isItemEnchanted()) {
			stack.addEnchantment(Enchantments.SILK_TOUCH, 1);
		}
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

	@Override
	public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
		return !(newStack.isItemEqual(oldStack));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(CreativeTabs par2CreativeTabs, NonNullList<ItemStack> itemList) {
		if (!isInCreativeTab(par2CreativeTabs)) {
			return;
		}
		ItemStack uncharged = new ItemStack(ModItems.ROCK_CUTTER);
		uncharged.addEnchantment(Enchantments.SILK_TOUCH, 1);
		ItemStack charged = new ItemStack(ModItems.ROCK_CUTTER);
		charged.addEnchantment(Enchantments.SILK_TOUCH, 1);
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
	public boolean canProvideEnergy(ItemStack stack) {
		return false;
	}

	@Override
	public double getMaxTransfer(ItemStack stack) {
		return 1000;
	}
}
