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

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import reborncore.api.items.ArmorBlockEntityTicker;
import reborncore.api.items.ArmorRemoveHandler;
import reborncore.common.powerSystem.RcEnergyTier;
import techreborn.TechReborn;
import techreborn.config.TechRebornConfig;
import techreborn.utils.TRItemUtils;

import java.util.List;

public class NanoSuitItem extends TREnergyArmourItem implements ArmorBlockEntityTicker, ArmorRemoveHandler {
	private static final EntityAttributeModifier POWERED_ATTRIBUTE_MODIFIER = new EntityAttributeModifier(Identifier.of(TechReborn.MOD_ID, "nano_suit_armor"), 14, EntityAttributeModifier.Operation.ADD_VALUE);
	private static final EntityAttributeModifier DEPLETED_ATTRIBUTE_MODIFIER = new EntityAttributeModifier(Identifier.of(TechReborn.MOD_ID, "nano_suit_armor"), 0, EntityAttributeModifier.Operation.ADD_VALUE);

	public NanoSuitItem(RegistryEntry<ArmorMaterial> material, Type slot) {
		super(material, slot, TechRebornConfig.nanoSuitCapacity, RcEnergyTier.HIGH);
	}

	// TREnergyArmourItem
	@Override
	public long getEnergyMaxOutput(ItemStack stack) { return 0; }

	// ArmorBlockEntityTicker
	@Override
	public void tickArmor(ItemStack stack, PlayerEntity playerEntity) {
		// Night Vision
		if (this.getSlotType() == EquipmentSlot.HEAD) {
			if (TRItemUtils.isActive(stack) && tryUseEnergy(stack, TechRebornConfig.suitNightVisionCost)) {
				playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 220, 1, false, false));
			} else {
				playerEntity.removeStatusEffect(StatusEffects.NIGHT_VISION);
			}
		}

		AttributeModifiersComponent attributes = stack.getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT);
		attributes = attributes.with(EntityAttributes.GENERIC_ARMOR, getStoredEnergy(stack) > 0 ? POWERED_ATTRIBUTE_MODIFIER : DEPLETED_ATTRIBUTE_MODIFIER, AttributeModifierSlot.forEquipmentSlot(this.getSlotType()));
		stack.set(DataComponentTypes.ATTRIBUTE_MODIFIERS, attributes);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack thisStack = user.getStackInHand(hand);
		EquipmentSlot slotType = this.getSlotType();
		if (user.isSneaking() && slotType == EquipmentSlot.HEAD) {
			TRItemUtils.switchActive(thisStack, 1, user);
			return TypedActionResult.success(thisStack);
		}
		return super.use(world, user, hand);
	}

	@Override
	public void onRemoved(PlayerEntity playerEntity) {
		playerEntity.removeStatusEffect(StatusEffects.NIGHT_VISION);
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		if (this.type == Type.HELMET) {
			TRItemUtils.buildActiveTooltip(stack, tooltip);
		}
	}
}
