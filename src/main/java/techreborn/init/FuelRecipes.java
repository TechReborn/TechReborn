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

package techreborn.init;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

// Class containing definitions of burnable materials
public class FuelRecipes {
	public static Item.Properties apply(String name, Item.Properties properties) {
		ResourceKey<ContextIntProvider> burnTime = switch (name) {
			case "rubber_slab" -> ContextIntProviders.COOKING_TIME_WOOD_SLABS;
			case "rubber_door", "treetap" -> ContextIntProviders.COOKING_TIME_WOOD_ITEMS_LARGE;
			case "rubber_sapling" -> ContextIntProviders.COOKING_TIME_DRY_PLANTS;
			case "rubber_button", "rubber_log", "rubber_log_stripped", "rubber_wood", "stripped_rubber_wood",
				"rubber_planks", "rubber_fence", "rubber_fence_gate", "rubber_stair", "rubber_trapdoor",
				"rubber_pressure_plate", "resin_basin", "wood_plate" -> ContextIntProviders.COOKING_TIME_WOOD_BLOCKS;
			default -> null;
		};
		return burnTime == null ? properties : properties.cookingFuel(burnTime);
	}

	public static int getBurnTime(Level level, BlockPos pos, ItemStack stack) {
		CookingFuel fuel = stack.get(DataComponents.COOKING_FUEL);
		if (fuel == null || !(level instanceof ServerLevel serverLevel)) {
			return 0;
		}
		LootContext context = new LootContext.Builder(
			new LootParams.Builder(serverLevel)
				.withParameter(LootContextParams.BLOCK_STATE, level.getBlockState(pos))
				.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
				.create(LootContextParamSets.BLOCK_INTERACT)
		).create(Optional.empty());
		return fuel.burnTime().get(context, 0);
	}
}
