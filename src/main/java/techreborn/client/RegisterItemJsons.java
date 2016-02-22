package techreborn.client;

import jline.internal.Log;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;
import reborncore.common.util.LogHelper;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;

public class RegisterItemJsons {
	public static void registerModels() {
		registerItems();
		registerBlocks();
	}

	private static void registerItems() {
	}

	private static void registerBlocks() {	
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.ironFence), 0, new ModelResourceLocation("techreborn:ironFence", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.ironFenceGate), 0, new ModelResourceLocation("techreborn:ironFenceGate", "inventory"));
	}
}
