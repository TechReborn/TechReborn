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

import com.google.common.collect.Lists;
import net.minecraft.ChatFormat;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.state.StateFactory;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import reborncore.api.IToolDrop;
import reborncore.api.ToolManager;
import reborncore.api.tile.IMachineGuiHandler;
import reborncore.client.models.ModelCompound;
import reborncore.client.models.RebornModelRegistry;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.PropertyString;
import reborncore.common.util.ArrayUtils;
import reborncore.common.util.ChatUtils;
import reborncore.common.util.StringUtils;
import techreborn.TechReborn;
import techreborn.tiles.machine.tier1.TilePlayerDectector;
import techreborn.utils.MessageIDs;

import javax.annotation.Nullable;
import java.util.List;

public class BlockPlayerDetector extends BlockMachineBase {

	public static final String[] types = new String[] { "all", "others", "you" };
	static List<String> typeNamesList = Lists.newArrayList(ArrayUtils.arrayToLowercase(types));
	public static PropertyString TYPE;

	public BlockPlayerDetector() {
		super(Block.Settings.of(Material.METAL), true);
		this.setDefaultState(this.stateFactory.getDefaultState().with(TYPE, types[0]));
		for (int i = 0; i < types.length; i++) {
			RebornModelRegistry.registerModel(new ModelCompound(TechReborn.MOD_ID, this, i, "machines/tier1_machines").setInvVariant("type=" + types[i]));
		}
	}

	// BlockMachineBase
	@Override
	public BlockEntity createBlockEntity(BlockView worldIn) {
		return new TilePlayerDectector();
	}
	
	@Override
	public void onPlaced(World worldIn, BlockPos pos, BlockState state, LivingEntity placer,
	                            ItemStack stack) {
		super.onPlaced(worldIn, pos, state, placer, stack);
		BlockEntity tile = worldIn.getBlockEntity(pos);
		if (tile instanceof TilePlayerDectector) {
			((TilePlayerDectector) tile).owenerUdid = placer.getUuid().toString();
		}
	}
	
	@Override
	public boolean activate(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockHitResult hitResult) {
		ItemStack stack = playerIn.getStackInHand(Hand.MAIN_HAND);
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
			
		if (tileEntity == null) {
			return super.activate(state, worldIn, pos, playerIn, hand, hitResult);
		}
		
		String type = state.get(TYPE);
		String newType = type;
		ChatFormat color = ChatFormat.GREEN;
		
		if (!stack.isEmpty() && ToolManager.INSTANCE.canHandleTool(stack)) {
			if (ToolManager.INSTANCE.handleTool(stack, pos, worldIn, playerIn, hitResult.getSide(), false)) {
				if (playerIn.isSneaking()) {
					if (tileEntity instanceof IToolDrop) {
						ItemStack drop = ((IToolDrop) tileEntity).getToolDrop(playerIn);
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
					if (type.equals("all")) {
						newType = "others";
						color = ChatFormat.RED;
					} else if (type.equals("others")) {
						newType = "you";
						color = ChatFormat.BLUE;
					} else if (type.equals("you")) {
						newType = "all";
					}
					worldIn.setBlockState(pos, state.with(TYPE, newType));
				}
			}
		}

		if (worldIn.isClient) {
			ChatUtils.sendNoSpamMessages(MessageIDs.playerDetectorID, new TextComponent(
				ChatFormat.GRAY + I18n.translate("techreborn.message.detects") + " " + color
					+ StringUtils.toFirstCapital(newType)));
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
		TYPE = new PropertyString("type", types);
		builder.add(TYPE);
	}

	@Override
	public boolean canConnectRedstone(BlockState state, BlockView world, BlockPos pos, @Nullable Direction side) {
		return true;
	}

	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	@Override
	public int getWeakRedstonePower(BlockState blockState, BlockView blockAccess, BlockPos pos, Direction side) {
		BlockEntity entity = blockAccess.getBlockEntity(pos);
		if (entity instanceof TilePlayerDectector) {
			return ((TilePlayerDectector) entity).isProvidingPower() ? 15 : 0;
		}
		return 0;
	}

	@Override
	public int getStrongRedstonePower(BlockState blockState, BlockView blockAccess, BlockPos pos, Direction side) {
		BlockEntity entity = blockAccess.getBlockEntity(pos);
		if (entity instanceof TilePlayerDectector) {
			return ((TilePlayerDectector) entity).isProvidingPower() ? 15 : 0;
		}
		return 0;
	}
}
