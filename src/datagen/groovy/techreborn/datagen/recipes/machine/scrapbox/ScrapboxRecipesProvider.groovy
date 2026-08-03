/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2024 TechReborn
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

package techreborn.datagen.recipes.machine.scrapbox

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.minecraft.core.HolderLookup
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.TRContent

import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

class ScrapboxRecipesProvider extends TechRebornRecipesProvider {
	static List<String> OUTPUT = [
		//sapling
		"minecraft:acacia_sapling",
		"minecraft:bamboo",
		"minecraft:birch_sapling",
		"minecraft:cherry_sapling",
		"minecraft:dark_oak_sapling",
		"minecraft:jungle_sapling",
		"minecraft:mangrove_propagule",
		"minecraft:oak_sapling",
		"minecraft:pale_oak_sapling",
		"techreborn:rubber_sapling",
		"minecraft:spruce_sapling",
		//nugget
		"techreborn:aluminum_nugget",
		"techreborn:brass_nugget",
		"techreborn:bronze_nugget",
		"techreborn:chrome_nugget",
		"minecraft:copper_nugget",
		"techreborn:diamond_nugget",
		"techreborn:electrum_nugget",
		"techreborn:emerald_nugget",
		"minecraft:gold_nugget",
		"techreborn:hot_tungstensteel_nugget",
		"techreborn:invar_nugget",
		"techreborn:iridium_nugget",
		"minecraft:iron_nugget",
		"techreborn:lead_nugget",
		"techreborn:nickel_nugget",
		"techreborn:platinum_nugget",
		"techreborn:refined_iron_nugget",
		"techreborn:silver_nugget",
		"techreborn:steel_nugget",
		"techreborn:tin_nugget",
		"techreborn:titanium_nugget",
		"techreborn:tungsten_nugget",
		"techreborn:tungstensteel_nugget",
		"techreborn:zinc_nugget",
		//gem
		"minecraft:amethyst_shard",
		"minecraft:diamond",
		"minecraft:emerald",
		"minecraft:lapis_lazuli",
		"techreborn:peridot_gem",
		"minecraft:quartz",
		"techreborn:red_garnet_gem",
		"techreborn:ruby_gem",
		"techreborn:sapphire_gem",
		"techreborn:yellow_garnet_gem",
		//dust
		"techreborn:almandine_dust",
		"techreborn:aluminum_dust",
		"techreborn:andesite_dust",
		"techreborn:andradite_dust",
		"techreborn:ashes_dust",
		"techreborn:basalt_dust",
		"techreborn:bauxite_dust",
		"techreborn:brass_dust",
		"techreborn:bronze_dust",
		"techreborn:calcite_dust",
		"techreborn:charcoal_dust",
		"techreborn:chrome_dust",
		"techreborn:cinnabar_dust",
		"techreborn:clay_dust",
		"techreborn:coal_dust",
		"techreborn:dark_ashes_dust",
		"techreborn:diamond_dust",
		"techreborn:diorite_dust",
		"techreborn:electrum_dust",
		"techreborn:emerald_dust",
		"techreborn:ender_eye_dust",
		"techreborn:ender_pearl_dust",
		"techreborn:endstone_dust",
		"techreborn:flint_dust",
		"techreborn:galena_dust",
		"minecraft:glowstone_dust",
		"techreborn:granite_dust",
		"techreborn:grossular_dust",
		"techreborn:invar_dust",
		"techreborn:lazurite_dust",
		"techreborn:magnesium_dust",
		"techreborn:manganese_dust",
		"techreborn:marble_dust",
		"techreborn:netherrack_dust",
		"techreborn:nickel_dust",
		"techreborn:obsidian_dust",
		"techreborn:olivine_dust",
		"techreborn:peridot_dust",
		"techreborn:phosphorous_dust",
		"techreborn:platinum_dust",
		"techreborn:pyrite_dust",
		"techreborn:pyrope_dust",
		"techreborn:quartz_dust",
		"techreborn:red_garnet_dust",
		"minecraft:redstone",
		"techreborn:ruby_dust",
		"techreborn:saltpeter_dust",
		"techreborn:sapphire_dust",
		"techreborn:saw_dust",
		"techreborn:sodalite_dust",
		"techreborn:spessartine_dust",
		"techreborn:sphalerite_dust",
		"techreborn:steel_dust",
		"techreborn:sulfur_dust",
		"techreborn:titanium_dust",
		"techreborn:uvarovite_dust",
		"techreborn:yellow_garnet_dust",
		"techreborn:zinc_dust",
		//raw
		"minecraft:raw_copper",
		"minecraft:raw_gold",
		"minecraft:raw_iron",
		"techreborn:raw_lead",
		"techreborn:raw_silver",
		"techreborn:raw_tin",
		"techreborn:raw_uranium",
		//small dust
		"techreborn:almandine_small_dust",
		"techreborn:andesite_small_dust",
		"techreborn:andradite_small_dust",
		"techreborn:ashes_small_dust",
		"techreborn:basalt_small_dust",
		"techreborn:bauxite_small_dust",
		"techreborn:calcite_small_dust",
		"techreborn:charcoal_small_dust",
		"techreborn:chrome_small_dust",
		"techreborn:cinnabar_small_dust",
		"techreborn:clay_small_dust",
		"techreborn:coal_small_dust",
		"techreborn:dark_ashes_small_dust",
		"techreborn:diamond_small_dust",
		"techreborn:diorite_small_dust",
		"techreborn:electrum_small_dust",
		"techreborn:emerald_small_dust",
		"techreborn:ender_eye_small_dust",
		"techreborn:ender_pearl_small_dust",
		"techreborn:endstone_small_dust",
		"techreborn:flint_small_dust",
		"techreborn:galena_small_dust",
		"techreborn:glowstone_small_dust",
		"techreborn:granite_small_dust",
		"techreborn:grossular_small_dust",
		"techreborn:invar_small_dust",
		"techreborn:lazurite_small_dust",
		"techreborn:magnesium_small_dust",
		"techreborn:manganese_small_dust",
		"techreborn:marble_small_dust",
		"techreborn:netherrack_small_dust",
		"techreborn:nickel_small_dust",
		"techreborn:obsidian_small_dust",
		"techreborn:olivine_small_dust",
		"techreborn:peridot_small_dust",
		"techreborn:phosphorous_small_dust",
		"techreborn:platinum_small_dust",
		"techreborn:pyrite_small_dust",
		"techreborn:pyrope_small_dust",
		"techreborn:quartz_small_dust",
		"techreborn:red_garnet_small_dust",
		"techreborn:redstone_small_dust",
		"techreborn:ruby_small_dust",
		"techreborn:saltpeter_small_dust",
		"techreborn:sapphire_small_dust",
		"techreborn:saw_small_dust",
		"techreborn:sodalite_small_dust",
		"techreborn:spessartine_small_dust",
		"techreborn:sphalerite_small_dust",
		"techreborn:steel_small_dust",
		"techreborn:sulfur_small_dust",
		"techreborn:titanium_small_dust",
		"techreborn:tungsten_small_dust",
		"techreborn:uvarovite_small_dust",
		"techreborn:yellow_garnet_small_dust",
		"techreborn:zinc_small_dust",
		//misc
		"techreborn:cell",
		"minecraft:charcoal",
		"minecraft:clay",
		"minecraft:coal",
		"minecraft:flint",
		"minecraft:gunpowder",
		"minecraft:player_head",
		"minecraft:player_head",
		"minecraft:player_head",
		"minecraft:player_head",
		"techreborn:rubber",
		"techreborn:sap",
	]

	ScrapboxRecipesProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generateRecipes() {
		OUTPUT.each { outputItem ->
			offerScrapboxRecipe {
				power 10
				time 20
				ingredients TRContent.SCRAP_BOX
				outputs outputItem
			}
		}
	}

	// Read all of the outputs from the scrapbox recipes
	static void main(String[] args) {
		Path dir = Path.of("src/main/resources/data/techreborn/recipes/scrapbox/auto")
		List<String> outputs = new ArrayList<>()
		Files.walk(dir)
			.filter { it.toString().endsWith(".json") }
			.forEach { file ->
				String content = Files.readString(file)
				JsonObject json = new JsonParser().parse(content).getAsJsonObject()
				String value = json.getAsJsonArray("results").get(0).getAsJsonObject().get("item").getAsString()
				outputs.addAll(value)
			}

		println("static List<String> OUTPUT = [")
		outputs.each { println("    \"$it\",") }
		println("]")
	}
}
