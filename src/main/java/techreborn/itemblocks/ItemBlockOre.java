package techreborn.itemblocks;

import techreborn.Core;
import techreborn.blocks.BlockOre;
import techreborn.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;

public class ItemBlockOre extends ItemMultiTexture{
	
	public ItemBlockOre(Block block)
	{
		super(ModBlocks.ore, ModBlocks.ore, BlockOre.types);
	}

}
