package techreborn.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import techreborn.blocks.BlockOre;
import techreborn.blocks.BlockOre2;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.items.*;

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
		register(ModItems.nanosaber, "nanosaber");

		register(ModItems.frequencyTransmitter, "frequencytransmitter");
		register(ModItems.uuMatter, "misc/uu_matter");
		register(ModItems.scrapBox, "misc/scrapbox");

		register(ModItems.ironDrill, "tool/steel_drill");
		register(ModItems.diamondDrill, "tool/diamond_drill");
		register(ModItems.advancedDrill, "tool/advanced_drill");

		register(ModItems.ironChainsaw, "tool/steel_chainsaw");
		register(ModItems.diamondChainsaw, "tool/diamond_chainsaw");
		register(ModItems.advancedChainsaw, "tool/advanced_chainsaw");

		register(ModItems.steelJackhammer, "tool/steel_jackhammer");
		register(ModItems.diamondJackhammer, "tool/diamond_jackhammer");
		register(ModItems.advancedJackhammer, "tool/advanced_jackhammer");
		register(ModBlocks.rubberSapling, "misc/rubber_sapling");

		for (int i = 0; i < ItemIngots.types.length; ++i) {
			String[] name = ItemIngots.types.clone();
			registerBlockstate(ModItems.ingots, i, name[i]);
		}

		for (int i = 0; i < ItemGems.types.length; ++i) {
			String[] name = ItemGems.types.clone();
			registerBlockstate(ModItems.gems, i, name[i]);
		}

		for (int i = 0; i < ItemPlates.types.length; ++i) {
			String[] name = ItemPlates.types.clone();
			registerBlockstate(ModItems.plate, i, name[i]);
		}

		for (int i = 0; i < ItemNuggets.types.length; ++i) {
			String[] name = ItemNuggets.types.clone();
			registerBlockstate(ModItems.nuggets, i, name[i]);
		}

		for (int i = 0; i < ItemDusts.types.length; ++i) {
			String[] name = ItemDusts.types.clone();
			registerBlockstate(ModItems.dusts, i, name[i]);
		}

		for (int i = 0; i < ItemDustsSmall.types.length; ++i) {
			String[] name = ItemDustsSmall.types.clone();
			registerBlockstate(ModItems.smallDusts, i, name[i]);
		}

		for (int i = 0; i < ItemUpgrades.types.length; ++i) {
			String[] name = ItemUpgrades.types.clone();
			registerBlockstate(ModItems.upgrades, i, name[i]);
		}

		for (int i = 0; i < BlockOre.ores.length; ++i) {
			String[] name = BlockOre.ores.clone();
			registerBlockstate(ModBlocks.ore, i, name[i]);
		}

		for (int i = 0; i < BlockOre2.ores.length; ++i) {
			String[] name = BlockOre2.ores.clone();
			registerBlockstate(ModBlocks.ore2, i, name[i]);
		}
	}

	private static void registerBlocks() {
		register(ModBlocks.ironFence, "ironfence");
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
		ResourceLocation loc = i.getRegistryName();
		ModelLoader.setCustomModelResourceLocation(i, meta, new ModelResourceLocation(loc, "type=" + variant));
	}

	private static void registerBlockstate(Block i, int meta, String variant) {
		ResourceLocation loc = i.getRegistryName();
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(i), meta, new ModelResourceLocation(loc, "type=" + variant));
	}
}
