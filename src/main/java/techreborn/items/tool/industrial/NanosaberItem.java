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

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import reborncore.common.powerSystem.RcEnergyItem;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.util.ItemUtils;
import techreborn.component.TRDataComponentTypes;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRToolMaterials;
import techreborn.utils.TRItemUtils;

import java.util.List;

public class NanosaberItem extends SwordItem implements RcEnergyItem {
	private static final EntityAttributeModifier ENABLED_ATTACK_DAMAGE_MODIFIER = new EntityAttributeModifier(Identifier.of("techreborn", "nano_saber_attack_damage"), TechRebornConfig.nanosaberDamage, EntityAttributeModifier.Operation.ADD_VALUE);
	private static final EntityAttributeModifier ENABLED_ATTACK_SPEED_MODIFIER = new EntityAttributeModifier(Identifier.of("techreborn", "nano_saber_attack_speed"), 3, EntityAttributeModifier.Operation.ADD_VALUE);
	private static final EntityAttributeModifier DISABLED_ATTACK_DAMAGE_MODIFIER = new EntityAttributeModifier(Identifier.of("techreborn", "nano_saber_attack_damage"), 0, EntityAttributeModifier.Operation.ADD_VALUE);
	private static final EntityAttributeModifier DISABLED_ATTACK_SPEED_MODIFIER = new EntityAttributeModifier(Identifier.of("techreborn", "nano_saber_attack_speed"), 0, EntityAttributeModifier.Operation.ADD_VALUE);

	// 1ME max charge with 2k charge rate
	public NanosaberItem() {
		super(TRToolMaterials.NANOSABER, new Item.Settings()
			.maxDamage(0)
			.attributeModifiers(PickaxeItem.createAttributeModifiers(TRToolMaterials.NANOSABER, 1, 1))
		);
	}

	// SwordItem
	@Override
	public boolean postHit(ItemStack stack, LivingEntity entityHit, LivingEntity entityHitter) {
		tryUseEnergy(stack, TechRebornConfig.nanosaberCost);
		return true;
	}

	// ToolItem
	@Override
	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return false;
	}

	// Item
	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		TRItemUtils.checkActive(stack, TechRebornConfig.nanosaberCost, entityIn);

		boolean isActive = stack.get(TRDataComponentTypes.IS_ACTIVE) == Boolean.TRUE;
		AttributeModifiersComponent attributes = stack.getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT);
		attributes = attributes.with(EntityAttributes.GENERIC_ATTACK_DAMAGE, isActive ? ENABLED_ATTACK_DAMAGE_MODIFIER : DISABLED_ATTACK_DAMAGE_MODIFIER, AttributeModifierSlot.MAINHAND)
			.with(EntityAttributes.GENERIC_ATTACK_SPEED, isActive ? ENABLED_ATTACK_SPEED_MODIFIER : DISABLED_ATTACK_SPEED_MODIFIER, AttributeModifierSlot.MAINHAND);
		stack.set(DataComponentTypes.ATTRIBUTE_MODIFIERS, attributes);
	}

	@Override
	public TypedActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
		final ItemStack stack = player.getStackInHand(hand);
		if (player.isSneaking()) {
			TRItemUtils.switchActive(stack, TechRebornConfig.nanosaberCost, player);
			return new TypedActionResult<>(ActionResult.SUCCESS, stack);
		}
		return new TypedActionResult<>(ActionResult.PASS, stack);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		TRItemUtils.buildActiveTooltip(stack, tooltip);
	}

	@Override
	public int getItemBarStep(ItemStack stack) {
		return ItemUtils.getPowerForDurabilityBar(stack);
	}

	@Override
	public boolean isItemBarVisible(ItemStack stack) {
		return true;
	}

	@Override
	public int getItemBarColor(ItemStack stack) {
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
