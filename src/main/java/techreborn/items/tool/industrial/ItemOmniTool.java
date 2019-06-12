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

package techreborn.items.tool.industrial;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormat;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.ExternalPowerSystems;
import reborncore.common.powerSystem.ItemPowerManager;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.ItemDurabilityExtensions;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.TorchHelper;
import techreborn.TechReborn;
import techreborn.config.ConfigTechReborn;
import techreborn.init.TRContent;

import javax.annotation.Nullable;
import java.util.List;

public class ItemOmniTool extends PickaxeItem implements IEnergyItemInfo, ItemDurabilityExtensions {

	public static final int maxCharge = ConfigTechReborn.OmniToolCharge;
	public int transferLimit = 1_000;
	public int cost = 100;
	public int hitCost = 125;

	// 4M FE max charge with 1k charge rate
	public ItemOmniTool() {
		super(ToolMaterials.DIAMOND, 1, 1, new Item.Settings().itemGroup(TechReborn.ITEMGROUP).stackSize(1));
	}
	
	// ItemPickaxe
	@Override
	public boolean isEffectiveOn(BlockState state) {
		return Items.DIAMOND_AXE.isEffectiveOn(state) || Items.DIAMOND_SWORD.isEffectiveOn(state)
				|| Items.DIAMOND_PICKAXE.isEffectiveOn(state) || Items.DIAMOND_SHOVEL.isEffectiveOn(state)
				|| Items.SHEARS.isEffectiveOn(state);
	}

	// ItemTool
	@Override
	public boolean onBlockBroken(ItemStack stack, World worldIn, BlockState blockIn, BlockPos pos, LivingEntity entityLiving) {
		ItemPowerManager capEnergy = new ItemPowerManager(stack);

		capEnergy.extractEnergy(cost, false);
		ExternalPowerSystems.requestEnergyFromArmor(capEnergy, entityLiving);

		return true;
	}



	// @Override
	// public float getDigSpeed(ItemStack stack, IBlockState state) {
	// IEnergyStorage capEnergy = stack.getCapability(CapabilityEnergy.ENERGY, null);
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
	public boolean onEntityDamaged(ItemStack stack, LivingEntity entityliving, LivingEntity attacker) {
		ItemPowerManager capEnergy = new ItemPowerManager(stack);
		if (capEnergy.getEnergyStored() >= hitCost) {
			capEnergy.extractEnergy(hitCost, false);
			ExternalPowerSystems.requestEnergyFromArmor(capEnergy, entityliving);

			entityliving.damage(DamageSource.player((PlayerEntity) attacker), 8F);
		}
		return false;
	}

	// Item
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		return TorchHelper.placeTorch(context);
	}

	@Override
	public boolean canRepair(ItemStack itemStack_1, ItemStack itemStack_2) {
		return false;
	}

	@Override
	public void buildTooltip(ItemStack stack, @Nullable World worldIn, List<Component> tooltip, TooltipContext flagIn) {
		tooltip.add(new TextComponent("WIP Coming Soon").applyFormat(ChatFormat.RED));
		// TODO 
		// Remember to remove WIP override and imports once complete
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

	@Environment(EnvType.CLIENT)
	@Override
	public void appendItemsForGroup(
		ItemGroup par2ItemGroup, DefaultedList<ItemStack> itemList) {
		if (!isInItemGroup(par2ItemGroup)) {
			return;
		}
		ItemStack uncharged = new ItemStack(TRContent.OMNI_TOOL);
		ItemStack charged = new ItemStack(TRContent.OMNI_TOOL);
		ItemPowerManager capEnergy = new ItemPowerManager(charged);
		capEnergy.setEnergyStored(capEnergy.getMaxEnergyStored());

		itemList.add(uncharged);
		itemList.add(charged);
	}
	
	// IEnergyItemInfo
	@Override
	public int getCapacity() {
		return maxCharge;
	}

	@Override
	public int getMaxInput() {
		return transferLimit;
	}

	@Override
	public int getMaxOutput() {
		return 0;
	}
}
