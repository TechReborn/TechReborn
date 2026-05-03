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

package techreborn.items.tool.industrial;

import org.jetbrains.annotations.Nullable;
import reborncore.common.powerSystem.RcEnergyItem;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.util.ItemUtils;
import techreborn.component.TRDataComponentTypes;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRItemSettings;
import techreborn.init.TRToolMaterials;
import techreborn.utils.TRItemUtils;

import java.util.function.Consumer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

public class NanosaberItem extends Item implements RcEnergyItem {
	private static final AttributeModifier ENABLED_ATTACK_DAMAGE_MODIFIER = new AttributeModifier(Identifier.fromNamespaceAndPath("techreborn", "nano_saber_attack_damage"), TechRebornConfig.nanosaberDamage, AttributeModifier.Operation.ADD_VALUE);
	private static final AttributeModifier ENABLED_ATTACK_SPEED_MODIFIER = new AttributeModifier(Identifier.fromNamespaceAndPath("techreborn", "nano_saber_attack_speed"), 3, AttributeModifier.Operation.ADD_VALUE);
	private static final AttributeModifier DISABLED_ATTACK_DAMAGE_MODIFIER = new AttributeModifier(Identifier.fromNamespaceAndPath("techreborn", "nano_saber_attack_damage"), 0, AttributeModifier.Operation.ADD_VALUE);
	private static final AttributeModifier DISABLED_ATTACK_SPEED_MODIFIER = new AttributeModifier(Identifier.fromNamespaceAndPath("techreborn", "nano_saber_attack_speed"), 0, AttributeModifier.Operation.ADD_VALUE);

	// 1ME max charge with 2k charge rate
	public NanosaberItem(String name) {
		super(TRItemSettings.unbreakable(name).sword(TRToolMaterials.NANOSABER, 1f, 1f));
	}

	// SwordItem
	@Override
	public void hurtEnemy(ItemStack stack, LivingEntity entityHit, LivingEntity entityHitter) {
		tryUseEnergy(stack, TechRebornConfig.nanosaberCost);
	}

	// Item
	@Override
	public void inventoryTick(ItemStack stack, ServerLevel worldIn, Entity entityIn, @Nullable EquipmentSlot slot) {
		TRItemUtils.checkActive(stack, TechRebornConfig.nanosaberCost, entityIn);

		boolean isActive = stack.get(TRDataComponentTypes.IS_ACTIVE) == Boolean.TRUE;
		ItemAttributeModifiers attributes = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
		attributes = attributes.withModifierAdded(Attributes.ATTACK_DAMAGE, isActive ? ENABLED_ATTACK_DAMAGE_MODIFIER : DISABLED_ATTACK_DAMAGE_MODIFIER, EquipmentSlotGroup.MAINHAND)
			.withModifierAdded(Attributes.ATTACK_SPEED, isActive ? ENABLED_ATTACK_SPEED_MODIFIER : DISABLED_ATTACK_SPEED_MODIFIER, EquipmentSlotGroup.MAINHAND);
		stack.set(DataComponents.ATTRIBUTE_MODIFIERS, attributes);
	}

	@Override
	public InteractionResult use(final Level world, final Player player, final InteractionHand hand) {
		final ItemStack stack = player.getItemInHand(hand);
		if (player.isShiftKeyDown()) {
			TRItemUtils.switchActive(stack, TechRebornConfig.nanosaberCost, player);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> tooltip, TooltipFlag type) {
		TRItemUtils.buildActiveTooltip(stack, tooltip);
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		return ItemUtils.getPowerForDurabilityBar(stack);
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return true;
	}

	@Override
	public int getBarColor(ItemStack stack) {
		return ItemUtils.getColorForDurabilityBar(stack);
	}

	// RcEnergyItem
	@Override
	public long getEnergyCapacity(ItemStack stack) {
		return TechRebornConfig.nanosaberCharge;
	}

	@Override
	public RcEnergyTier getTier() {
		return RcEnergyTier.EXTREME;
	}

	@Override
	public long getEnergyMaxOutput(ItemStack stack) {
		return 0;
	}

}
