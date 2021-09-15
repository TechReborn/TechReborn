/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore.api.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;

public interface ApplyArmorToDamageCallback {

	Event<ApplyArmorToDamageCallback> EVENT = EventFactory.createArrayBacked(ApplyArmorToDamageCallback.class,
			(listeners) -> (player, damageSource, amount) -> {
				float damageAmount = amount;
				for (ApplyArmorToDamageCallback listener : listeners){
					damageAmount = listener.applyArmorToDamage(player, damageSource, damageAmount);
				}
				return damageAmount;
			});

	/**
	 *  Apply armor to amount of damage inflicted. Decreases it in most cases unless armor should increase damage inflicted.
	 *  Event is called after damage is being reduced by armor already and before damage reduction from enchants.
	 *
	 * @param player PlayerEntity Player being damaged
	 * @param source DamageSource Type of damage
	 * @param amount float Current amount of damage
	 * @return float Amount of damage after reduction
	 */
	float applyArmorToDamage(PlayerEntity player, DamageSource source, float amount);
}
