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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import reborncore.api.items.ItemStackModifiers;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.ChatUtils;
import reborncore.common.util.ItemDurabilityExtensions;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.StringUtils;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHolder;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyTier;
import techreborn.TechReborn;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRContent;
import techreborn.utils.MessageIDs;

import javax.annotation.Nullable;
import java.util.List;

public class NanosaberItem extends SwordItem implements EnergyHolder, ItemDurabilityExtensions, ItemStackModifiers {
	public static final int maxCharge = TechRebornConfig.nanoSaberCharge;
	public int transferLimit = 1_000;
	public int cost = 250;

	// 4M FE max charge with 1k charge rate
	public NanosaberItem() {
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
					if (Energy.of(stack).getMaxStored() - Energy.of(stack).getEnergy() >= 0.9 * Energy.of(stack).getMaxStored()) {
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
		return Energy.of(stack).use(cost);
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
			ItemUtils.switchActive(stack, cost, world.isClient, MessageIDs.poweredToolID);
			return new TypedActionResult<>(ActionResult.SUCCESS, stack);
		}
		return new TypedActionResult<>(ActionResult.PASS, stack);
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		ItemUtils.checkActive(stack, cost, entityIn.world.isClient, MessageIDs.poweredToolID);
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
		inactiveUncharged.getOrCreateTag().putBoolean("isActive", false);

		ItemStack inactiveCharged = new ItemStack(TRContent.NANOSABER);
		inactiveCharged.setTag(new CompoundTag());
		inactiveCharged.getOrCreateTag().putBoolean("isActive", false);
		Energy.of(inactiveCharged).set(Energy.of(inactiveCharged).getMaxStored());

		ItemStack activeCharged = new ItemStack(TRContent.NANOSABER);
		activeCharged.setTag(new CompoundTag());
		activeCharged.getOrCreateTag().putBoolean("isActive", true);
		Energy.of(activeCharged).set(Energy.of(activeCharged).getMaxStored());

		itemList.add(inactiveUncharged);
		itemList.add(inactiveCharged);
		itemList.add(activeCharged);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World worldIn, List<Text> tooltip, TooltipContext flagIn) {
		ItemUtils.buildActiveTooltip(stack, tooltip);
	}

	// IEnergyItemInfo
	@Override
	public double getMaxStoredPower() {
		return maxCharge;
	}

	@Override
	public EnergyTier getTier() {
		return EnergyTier.HIGH;
	}

	@Override
	public double getMaxInput(EnergySide side) {
		return transferLimit;
	}

	@Override
	public double getMaxOutput(EnergySide side) {
		return 0;
	}
}
