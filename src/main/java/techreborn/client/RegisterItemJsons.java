package techreborn.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;

public class RegisterItemJsons
{
	public static void registerModels()
	{
		registerItems();
		registerBlocks();
	}

	private static void registerItems()
	{
		register(ModItems.reBattery, "reBattery");
		register(ModItems.lithiumBattery, "lithiumBattery");
		register(ModItems.energyCrystal, "energyCrystal");
		register(ModItems.lapotronCrystal, "lapotronCrystal");
		register(ModItems.lapotronicOrb, "lapotronOrb");
		register(ModItems.nanosaber, "nanosaber");
	}

	private static void registerBlocks()
	{
		register(ModBlocks.ironFence, "ironFence");
	}

	private static void register(Item item, int meta, String name)
	{
		ModelLoader.setCustomModelResourceLocation(item, meta,
				new ModelResourceLocation("techreborn:" + name, "inventory"));
	}

	private static void register(Item item, String name)
	{
		register(item, 0, name);
	}

	private static void register(Block block, int meta, String name)
	{
		register(Item.getItemFromBlock(block), meta, name);
	}

	private static void register(Block block, String name)
	{
		register(Item.getItemFromBlock(block), 0, name);
	}
}
