package techreborn.itemblocks;

import net.minecraft.block.Block;
import reborncore.common.itemblock.ItemBlockBase;
import techreborn.blocks.BlockOre2;
import techreborn.init.ModBlocks;

public class ItemBlockOre2 extends ItemBlockBase {

	public ItemBlockOre2(Block block) {
		super(ModBlocks.ore2, ModBlocks.ore2, BlockOre2.ores);
	}

}
