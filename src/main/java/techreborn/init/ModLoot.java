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

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.function.SetDamageLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRContent.Ingots;
import techreborn.init.TRContent.Parts;

public class ModLoot {

	public static void init() {

		LootPoolEntry copperIngot = makeEntry(Items.COPPER_INGOT);
		LootPoolEntry tinIngot = makeEntry(Ingots.TIN);
		LootPoolEntry leadIngot = makeEntry(Ingots.LEAD);
		LootPoolEntry silverIngot = makeEntry(Ingots.SILVER);
		LootPoolEntry refinedIronIngot = makeEntry(Ingots.REFINED_IRON);
		LootPoolEntry advancedAlloyIngot = makeEntry(Ingots.ADVANCED_ALLOY);
		LootPoolEntry basicFrame = makeEntry(TRContent.MachineBlocks.BASIC.frame.asItem());
		LootPoolEntry basicCircuit = makeEntry(Parts.ELECTRONIC_CIRCUIT);
		LootPoolEntry rubberSapling = makeEntry(TRContent.RUBBER_SAPLING, 25);

		LootPool poolBasic = LootPool.builder().with(copperIngot).with(tinIngot)
			.with(leadIngot).with(silverIngot).with(refinedIronIngot).with(advancedAlloyIngot)
			.with(basicFrame).with(basicCircuit).with(rubberSapling).rolls(UniformLootNumberProvider.create(1.0f, 2.0f))
			.build();

		LootPoolEntry aluminumIngot = makeEntry(Ingots.ALUMINUM);
		LootPoolEntry electrumIngot = makeEntry(Ingots.ELECTRUM);
		LootPoolEntry invarIngot = makeEntry(Ingots.INVAR);
		LootPoolEntry nickelIngot = makeEntry(Ingots.NICKEL);
		LootPoolEntry steelIngot = makeEntry(Ingots.STEEL);
		LootPoolEntry zincIngot = makeEntry(Ingots.ZINC);
		LootPoolEntry advancedFrame = makeEntry(TRContent.MachineBlocks.ADVANCED.frame.asItem());
		LootPoolEntry advancedCircuit = makeEntry(Parts.ADVANCED_CIRCUIT);
		LootPoolEntry dataStorageChip = makeEntry(Parts.DATA_STORAGE_CHIP);

		LootPool poolAdvanced = LootPool.builder().with(aluminumIngot).with(electrumIngot)
			.with(invarIngot).with(nickelIngot).with(steelIngot).with(zincIngot)
			.with(advancedFrame).with(advancedCircuit).with(dataStorageChip).rolls(UniformLootNumberProvider.create(1.0f, 3.0f))
			.build();

		LootPoolEntry chromeIngot = makeEntry(Ingots.CHROME);
		LootPoolEntry iridiumIngot = makeEntry(Ingots.IRIDIUM);
		LootPoolEntry platinumIngot = makeEntry(Ingots.PLATINUM);
		LootPoolEntry titaniumIngot = makeEntry(Ingots.TITANIUM);
		LootPoolEntry tungstenIngot = makeEntry(Ingots.TUNGSTEN);
		LootPoolEntry tungstensteelIngot = makeEntry(Ingots.TUNGSTENSTEEL);
		LootPoolEntry industrialFrame = makeEntry(TRContent.MachineBlocks.INDUSTRIAL.frame.asItem());
		LootPoolEntry industrialCircuit = makeEntry(Parts.INDUSTRIAL_CIRCUIT);
		LootPoolEntry energyFlowChip = makeEntry(Parts.ENERGY_FLOW_CHIP);

		LootPool poolIndustrial = LootPool.builder().with(chromeIngot).with(iridiumIngot)
				.with(platinumIngot).with(titaniumIngot).with(tungstenIngot).with(tungstensteelIngot)
				.with(industrialFrame).with(industrialCircuit).with(energyFlowChip).rolls(UniformLootNumberProvider.create(1.0f, 3.0f))
				.build();

		LootPoolEntry rubber = ItemEntry.builder(Parts.RUBBER).weight(10).build();
		LootPoolEntry treeTap = ItemEntry.builder(TRContent.TREE_TAP).weight(10)
			.apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.0f, 0.9f))).build();
		LootPoolEntry scrap = ItemEntry.builder(Parts.SCRAP).weight(10).build();

		LootPool poolFishingJunk = LootPool.builder().with(rubber).with(treeTap).with(scrap).build();


		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			String stringId = id.toString();
			if (!stringId.startsWith("minecraft:gameplay") && !stringId.startsWith("minecraft:chests")) {
				return;
			}

			if (TechRebornConfig.enableOverworldLoot) {
				switch (stringId) {
					case "minecraft:chests/abandoned_mineshaft",
						"minecraft:chests/desert_pyramid",
						"minecraft:chests/igloo_chest",
						"minecraft:chests/jungle_temple",
						"minecraft:chests/simple_dungeon",
						"minecraft:chests/shipwreck_treasure",
						"minecraft:chest/underwater_ruin_small",
						"minecraft:chests/village/village_weaponsmith",
						"minecraft:chests/village/village_armorer",
						"minecraft:chests/village/village_toolsmith"
						-> tableBuilder.pool(poolBasic);
					case "minecraft:chests/stronghold_corridor",
						"minecraft:chests/stronghold_crossing",
						"minecraft:chests/stronghold_library",
						"minecraft:chest/underwater_ruin_big",
						"minecraft:chests/pillager_outpost"
						-> tableBuilder.pool(poolAdvanced);
					case "minecraft:chests/woodland_mansion",
						"minecraft:chests/ancient_city"
						-> tableBuilder.pool(poolIndustrial);
				}
			}

			if (TechRebornConfig.enableNetherLoot) {
				if (stringId.equals("minecraft:chests/nether_bridge") ||
						stringId.equals("minecraft:chests/bastion_bridge") ||
						stringId.equals("minecraft:chests/bastion_hoglin_stable") ||
						stringId.equals("minecraft:chests/bastion_treasure") ||
						stringId.equals("minecraft:chests/bastion_other")) {
					tableBuilder.pool(poolAdvanced);
				}
			}

			if (TechRebornConfig.enableEndLoot) {
				if (stringId.equals("minecraft:chests/end_city_treasure")) {
					tableBuilder.pool(poolIndustrial);
				}
			}

			if (TechRebornConfig.enableFishingJunkLoot) {
				if (stringId.equals("minecraft:gameplay/fishing/junk")) {
					tableBuilder.pool(poolFishingJunk);
				}
			}
		});

	}

	/**
	 * Makes loot entry from item provided
	 *
	 * @param item {@link ItemConvertible} Item to include into LootEntry
	 * @return {@link LootPoolEntry} Entry for item provided
	 */
	private static LootPoolEntry makeEntry(ItemConvertible item) {
		return makeEntry(item, 5);
	}

	/**
	 * Makes loot entry from item provided with weight provided
	 *
	 * @param item   {@link ItemConvertible} Item to include into LootEntry
	 * @param weight {@code int} Weight of that item
	 * @return {@link LootPoolEntry} Entry for item and weight provided
	 */
	private static LootPoolEntry makeEntry(ItemConvertible item, int weight) {
		return ItemEntry.builder(item).weight(weight)
				.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))).build();
	}


}
