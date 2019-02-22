package techreborn.init;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class ModTags {

	public static Tag<Block> FENCE_IRON = get("fence_iron");

	private static <T> Tag<T> get(String name) {
		return (Tag<T>) new BlockTags.Wrapper(new ResourceLocation("techreborn", name));
	}

}
