/**
 * 
 */
package techreborn.blocks;

import java.util.function.Supplier;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;
import reborncore.api.blockentity.IMachineGuiHandler;
import reborncore.common.blocks.BlockMachineBase;
import techreborn.client.EGui;

/**
 * @author drcrazy
 *
 */
public class GenericMachineBlock extends BlockMachineBase {

	private EGui gui;
	private Supplier<BlockEntity> blockEntityClass;

	public GenericMachineBlock(EGui gui, Supplier<BlockEntity> blockEntityClass) {
		super();
		this.blockEntityClass = blockEntityClass;
		this.gui = gui;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView worldIn) {
		if (blockEntityClass == null) {
			return null;
		}
		return blockEntityClass.get();
	}

	@Override
	public IMachineGuiHandler getGui() {
		return gui;
	}

}
