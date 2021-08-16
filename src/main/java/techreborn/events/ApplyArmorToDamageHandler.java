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

package techreborn.events;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import reborncore.api.events.ApplyArmorToDamageCallback;
import techreborn.config.TechRebornConfig;
import techreborn.items.armor.QuantumSuitItem;

public class ApplyArmorToDamageHandler implements ApplyArmorToDamageCallback {

	public static void init() {
		ApplyArmorToDamageCallback.EVENT.register(new ApplyArmorToDamageHandler());
	}

	@Override
	public float applyArmorToDamage(PlayerEntity player, DamageSource source, float amount) {
		double damageAbsorbed = 0.0d;
		for (ItemStack stack : player.getArmorItems()) {
			if (!(stack.getItem() instanceof QuantumSuitItem item)) {
				continue;
			}

			double stackEnergy = item.getStoredEnergy(stack);
			if (stackEnergy == 0.0d) {
				continue;
			}

			double damageToAbsorb = Math.min(stackEnergy, amount * 0.2d);
			if (item.tryUseEnergy(stack, (long) (damageToAbsorb * TechRebornConfig.damageAbsorbCost))) {
				damageAbsorbed += damageToAbsorb;
			}
		}

		return amount - (float) damageAbsorbed;
	}
}
