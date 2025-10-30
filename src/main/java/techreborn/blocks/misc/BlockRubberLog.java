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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import reborncore.common.util.WorldUtils;
import techreborn.config.TechRebornConfig;
import techreborn.events.TRRecipeHandler;
import techreborn.init.ModSounds;
import techreborn.init.TRBlockSettings;
import techreborn.init.TRContent;
import techreborn.items.tool.TreeTapItem;
import techreborn.items.tool.basic.ElectricTreetapItem;

/**
 * Created by modmuss50 on 19/02/2016.
 */
public class BlockRubberLog extends RotatedPillarBlock {

	public static final EnumProperty<Direction> SAP_SIDE = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty HAS_SAP = BooleanProperty.create("hassap");
	public static final BooleanProperty SHOULD_SAP = BooleanProperty.create("shouldsap");

	public BlockRubberLog(String name) {
		super(TRBlockSettings.rubberLog(name));
		this.registerDefaultState(this.defaultBlockState().setValue(SAP_SIDE, Direction.NORTH).setValue(HAS_SAP, false).setValue(SHOULD_SAP, true).setValue(AXIS, Direction.Axis.Y));
		FlammableBlockRegistry.getDefaultInstance().add(this, 5, 5);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(SAP_SIDE, HAS_SAP, SHOULD_SAP);
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.setPlacedBy(world, pos, state, placer, itemStack);
		if (placer instanceof Player) {
			world.setBlockAndUpdate(pos, state.setValue(SHOULD_SAP, false));
		}
	}

	/* FIXME @Override
	public boolean isIn(Tag<Block> tagIn) {
		return tagIn == BlockTags.LOGS;
	}*/

	@SuppressWarnings("deprecation")
	@Override
	public BlockState playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
		int i = 4;
		int j = i + 1;
		if (worldIn instanceof ServerLevel && worldIn.hasChunksAt(pos.offset(-j, -j, -j), pos.offset(j, j, j))) {
			for (BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-i, -i, -i), pos.offset(i, i, i))) {
				BlockState state1 = worldIn.getBlockState(blockpos);
				if (state1.is(BlockTags.LEAVES)) {
					state1.tick((ServerLevel) worldIn, blockpos, worldIn.getRandom());
					state1.randomTick((ServerLevel) worldIn, blockpos, worldIn.getRandom());
				}
			}
		}
		return super.playerWillDestroy(worldIn, pos, state, player);
	}

	@Override
	public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
		super.randomTick(state, worldIn, pos, random);
		if (state.getValue(AXIS) != Direction.Axis.Y) return;
		if (!state.getValue(SHOULD_SAP)) return;
		if (state.getValue(HAS_SAP)) return;

		if (random.nextInt(50) == 0) {
			Direction facing = Direction.from2DDataValue(random.nextInt(4));
			if (worldIn.getBlockState(pos.relative(Direction.DOWN, 1)).getBlock() == this
					&& worldIn.getBlockState(pos.above()).getBlock() == this) {
				worldIn.setBlockAndUpdate(pos, state.setValue(HAS_SAP, true).setValue(SAP_SIDE, facing));
			}
		}
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level worldIn, BlockPos pos, Player playerIn, BlockHitResult hitResult) {
		ItemStack stack = playerIn.getItemInHand(InteractionHand.MAIN_HAND);
		if (stack.isEmpty()) {
			return InteractionResult.PASS;
		}

		if ((stack.getItem() instanceof ElectricTreetapItem item && item.getStoredEnergy(stack) > 20) || stack.getItem() instanceof TreeTapItem) {
			if (state.getValue(HAS_SAP) && state.getValue(SAP_SIDE) == hitResult.getDirection()) {
				worldIn.setBlockAndUpdate(pos, state.setValue(HAS_SAP, false).setValue(SAP_SIDE, Direction.from2DDataValue(0)));
				worldIn.playSound(playerIn, pos, ModSounds.SAP_EXTRACT, SoundSource.BLOCKS, 0.6F, 1F);
				if (worldIn.isClientSide) {
					return InteractionResult.SUCCESS;
				}
				if (stack.getItem() instanceof ElectricTreetapItem item) {
					item.tryUseEnergy(stack, TechRebornConfig.electricTreetapCost);
				} else {
					stack.hurtAndBreak(1, playerIn, EquipmentSlot.MAINHAND);
				}
				if (!playerIn.getInventory().add(TRContent.Parts.SAP.getStack())) {
					WorldUtils.dropItem(TRContent.Parts.SAP.getStack(), worldIn, pos.relative(hitResult.getDirection()));
				}
				if (playerIn instanceof ServerPlayer && !TechRebornConfig.vanillaUnlockRecipes) {
					TRRecipeHandler.unlockTRRecipes((ServerPlayer) playerIn);
				}
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}
}
