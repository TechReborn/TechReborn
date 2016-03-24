package techreborn.itemblocks;

import net.minecraft.block.Block;
import reborncore.common.itemblock.ItemBlockBase;
import techreborn.blocks.BlockMachineFrame;
import techreborn.init.ModBlocks;

public class ItemBlockMachineFrame extends ItemBlockBase {

    public ItemBlockMachineFrame(Block block) {
        super(ModBlocks.machineframe, ModBlocks.machineframe, BlockMachineFrame.types);
    }

}
