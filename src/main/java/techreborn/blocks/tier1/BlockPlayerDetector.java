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

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import reborncore.api.IToolDrop;
import reborncore.api.ToolManager;
import reborncore.api.tile.IMachineGuiHandler;
import reborncore.common.blocks.PropertyString;
import reborncore.common.blocks.RebornMachineBlock;
import reborncore.common.util.ArrayUtils;
import reborncore.common.util.ChatUtils;
import reborncore.common.util.StringUtils;

import techreborn.init.ModBlocks;
import techreborn.lib.MessageIDs;
import techreborn.lib.ModInfo;
import techreborn.tiles.tier1.TilePlayerDectector;
import techreborn.utils.TechRebornCreativeTab;

import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

public class BlockPlayerDetector extends RebornMachineBlock {

	public static final String[] types = new String[] { "all", "others", "you" };
	static List<String> typeNamesList = Lists.newArrayList(ArrayUtils.arrayToLowercase(types));
	public static PropertyString TYPE;

	public BlockPlayerDetector() {
		super(true);
		setCreativeTab(TechRebornCreativeTab.instance);
		this.setDefaultState(this.getStateFromMeta(0));
		for (int i = 0; i < types.length; i++) {
			ShootingStar.registerModel(new ModelCompound(ModInfo.MOD_ID, this, i, "machines/tier1_machines").setInvVariant("type=" + types[i]));
		}
	}

	// RebornMachineBlock
	@Override
	public TileEntity createNewTileEntity(final World world, final int meta) {
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
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
	                                EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(EnumHand.MAIN_HAND);
		TileEntity tileEntity = worldIn.getTileEntity(pos);
			
		if (tileEntity == null) {
			return super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
		}
		
		String type = state.getValue(TYPE);
		String newType = type;
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
					if (type.equals("all")) {
						newType = "others";
						color = TextFormatting.RED;
					} else if (type.equals("others")) {
						newType = "you";
						color = TextFormatting.BLUE;
					} else if (type.equals("you")) {
						newType = "all";
					}
					worldIn.setBlockState(pos, state.withProperty(TYPE, newType));
				}
			}
		}

		if (worldIn.isRemote) {
			ChatUtils.sendNoSpamMessages(MessageIDs.playerDetectorID, new TextComponentString(
				TextFormatting.GRAY + I18n.format("techreborn.message.detects") + " " + color
					+ StringUtils.toFirstCapital(newType)));
		}
		return true;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		TYPE = new PropertyString("type", types);
		return new BlockStateContainer(this, TYPE);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta > types.length) {
			meta = 0;
		}
		return getBlockState().getBaseState().withProperty(TYPE, typeNamesList.get(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return typeNamesList.indexOf(state.getValue(TYPE));
	}
	
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
    	return state;
    }

	@Override
	public IMachineGuiHandler getGui() {
		return null;
	}
	
	// Block
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(ModBlocks.PLAYER_DETECTOR, 1, typeNamesList.indexOf(state.getValue(TYPE)));
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
		for (int meta = 0; meta < types.length; meta++) {
			list.add(new ItemStack(this, 1, meta));
		}
	}

	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return true;
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		TileEntity entity = blockAccess.getTileEntity(pos);
		if (entity instanceof TilePlayerDectector) {
			return ((TilePlayerDectector) entity).isProvidingPower() ? 15 : 0;
		}
		return 0;
	}

	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		TileEntity entity = blockAccess.getTileEntity(pos);
		if (entity instanceof TilePlayerDectector) {
			return ((TilePlayerDectector) entity).isProvidingPower() ? 15 : 0;
		}
		return 0;
	}
}
