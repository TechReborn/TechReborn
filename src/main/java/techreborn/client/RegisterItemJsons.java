package techreborn.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import techreborn.init.ModBlocks;

public class RegisterItemJsons {
	public static void registerModels() {
		registerItems();
		registerBlocks();
	}

	private static void registerItems() {
	}

	private static void registerBlocks() {	
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.ironFence), 0, new ModelResourceLocation("techreborn:ironFence", "inventory"));
	}
}
