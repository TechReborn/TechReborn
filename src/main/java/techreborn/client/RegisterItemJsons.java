package techreborn.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;

public class RegisterItemJsons {
	public static void registerModels() {
		registerItems();
		registerBlocks();
	}

	private static void registerItems() {
		register(ModItems.reBattery, "rebattery");
		register(ModItems.lithiumBattery, "lithiumbattery");
		register(ModItems.energyCrystal, "energycrystal");
		register(ModItems.lapotronCrystal, "lapotroncrystal");
		register(ModItems.lapotronicOrb, "lapotronicorb");
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
}
