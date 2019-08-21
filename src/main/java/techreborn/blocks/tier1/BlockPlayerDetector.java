/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.blocks.tier1;

import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import reborncore.common.util.StringUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import reborncore.api.IToolDrop;
import reborncore.api.ToolManager;
import reborncore.api.blockentity.IMachineGuiHandler;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.util.ChatUtils;
import techreborn.blockentity.machine.tier1.PlayerDectectorBlockEntity;
import techreborn.utils.MessageIDs;

public class BlockPlayerDetector extends BlockMachineBase {

	public static EnumProperty<PlayerDetectorType> TYPE;

	public BlockPlayerDetector() {
		super(Block.Settings.of(Material.METAL).strength(2f, 2f), true);
		this.setDefaultState(this.stateFactory.getDefaultState().with(TYPE, PlayerDetectorType.ALL));
	}

	// BlockMachineBase
	@Override
	public BlockEntity createBlockEntity(BlockView worldIn) {
		return new PlayerDectectorBlockEntity();
	}
	
	@Override
	public void onPlaced(World worldIn, BlockPos pos, BlockState state, LivingEntity placer,
	                            ItemStack stack) {
		super.onPlaced(worldIn, pos, state, placer, stack);
		BlockEntity blockEntity = worldIn.getBlockEntity(pos);
		if (blockEntity instanceof PlayerDectectorBlockEntity) {
			((PlayerDectectorBlockEntity) blockEntity).owenerUdid = placer.getUuid().toString();
		}
	}
	
	@Override
	public boolean activate(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockHitResult hitResult) {
		ItemStack stack = playerIn.getStackInHand(Hand.MAIN_HAND);
		BlockEntity blockEntity = worldIn.getBlockEntity(pos);
			
		if (blockEntity == null) {
			return super.activate(state, worldIn, pos, playerIn, hand, hitResult);
		}
		
		PlayerDetectorType type = state.get(TYPE);
		PlayerDetectorType newType = type;
		Formatting color = Formatting.GREEN;
		
		if (!stack.isEmpty() && ToolManager.INSTANCE.canHandleTool(stack)) {
			if (ToolManager.INSTANCE.handleTool(stack, pos, worldIn, playerIn, hitResult.getSide(), false)) {
				if (playerIn.isSneaking()) {
					if (blockEntity instanceof IToolDrop) {
						ItemStack drop = ((IToolDrop) blockEntity).getToolDrop(playerIn);
						if (drop == null) {
							return false;
						}
						if (!drop.isEmpty()) {
							dropStack(worldIn, pos, drop);
						}
						if (!worldIn.isClient) {
							worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
						}
						return true;
					}
				} else {
					if (type == PlayerDetectorType.ALL) {
						newType = PlayerDetectorType.OTHERS;
						color = Formatting.RED;
					} else if (type == PlayerDetectorType.OTHERS) {
						newType = PlayerDetectorType.YOU;
						color = Formatting.BLUE;
					} else if (type == PlayerDetectorType.YOU) {
						newType = PlayerDetectorType.ALL;
					}
					worldIn.setBlockState(pos, state.with(TYPE, newType));
				}
			}
		}

		if (worldIn.isClient) {
			ChatUtils.sendNoSpamMessages(MessageIDs.playerDetectorID, new LiteralText(
				Formatting.GRAY + StringUtils.t("techreborn.message.detects") + " " + color
					+ StringUtils.toFirstCapital(newType.asString())));
		}
		return true;
	}
	
	@Override
	public IMachineGuiHandler getGui() {
		return null;
	}
	
	// Block
	@Override
	protected void appendProperties(StateFactory.Builder<Block, BlockState> builder) {
		TYPE = EnumProperty.of("type", PlayerDetectorType.class);
		builder.add(TYPE);
	}

	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	@Override
	public int getWeakRedstonePower(BlockState blockState, BlockView blockAccess, BlockPos pos, Direction side) {
		BlockEntity entity = blockAccess.getBlockEntity(pos);
		if (entity instanceof PlayerDectectorBlockEntity) {
			return ((PlayerDectectorBlockEntity) entity).isProvidingPower() ? 15 : 0;
		}
		return 0;
	}

	@Override
	public int getStrongRedstonePower(BlockState blockState, BlockView blockAccess, BlockPos pos, Direction side) {
		BlockEntity entity = blockAccess.getBlockEntity(pos);
		if (entity instanceof PlayerDectectorBlockEntity) {
			return ((PlayerDectectorBlockEntity) entity).isProvidingPower() ? 15 : 0;
		}
		return 0;
	}
	
	public enum PlayerDetectorType implements StringIdentifiable {
		ALL("all"), OTHERS("others"), YOU("you");

		private final String name;

		private PlayerDetectorType(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}

		@Override
		public String asString() {
			return this.name;
		}
	}
}
