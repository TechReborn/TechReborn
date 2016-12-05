package techreborn.itemblocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;
import techreborn.blocks.BlockMachineCasing;
import techreborn.init.ModBlocks;

public class ItemBlockMachineCasing extends ItemMultiTexture {

	public ItemBlockMachineCasing(Block block) {
		super(ModBlocks.machineCasing, ModBlocks.machineCasing, BlockMachineCasing.types);
	}

}
