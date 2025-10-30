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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRContent.Ingots;
import techreborn.init.TRContent.Parts;

public class ModLoot {

	public static void init() {

		LootPoolEntryContainer copperIngot = makeEntry(Items.COPPER_INGOT);
		LootPoolEntryContainer tinIngot = makeEntry(Ingots.TIN);
		LootPoolEntryContainer leadIngot = makeEntry(Ingots.LEAD);
		LootPoolEntryContainer silverIngot = makeEntry(Ingots.SILVER);
		LootPoolEntryContainer refinedIronIngot = makeEntry(Ingots.REFINED_IRON);
		LootPoolEntryContainer advancedAlloyIngot = makeEntry(Ingots.ADVANCED_ALLOY);
		LootPoolEntryContainer basicFrame = makeEntry(TRContent.MachineBlocks.BASIC.frame.asItem());
		LootPoolEntryContainer basicCircuit = makeEntry(Parts.ELECTRONIC_CIRCUIT);
		LootPoolEntryContainer rubberSapling = makeEntry(TRContent.RUBBER_SAPLING, 25);

		LootPool poolBasic = LootPool.lootPool().with(copperIngot).with(tinIngot)
			.with(leadIngot).with(silverIngot).with(refinedIronIngot).with(advancedAlloyIngot)
			.with(basicFrame).with(basicCircuit).with(rubberSapling).setRolls(UniformGenerator.between(1.0f, 2.0f))
			.build();

		LootPoolEntryContainer aluminumIngot = makeEntry(Ingots.ALUMINUM);
		LootPoolEntryContainer electrumIngot = makeEntry(Ingots.ELECTRUM);
		LootPoolEntryContainer invarIngot = makeEntry(Ingots.INVAR);
		LootPoolEntryContainer nickelIngot = makeEntry(Ingots.NICKEL);
		LootPoolEntryContainer steelIngot = makeEntry(Ingots.STEEL);
		LootPoolEntryContainer zincIngot = makeEntry(Ingots.ZINC);
		LootPoolEntryContainer advancedFrame = makeEntry(TRContent.MachineBlocks.ADVANCED.frame.asItem());
		LootPoolEntryContainer advancedCircuit = makeEntry(Parts.ADVANCED_CIRCUIT);
		LootPoolEntryContainer dataStorageChip = makeEntry(Parts.DATA_STORAGE_CHIP);

		LootPool poolAdvanced = LootPool.lootPool().with(aluminumIngot).with(electrumIngot)
			.with(invarIngot).with(nickelIngot).with(steelIngot).with(zincIngot)
			.with(advancedFrame).with(advancedCircuit).with(dataStorageChip).setRolls(UniformGenerator.between(1.0f, 3.0f))
			.build();

		LootPoolEntryContainer chromeIngot = makeEntry(Ingots.CHROME);
		LootPoolEntryContainer iridiumIngot = makeEntry(Ingots.IRIDIUM);
		LootPoolEntryContainer platinumIngot = makeEntry(Ingots.PLATINUM);
		LootPoolEntryContainer titaniumIngot = makeEntry(Ingots.TITANIUM);
		LootPoolEntryContainer tungstenIngot = makeEntry(Ingots.TUNGSTEN);
		LootPoolEntryContainer tungstensteelIngot = makeEntry(Ingots.TUNGSTENSTEEL);
		LootPoolEntryContainer industrialFrame = makeEntry(TRContent.MachineBlocks.INDUSTRIAL.frame.asItem());
		LootPoolEntryContainer industrialCircuit = makeEntry(Parts.INDUSTRIAL_CIRCUIT);
		LootPoolEntryContainer energyFlowChip = makeEntry(Parts.ENERGY_FLOW_CHIP);

		LootPool poolIndustrial = LootPool.lootPool().with(chromeIngot).with(iridiumIngot)
				.with(platinumIngot).with(titaniumIngot).with(tungstenIngot).with(tungstensteelIngot)
				.with(industrialFrame).with(industrialCircuit).with(energyFlowChip).setRolls(UniformGenerator.between(1.0f, 3.0f))
				.build();

		LootTableEvents.MODIFY.register((key, tableBuilder, source) -> {
			String stringId = key.location().toString();
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
						"minecraft:chests/underwater_ruin_small",
						"minecraft:chests/village/village_weaponsmith",
						"minecraft:chests/village/village_armorer",
						"minecraft:chests/village/village_toolsmith"
						-> tableBuilder.pool(poolBasic);
					case "minecraft:chests/stronghold_corridor",
						"minecraft:chests/stronghold_crossing",
						"minecraft:chests/stronghold_library",
						"minecraft:chests/underwater_ruin_big",
						"minecraft:chests/pillager_outpost"
						-> tableBuilder.pool(poolAdvanced);
					case "minecraft:chests/woodland_mansion",
						"minecraft:chests/ancient_city"
						-> tableBuilder.pool(poolIndustrial);
					case "minecraft:archeology/trail_ruins_common"
						-> tableBuilder.modifyPools(poolBuilder -> poolBuilder.with(LootItem.lootTableItem(Parts.RUBBER).build()));
					case "minecraft:gameplay/cat_morning_gift"
						-> tableBuilder.modifyPools(poolBuilder -> poolBuilder.with(LootItem.lootTableItem(Parts.SCRAP).setWeight(5).build()));
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
					LootPoolEntryContainer rubber = LootItem.lootTableItem(Parts.RUBBER).setWeight(10).build();
					LootPoolEntryContainer treeTap = LootItem.lootTableItem(TRContent.TREE_TAP).setWeight(10)
						.apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.0f, 0.9f))).build();
					LootPoolEntryContainer scrap = LootItem.lootTableItem(Parts.SCRAP).setWeight(10).build();
					tableBuilder.modifyPools(poolBuilder -> poolBuilder
						.with(rubber).with(treeTap).with(scrap));
				}
			}
		});

	}

	/**
	 * Makes loot entry from item provided
	 *
	 * @param item {@link ItemLike} Item to include into LootEntry
	 * @return {@link LootPoolEntryContainer} Entry for item provided
	 */
	private static LootPoolEntryContainer makeEntry(ItemLike item) {
		return makeEntry(item, 5);
	}

	/**
	 * Makes loot entry from item provided with weight provided
	 *
	 * @param item   {@link ItemLike} Item to include into LootEntry
	 * @param weight {@code int} Weight of that item
	 * @return {@link LootPoolEntryContainer} Entry for item and weight provided
	 */
	private static LootPoolEntryContainer makeEntry(ItemLike item, int weight) {
		return LootItem.lootTableItem(item).setWeight(weight)
				.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))).build();
	}


}
