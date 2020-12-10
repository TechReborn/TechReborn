/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.blocks.misc;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.common.util.WorldUtils;
import team.reborn.energy.Energy;
import techreborn.events.TRRecipeHandler;
import techreborn.init.ModSounds;
import techreborn.init.TRContent;
import techreborn.items.tool.TreeTapItem;
import techreborn.items.tool.basic.ElectricTreetapItem;

import java.util.Random;

/**
 * Created by modmuss50 on 19/02/2016.
 */
public class BlockRubberLog extends PillarBlock {

	public static DirectionProperty SAP_SIDE = Properties.HORIZONTAL_FACING;
	public static BooleanProperty HAS_SAP = BooleanProperty.of("hassap");
	public static BooleanProperty SHOULD_SAP = BooleanProperty.of("shouldsap");

	public BlockRubberLog() {
		super(Settings.of(Material.WOOD, (blockState) -> MaterialColor.SPRUCE)
				.strength(2.0F, 2f)
				.sounds(BlockSoundGroup.WOOD)
				.ticksRandomly());
		this.setDefaultState(this.getDefaultState().with(SAP_SIDE, Direction.NORTH).with(HAS_SAP, false).with(SHOULD_SAP, true).with(AXIS, Direction.Axis.Y));
		FlammableBlockRegistry.getDefaultInstance().add(this, 5, 5);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(SAP_SIDE, HAS_SAP, SHOULD_SAP);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
		if (placer instanceof PlayerEntity) {
			world.setBlockState(pos, state.with(SHOULD_SAP, false));
		}
	}

	@Override
	public boolean isIn(Tag<Block> tagIn) {
		return tagIn == BlockTags.LOGS;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onBreak(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		int i = 4;
		int j = i + 1;
		if (worldIn instanceof ServerWorld && worldIn.isRegionLoaded(pos.add(-j, -j, -j), pos.add(j, j, j))) {
			for (BlockPos blockpos : BlockPos.iterate(pos.add(-i, -i, -i), pos.add(i, i, i))) {
				BlockState state1 = worldIn.getBlockState(blockpos);
				if (state1.isIn(BlockTags.LEAVES)) {
					state1.scheduledTick((ServerWorld) worldIn, blockpos, worldIn.getRandom());
					state1.randomTick((ServerWorld) worldIn, blockpos, worldIn.getRandom());
				}
			}
		}
		super.onBreak(worldIn, pos, state, player);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		super.randomTick(state, worldIn, pos, random);
		if (state.get(AXIS) != Direction.Axis.Y) return;
		if (!state.get(SHOULD_SAP)) return;
		if (state.get(HAS_SAP)) return;

		if (random.nextInt(50) == 0) {
			Direction facing = Direction.fromHorizontal(random.nextInt(4));
			if (worldIn.getBlockState(pos.offset(Direction.DOWN, 1)).getBlock() == this
					&& worldIn.getBlockState(pos.up()).getBlock() == this) {
				worldIn.setBlockState(pos, state.with(HAS_SAP, true).with(SAP_SIDE, facing));
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn,
							  Hand hand, BlockHitResult hitResult) {
		ItemStack stack = playerIn.getStackInHand(Hand.MAIN_HAND);
		if (stack.isEmpty()) {
			return ActionResult.PASS;
		}

		if ((Energy.valid(stack) && Energy.of(stack).getEnergy() > 20 && stack.getItem() instanceof ElectricTreetapItem) || stack.getItem() instanceof TreeTapItem) {
			if (state.get(HAS_SAP) && state.get(SAP_SIDE) == hitResult.getSide()) {
				worldIn.setBlockState(pos, state.with(HAS_SAP, false).with(SAP_SIDE, Direction.fromHorizontal(0)));
				worldIn.playSound(playerIn, pos, ModSounds.SAP_EXTRACT, SoundCategory.BLOCKS, 0.6F, 1F);
				if (worldIn.isClient) {
					return ActionResult.SUCCESS;
				}
				if (Energy.valid(stack)) {
					Energy.of(stack).use(20);
				} else {
					stack.damage(1, playerIn, player -> player.sendToolBreakStatus(hand));
				}
				if (!playerIn.inventory.insertStack(TRContent.Parts.SAP.getStack())) {
					WorldUtils.dropItem(TRContent.Parts.SAP.getStack(), worldIn, pos.offset(hitResult.getSide()));
				}
				if (playerIn instanceof ServerPlayerEntity) {
					TRRecipeHandler.unlockTRRecipes((ServerPlayerEntity) playerIn);
				}
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.PASS;
	}
}
