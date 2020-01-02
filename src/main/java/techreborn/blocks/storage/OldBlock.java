package techreborn.blocks.storage;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import techreborn.blocks.GenericMachineBlock;
import techreborn.client.EGui;

import java.util.function.Supplier;

@Deprecated
public class OldBlock extends GenericMachineBlock {

	private BlockState replacement;

	public OldBlock(EGui gui, Supplier<BlockEntity> blockEntityClass, BlockState replacement) {
		super(gui, blockEntityClass);
		this.replacement = replacement;
	}

	@Override
	public void onPlaced(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.onPlaced(worldIn, pos, state, placer, stack);
		worldIn.setBlockState(pos, replacement);
	}
}
