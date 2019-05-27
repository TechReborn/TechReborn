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

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import reborncore.api.IToolDrop;
import reborncore.api.ToolManager;
import reborncore.api.tile.IMachineGuiHandler;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.util.ChatUtils;
import reborncore.common.util.StringUtils;
import techreborn.tiles.machine.tier1.TilePlayerDectector;
import techreborn.utils.MessageIDs;

import javax.annotation.Nullable;

public class BlockPlayerDetector extends BlockMachineBase {

	public static EnumProperty<PlayerDetectorType> TYPE;

	public BlockPlayerDetector() {
		super(Block.Properties.create(Material.IRON), true);
		this.setDefaultState(this.stateContainer.getBaseState().with(TYPE, PlayerDetectorType.ALL));
	}

	// BlockMachineBase
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new TilePlayerDectector();
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof TilePlayerDectector) {
			((TilePlayerDectector) tile).owenerUdid = placer.getUniqueID().toString();
		}
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer playerIn,
			EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(EnumHand.MAIN_HAND);
		TileEntity tileEntity = worldIn.getTileEntity(pos);

		if (tileEntity == null) {
			return super.onBlockActivated(state, worldIn, pos, playerIn, hand, side, hitX, hitY, hitZ);
		}

		PlayerDetectorType type = state.get(TYPE);
		PlayerDetectorType newType = type;
		TextFormatting color = TextFormatting.GREEN;

		if (!stack.isEmpty() && ToolManager.INSTANCE.canHandleTool(stack)) {
			if (ToolManager.INSTANCE.handleTool(stack, pos, worldIn, playerIn, side, false)) {
				if (playerIn.isSneaking()) {
					if (tileEntity instanceof IToolDrop) {
						ItemStack drop = ((IToolDrop) tileEntity).getToolDrop(playerIn);
						if (drop == null) {
							return false;
						}
						if (!drop.isEmpty()) {
							spawnAsEntity(worldIn, pos, drop);
						}
						if (!worldIn.isRemote) {
							worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
						}
						return true;
					}
				} else {
					if (type == PlayerDetectorType.ALL) {
						newType = PlayerDetectorType.OTHERS;
						color = TextFormatting.RED;
					} else if (type == PlayerDetectorType.OTHERS) {
						newType = PlayerDetectorType.YOU;
						color = TextFormatting.BLUE;
					} else if (type == PlayerDetectorType.YOU) {
						newType = PlayerDetectorType.ALL;
					}
					worldIn.setBlockState(pos, state.with(TYPE, newType));
				}
			}
		}

		if (worldIn.isRemote) {
			ChatUtils.sendNoSpamMessages(MessageIDs.playerDetectorID, new TextComponentString(TextFormatting.GRAY
					+ I18n.format("techreborn.message.detects") + " " + color + StringUtils.toFirstCapital(newType.getName())));
		}
		return true;
	}

	@Override
	public IMachineGuiHandler getGui() {
		return null;
	}

	// Block
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
		TYPE = EnumProperty.create("type", PlayerDetectorType.class);
		builder.add(TYPE);
	}

	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockReader world, BlockPos pos, @Nullable EnumFacing side) {
		return true;
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public int getWeakPower(IBlockState blockState, IBlockReader blockAccess, BlockPos pos, EnumFacing side) {
		TileEntity entity = blockAccess.getTileEntity(pos);
		if (entity instanceof TilePlayerDectector) {
			return ((TilePlayerDectector) entity).isProvidingPower() ? 15 : 0;
		}
		return 0;
	}

	@Override
	public int getStrongPower(IBlockState blockState, IBlockReader blockAccess, BlockPos pos, EnumFacing side) {
		TileEntity entity = blockAccess.getTileEntity(pos);
		if (entity instanceof TilePlayerDectector) {
			return ((TilePlayerDectector) entity).isProvidingPower() ? 15 : 0;
		}
		return 0;
	}

	public enum PlayerDetectorType implements IStringSerializable {
		ALL("all"), OTHERS("others"), YOU("you");

		private final String name;

		private PlayerDetectorType(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.getName();
		}

		@Override
		public String getName() {
			return this.name;
		}
	}
}
