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
import techreborn.blocks.cable.BlockCable;
import techreborn.blocks.cable.EnumCableType;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.items.*;
import techreborn.items.ingredients.ItemDusts;
import techreborn.items.ingredients.ItemDustsSmall;
import techreborn.items.ingredients.ItemGems;
import techreborn.items.ingredients.ItemIngots;
import techreborn.items.ingredients.ItemNuggets;
import techreborn.items.ingredients.ItemParts;
import techreborn.items.ingredients.ItemPlates;

import java.util.Map;

public class RegisterItemJsons {
	public static void registerModels() {
		registerItems();
		registerBlocks();
	}

	private static void registerItems() {
		
		// Armor
		register(ModItems.CLOAKING_DEVICE, "armor/cloaking_device");
		register(ModItems.LAPOTRONIC_ORB_PACK, "armor/lapotronic_orbpack");
		register(ModItems.LITHIUM_BATTERY_PACK, "armor/lithium_batpack");		
		
		// Battery
		register(ModItems.ENERGY_CRYSTAL, "battery/energy_crystal");
		register(ModItems.LAPOTRONIC_CRYSTAL, "battery/lapotron_crystal");
		register(ModItems.LAPOTRONIC_ORB, "battery/lapotronic_orb");
		register(ModItems.LITHIUM_BATTERY, "battery/lithium_battery");
		register(ModItems.RE_BATTERY, "battery/re_battery");
		
		// Tools
		register(ModItems.ADVANCED_CHAINSAW, "tool/advanced_chainsaw");
		register(ModItems.ADVANCED_DRILL, "tool/advanced_drill");
		register(ModItems.ADVANCED_JACKHAMMER, "tool/advanced_jackhammer");
		register(ModItems.DEBUG, "tool/debug");
		register(ModItems.DIAMOND_CHAINSAW, "tool/diamond_chainsaw");
		register(ModItems.DIAMOND_DRILL, "tool/diamond_drill");
		register(ModItems.DIAMOND_JACKHAMMER, "tool/diamond_jackhammer");
		register(ModItems.ELECTRIC_TREE_TAP, "tool/electric_treetap");
		register(ModItems.NANOSABER, "tool/nanosaber");
		register(ModItems.OMNI_TOOL, "tool/omni_tool");
		register(ModItems.ROCK_CUTTER, "tool/rock_cutter");
		register(ModItems.STEEL_CHAINSAW, "tool/steel_chainsaw");
		register(ModItems.STEEL_DRILL, "tool/steel_drill");
		register(ModItems.STEEL_JACKHAMMER, "tool/steel_jackhammer");
		register(ModItems.TREE_TAP, "tool/treetap");
		register(ModItems.WRENCH, "tool/wrench");

		// Reactor >>
		register(ModItems.THORIUM_FUEL_ROD_SINGLE, "reactor/thorium_fuel_rod_single");
		register(ModItems.THORIUM_FUEL_ROD_DUAL, "reactor/thorium_fuel_rod_dual");
		register(ModItems.THORIUM_FUEL_ROD_QUAD, "reactor/thorium_fuel_rod_quad");
		register(ModItems.DEPLETED_THORIUM_FUEL_ROD_SINGLE, "reactor/depleted_thorium_fuel_rod_single");
		register(ModItems.DEPLETED_THORIUM_FUEL_ROD_DUAL, "reactor/depleted_thorium_fuel_rod_dual");
		register(ModItems.DEPLETED_THORIUM_FUEL_ROD_QUAD, "reactor/depleted_thorium_fuel_rod_quad");
		register(ModItems.IRIDIUM_NEUTRON_REFLECTOR, "reactor/iridium_neutron_reflector");
		// << Reactor

		// Misc
		register(ModItems.FREQUENCY_TRANSMITTER, "misc/frequency_transmitter");
		register(ModItems.MANUAL, "misc/manual");
		register(ModItems.MISSING_RECIPE_PLACEHOLDER, "misc/missing_recipe");
		register(ModItems.SCRAP_BOX, "misc/scrapbox");
		String[] name = ItemUpgrades.types.clone();
		for (int i = 0; i < ItemUpgrades.types.length; ++i) {
			registerBlockstate(ModItems.UPGRADES, i, name[i], "items/misc/");
		}
		
		if (ConfigTechReborn.enableGemArmorAndTools) {
			registerBlockstateMultiItem(ModItems.RUBY_HELMET, "ruby_helmet", "items/tool/armour");
			registerBlockstateMultiItem(ModItems.RUBY_CHESTPLATE, "ruby_chestplate", "items/tool/armour");
			registerBlockstateMultiItem(ModItems.RUBY_LEGGINGS, "ruby_leggings", "items/tool/armour");
			registerBlockstateMultiItem(ModItems.RUBY_BOOTS, "ruby_boots", "items/tool/armour");
			registerBlockstateMultiItem(ModItems.SAPPHIRE_HELMET, "sapphire_helmet", "items/tool/armour");
			registerBlockstateMultiItem(ModItems.SAPPHIRE_CHSTPLATE, "sapphire_chestplate", "items/tool/armour");
			registerBlockstateMultiItem(ModItems.SAPPHIRE_LEGGINGS, "sapphire_leggings", "items/tool/armour");
			registerBlockstateMultiItem(ModItems.SAPPHIRE_BOOTS, "sapphire_boots", "items/tool/armour");
			registerBlockstateMultiItem(ModItems.PERIDOT_HELMET, "peridot_helmet", "items/tool/armour");
			registerBlockstateMultiItem(ModItems.PERIDOT_CHESTPLATE, "peridot_chestplate", "items/tool/armour");
			registerBlockstateMultiItem(ModItems.PERIDOT_LEGGINGS, "peridot_leggings", "items/tool/armour");
			registerBlockstateMultiItem(ModItems.PERIDOT_BOOTS, "peridot_boots", "items/tool/armour");
			registerBlockstateMultiItem(ModItems.BRONZE_HELMET, "bronze_helmet", "items/tool/armour");
			registerBlockstateMultiItem(ModItems.BRONZE_CHESTPLATE, "bronze_chestplate", "items/tool/armour");
			registerBlockstateMultiItem(ModItems.BRONZE_LEGGINGS, "bronze_leggings", "items/tool/armour");
			registerBlockstateMultiItem(ModItems.BRONZE_BOOTS, "bronze_boots", "items/tool/armour");

			registerBlockstateMultiItem(ModItems.RUBY_PICKAXE, "ruby_pickaxe", "items/tool/tool");
			registerBlockstateMultiItem(ModItems.RUBY_SWORD, "ruby_sword", "items/tool/tool");
			registerBlockstateMultiItem(ModItems.RUBY_AXE, "ruby_axe", "items/tool/tool");
			registerBlockstateMultiItem(ModItems.RUBY_SPADE, "ruby_spade", "items/tool/tool");
			registerBlockstateMultiItem(ModItems.RUBY_HOE, "ruby_hoe", "items/tool/tool");
			registerBlockstateMultiItem(ModItems.SAPPHIRE_PICKAXE, "sapphire_pickaxe", "items/tool/tool");
			registerBlockstateMultiItem(ModItems.SAPPHIRE_SWORD, "sapphire_sword", "items/tool/tool");
			registerBlockstateMultiItem(ModItems.SAPPHIRE_AXE, "sapphire_axe", "items/tool/tool");
			registerBlockstateMultiItem(ModItems.SAPPHIRE_SPADE, "sapphire_spade", "items/tool/tool");
			registerBlockstateMultiItem(ModItems.SAPPHIRE_HOE, "sapphire_hoe", "items/tool/tool");
			registerBlockstateMultiItem(ModItems.PERIDOT_PICKAXE, "peridot_pickaxe", "items/tool/tool");
			registerBlockstateMultiItem(ModItems.PERIDOT_SWORD, "peridot_sword", "items/tool/tool");
			registerBlockstateMultiItem(ModItems.PERIDOT_AXE, "peridot_axe", "items/tool/tool");
			registerBlockstateMultiItem(ModItems.PERIDOT_SAPPHIRE, "peridot_spade", "items/tool/tool");
			registerBlockstateMultiItem(ModItems.PERIDOT_HOE, "peridot_hoe", "items/tool/tool");
			registerBlockstateMultiItem(ModItems.BRONZE_PICKAXE, "bronze_pickaxe", "items/tool/tool");
			registerBlockstateMultiItem(ModItems.BRONZE_SWORD, "bronze_sword", "items/tool/tool");
			registerBlockstateMultiItem(ModItems.BRONZE_AXE, "bronze_axe", "items/tool/tool");
			registerBlockstateMultiItem(ModItems.BRONZE_SPADE, "bronze_spade", "items/tool/tool");
			registerBlockstateMultiItem(ModItems.BRONZE_HOE, "bronze_hoe", "items/tool/tool");
		}

		// Ingredients
		name = ItemIngots.types.clone();
		for (int i = 0; i < ItemIngots.types.length; ++i) {
			registerBlockstate(ModItems.INGOTS, i, name[i], "items/materials/");
		}

		name = ItemGems.types.clone();
		for (int i = 0; i < ItemGems.types.length; ++i) {
			registerBlockstate(ModItems.GEMS, i, name[i], "items/materials/");
		}
		
		name = ItemPlates.types.clone();
		for (int i = 0; i < ItemPlates.types.length; ++i) {
			if (!name[i].equals(ModItems.META_PLACEHOLDER)) {
				registerBlockstate(ModItems.PLATES, i, name[i], "items/materials/");
			}
		}

		name = ItemNuggets.types.clone();
		for (int i = 0; i < ItemNuggets.types.length; ++i) {
			if (!name[i].equals(ModItems.META_PLACEHOLDER)) {
				registerBlockstate(ModItems.NUGGETS, i, name[i], "items/materials/");
			}
		}

		name = ItemDusts.types.clone();
		for (int i = 0; i < ItemDusts.types.length; ++i) {
			if (!name[i].equals(ModItems.META_PLACEHOLDER)) {
				registerBlockstate(ModItems.DUSTS, i, name[i], "items/materials/");
			}
		}

		name = ItemDustsSmall.types.clone();
		for (int i = 0; i < ItemDustsSmall.types.length; ++i) {
			if (!name[i].equals(ModItems.META_PLACEHOLDER)) {
				registerBlockstate(ModItems.SMALL_DUSTS, i, name[i], "items/materials/");
			}
		}

		name = ItemParts.types.clone();
		for (int i = 0; i < ItemParts.types.length; ++i) {
			if (!name[i].equals(ModItems.META_PLACEHOLDER)) {
				registerBlockstate(ModItems.PARTS, i, name[i], "items/materials/");
			}
		}
		
		register(ModItems.UU_MATTER, "misc/uu_matter");

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
		register(ModBlocks.RUBBER_SAPLING, "misc/rubber_sapling");
	}

	public static void setBlockStateMapper(Block block, String path, IProperty<?>... ignoredProperties) {
		final String slash = !path.isEmpty() ? "/" : "";
		ModelLoader.setCustomStateMapper(block, new DefaultStateMapper() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				Map<IProperty<?>, Comparable<?>> map = Maps.<IProperty<?>, Comparable<?>>newLinkedHashMap(state.getProperties());
				for (IProperty<?> iproperty : ignoredProperties) {
					map.remove(iproperty);
				}
				return new ModelResourceLocation(new ResourceLocation(block.getRegistryName().getNamespace(), path + slash + block.getRegistryName().getPath()), this.getPropertyString(map));
			}
		});
	}

	private static void register(Item item, int meta, String name) {
		ModelLoader.setCustomModelResourceLocation(item, meta,
			new ModelResourceLocation("techreborn:" + name, "inventory"));
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
		ResourceLocation loc = new ResourceLocation("techreborn", dir + i.getRegistryName().getPath());
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
		ResourceLocation loc = new ResourceLocation("techreborn", path);
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(loc, "type=" + variantName));
	}

	private static void registerBlockstateMultiItem(Item item, int meta, String variantName, String path) {
		ResourceLocation loc = new ResourceLocation("techreborn", path);
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(loc, "type=" + variantName));
	}
}
