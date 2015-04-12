package techreborn.itemblocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;
import techreborn.blocks.BlockOre;
import techreborn.init.ModBlocks;

public class ItemBlockOre extends ItemMultiTexture{
	
	public ItemBlockOre(Block block)
	{
		super(ModBlocks.ore, ModBlocks.ore, BlockOre.types);
	}

}
