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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import reborncore.api.items.ArmorBlockEntityTicker;
import reborncore.common.powerSystem.RcEnergyTier;
import techreborn.config.TechRebornConfig;

public class NanoSuitItem extends TREnergyArmourItem implements ArmorBlockEntityTicker {

	public NanoSuitItem(ArmorMaterial material, Type slot) {
		super(material, slot, TechRebornConfig.nanoSuitCapacity, RcEnergyTier.HIGH);
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
		var attributes = ArrayListMultimap.create(super.getAttributeModifiers(stack, getSlotType()));

		if (equipmentSlot == this.getSlotType() && getStoredEnergy(stack) > 0) {
			attributes.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(MODIFIERS[getSlotType().getEntitySlotId()], "Armor modifier", 8, EntityAttributeModifier.Operation.ADDITION));
			attributes.put(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, new EntityAttributeModifier(MODIFIERS[getSlotType().getEntitySlotId()], "Knockback modifier", 2, EntityAttributeModifier.Operation.ADDITION));
		}

		return ImmutableMultimap.copyOf(attributes);
	}

	// ArmorBlockEntityTicker
	@Override
	public void tickArmor(ItemStack stack, PlayerEntity playerEntity) {
		World world = playerEntity.getWorld();
		switch (this.getSlotType()) {
			case HEAD -> {
				if ((world.isNight() || world.getLightLevel(playerEntity.getBlockPos()) <= 4) && tryUseEnergy(stack, TechRebornConfig.nanoSuitNightVisionCost)) {
					playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 1000, 1, false, false));
				} else {
					playerEntity.removeStatusEffect(StatusEffects.NIGHT_VISION);
				}
			}
			case FEET -> {
				if (tryUseEnergy(stack, TechRebornConfig.nanoSuitFlightCost)) {
					playerEntity.setNoGravity(true);
					playerEntity.updatePosition(playerEntity.getX(), playerEntity.getY() + 0.1, playerEntity.getZ());
					world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, playerEntity.getX(), playerEntity.getY(),  playerEntity.getZ(), 0.0, -1.0, 0.0);
				} else {
					playerEntity.setNoGravity(false);
					playerEntity.sendMessage(Text.literal("uwu"));
				}
			}
		}
	}

	public void boostPlayer(ItemStack stack, PlayerEntity playerEntity) {
		World world = playerEntity.getWorld();
		if (tryUseEnergy(stack, TechRebornConfig.nanoSuitFlightCost)) {
			playerEntity.setNoGravity(true);
			playerEntity.updatePosition(playerEntity.getX(), playerEntity.getY() + 0.3, playerEntity.getZ());
			world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, playerEntity.getX(), playerEntity.getY(),  playerEntity.getZ(), 1.0, 1.0, 1.0);
		} else {
			playerEntity.setNoGravity(false);
		}
	}
}
