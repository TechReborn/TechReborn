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

package techreborn.api.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import techreborn.blockentity.cable.CableBlockEntity;
import techreborn.init.TRContent;

public interface CableElectrocutionEvent {
	Event<CableElectrocutionEvent> EVENT = EventFactory.createArrayBacked(CableElectrocutionEvent.class, (listeners) ->
		(livingEntity, cableType, blockPos, world, cableBlockEntity) -> {
			for (CableElectrocutionEvent listener : listeners) {
				if (!listener.electrocute(livingEntity, cableType, blockPos, world, cableBlockEntity)) {
					return false;
				}
			}
			return true;
		}
	);

	/**
	 * Fired before an entity is electrocuted
	 *
	 * @return true to electrocute the entity (if not other listeners return false), false to do nothing
	 */
	boolean electrocute(LivingEntity livingEntity, TRContent.Cables cableType, BlockPos blockPos, World world, CableBlockEntity cableBlockEntity);
}
