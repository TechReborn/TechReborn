/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2026 TechReborn
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

package techreborn.items.reactor;

import net.minecraft.world.item.ItemStack;

import techreborn.blockentity.generator.nuclear.NuclearReactorBlockEntity;

/**
 * Reactor plating that increases max heat capacity and reduces explosion
 * effects.
 */
public class ReactorPlatingItem extends ReactorComponentItem {

	private final int heatCapacityBonus;
	private final float explosionMultiplier;

	public ReactorPlatingItem(String name, int heatCapacityBonus, float explosionMultiplier) {
		super(name, 0); // No durability
		this.heatCapacityBonus = heatCapacityBonus;
		this.explosionMultiplier = explosionMultiplier;
	}

	@Override
	public void processHeat(ItemStack stack, NuclearReactorBlockEntity reactor, int x, int y) {
		reactor.addMaxHeat(heatCapacityBonus);
		if (explosionMultiplier < 1.0f) {
			reactor.multiplyHeatEffectModifier(explosionMultiplier);
		}
	}

	@Override
	public float getExplosionInfluence(ItemStack stack) {
		return explosionMultiplier < 1.0f ? explosionMultiplier : 0;
	}
}
