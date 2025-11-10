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

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import reborncore.api.IToolDrop;
import reborncore.api.ToolManager;
import reborncore.api.blockentity.IMachineGuiHandler;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.util.StringUtils;
import techreborn.blockentity.GuiType;
import techreborn.blockentity.machine.tier1.PlayerDetectorBlockEntity;
import techreborn.init.TRBlockSettings;

public class PlayerDetectorBlock extends BlockMachineBase {

	public static final EnumProperty<PlayerDetectorType> TYPE = EnumProperty.create("type", PlayerDetectorType.class);

	public PlayerDetectorBlock(String name) {
		super(TRBlockSettings.playerDetector(name), true);
		this.registerDefaultState(this.getStateDefinition().any().setValue(TYPE, PlayerDetectorType.ALL));
	}

	// BlockMachineBase
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PlayerDetectorBlockEntity(pos, state);
	}

	@Override
	public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(worldIn, pos, state, placer, stack);
		BlockEntity blockEntity = worldIn.getBlockEntity(pos);
		if (blockEntity instanceof PlayerDetectorBlockEntity) {
			((PlayerDetectorBlockEntity) blockEntity).ownerUdid = placer.getUUID().toString();
		}
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level worldIn, BlockPos pos, Player playerIn, BlockHitResult hitResult) {
		if (!playerIn.mayBuild()){
			return super.useWithoutItem(state, worldIn, pos, playerIn, hitResult);
		}
		BlockEntity blockEntity = worldIn.getBlockEntity(pos);
		if (blockEntity == null) {
			return super.useWithoutItem(state, worldIn, pos, playerIn, hitResult);
		}

		ItemStack stack = playerIn.getItemInHand(InteractionHand.MAIN_HAND);
		PlayerDetectorType type = state.getValue(TYPE);
		PlayerDetectorType newType = type;
		ChatFormatting color = ChatFormatting.GREEN;

		if (!stack.isEmpty() && ToolManager.INSTANCE.canHandleTool(stack)) {
			if (ToolManager.INSTANCE.handleTool(stack, pos, worldIn, playerIn, hitResult.getDirection(), false)) {
				if (playerIn.isShiftKeyDown()) {
					if (blockEntity instanceof IToolDrop) {
						ItemStack drop = ((IToolDrop) blockEntity).getToolDrop(playerIn);
						if (drop == null) {
							return InteractionResult.PASS;
						}
						if (!drop.isEmpty()) {
							popResource(worldIn, pos, drop);
						}
						if (!worldIn.isClientSide()) {
							worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
						}
						return InteractionResult.SUCCESS;
					}
				} else {
					if (type == PlayerDetectorType.ALL) {
						newType = PlayerDetectorType.OTHERS;
						color = ChatFormatting.RED;
					} else if (type == PlayerDetectorType.OTHERS) {
						newType = PlayerDetectorType.YOU;
						color = ChatFormatting.BLUE;
					} else if (type == PlayerDetectorType.YOU) {
						newType = PlayerDetectorType.ALL;
					}
					worldIn.setBlockAndUpdate(pos, state.setValue(TYPE, newType));
				}
			}
		}

		if (playerIn instanceof ServerPlayer serverPlayerEntity) {
			serverPlayerEntity.displayClientMessage(Component.translatable("techreborn.message.detects")
											.withStyle(ChatFormatting.GRAY)
											.append(" ")
											.append(
												Component.literal(StringUtils.toFirstCapital(newType.getSerializedName()))
													.withStyle(color)
											), true);
		}

		if (getGui() != null && !playerIn.isShiftKeyDown()) {
			getGui().open(playerIn, pos, worldIn);
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public IMachineGuiHandler getGui() {
		return GuiType.PLAYER_DETECTOR;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(TYPE);
	}

	// AbstractBlock
	@SuppressWarnings("deprecation")
	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
		BlockEntity entity = blockAccess.getBlockEntity(pos);
		if (entity instanceof PlayerDetectorBlockEntity) {
			return ((PlayerDetectorBlockEntity) entity).isProvidingPower() ? 15 : 0;
		}
		return 0;
	}

	public enum PlayerDetectorType implements StringRepresentable {
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
		public String getSerializedName() {
			return this.name;
		}
	}
}
