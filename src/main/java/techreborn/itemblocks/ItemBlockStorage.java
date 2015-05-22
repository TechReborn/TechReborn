package techreborn.itemblocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;
import techreborn.blocks.BlockStorage;
import techreborn.init.ModBlocks;

public class ItemBlockStorage extends ItemBlockBase {

	public ItemBlockStorage(Block block)
	{
		super(ModBlocks.storage, ModBlocks.storage, BlockStorage.types);
	}

}
