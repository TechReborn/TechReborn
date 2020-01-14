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

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
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
import techreborn.utils.TechRebornCreativeTab;

import javax.annotation.Nullable;
import java.util.Random;

public class ItemChainsaw extends ItemAxe implements IEnergyItemInfo {

	public int maxCharge = 1;
	public int cost = 250;
	public float poweredSpeed = 20F;
	public float unpoweredSpeed = 2.0F;
	public double transferLimit = 100;
	public boolean isBreaking = false;

	public ItemChainsaw(ToolMaterial material, String unlocalizedName, int energyCapacity, float unpoweredSpeed) {
		super(material);
		setCreativeTab(TechRebornCreativeTab.instance);
		setMaxStackSize(1);
		setTranslationKey(unlocalizedName);
		this.maxCharge = energyCapacity;
		this.efficiency = unpoweredSpeed;

		this.addPropertyOverride(new ResourceLocation("techreborn:animated"), new IItemPropertyGetter() {
			@Override
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
				if (!stack.isEmpty() && new ForgePowerItemManager(stack).getEnergyStored() >= cost
						&& entityIn != null && entityIn.getHeldItemMainhand().equals(stack)) {
					return 1.0F;
				}
				return 0.0F;
			}
		});
	}
	
	// ItemAxe
	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		if (new ForgePowerItemManager(stack).getEnergyStored() >= cost
				&& (state.getBlock().isToolEffective("axe", state) || state.getMaterial() == Material.WOOD)) {
			return this.poweredSpeed;
		} else {
			return super.getDestroySpeed(stack, state);
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
	public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase entityliving1) {
		return true;
	}

	// Item
	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
		return super.onBlockStartBreak(itemstack, pos, player);
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

	@Override
	public boolean isRepairable() {
		return false;
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
		return transferLimit;
	}

	@Override
	public int getItemEnchantability() {
		return 20;
	}
}
