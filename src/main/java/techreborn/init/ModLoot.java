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

package techreborn.init;

import net.minecraft.util.Identifier;
import net.minecraft.world.loot.LootPool;
import net.minecraft.world.loot.LootTables;
import net.minecraft.world.loot.UniformLootTableRange;
import net.minecraft.world.loot.condition.LootCondition;
import net.minecraft.world.loot.entry.LootTableEntry;
import net.minecraft.world.storage.loot.*;


import techreborn.TechReborn;
import techreborn.config.ConfigTechReborn;

import java.util.ArrayList;
import java.util.List;

public class ModLoot {

	public static List<Identifier> lootTables = new ArrayList<Identifier>();

	public static void init() {
		if (ConfigTechReborn.enableOverworldLoot) {
			lootTables.add(new Identifier(TechReborn.MOD_ID, "chests/abandoned_mineshaft"));
			lootTables.add(new Identifier(TechReborn.MOD_ID, "chests/desert_pyramid"));
			lootTables.add(new Identifier(TechReborn.MOD_ID, "chests/igloo_chest"));
			lootTables.add(new Identifier(TechReborn.MOD_ID, "chests/jungle_temple"));
			lootTables.add(new Identifier(TechReborn.MOD_ID, "chests/simple_dungeon"));
			lootTables.add(new Identifier(TechReborn.MOD_ID, "chests/stronghold_corridor"));
			lootTables.add(new Identifier(TechReborn.MOD_ID, "chests/stronghold_crossing"));
			lootTables.add(new Identifier(TechReborn.MOD_ID, "chests/stronghold_library"));
			lootTables.add(new Identifier(TechReborn.MOD_ID, "chests/village_blacksmith"));
			lootTables.add(new Identifier(TechReborn.MOD_ID, "chests/woodland_mansion"));
		}
		if (ConfigTechReborn.enableNetherLoot) {
			lootTables.add(new Identifier(TechReborn.MOD_ID, "chests/nether_bridge"));
		}
		if (ConfigTechReborn.enableEndLoot) {
			lootTables.add(new Identifier(TechReborn.MOD_ID, "chests/end_city_treasure"));
		}

		for (Identifier lootTable : lootTables) {
			LootTables.registerLootTable(lootTable);
		}
	}

	@SubscribeEvent
	public void lootLoad(LootTableLoadEvent event) {
		if (!event.getName().getNamespace().equals("minecraft")) {
			return;
		}
		for (Identifier lootTable : lootTables) {
			if (event.getName().getNamespace().equals(lootTable.getPath())) {
				event.getTable().addPool(getLootPool(lootTable));
				TechReborn.LOGGER.debug("Loot pool injected into " + lootTable.getPath());
			}
		}
	}

	/**
	 * Generates loot pool to be injected into vanilla loot pools
	 *
	 * @param lootTable ResourceLocation Loot table to inject
	 * @return LootPool Loot pool to inject
	 */
	private LootPool getLootPool(Identifier lootTable) {
		LootEntry entry = new LootTableEntry(lootTable, 1, 0, new LootCondition[0], "lootEntry_" + lootTable.toString());
		LootPool pool = new LootPool(new LootEntry[] { entry }, new LootCondition[0], new UniformLootTableRange(1), new UniformLootTableRange(0, 1), "lootPool_" + lootTable.toString());
		return pool;
	}
}
