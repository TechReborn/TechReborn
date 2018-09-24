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

package techreborn.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import techreborn.TechReborn;
import techreborn.config.ConfigTechReborn;
import techreborn.init.TRBlocks;
import techreborn.init.TRContent;

public class RegisterItemJsons {
	public static void registerModels() {
		TRContent.registerModel();
		registerItems();
		registerBlocks();
	}

	private static void registerItems() {
		// Armor
		register(TRContent.CLOAKING_DEVICE, "armor/cloaking_device");
		register(TRContent.LAPOTRONIC_ORBPACK, "armor/lapotronic_orbpack");
		register(TRContent.LITHIUM_ION_BATPACK, "armor/lithium_batpack");

		// Battery
		register(TRContent.RED_CELL_BATTERY, "battery/re_battery");
		register(TRContent.LITHIUM_ION_BATTERY, "battery/lithium_battery");
		register(TRContent.ENERGY_CRYSTAL, "battery/energy_crystal");
		register(TRContent.LAPOTRON_CRYSTAL, "battery/lapotron_crystal");
		register(TRContent.LAPOTRONIC_ORB, "battery/lapotronic_orb");

		// Tools
		register(TRContent.INDUSTRIAL_CHAINSAW, "tool/industrial_chainsaw");
		register(TRContent.INDUSTRIAL_DRILL, "tool/industrial_drill");
		register(TRContent.INDUSTRIAL_JACKHAMMER, "tool/industrial_jackhammer");
		register(TRContent.DEBUG_TOOL, "tool/debug");
		register(TRContent.ADVANCED_CHAINSAW, "tool/advanced_chainsaw");
		register(TRContent.ADVANCED_DRILL, "tool/advanced_drill");
		register(TRContent.ADVANCED_JACKHAMMER, "tool/advanced_jackhammer");
		register(TRContent.ELECTRIC_TREE_TAP, "tool/electric_treetap");
		register(TRContent.NANOSABER, "tool/nanosaber");
		register(TRContent.OMNI_TOOL, "tool/omni_tool");
		register(TRContent.ROCK_CUTTER, "tool/rock_cutter");
		register(TRContent.BASIC_CHAINSAW, "tool/basic_chainsaw");
		register(TRContent.BASIC_DRILL, "tool/basic_drill");
		register(TRContent.BASIC_JACKHAMMER, "tool/basic_jackhammer");
		register(TRContent.TREE_TAP, "tool/treetap");
		register(TRContent.WRENCH, "tool/wrench");

		// Other
		register(TRContent.FREQUENCY_TRANSMITTER, "misc/frequency_transmitter");
		register(TRContent.SCRAP_BOX, "misc/scrapbox");
		register(TRContent.MANUAL, "misc/manual");
		;

		// Gem armor & tools
		if (ConfigTechReborn.enableGemArmorAndTools) {
			registerBlockstateMultiItem(TRContent.RUBY_HELMET, "ruby_helmet", "items/armour");
			registerBlockstateMultiItem(TRContent.RUBY_CHESTPLATE, "ruby_chestplate", "items/armour");
			registerBlockstateMultiItem(TRContent.RUBY_LEGGINGS, "ruby_leggings", "items/armour");
			registerBlockstateMultiItem(TRContent.RUBY_BOOTS, "ruby_boots", "items/armour");
			registerBlockstateMultiItem(TRContent.SAPPHIRE_HELMET, "sapphire_helmet", "items/armour");
			registerBlockstateMultiItem(TRContent.SAPPHIRE_CHESTPLATE, "sapphire_chestplate", "items/armour");
			registerBlockstateMultiItem(TRContent.SAPPHIRE_LEGGINGS, "sapphire_leggings", "items/armour");
			registerBlockstateMultiItem(TRContent.SAPPHIRE_BOOTS, "sapphire_boots", "items/armour");
			registerBlockstateMultiItem(TRContent.PERIDOT_HELMET, "peridot_helmet", "items/armour");
			registerBlockstateMultiItem(TRContent.PERIDOT_CHESTPLATE, "peridot_chestplate", "items/armour");
			registerBlockstateMultiItem(TRContent.PERIDOT_LEGGINGS, "peridot_leggings", "items/armour");
			registerBlockstateMultiItem(TRContent.PERIDOT_BOOTS, "peridot_boots", "items/armour");
			registerBlockstateMultiItem(TRContent.BRONZE_HELMET, "bronze_helmet", "items/armour");
			registerBlockstateMultiItem(TRContent.BRONZE_CHESTPLATE, "bronze_chestplate", "items/armour");
			registerBlockstateMultiItem(TRContent.BRONZE_LEGGINGS, "bronze_leggings", "items/armour");
			registerBlockstateMultiItem(TRContent.BRONZE_BOOTS, "bronze_boots", "items/armour");

			registerBlockstateMultiItem(TRContent.RUBY_PICKAXE, "ruby_pickaxe", "items/tool");
			registerBlockstateMultiItem(TRContent.RUBY_SWORD, "ruby_sword", "items/tool");
			registerBlockstateMultiItem(TRContent.RUBY_AXE, "ruby_axe", "items/tool");
			registerBlockstateMultiItem(TRContent.RUBY_SPADE, "ruby_spade", "items/tool");
			registerBlockstateMultiItem(TRContent.RUBY_HOE, "ruby_hoe", "items/tool");
			registerBlockstateMultiItem(TRContent.SAPPHIRE_PICKAXE, "sapphire_pickaxe", "items/tool");
			registerBlockstateMultiItem(TRContent.SAPPHIRE_SWORD, "sapphire_sword", "items/tool");
			registerBlockstateMultiItem(TRContent.SAPPHIRE_AXE, "sapphire_axe", "items/tool");
			registerBlockstateMultiItem(TRContent.SAPPHIRE_SPADE, "sapphire_spade", "items/tool");
			registerBlockstateMultiItem(TRContent.SAPPHIRE_HOE, "sapphire_hoe", "items/tool");
			registerBlockstateMultiItem(TRContent.PERIDOT_PICKAXE, "peridot_pickaxe", "items/tool");
			registerBlockstateMultiItem(TRContent.PERIDOT_SWORD, "peridot_sword", "items/tool");
			registerBlockstateMultiItem(TRContent.PERIDOT_AXE, "peridot_axe", "items/tool");
			registerBlockstateMultiItem(TRContent.PERIDOT_SPADE, "peridot_spade", "items/tool");
			registerBlockstateMultiItem(TRContent.PERIDOT_HOE, "peridot_hoe", "items/tool");
			registerBlockstateMultiItem(TRContent.BRONZE_PICKAXE, "bronze_pickaxe", "items/tool");
			registerBlockstateMultiItem(TRContent.BRONZE_SWORD, "bronze_sword", "items/tool");
			registerBlockstateMultiItem(TRContent.BRONZE_AXE, "bronze_axe", "items/tool");
			registerBlockstateMultiItem(TRContent.BRONZE_SPADE, "bronze_spade", "items/tool");
			registerBlockstateMultiItem(TRContent.BRONZE_HOE, "bronze_hoe", "items/tool");
		}

		register(TRBlocks.RUBBER_SAPLING, "misc/rubber_sapling");
	}

	private static void registerBlocks() {
		register(TRBlocks.REFINED_IRON_FENCE, "iron_fence");
	}

	private static void register(Item item, int meta, String name) {
		ResourceLocation loc = new ResourceLocation(TechReborn.MOD_ID, name);
		ModelLoader.setCustomModelResourceLocation(item, meta,
			new ModelResourceLocation(loc, "inventory"));
	}

	private static void register(Item item, String name) {
		register(item, 0, name);
	}

	private static void register(Block block, int meta, String name) {
		register(Item.getItemFromBlock(block), meta, name);
	}

	private static void register(Block block, String name) {
		register(block, 0, name);
	}

	@SuppressWarnings("unused")
	private static void registerBlockstate(Item i, int meta, String variant) {
		registerBlockstate(i, meta, variant, "");
	}

	private static void registerBlockstate(Item i, int meta, String variant, String dir) {
		ResourceLocation loc = new ResourceLocation(TechReborn.MOD_ID, dir + i.getRegistryName().getPath());
		ModelLoader.setCustomModelResourceLocation(i, meta, new ModelResourceLocation(loc, "type=" + variant));
	}

	@SuppressWarnings("unused")
	private static void registerBlockstate(Block i, int meta, String variant) {
		registerBlockstate(i, meta, variant, "");
	}

	private static void registerBlockstate(Block i, int meta, String variant, String dir) {
		registerBlockstate(Item.getItemFromBlock(i), meta, variant, dir);
	}

	private static void registerBlockstateMultiItem(Item item, String variantName, String path) {
		registerBlockstateMultiItem(item, 0, variantName, path);
	}

	private static void registerBlockstateMultiItem(Item item, int meta, String variantName, String path) {
		ResourceLocation loc = new ResourceLocation(TechReborn.MOD_ID, path);
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(loc, "type=" + variantName));
	}
}
