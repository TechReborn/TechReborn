package techreborn.blocks.machine.tier1;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.api.blockentity.IMachineGuiHandler;
import techreborn.blocks.GenericMachineBlock;
import techreborn.init.TRContent;
import techreborn.utils.WorldHelper;

import java.util.function.Supplier;

public class BlockMiningRig extends GenericMachineBlock {
	public BlockMiningRig(IMachineGuiHandler gui, Supplier<BlockEntity> blockEntityClass) {
		super(gui, blockEntityClass);
	}

	@Override
	public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockHitResult hitResult) {
		ItemStack holding = playerIn.inventory.getStack(playerIn.inventory.selectedSlot);
		boolean hasPlaced = false;
		if(holding.getItem() == TRContent.DRILL_PIPE.asItem()){

			if(worldIn.isClient()){
				return ActionResult.SUCCESS;
			}

			int amountToAdd = 1;
			if(holding.getCount() >= 10) {
				amountToAdd = 10;
			}

			for(int i = 0; i < amountToAdd; i++) {
				// TODO could improve this by just storing position and incrementing
				BlockPos addPos = WorldHelper.getBlockAlongY(pos, 1, Blocks.AIR, worldIn, false, TRContent.DRILL_PIPE);
				if (addPos != null) {
					worldIn.setBlockState(addPos, TRContent.DRILL_PIPE.getDefaultState());
					holding.decrement(1);
					hasPlaced = true;
				}
			}

			if(hasPlaced) {
				worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 100, 1, false);
			}

			return ActionResult.SUCCESS;
		}

		return super.onUse(state, worldIn, pos, playerIn, hand, hitResult);
	}
}
