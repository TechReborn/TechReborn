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

package techreborn.blocks.machine.tier1;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
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
import reborncore.common.util.StringUtils;
import techreborn.blockentity.machine.tier1.PlayerDectectorBlockEntity;
import techreborn.utils.MessageIDs;

public class PlayerDetectorBlock extends BlockMachineBase {

	public static final EnumProperty<PlayerDetectorType> TYPE = EnumProperty.of("type", PlayerDetectorType.class);

	public PlayerDetectorBlock() {
		super(Block.Settings.of(Material.METAL).strength(2f, 2f), true);
		this.setDefaultState(this.getStateManager().getDefaultState().with(TYPE, PlayerDetectorType.ALL));
	}

	// BlockMachineBase
	@Override
	public BlockEntity createBlockEntity(BlockView worldIn) {
		return new PlayerDectectorBlockEntity();
	}

	@Override
	public void onPlaced(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.onPlaced(worldIn, pos, state, placer, stack);
		BlockEntity blockEntity = worldIn.getBlockEntity(pos);
		if (blockEntity instanceof PlayerDectectorBlockEntity) {
			((PlayerDectectorBlockEntity) blockEntity).owenerUdid = placer.getUuid().toString();
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockHitResult hitResult) {
		ItemStack stack = playerIn.getStackInHand(Hand.MAIN_HAND);
		BlockEntity blockEntity = worldIn.getBlockEntity(pos);

		if (blockEntity == null) {
			return super.onUse(state, worldIn, pos, playerIn, hand, hitResult);
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
							return ActionResult.PASS;
						}
						if (!drop.isEmpty()) {
							dropStack(worldIn, pos, drop);
						}
						if (!worldIn.isClient) {
							worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
						}
						return ActionResult.SUCCESS;
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
			ChatUtils.sendNoSpamMessages(MessageIDs.playerDetectorID,
					new TranslatableText("techreborn.message.detects")
							.formatted(Formatting.GRAY)
							.append(" ")
							.append(
									new LiteralText(StringUtils.toFirstCapital(newType.asString()))
											.formatted(color)
							)
			);
		}
		return ActionResult.SUCCESS;
	}

	@Override
	public IMachineGuiHandler getGui() {
		return null;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(TYPE);
	}

	// AbstractBlock
	@SuppressWarnings("deprecation")
	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getWeakRedstonePower(BlockState blockState, BlockView blockAccess, BlockPos pos, Direction side) {
		BlockEntity entity = blockAccess.getBlockEntity(pos);
		if (entity instanceof PlayerDectectorBlockEntity) {
			return ((PlayerDectectorBlockEntity) entity).isProvidingPower() ? 15 : 0;
		}
		return 0;
	}

	@SuppressWarnings("deprecation")
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

		PlayerDetectorType(String name) {
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
