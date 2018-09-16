package techreborn.utils;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import techreborn.TechReborn;

public class InitUtils {
	public static <I extends Item> I setup(I item, String name) {
		item.setRegistryName(new ResourceLocation(TechReborn.MOD_ID, name));
		item.setTranslationKey(TechReborn.MOD_ID + "." + name);
		item.setCreativeTab(TechReborn.TAB);
		return item;
	}

	public static <I extends Item> I setupIngredient(I item, String name, String type) {
		item.setRegistryName(new ResourceLocation(TechReborn.MOD_ID, name + "_" + type));
		item.setTranslationKey(TechReborn.MOD_ID + "." + type + "." + name);
		item.setCreativeTab(TechReborn.TAB);
		return item;
	}

	public static <B extends Block> B setup(B block, String name) {
		block.setRegistryName(new ResourceLocation(TechReborn.MOD_ID, name));
		block.setTranslationKey(TechReborn.MOD_ID + "." + name);
		block.setCreativeTab(TechReborn.TAB);
		return block;
	}

	public static <B extends Block> B setupIngredient(B block, String name, String type) {
		block.setRegistryName(new ResourceLocation(TechReborn.MOD_ID, name + "_" + type));
		block.setTranslationKey(TechReborn.MOD_ID + "." + type + "." + name);
		block.setCreativeTab(TechReborn.TAB);
		return block;
	}
}
