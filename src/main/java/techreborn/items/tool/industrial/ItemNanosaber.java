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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormat;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.ExternalPowerSystems;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.PoweredItemContainerProvider;
import reborncore.common.powerSystem.ItemPowerManager;
import reborncore.common.util.ChatUtils;
import reborncore.common.util.ItemUtils;
import techreborn.TechReborn;
import techreborn.config.ConfigTechReborn;
import techreborn.init.TRContent;
import techreborn.utils.MessageIDs;

import javax.annotation.Nullable;
import java.util.List;

public class ItemNanosaber extends SwordItem implements IEnergyItemInfo {
	public static final int maxCharge = ConfigTechReborn.nanoSaberCharge;
	public int transferLimit = 1_000;
	public int cost = 250;

	// 4M FE max charge with 1k charge rate
	public ItemNanosaber() {
		super(ToolMaterials.DIAMOND, 1, 1, new Item.Settings().itemGroup(TechReborn.ITEMGROUP).setNoRepair().maxStackSize(1));
		this.addProperty(new Identifier("techreborn:active"), new ItemPropertyGetter() {
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
	public boolean onEntityDamaged(ItemStack stack, LivingEntity entityHit, LivingEntity entityHitter) {
		ItemPowerManager capEnergy = new ItemPowerManager(stack);
		if (capEnergy.getEnergyStored() >= cost) {
			capEnergy.extractEnergy(cost, false);
			ExternalPowerSystems.requestEnergyFromArmor(capEnergy, entityHitter);

			return true;
		} else {
			return false;
		}
	}

	// Item
	@Override
	public Multimap<String, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		Multimap<String, EntityAttributeModifier> multimap = HashMultimap.<String, EntityAttributeModifier>create();
		int modifier = 0;
		if (ItemUtils.isActive(stack)) {
			modifier = 9;
		}
		if (slot == EquipmentSlot.MAINHAND) {
			multimap.put(EntityAttributes.ATTACK_DAMAGE.getId(),
				new EntityAttributeModifier(MODIFIER_DAMAGE, "Weapon modifier", (double) modifier, 0));
			multimap.put(EntityAttributes.ATTACK_SPEED.getId(),
				new EntityAttributeModifier(MODIFIER_SWING_SPEED, "Weapon modifier", -2.4000000953674316D, 0));
		}
		return multimap;
	}

	@Override
	public TypedActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
		final ItemStack stack = player.getStackInHand(hand);
		if (player.isSneaking()) {
			if (new ItemPowerManager(stack).getEnergyStored() < cost) {
				ChatUtils.sendNoSpamMessages(MessageIDs.nanosaberID, new TextComponent(
					ChatFormat.GRAY + I18n.translate("techreborn.message.nanosaberEnergyErrorTo") + " "
						+ ChatFormat.GOLD + I18n
						.translate("techreborn.message.nanosaberActivate")));
			} else {
				if (!ItemUtils.isActive(stack)) {
					if (stack.getTag() == null) {
						stack.setTag(new CompoundTag());
					}
					stack.getTag().putBoolean("isActive", true);
					if (world.isClient) {
						ChatUtils.sendNoSpamMessages(MessageIDs.nanosaberID, new TextComponent(
							ChatFormat.GRAY + I18n.translate("techreborn.message.setTo") + " "
								+ ChatFormat.GOLD + I18n
								.translate("techreborn.message.nanosaberActive")));
					}
				} else {
					stack.getTag().putBoolean("isActive", false);
					if (world.isClient) {
						ChatUtils.sendNoSpamMessages(MessageIDs.nanosaberID, new TextComponent(
							ChatFormat.GRAY + I18n.translate("techreborn.message.setTo") + " "
								+ ChatFormat.GOLD + I18n
								.translate("techreborn.message.nanosaberInactive")));
					}
				}
			}
			return new TypedActionResult<>(ActionResult.SUCCESS, stack);
		}
		return new TypedActionResult<>(ActionResult.PASS, stack);
	}

	@Override
	public void onEntityTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (ItemUtils.isActive(stack) && new ItemPowerManager(stack).getEnergyStored() < cost) {
			if(worldIn.isClient){
				ChatUtils.sendNoSpamMessages(MessageIDs.nanosaberID, new TextComponent(
					ChatFormat.GRAY + I18n.translate("techreborn.message.nanosaberEnergyError") + " "
						+ ChatFormat.GOLD + I18n
						.translate("techreborn.message.nanosaberDeactivating")));
			}
			stack.getTag().putBoolean("isActive", false);
		}
	}

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
	public ICapabilityProvider initCapabilities(ItemStack stack,
	                                            @Nullable
		                                            CompoundTag nbt) {
		return new PoweredItemContainerProvider(stack);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendItemsForGroup(
		ItemGroup par2ItemGroup, DefaultedList<ItemStack> itemList) {
		if (!isInItemGroup(par2ItemGroup)) {
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
	public void buildTooltip(ItemStack stack, @Nullable World worldIn, List<Component> tooltip, TooltipContext flagIn) {
		if (!ItemUtils.isActive(stack)) {
			tooltip.add(new TranslatableComponent("techreborn.message.nanosaberInactive").applyFormat(ChatFormat.GRAY));
		} else {
			tooltip.add(new TranslatableComponent("techreborn.message.nanosaberActive").applyFormat(ChatFormat.GRAY));
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
