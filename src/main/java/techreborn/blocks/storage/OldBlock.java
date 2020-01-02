package techreborn.blocks.storage;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import techreborn.blocks.GenericMachineBlock;
import techreborn.client.EGui;

import java.util.function.Supplier;

@Deprecated
public class OldBlock extends GenericMachineBlock {

	public OldBlock(EGui gui, Supplier<BlockEntity> blockEntityClass) {
		super(gui, blockEntityClass);
	}
}
