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

import ic2.api.item.IMiningDrill;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.Optional;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.ExternalPowerSystems;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.PoweredItemContainerProvider;
import reborncore.common.powerSystem.forge.ForgePowerItemManager;
import reborncore.common.util.ItemUtils;
import techreborn.init.ModItems;
import techreborn.utils.StringUtilities;
import techreborn.utils.TechRebornCreativeTab;

import javax.annotation.Nullable;
import java.util.Random;

@Optional.Interface(iface = "ic2.api.item.IMiningDrill", modid = "ic2")
public class ItemDrill extends ItemPickaxe implements IEnergyItemInfo, IMiningDrill {

	public int maxCharge = 1;
	public int cost = 250;
	public float unpoweredSpeed = 2.0F;
	public double transferLimit = 100;

	public ItemDrill(ToolMaterial material, String unlocalizedName, int energyCapacity, float unpoweredSpeed, float efficiencyOnProperMaterial) {
		super(material);
		this.efficiency = efficiencyOnProperMaterial;
		setCreativeTab(TechRebornCreativeTab.instance);
		setMaxStackSize(1);
		setTranslationKey(unlocalizedName);
		this.maxCharge = energyCapacity;
		this.unpoweredSpeed = unpoweredSpeed;
	}
	
	// ItemPickaxe
	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		if (new ForgePowerItemManager(stack).getEnergyStored() < cost) {
			return unpoweredSpeed;
		}
		if (Items.WOODEN_PICKAXE.getDestroySpeed(stack, state) > 1.0F
				|| Items.WOODEN_SHOVEL.getDestroySpeed(stack, state) > 1.0F) {
			return efficiency;
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
	
	//Item
	@Override
	public boolean isRepairable() {
		return false;
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

	// IC2 Drill Compatibility >>
	@Optional.Method(modid = "ic2")
	@Override
	public int energyUse(ItemStack stack, World world, BlockPos pos, IBlockState state) {
		if (stack.getItem() == ModItems.STEEL_DRILL)
			return 6; // minerMineOperationCostDrill = 6
		else if (stack.getItem() == ModItems.DIAMOND_DRILL)
			return 20; // minerMineOperationCostDDrill = 20
		else if (stack.getItem() == ModItems.ADVANCED_DRILL)
			return 200; // minerMineOperationCostIDrill = 200
		else
			throw new IllegalArgumentException("Invalid drill: " + StringUtilities.stackToStringSafe(stack));
	}

	@Optional.Method(modid = "ic2")
	@Override
	public int breakTime(ItemStack stack, World world, BlockPos pos, IBlockState state) {
		if (stack.getItem() == ModItems.STEEL_DRILL)
			return 200; // minerMineOperationDurationDrill = 200
		else if (stack.getItem() == ModItems.DIAMOND_DRILL)
			return 50; // minerMineOperationDurationDDrill = 50
		else if (stack.getItem() == ModItems.ADVANCED_DRILL)
			return 20; // minerMineOperationDurationIDrill = 20
		else
			throw new IllegalArgumentException("Invalid drill: " + StringUtilities.stackToStringSafe(stack));
	}

	@Optional.Method(modid = "ic2")
	@Override
	public boolean breakBlock(ItemStack stack, World world, BlockPos pos, IBlockState state) {
		if (stack.getItem() == ModItems.STEEL_DRILL)
			return tryUsePower(stack, 50); // miningDrillCost = 50
		else if (stack.getItem() == ModItems.DIAMOND_DRILL)
			return tryUsePower(stack, 80); // mdiamondDrillCost = 80
		else if (stack.getItem() == ModItems.ADVANCED_DRILL)
			return tryUsePower(stack, 800); // iridiumDrillCost = 800
		else
			throw new IllegalArgumentException("Invalid drill: " + StringUtilities.stackToStringSafe(stack));
	}

	@Optional.Method(modid = "ic2")
	@Override
	public boolean tryUsePower(ItemStack drill, double amount) {
		ForgePowerItemManager capEnergy = new ForgePowerItemManager(drill);

		// check if there is enough energy
		if (capEnergy.getEnergyStored() < (int) amount) return false;

		// use energy
		capEnergy.extractEnergy((int) amount, false);
		return true;

	}
	// << IC2 Drill Compatibility
}
