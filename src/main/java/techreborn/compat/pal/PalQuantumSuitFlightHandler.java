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

package techreborn.compat.pal;

import io.github.ladysnake.pal.AbilitySource;
import io.github.ladysnake.pal.Pal;
import io.github.ladysnake.pal.VanillaAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import techreborn.TechReborn;
import techreborn.items.armor.QuantumSuitFlightHandler;

public class PalQuantumSuitFlightHandler extends QuantumSuitFlightHandler {
	private static final AbilitySource QUANTUM_CHESTPLATE_FLIGHT = Pal.getAbilitySource(Identifier.of(TechReborn.MOD_ID, "quantum_chestplate_flight"), AbilitySource.CONSUMABLE);

	@Override
	public void setAllowFlight(PlayerEntity playerEntity, boolean allowed) {
		if (allowed) {
			if (!QUANTUM_CHESTPLATE_FLIGHT.grants(playerEntity, VanillaAbilities.ALLOW_FLYING)) {
				QUANTUM_CHESTPLATE_FLIGHT.grantTo(playerEntity, VanillaAbilities.ALLOW_FLYING);
			}
		} else {
			if (QUANTUM_CHESTPLATE_FLIGHT.grants(playerEntity, VanillaAbilities.ALLOW_FLYING)) {
				QUANTUM_CHESTPLATE_FLIGHT.revokeFrom(playerEntity, VanillaAbilities.ALLOW_FLYING);
			}
		}
	}

	@Override
	public boolean isFlying(PlayerEntity playerEntity) {
		return playerEntity.getAbilities().flying && QUANTUM_CHESTPLATE_FLIGHT.isActivelyGranting(playerEntity, VanillaAbilities.ALLOW_FLYING);
	}
}
