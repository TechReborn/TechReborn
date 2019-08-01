/**
 * 
 */
package techreborn.blocks;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;
import reborncore.api.blockentity.IMachineGuiHandler;
import reborncore.common.blocks.BlockMachineBase;
import techreborn.client.EGui;

/**
 * @author drcrazy
 * @param <T>
 *
 */
public class GenericMachineBlock extends BlockMachineBase {

	private EGui gui;
	private Class<?> blockEntityClass;

	public GenericMachineBlock(EGui gui, Class<?> blockEntityClass) {
		super();
		this.blockEntityClass = blockEntityClass;
		this.gui = gui;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView worldIn) {
		if (blockEntityClass == null) {
			return null;
		}
		BlockEntity blockEntity = null;
		try {
			blockEntity = (BlockEntity) blockEntityClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Failed to create BlockEntity blockEntity", e);
		}

		return blockEntity;
	}

	@Override
	public IMachineGuiHandler getGui() {
		return gui;
	}

}
