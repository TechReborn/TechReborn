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

import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.UniformLootTableRange;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRContent.Ingots;
import techreborn.init.TRContent.Parts;

public class ModLoot {

	public static void init() {

		LootEntry copperIngot = makeEntry(Ingots.COPPER);
		LootEntry tinIngot = makeEntry(Ingots.TIN);
		LootEntry leadIngot = makeEntry(Ingots.LEAD);
		LootEntry silverIngot = makeEntry(Ingots.SILVER);
		LootEntry refinedronIngot = makeEntry(Ingots.REFINED_IRON);
		LootEntry advancedalloyIngot = makeEntry(Ingots.ADVANCED_ALLOY);
		LootEntry basicFrame = makeEntry(TRContent.MachineBlocks.BASIC.frame.asItem());
		LootEntry basicCircuit = makeEntry(Parts.ELECTRONIC_CIRCUIT);
		
		LootEntry aluminumIngot = makeEntry(Ingots.ALUMINUM);
		LootEntry electrumIngot = makeEntry(Ingots.ELECTRUM);
		LootEntry invarIngot = makeEntry(Ingots.INVAR);
		LootEntry nickelIngot = makeEntry(Ingots.NICKEL);
		LootEntry steelIngot = makeEntry(Ingots.STEEL);
		LootEntry zincIngot = makeEntry(Ingots.ZINC);
		LootEntry advancedFrame = makeEntry(TRContent.MachineBlocks.ADVANCED.frame.asItem());
		LootEntry advancedCircuit = makeEntry(Parts.ADVANCED_CIRCUIT);
		LootEntry dataStorageChip = makeEntry(Parts.DATA_STORAGE_CHIP);
		
		LootEntry chromeIngot = makeEntry(Ingots.CHROME);		
		LootEntry iridiumIngot = makeEntry(Ingots.IRIDIUM);		
		LootEntry platinumIngot = makeEntry(Ingots.PLATINUM);
		LootEntry titaniumIngot = makeEntry(Ingots.TITANIUM);
		LootEntry tungstenIngot = makeEntry(Ingots.TUNGSTEN);
		LootEntry tungstensteelIngot = makeEntry(Ingots.TUNGSTENSTEEL);
		LootEntry industrialFrame = makeEntry(TRContent.MachineBlocks.INDUSTRIAL.frame.asItem());
		LootEntry industrialCircuit = makeEntry(Parts.INDUSTRIAL_CIRCUIT);
		LootEntry energyFlowChip = makeEntry(Parts.ENERGY_FLOW_CHIP);
		
		

		LootPool poolBasic = FabricLootPoolBuilder.builder().withEntry(copperIngot).withEntry(tinIngot)
				.withEntry(leadIngot).withEntry(silverIngot).withEntry(refinedronIngot).withEntry(advancedalloyIngot)
				.withEntry(basicFrame).withEntry(basicCircuit).withRolls(UniformLootTableRange.between(1.0f, 2.0f))
				.build();
		
		LootPool poolAdvanced = FabricLootPoolBuilder.builder().withEntry(aluminumIngot).withEntry(electrumIngot)
				.withEntry(invarIngot).withEntry(nickelIngot).withEntry(steelIngot).withEntry(zincIngot)
				.withEntry(advancedFrame).withEntry(advancedCircuit).withEntry(dataStorageChip).withRolls(UniformLootTableRange.between(1.0f, 3.0f))
				.build();

		LootPool poolIndustrial = FabricLootPoolBuilder.builder().withEntry(chromeIngot).withEntry(iridiumIngot)
				.withEntry(platinumIngot).withEntry(titaniumIngot).withEntry(tungstenIngot).withEntry(tungstensteelIngot)
				.withEntry(industrialFrame).withEntry(industrialCircuit).withEntry(energyFlowChip).withRolls(UniformLootTableRange.between(1.0f, 3.0f))
				.build();
		
		LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, ident, supplier, setter) -> {
			String stringId = ident.toString();
			if (!stringId.startsWith("minecraft:chests")) {
				return;
			}

			if (TechRebornConfig.enableOverworldLoot) {
				switch (stringId) {
				case "minecraft:chests/abandoned_mineshaft":
                    case "minecraft:chests/desert_pyramid":
                    case "minecraft:chests/igloo_chest":
                    case "minecraft:chests/jungle_temple":
                    case "minecraft:chests/simple_dungeon":
                    case "minecraft:chests/village/village_weaponsmith":
                    case "minecraft:chests/village/village_armorer":
                    case "minecraft:chests/village/village_toolsmith":
                        supplier.withPool(poolBasic);
					break;
                    case "minecraft:chests/stronghold_corridor":
                    case "minecraft:chests/stronghold_crossing":
                    case "minecraft:chests/stronghold_library":
                        supplier.withPool(poolAdvanced);
					break;
                    case "minecraft:chests/woodland_mansion":
					 supplier.withPool(poolIndustrial);
					break;
				}
			}

			if (TechRebornConfig.enableNetherLoot) {
				if (stringId.equals("minecraft:chests/nether_bridge")) {
					supplier.withPool(poolAdvanced);
				}
			}

			if (TechRebornConfig.enableEndLoot) {
				if (stringId.equals("minecraft:chests/end_city_treasure")) {
					supplier.withPool(poolIndustrial);
				}
			}

		});
	}

	/**
	 * Makes loot entry from item provided
	 * 
	 * @param item Item to include into LootEntry
	 * @return LootEntry for item provided
	 */
	private static LootEntry makeEntry(ItemConvertible item) {

		return ItemEntry.builder(item).setWeight(5)
				.withFunction(SetCountLootFunction.builder(UniformLootTableRange.between(1.0f, 2.0f))).build();
	}


}
