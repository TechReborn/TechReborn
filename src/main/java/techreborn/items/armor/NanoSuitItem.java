/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

package techreborn.items.armor;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.api.items.ArmorBlockEntityTicker;
import reborncore.api.items.ArmorRemoveHandler;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.util.ItemUtils;
import techreborn.config.TechRebornConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NanoSuitItem extends TREnergyArmourItem implements ArmorBlockEntityTicker, ArmorRemoveHandler {
	private static final Multimap<EntityAttribute, EntityAttributeModifier> FULL_SUIT = new AttributeModifierBuilder().armor(10).toughness(4).build();
	private final Multimap<EntityAttribute, EntityAttributeModifier> noPowerAttributes;
	private final Multimap<EntityAttribute, EntityAttributeModifier> hasPowerAttributes;
	private final Multimap<EntityAttribute, EntityAttributeModifier> fullSuitAttributes;

	public NanoSuitItem(ArmorMaterial material, Type slot) {
		super(material, slot, TechRebornConfig.nanoSuitCapacity, RcEnergyTier.HIGH);
		switch (slot) {
			case HELMET, BOOTS:
				noPowerAttributes = new AttributeModifierBuilder(slot).armor(1).build();
				hasPowerAttributes = new AttributeModifierBuilder(slot).armor(3).toughness(2).knockback(1).build();
				fullSuitAttributes = new AttributeModifierBuilder(slot).armor(5).toughness(3).knockback(1).build();
				break;
			case CHESTPLATE:
				noPowerAttributes = new AttributeModifierBuilder(slot).armor(2).build();
				hasPowerAttributes = new AttributeModifierBuilder(slot).armor(6).toughness(2).knockback(1).build();
				fullSuitAttributes = new AttributeModifierBuilder(slot).armor(10).toughness(3).knockback(1).build();
				break;
			case LEGGINGS:
				noPowerAttributes = new AttributeModifierBuilder(slot).armor(3).build();
				hasPowerAttributes = new AttributeModifierBuilder(slot).armor(8).toughness(2).knockback(1).build();
				fullSuitAttributes = new AttributeModifierBuilder(slot).armor(10).toughness(3).knockback(1).build();
				break;
			default:
				throw new IllegalArgumentException("Invalid slot type");
		}
	}

	// TREnergyArmourItem
	@Override
	public long getEnergyMaxOutput() { return 0; }

	// ArmorItem
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return HashMultimap.create();
	}

	// FabricItem
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot equipmentSlot) {
		if (equipmentSlot != this.getSlotType()) {
			return ImmutableMultimap.of();
		}
		NbtCompound nbt = stack.getOrCreateNbt();
		if (getStoredEnergy(stack) > 0) {
			if (nbt.contains("HideFlags")) {
				return fullSuitAttributes;
			} else {
				return hasPowerAttributes;
			}
		} else {
			return noPowerAttributes;
		}
	}

	// ArmorBlockEntityTicker
	@Override
	public void tickArmor(ItemStack stack, boolean hasFullSuit, PlayerEntity playerEntity) {
		// Night Vision
		NbtCompound nbt = stack.getOrCreateNbt();
		if (Objects.requireNonNull(this.getSlotType()) == EquipmentSlot.HEAD) {
			if (nbt.getBoolean("isActive") && tryUseEnergy(stack, TechRebornConfig.suitNightVisionCost)) {
				playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 220, 1, false, false));
			} else {
				playerEntity.removeStatusEffect(StatusEffects.NIGHT_VISION);
			}
		}
		if (nbt.contains("HideFlags")) {
			if (!hasFullSuit) {
				nbt.remove("HideFlags");
			}
		} else if (hasFullSuit) {
			nbt.putInt("HideFlags", ItemStack.TooltipSection.MODIFIERS.getFlag());
		}
	}

	@Override
	public void onRemoved(PlayerEntity playerEntity) {
		playerEntity.removeStatusEffect(StatusEffects.NIGHT_VISION);
		ItemStack stack = playerEntity.playerScreenHandler.getCursorStack();
		if (stack.getItem() instanceof NanoSuitItem) {
			NbtCompound nbt = stack.getOrCreateNbt();
			nbt.remove("HideFlags");
			nbt.remove("isTicking");
		} else {
			playerEntity.getInventory().main.forEach(itemStack -> {
				if (itemStack.getItem() instanceof NanoSuitItem) {
					NbtCompound nbt = itemStack.getOrCreateNbt();
					nbt.remove("HideFlags");
					nbt.remove("isTicking");
				}
			});
		}
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if (this.type == Type.HELMET) {
			ItemUtils.buildActiveTooltip(stack, tooltip);
		}
	}

	public void appendArmorTooltip(ItemStack stack, List<Text> tooltip, boolean shift) {
		List<Text> buffer = new ArrayList<>();
		if (getStoredEnergy(stack) > 0) {
			if (stack.getOrCreateNbt().contains("HideFlags")) {
				buffer.add(Text.empty());
				buffer.add(AttributeModifierBuilder.text(getSlotType()).formatted(Formatting.GRAY));
				if (shift) {
					AttributeModifierBuilder.appendText(buffer, fullSuitAttributes, Formatting.BLUE);
				} else {
					AttributeModifierBuilder.appendText(buffer, hasPowerAttributes, Formatting.BLUE);
					buffer.add(Text.empty());
					buffer.add(Text.translatable("item.modifiers.full_suit").formatted(Formatting.YELLOW));
					AttributeModifierBuilder.appendText(buffer, FULL_SUIT, Formatting.YELLOW);
				}
			} else if (shift) {
				buffer.add(Text.translatable("item.modifiers.all_equipment").formatted(Formatting.GRAY));
				AttributeModifierBuilder.appendText(buffer, FULL_SUIT, Formatting.BLUE);
			}
		} else {
			if (!shift && stack.getOrCreateNbt().contains("isTicking")) {
				return;
			}
			buffer.add(Text.translatable("item.modifiers.power").formatted(Formatting.GRAY));
			AttributeModifierBuilder.appendDiffText(buffer, noPowerAttributes, hasPowerAttributes, Formatting.BLUE);
			buffer.add(Text.translatable("item.modifiers.all_equipment").formatted(Formatting.GRAY));
			AttributeModifierBuilder.appendText(buffer, FULL_SUIT, Formatting.BLUE);
		}
		AttributeModifierBuilder.appendEnd(tooltip, buffer);
	}
}
