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

import com.google.common.collect.Multimap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import reborncore.api.items.ItemStackModifiers;
import reborncore.common.util.StringUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.world.World;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.api.power.ItemPowerManager;
import reborncore.common.powerSystem.ExternalPowerSystems;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.ChatUtils;
import reborncore.common.util.ItemDurabilityExtensions;
import reborncore.common.util.ItemUtils;
import techreborn.TechReborn;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRContent;
import techreborn.utils.MessageIDs;

import javax.annotation.Nullable;
import java.util.List;

public class ItemNanosaber extends SwordItem implements IEnergyItemInfo, ItemDurabilityExtensions, ItemStackModifiers {
	public static final int maxCharge = TechRebornConfig.nanoSaberCharge;
	public int transferLimit = 1_000;
	public int cost = 250;

	// 4M FE max charge with 1k charge rate
	public ItemNanosaber() {
		super(ToolMaterials.DIAMOND, 1, 1, new Item.Settings().group(TechReborn.ITEMGROUP).maxCount(1));
		this.addPropertyGetter(new Identifier("techreborn:active"), new ItemPropertyGetter() {
			@Override
			@Environment(EnvType.CLIENT)
			public float call(ItemStack stack,
			                   @Nullable
				                   World worldIn,
			                   @Nullable
				                   LivingEntity entityIn) {
				if (ItemUtils.isActive(stack)) {
					ItemPowerManager capEnergy = new ItemPowerManager(stack);
					if (capEnergy.getMaxEnergyStored() - capEnergy.getEnergyStored() >= 0.9	* capEnergy.getMaxEnergyStored()) {
						return 0.5F;
					}
					return 1.0F;
				}
				return 0.0F;
			}
		});
	}
	
	// ItemSword
	@Override
	public boolean postHit(ItemStack stack, LivingEntity entityHit, LivingEntity entityHitter) {
		ItemPowerManager capEnergy = new ItemPowerManager(stack);
		if (capEnergy.getEnergyStored() >= cost) {
			capEnergy.useEnergy(cost, false);
			ExternalPowerSystems.requestEnergyFromArmor(capEnergy, entityHitter);

			return true;
		}
		return false;
	}

	@Override
	public void getAttributeModifiers(EquipmentSlot slot, ItemStack stack, Multimap<String, EntityAttributeModifier> attributes) {
		attributes.removeAll(EntityAttributes.ATTACK_DAMAGE.getId());
		attributes.removeAll(EntityAttributes.ATTACK_SPEED.getId());

		if (slot== EquipmentSlot.MAINHAND && ItemUtils.isActive(stack)) {
			attributes.put(EntityAttributes.ATTACK_DAMAGE.getId(), new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_UUID, "Weapon modifier", 12, EntityAttributeModifier.Operation.ADDITION));
			attributes.put(EntityAttributes.ATTACK_SPEED.getId(), new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_UUID, "Weapon modifier", 3, EntityAttributeModifier.Operation.ADDITION));
		}
	}

	@Override
	public TypedActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
		final ItemStack stack = player.getStackInHand(hand);
		if (player.isSneaking()) {
			if (new ItemPowerManager(stack).getEnergyStored() < cost) {
				ChatUtils.sendNoSpamMessages(MessageIDs.nanosaberID, new LiteralText(
					Formatting.GRAY + StringUtils.t("techreborn.message.nanosaberEnergyErrorTo") + " "
						+ Formatting.GOLD + StringUtils.t("techreborn.message.nanosaberActivate")));
			} else {
				if (!ItemUtils.isActive(stack)) {
					if (stack.getTag() == null) {
						stack.setTag(new CompoundTag());
					}
					stack.getTag().putBoolean("isActive", true);
					if (world.isClient) {
						ChatUtils.sendNoSpamMessages(MessageIDs.nanosaberID, new LiteralText(
							Formatting.GRAY + StringUtils.t("techreborn.message.setTo") + " "
								+ Formatting.GOLD + StringUtils.t("techreborn.message.nanosaberActive")));
					}
				} else {
					stack.getTag().putBoolean("isActive", false);
					if (world.isClient) {
						ChatUtils.sendNoSpamMessages(MessageIDs.nanosaberID, new LiteralText(
							Formatting.GRAY + StringUtils.t("techreborn.message.setTo") + " "
								+ Formatting.GOLD + StringUtils.t("techreborn.message.nanosaberInactive")));
					}
				}
			}
			return new TypedActionResult<>(ActionResult.SUCCESS, stack);
		}
		return new TypedActionResult<>(ActionResult.PASS, stack);
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (ItemUtils.isActive(stack) && new ItemPowerManager(stack).getEnergyStored() < cost) {
			if(worldIn.isClient){
				ChatUtils.sendNoSpamMessages(MessageIDs.nanosaberID, new LiteralText(
					Formatting.GRAY + StringUtils.t("techreborn.message.nanosaberEnergyError") + " "
						+ Formatting.GOLD + StringUtils.t("techreborn.message.nanosaberDeactivating")));
			}
			stack.getTag().putBoolean("isActive", false);
		}
	}

	@Override
	public boolean canRepair(ItemStack itemStack_1, ItemStack itemStack_2) {
		return false;
	}

	@Override
	public double getDurability(ItemStack stack) {
		return 1 - ItemUtils.getPowerForDurabilityBar(stack);
	}

	@Override
	public boolean showDurability(ItemStack stack) {
		return true;
	}

	@Override
	public int getDurabilityColor(ItemStack stack) {
		return PowerSystem.getDisplayPower().colour;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendStacks(
		ItemGroup par2ItemGroup, DefaultedList<ItemStack> itemList) {
		if (!isIn(par2ItemGroup)) {
			return;
		}
		ItemStack inactiveUncharged = new ItemStack(this);
		inactiveUncharged.setTag(new CompoundTag());
		inactiveUncharged.getTag().putBoolean("isActive", false);

		ItemStack inactiveCharged = new ItemStack(TRContent.NANOSABER);
		inactiveCharged.setTag(new CompoundTag());
		inactiveCharged.getTag().putBoolean("isActive", false);
		ItemPowerManager capEnergy = new ItemPowerManager(inactiveCharged);
		capEnergy.setEnergyStored(capEnergy.getMaxEnergyStored());

		ItemStack activeCharged = new ItemStack(TRContent.NANOSABER);
		activeCharged.setTag(new CompoundTag());
		activeCharged.getTag().putBoolean("isActive", true);
		ItemPowerManager capEnergy2 = new ItemPowerManager(activeCharged);
		capEnergy2.setEnergyStored(capEnergy2.getMaxEnergyStored());

		itemList.add(inactiveUncharged);
		itemList.add(inactiveCharged);
		itemList.add(activeCharged);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World worldIn, List<Text> tooltip, TooltipContext flagIn) {
		if (!ItemUtils.isActive(stack)) {
			tooltip.add(new TranslatableText("techreborn.message.nanosaberInactive").formatted(Formatting.GRAY));
		} else {
			tooltip.add(new TranslatableText("techreborn.message.nanosaberActive").formatted(Formatting.GRAY));
		}
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
