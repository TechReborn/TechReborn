package techreborn.itemblocks;

import net.minecraft.block.Block;
import reborncore.common.itemblock.ItemBlockBase;
import techreborn.blocks.BlockOre;
import techreborn.init.ModBlocks;

public class ItemBlockOre extends ItemBlockBase {

	public ItemBlockOre(Block block) {
		super(ModBlocks.ore, ModBlocks.ore, BlockOre.ores);
	}

}
