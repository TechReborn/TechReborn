package techreborn.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.items.ItemGems;
import techreborn.items.ItemIngots;
import techreborn.items.ItemNuggets;
import techreborn.items.ItemPlates;

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

		register(ModItems.ironDrill, "tool/steel_drill");
		register(ModItems.diamondDrill, "tool/diamond_drill");
		register(ModItems.advancedDrill, "tool/advanced_drill");

		register(ModItems.ironChainsaw, "tool/steel_chainsaw");
		register(ModItems.diamondChainsaw, "tool/diamond_chainsaw");
		register(ModItems.advancedChainsaw, "tool/advanced_chainsaw");

		register(ModItems.steelJackhammer, "tool/steel_jackhammer");
		register(ModItems.diamondJackhammer, "tool/diamond_jackhammer");
		register(ModItems.advancedJackhammer, "tool/advanced_jackhammer");

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
}
