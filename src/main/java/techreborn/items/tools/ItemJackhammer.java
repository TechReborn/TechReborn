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

package techreborn.items.tools;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.api.power.IEnergyInterfaceItem;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.PoweredItem;
import reborncore.common.powerSystem.PoweredItemContainerProvider;
import techreborn.client.TechRebornCreativeTab;
import techreborn.utils.OreDictUtils;

import javax.annotation.Nullable;
import java.util.Random;

public class ItemJackhammer extends ItemPickaxe implements IEnergyItemInfo, IEnergyInterfaceItem {

	public int maxCharge = 1;
	public int cost = 250;
	public double transferLimit = 100;

	public ItemJackhammer(ToolMaterial material, String unlocalizedName, int energyCapacity) {
		super(material);
		efficiencyOnProperMaterial = 20F;
		setCreativeTab(TechRebornCreativeTab.instance);
		setMaxStackSize(1);
		setMaxDamage(240);
		setUnlocalizedName(unlocalizedName);
		this.maxCharge = energyCapacity;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos,
	                                EntityLivingBase entityLiving) {
		Random rand = new Random();
		if (rand.nextInt(EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack) + 1) == 0) {
			PoweredItem.useEnergy(cost, stack);
		}
		return true;
	}

	@Override
	public boolean canHarvestBlock(final IBlockState state, final ItemStack stack) {
		return OreDictUtils.isOre(state, "stone")
			|| state.getMaterial() == Material.ROCK && PoweredItem.canUseEnergy(this.cost, stack);
	}

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state) {
		if ((OreDictUtils.isOre(state, "stone") || state.getBlock() == Blocks.STONE) && PoweredItem.canUseEnergy(cost, stack)) {
			return efficiencyOnProperMaterial;
		} else {
			return 0.5F;
		}
	}

	@Override
	public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase entityliving1) {
		return true;
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

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

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(CreativeTabs par2CreativeTabs, NonNullList<ItemStack> itemList) {
		if (!isInCreativeTab(par2CreativeTabs)) {
			return;
		}
		ItemStack itemStack = new ItemStack(this, 1);
		itemList.add(itemStack);

		ItemStack charged = new ItemStack(this, 1);
		PoweredItem.setEnergy(getMaxPower(charged), charged);
		itemList.add(charged);
	}

	@Override
	public double getEnergy(ItemStack stack) {
		NBTTagCompound tagCompound = getOrCreateNbtData(stack);
		if (tagCompound.hasKey("charge")) {
			return tagCompound.getDouble("charge");
		}
		return 0;
	}

	@Override
	public void setEnergy(double energy, ItemStack stack) {
		NBTTagCompound tagCompound = getOrCreateNbtData(stack);
		tagCompound.setDouble("charge", energy);

		if (this.getEnergy(stack) > getMaxPower(stack)) {
			this.setEnergy(getMaxPower(stack), stack);
		} else if (this.getEnergy(stack) < 0) {
			this.setEnergy(0, stack);
		}
	}

	@Override
	public double addEnergy(double energy, ItemStack stack) {
		return addEnergy(energy, false, stack);
	}

	@Override
	public double addEnergy(double energy, boolean simulate, ItemStack stack) {
		double energyReceived = Math.min(getMaxPower(stack) - energy, Math.min(this.getMaxPower(stack), energy));

		if (!simulate) {
			setEnergy(energy + energyReceived, stack);
		}
		return energyReceived;
	}


	@Override
	public boolean canUseEnergy(double input, ItemStack stack) {
		return input <= getEnergy(stack);
	}


	@Override
	public double useEnergy(double energy, ItemStack stack) {
		return useEnergy(energy, false, stack);
	}


	@Override
	public double useEnergy(double extract, boolean simulate, ItemStack stack) {
		double energyExtracted = Math.min(extract, Math.min(this.getMaxTransfer(stack), extract));

		if (!simulate) {
			setEnergy(getEnergy(stack) - energyExtracted, stack);
		}
		return energyExtracted;
	}


	@Override
	public boolean canAddEnergy(double energy, ItemStack stack) {
		return this.getEnergy(stack) + energy <= getMaxPower(stack);
	}


	public NBTTagCompound getOrCreateNbtData(ItemStack itemStack) {
		NBTTagCompound tagCompound = itemStack.getTagCompound();
		if (tagCompound == null) {
			tagCompound = new NBTTagCompound();
			itemStack.setTagCompound(tagCompound);
		}

		return tagCompound;
	}


	public double getDurabilityForDisplay(ItemStack stack) {
		double charge = (PoweredItem.getEnergy(stack) / getMaxPower(stack));
		return 1 - charge;
	}


	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}


	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return PowerSystem.getDisplayPower().colour;
	}

	@Nullable
	public ICapabilityProvider initCapabilities(ItemStack stack,
	                                            @Nullable
		                                            NBTTagCompound nbt) {
		return new PoweredItemContainerProvider(stack);
	}
	
	/**
     * Called when the player is mining a block and the item in his hand changes.
     * Allows to not reset blockbreaking if only NBT or similar changes.
     * @param oldStack The old stack that was used for mining. Item in players main hand
     * @param newStack The new stack
     * @return True to reset block break progress
     */
	@Override
	public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
		return !(newStack.isItemEqual(oldStack));
	}
}
