package techreborn.itemblocks;

import net.minecraft.block.Block;
import reborncore.common.itemblock.ItemBlockBase;
import techreborn.blocks.BlockStorage;
import techreborn.init.ModBlocks;

public class ItemBlockStorage extends ItemBlockBase {

	public ItemBlockStorage(Block block) {
		super(ModBlocks.STORAGE, ModBlocks.STORAGE, BlockStorage.types);
	}

}
