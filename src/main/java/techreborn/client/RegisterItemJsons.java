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

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import techreborn.TechReborn;
import techreborn.blocks.cable.BlockCable;
import techreborn.blocks.cable.EnumCableType;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.init.TRContent;
import techreborn.init.TRItems;

import java.util.Map;

public class RegisterItemJsons {
	public static void registerModels() {
		TRContent.registerModel();
		registerItems();
		registerBlocks();
	}

	private static void registerItems() {
		// Armor
		register(TRItems.CLOAKING_DEVICE, "armor/cloaking_device");
		register(TRItems.LAPOTRONIC_ORBPACK, "armor/lapotronic_orbpack");
		register(TRItems.LITHIUM_ION_BATPACK, "armor/lithium_batpack");

		// Battery
		register(TRItems.RED_CELL_BATTERY, "battery/re_battery");
		register(TRItems.LITHIUM_ION_BATTERY, "battery/lithium_battery");
		register(TRItems.ENERGY_CRYSTAL, "battery/energy_crystal");
		register(TRItems.LAPOTRON_CRYSTAL, "battery/lapotron_crystal");
		register(TRItems.LAPOTRONIC_ORB, "battery/lapotronic_orb");

		// Tools
		register(TRItems.INDUSTRIAL_CHAINSAW, "tool/industrial_chainsaw");
		register(TRItems.INDUSTRIAL_DRILL, "tool/industrial_drill");
		register(TRItems.INDUSTRIAL_JACKHAMMER, "tool/industrial_jackhammer");
		register(TRItems.DEBUG_TOOL, "tool/debug");
		register(TRItems.ADVANCED_CHAINSAW, "tool/advanced_chainsaw");
		register(TRItems.ADVANCED_DRILL, "tool/advanced_drill");
		register(TRItems.ADVANCED_JACKHAMMER, "tool/advanced_jackhammer");
		register(TRItems.ELECTRIC_TREE_TAP, "tool/electric_treetap");
		register(TRItems.NANOSABER, "tool/nanosaber");
		register(TRItems.OMNI_TOOL, "tool/omni_tool");
		register(TRItems.ROCK_CUTTER, "tool/rock_cutter");
		register(TRItems.BASIC_CHAINSAW, "tool/basic_chainsaw");
		register(TRItems.BASIC_DRILL, "tool/basic_drill");
		register(TRItems.BASIC_JACKHAMMER, "tool/basic_jackhammer");
		register(TRItems.TREE_TAP, "tool/treetap");
		register(TRItems.WRENCH, "tool/wrench");

		// Other
		register(TRItems.FREQUENCY_TRANSMITTER, "misc/frequency_transmitter");
		register(TRItems.SCRAP_BOX, "misc/scrapbox");
		register(TRItems.MANUAL, "misc/manual");
		;

		// Gem armor & tools
		if (ConfigTechReborn.enableGemArmorAndTools) {
			registerBlockstateMultiItem(TRItems.RUBY_HELMET, "ruby_helmet", "items/armour");
			registerBlockstateMultiItem(TRItems.RUBY_CHESTPLATE, "ruby_chestplate", "items/armour");
			registerBlockstateMultiItem(TRItems.RUBY_LEGGINGS, "ruby_leggings", "items/armour");
			registerBlockstateMultiItem(TRItems.RUBY_BOOTS, "ruby_boots", "items/armour");
			registerBlockstateMultiItem(TRItems.SAPPHIRE_HELMET, "sapphire_helmet", "items/armour");
			registerBlockstateMultiItem(TRItems.SAPPHIRE_CHESTPLATE, "sapphire_chestplate", "items/armour");
			registerBlockstateMultiItem(TRItems.SAPPHIRE_LEGGINGS, "sapphire_leggings", "items/armour");
			registerBlockstateMultiItem(TRItems.SAPPHIRE_BOOTS, "sapphire_boots", "items/armour");
			registerBlockstateMultiItem(TRItems.PERIDOT_HELMET, "peridot_helmet", "items/armour");
			registerBlockstateMultiItem(TRItems.PERIDOT_CHESTPLATE, "peridot_chestplate", "items/armour");
			registerBlockstateMultiItem(TRItems.PERIDOT_LEGGINGS, "peridot_leggings", "items/armour");
			registerBlockstateMultiItem(TRItems.PERIDOT_BOOTS, "peridot_boots", "items/armour");
			registerBlockstateMultiItem(TRItems.BRONZE_HELMET, "bronze_helmet", "items/armour");
			registerBlockstateMultiItem(TRItems.BRONZE_CHESTPLATE, "bronze_chestplate", "items/armour");
			registerBlockstateMultiItem(TRItems.BRONZE_LEGGINGS, "bronze_leggings", "items/armour");
			registerBlockstateMultiItem(TRItems.BRONZE_BOOTS, "bronze_boots", "items/armour");

			registerBlockstateMultiItem(TRItems.RUBY_PICKAXE, "ruby_pickaxe", "items/tool");
			registerBlockstateMultiItem(TRItems.RUBY_SWORD, "ruby_sword", "items/tool");
			registerBlockstateMultiItem(TRItems.RUBY_AXE, "ruby_axe", "items/tool");
			registerBlockstateMultiItem(TRItems.RUBY_SPADE, "ruby_spade", "items/tool");
			registerBlockstateMultiItem(TRItems.RUBY_HOE, "ruby_hoe", "items/tool");
			registerBlockstateMultiItem(TRItems.SAPPHIRE_PICKAXE, "sapphire_pickaxe", "items/tool");
			registerBlockstateMultiItem(TRItems.SAPPHIRE_SWORD, "sapphire_sword", "items/tool");
			registerBlockstateMultiItem(TRItems.SAPPHIRE_AXE, "sapphire_axe", "items/tool");
			registerBlockstateMultiItem(TRItems.SAPPHIRE_SPADE, "sapphire_spade", "items/tool");
			registerBlockstateMultiItem(TRItems.SAPPHIRE_HOE, "sapphire_hoe", "items/tool");
			registerBlockstateMultiItem(TRItems.PERIDOT_PICKAXE, "peridot_pickaxe", "items/tool");
			registerBlockstateMultiItem(TRItems.PERIDOT_SWORD, "peridot_sword", "items/tool");
			registerBlockstateMultiItem(TRItems.PERIDOT_AXE, "peridot_axe", "items/tool");
			registerBlockstateMultiItem(TRItems.PERIDOT_SPADE, "peridot_spade", "items/tool");
			registerBlockstateMultiItem(TRItems.PERIDOT_HOE, "peridot_hoe", "items/tool");
			registerBlockstateMultiItem(TRItems.BRONZE_PICKAXE, "bronze_pickaxe", "items/tool");
			registerBlockstateMultiItem(TRItems.BRONZE_SWORD, "bronze_sword", "items/tool");
			registerBlockstateMultiItem(TRItems.BRONZE_AXE, "bronze_axe", "items/tool");
			registerBlockstateMultiItem(TRItems.BRONZE_SPADE, "bronze_spade", "items/tool");
			registerBlockstateMultiItem(TRItems.BRONZE_HOE, "bronze_hoe", "items/tool");
		}

		register(ModBlocks.RUBBER_SAPLING, "misc/rubber_sapling");

		for (EnumCableType cableType : EnumCableType.values()) {
			registerBlockstateMultiItem(Item.getItemFromBlock(ModBlocks.CABLE), cableType.ordinal(), cableType.getName().toLowerCase(), "cable_inv");
		}

		ModelLoader.setCustomStateMapper(ModBlocks.CABLE, new DefaultStateMapper() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				Map<IProperty<?>, Comparable<?>> map = Maps.<IProperty<?>, Comparable<?>>newLinkedHashMap(state.getProperties());
				if (state.getValue(BlockCable.TYPE).ordinal() <= 4) {
					return new ModelResourceLocation(new ResourceLocation(ModBlocks.CABLE.getRegistryName().getNamespace(), ModBlocks.CABLE.getRegistryName().getPath()) + "_thin", this.getPropertyString(map));
				}
				return new ModelResourceLocation(new ResourceLocation(ModBlocks.CABLE.getRegistryName().getNamespace(), ModBlocks.CABLE.getRegistryName().getPath()) + "_thick", this.getPropertyString(map));
			}
		});
	}

	private static void registerBlocks() {
		register(ModBlocks.REFINED_IRON_FENCE, "iron_fence");

	}

	private static void register(Item item, int meta, String name) {
		ResourceLocation loc = new ResourceLocation(TechReborn.MOD_ID, name);
		ModelLoader.setCustomModelResourceLocation(item, meta,
			new ModelResourceLocation(loc, "inventory"));
	}

	private static void register(Item item, String name) {
		register(item, 0, name);
	}

	@SuppressWarnings("unused")
	private static void register(Block block, int meta, String name) {
		register(Item.getItemFromBlock(block), meta, name);
	}

	private static void register(Block block, String name) {
		register(Item.getItemFromBlock(block), 0, name);
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
		ResourceLocation loc = new ResourceLocation(TechReborn.MOD_ID, path);
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(loc, "type=" + variantName));
	}

	private static void registerBlockstateMultiItem(Item item, int meta, String variantName, String path) {
		ResourceLocation loc = new ResourceLocation(TechReborn.MOD_ID, path);
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(loc, "type=" + variantName));
	}
}
