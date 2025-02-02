/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2025 TechReborn
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

package techreborn.init;

import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;

import java.util.WeakHashMap;

public class TRAbility {
	private static final Object PRESENT = new Object();
	public static final WeakHashMap<PlayerEntity, Object> ALLOW_FLYING = new WeakHashMap<>();
	public static final WeakHashMap<PlayerEntity, Object> ALLOW_FREE_FLYING = new WeakHashMap<>();

	public static void allowFlying(PlayerEntity player) {
		ALLOW_FLYING.put(player, PRESENT);
		player.getAbilities().allowFlying = true;
		player.sendAbilitiesUpdate();
	}

	public static void dontAllowFlying(PlayerEntity player) {
		if (ALLOW_FLYING.remove(player) == null) return;
		if (hasFlyingCost(player)) {
			PlayerAbilities abilities = player.getAbilities();
			abilities.allowFlying = false;
			abilities.flying = false;
			player.sendAbilitiesUpdate();
		}
	}

	public static void allowFreeFlying(PlayerEntity player) {
		ALLOW_FREE_FLYING.put(player, PRESENT);
	}

	public static void dontAllowFreeFlying(PlayerEntity player) {
		ALLOW_FREE_FLYING.remove(player);
	}

	public static boolean hasFlyingCost(PlayerEntity player) {
		return !(player.isCreative() || player.isSpectator() || ALLOW_FREE_FLYING.containsKey(player));
	}
}
