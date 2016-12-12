package techreborn.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;
import techreborn.blocks.BlockOre;
import techreborn.blocks.BlockOre2;
import techreborn.blocks.BlockStorage;
import techreborn.blocks.BlockStorage2;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.items.*;
import techreborn.parts.TechRebornParts;
import techreborn.parts.powerCables.EnumCableType;

public class RegisterItemJsons {
	public static void registerModels() {
		registerItems();
		registerBlocks();
	}

	private static void registerItems() {
		register(ModItems.reBattery, "battery/re_battery");
		register(ModItems.lithiumBattery, "battery/lithium_battery");
		register(ModItems.energyCrystal, "battery/energy_crystal");
		register(ModItems.lapotronCrystal, "battery/lapotron_crystal");
		register(ModItems.lapotronicOrb, "battery/lapotronic_orb");

		register(ModItems.frequencyTransmitter, "misc/frequency_transmitter");
		register(ModItems.uuMatter, "misc/uu_matter");
		register(ModItems.scrapBox, "misc/scrapbox");
		register(ModItems.manual, "misc/manual");
		register(ModItems.debug, "misc/debug");
		register(ModBlocks.rubberSapling, "misc/rubber_sapling");

		register(ModItems.ironDrill, "tool/steel_drill");
		register(ModItems.diamondDrill, "tool/diamond_drill");
		register(ModItems.advancedDrill, "tool/advanced_drill");
		register(ModItems.ironChainsaw, "tool/steel_chainsaw");
		register(ModItems.diamondChainsaw, "tool/diamond_chainsaw");
		register(ModItems.advancedChainsaw, "tool/advanced_chainsaw");
		register(ModItems.steelJackhammer, "tool/steel_jackhammer");
		register(ModItems.diamondJackhammer, "tool/diamond_jackhammer");
		register(ModItems.advancedJackhammer, "tool/advanced_jackhammer");
		register(ModItems.nanosaber, "tool/nanosaber");
		register(ModItems.treeTap, "tool/treetap");
		register(ModItems.electricTreetap, "tool/electric_treetap");
		register(ModItems.cloakingDevice, "tool/cloaking_device");
		register(ModItems.omniTool, "tool/omni_tool");
		register(ModItems.rockCutter, "tool/rock_cutter");
		register(ModItems.wrench, "tool/wrench");
		register(ModItems.lapotronpack, "tool/lapotronic_orbpack");
		register(ModItems.lithiumBatpack, "tool/lithium_batpack");

		registerBlockstateMultiItem(ModItems.rubyHelmet, "ruby_helmet", "items/tool/armour");
		registerBlockstateMultiItem(ModItems.rubyChestplate, "ruby_chestplate", "items/tool/armour");
		registerBlockstateMultiItem(ModItems.rubyLeggings, "ruby_leggings", "items/tool/armour");
		registerBlockstateMultiItem(ModItems.rubyBoots, "ruby_boots", "items/tool/armour");
		registerBlockstateMultiItem(ModItems.sapphireHelmet, "sapphire_helmet", "items/tool/armour");
		registerBlockstateMultiItem(ModItems.sapphireChestplate, "sapphire_chestplate", "items/tool/armour");
		registerBlockstateMultiItem(ModItems.sapphireLeggings, "sapphire_leggings", "items/tool/armour");
		registerBlockstateMultiItem(ModItems.sapphireBoots, "sapphire_boots", "items/tool/armour");
		registerBlockstateMultiItem(ModItems.peridotHelmet, "peridot_helmet", "items/tool/armour");
		registerBlockstateMultiItem(ModItems.peridotChestplate, "peridot_chestplate", "items/tool/armour");
		registerBlockstateMultiItem(ModItems.peridotLeggings, "peridot_leggings", "items/tool/armour");
		registerBlockstateMultiItem(ModItems.peridotBoots, "peridot_boots", "items/tool/armour");
		registerBlockstateMultiItem(ModItems.bronzeHelmet, "bronze_helmet", "items/tool/armour");
		registerBlockstateMultiItem(ModItems.bronzeChestplate, "bronze_chestplate", "items/tool/armour");
		registerBlockstateMultiItem(ModItems.bronzeLeggings, "bronze_leggings", "items/tool/armour");
		registerBlockstateMultiItem(ModItems.bronzeBoots, "bronze_boots", "items/tool/armour");

		registerBlockstateMultiItem(ModItems.rubyPickaxe, "ruby_pickaxe", "items/tool/tool");
		registerBlockstateMultiItem(ModItems.rubySword, "ruby_sword", "items/tool/tool");
		registerBlockstateMultiItem(ModItems.rubyAxe, "ruby_axe", "items/tool/tool");
		registerBlockstateMultiItem(ModItems.rubySpade, "ruby_spade", "items/tool/tool");
		registerBlockstateMultiItem(ModItems.rubyHoe, "ruby_hoe", "items/tool/tool");
		registerBlockstateMultiItem(ModItems.sapphirePickaxe, "sapphire_pickaxe", "items/tool/tool");
		registerBlockstateMultiItem(ModItems.sapphireSword, "sapphire_sword", "items/tool/tool");
		registerBlockstateMultiItem(ModItems.sapphireAxe, "sapphire_axe", "items/tool/tool");
		registerBlockstateMultiItem(ModItems.sapphireSpade, "sapphire_spade", "items/tool/tool");
		registerBlockstateMultiItem(ModItems.sapphireHoe, "sapphire_hoe", "items/tool/tool");
		registerBlockstateMultiItem(ModItems.peridotPickaxe, "peridot_pickaxe", "items/tool/tool");
		registerBlockstateMultiItem(ModItems.peridotSword, "peridot_sword", "items/tool/tool");
		registerBlockstateMultiItem(ModItems.peridotAxe, "peridot_axe", "items/tool/tool");
		registerBlockstateMultiItem(ModItems.peridotSpade, "peridot_spade", "items/tool/tool");
		registerBlockstateMultiItem(ModItems.peridotHoe, "peridot_hoe", "items/tool/tool");
		registerBlockstateMultiItem(ModItems.bronzePickaxe, "bronze_pickaxe", "items/tool/tool");
		registerBlockstateMultiItem(ModItems.bronzeSword, "bronze_sword", "items/tool/tool");
		registerBlockstateMultiItem(ModItems.bronzeAxe, "bronze_axe", "items/tool/tool");
		registerBlockstateMultiItem(ModItems.bronzeSpade, "bronze_spade", "items/tool/tool");
		registerBlockstateMultiItem(ModItems.bronzeHoe, "bronze_hoe", "items/tool/tool");

		for (int i = 0; i < ItemIngots.types.length; ++i) {
			String[] name = ItemIngots.types.clone();
			registerBlockstate(ModItems.ingots, i, name[i], "items/materials/");
		}

		for (int i = 0; i < ItemGems.types.length; ++i) {
			String[] name = ItemGems.types.clone();
			registerBlockstate(ModItems.gems, i, name[i], "items/materials/");
		}

		for (int i = 0; i < ItemPlates.types.length; ++i) {
			String[] name = ItemPlates.types.clone();
			registerBlockstate(ModItems.plate, i, name[i], "items/materials/");
		}

		for (int i = 0; i < ItemNuggets.types.length; ++i) {
			String[] name = ItemNuggets.types.clone();
			registerBlockstate(ModItems.nuggets, i, name[i], "items/materials/");
		}

		for (int i = 0; i < ItemDusts.types.length; ++i) {
			String[] name = ItemDusts.types.clone();
			registerBlockstate(ModItems.dusts, i, name[i], "items/materials/");
		}

		for (int i = 0; i < ItemDustsSmall.types.length; ++i) {
			String[] name = ItemDustsSmall.types.clone();
			registerBlockstate(ModItems.smallDusts, i, name[i], "items/materials/");
		}

		for (int i = 0; i < ItemParts.types.length; ++i) {
			String[] name = ItemParts.types.clone();
			registerBlockstate(ModItems.parts, i, name[i], "items/materials/");
		}

		for (int i = 0; i < ItemUpgrades.types.length; ++i) {
			String[] name = ItemUpgrades.types.clone();
			registerBlockstate(ModItems.upgrades, i, name[i], "items/misc/");
		}

		for (int i = 0; i < BlockOre.ores.length; ++i) {
			String[] name = BlockOre.ores.clone();
			registerBlockstate(ModBlocks.ore, i, name[i]);
		}

		for (int i = 0; i < BlockOre2.ores.length; ++i) {
			String[] name = BlockOre2.ores.clone();
			registerBlockstate(ModBlocks.ore2, i, name[i]);
		}

		for (int i = 0; i < BlockStorage.types.length; ++i) {
			String[] name = BlockStorage.types.clone();
			registerBlockstate(ModBlocks.storage, i, name[i]);
		}

		for (int i = 0; i < BlockStorage2.types.length; ++i) {
			String[] name = BlockStorage2.types.clone();
			registerBlockstate(ModBlocks.storage2, i, name[i]);
		}

		if (Loader.isModLoaded("reborncore-mcmultipart"))
			for (EnumCableType i : EnumCableType.values()) {
				String name = i.getName();
				registerBlockstate(TechRebornParts.cables, i.ordinal(), name, "items/misc/");
			}
	}

	private static void registerBlocks() {
		register(ModBlocks.ironFence, "iron_fence");
	}

	private static void register(Item item, int meta, String name) {
		ModelLoader.setCustomModelResourceLocation(item, meta,
			new ModelResourceLocation("techreborn:" + name, "inventory"));
	}

	private static void register(Item item, String name) {
		register(item, 0, name);
	}

	private static void register(Block block, int meta, String name) {
		register(Item.getItemFromBlock(block), meta, name);
	}

	private static void register(Block block, String name) {
		register(Item.getItemFromBlock(block), 0, name);
	}

	private static void registerBlockstate(Item i, int meta, String variant) {
		registerBlockstate(i, meta, variant, "");
	}

	private static void registerBlockstate(Item i, int meta, String variant, String dir) {
		ResourceLocation loc = new ResourceLocation("techreborn", dir + i.getRegistryName().getResourcePath());
		ModelLoader.setCustomModelResourceLocation(i, meta, new ModelResourceLocation(loc, "type=" + variant));
	}

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
}
