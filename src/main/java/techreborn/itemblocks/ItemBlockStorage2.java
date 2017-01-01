package techreborn.itemblocks;

import net.minecraft.block.Block;
import reborncore.common.itemblock.ItemBlockBase;
import techreborn.blocks.BlockStorage2;
import techreborn.init.ModBlocks;

public class ItemBlockStorage2 extends ItemBlockBase {

	public ItemBlockStorage2(Block block) {
		super(ModBlocks.STORAGE2, ModBlocks.STORAGE2, BlockStorage2.types);
	}

}
