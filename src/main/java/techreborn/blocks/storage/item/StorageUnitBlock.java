package techreborn.blocks.storage.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import techreborn.blockentity.storage.item.StorageUnitBaseBlockEntity;
import techreborn.blocks.GenericMachineBlock;
import techreborn.client.EGui;

import java.util.function.Supplier;

public class StorageUnitBlock extends GenericMachineBlock {

	public StorageUnitBlock(EGui gui, Supplier<BlockEntity> blockEntityClass) {
		super(gui, blockEntityClass);
	}

	@Override
	public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockHitResult hitResult) {
		final StorageUnitBaseBlockEntity storageEntity = (StorageUnitBaseBlockEntity) worldIn.getBlockEntity(pos);
		ItemStack stackInHand = playerIn.getStackInHand(Hand.MAIN_HAND);

		if(storageEntity != null && storageEntity.isSameType(stackInHand)) {

				// Add item which is the same type (in users inventory) into storage
				for (int i = 0; i < playerIn.inventory.getInvSize() && !storageEntity.isFull(); i++) {
					ItemStack curStack = playerIn.inventory.getInvStack(i);
					if (storageEntity.isSameType(curStack)) {
						playerIn.inventory.setInvStack(i, storageEntity.processInput(curStack));
					}
				}

				return ActionResult.SUCCESS;
		}
			return super.onUse(state, worldIn, pos, playerIn, hand, hitResult);
	}
}
