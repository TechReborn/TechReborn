package techreborn.events;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import techreborn.init.TRContent;

public class UseBlockHandler implements UseBlockCallback{

	public static void init() {
		UseBlockCallback.EVENT.register(new UseBlockHandler());
	}

	@Override
	public ActionResult interact(PlayerEntity playerEntity, World world, Hand hand, BlockHitResult blockHitResult) {
		ItemStack stack = playerEntity.getStackInHand(hand);

		if (stack.getItem() instanceof AxeItem) {
			BlockPos pos = blockHitResult.getBlockPos();
			BlockState hitState = world.getBlockState(pos);
			Block hitBlock = hitState.getBlock();

			Block strippedBlock = null;
			if (hitBlock == TRContent.RUBBER_LOG) {
				strippedBlock = TRContent.RUBBER_LOG_STRIPPED;
			} else if (hitBlock == TRContent.RUBBER_WOOD) {
				strippedBlock = TRContent.STRIPPED_RUBBER_WOOD;
			}

			if (strippedBlock != null) {
				// Play stripping sound
				world.playSound(playerEntity, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
				if (world.isClient) {
					return ActionResult.SUCCESS;
				}

				world.setBlockState(pos, strippedBlock.getDefaultState().with(PillarBlock.AXIS, hitState.get(PillarBlock.AXIS)), 11);

				// Damage axe
				stack.damage(1, playerEntity, playerx ->
						playerx.sendToolBreakStatus(hand)
				);
				return ActionResult.SUCCESS;
			}
		}

		return ActionResult.PASS;
	}
}
