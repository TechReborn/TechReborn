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

package techreborn.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import reborncore.api.ToolManager;
import reborncore.client.models.ModelCompound;
import reborncore.client.models.RebornModelRegistry;
import reborncore.common.RebornCoreConfig;
import reborncore.common.blocks.BlockWrenchEventHandler;
import reborncore.common.items.WrenchHelper;
import reborncore.common.multiblock.BlockMultiblockBase;
import techreborn.TechReborn;
import techreborn.init.ModBlocks;
import techreborn.tiles.TileMachineCasing;
import techreborn.utils.TechRebornCreativeTab;

public class BlockMachineCasing extends BlockMultiblockBase {

//	public static final String[] types = new String[] { "standard", "reinforced", "advanced" };
//	public static final PropertyString TYPE = new PropertyString("type", types);
//	private static final List<String> typesList = Lists.newArrayList(ArrayUtils.arrayToLowercase(types));

	public BlockMachineCasing() {
		super(Material.IRON);
		setCreativeTab(TechRebornCreativeTab.instance);
		setHardness(2F);
		RebornModelRegistry.registerModel(new ModelCompound(TechReborn.MOD_ID, this, "machines/structure"));
//		this.setDefaultState(this.getDefaultState().withProperty(TYPE, "standard"));
//		for (int i = 0; i < types.length; i++) {
//			RebornModelRegistry.registerModel(new ModelCompound(ModInfo.MOD_ID, this, i, "machines/structure").setInvVariant("type=" + types[i]));
//		}
		BlockWrenchEventHandler.wrenableBlocks.add(this);
	}

//	public static ItemStack getStackByName(String name, int count) {
//		name = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name);
//		for (int i = 0; i < types.length; i++) {
//			if (types[i].equalsIgnoreCase(name)) {
//				return new ItemStack(ModBlocks.MACHINE_CASINGS, count, i);
//			}
//		}
//		throw new InvalidParameterException("The machine casing " + name + " could not be found.");
//	}
//
//	public static ItemStack getStackByName(String name) {
//		return getStackByName(name, 1);
//	}
	
	/**
	 * Provides heat info per casing for Industrial Blast Furnace
	 * @param state IBlockstate Blockstate with Machine casing
	 * @return Integer Heat value for casing
	 */
	public int getHeatFromState(IBlockState state) {
		Block casing = state.getBlock();
		
		if (casing == null ) {
			return 0;
		}
		
		if (casing == ModBlocks.MACHINE_CASINGS_STANDARD) {
			return 1020 / 25;
		}
		else if (casing == ModBlocks.MACHINE_CASINGS_REINFORCED) {
			return 1700 / 25;
		}
		else if (casing == ModBlocks.MACHINE_CASINGS_ADVANCED) {
			return 2380 / 25;
		}
		return 0;
	}
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		if (RebornCoreConfig.wrenchRequired){
			Block casing = state.getBlock();
			if (casing == ModBlocks.MACHINE_CASINGS_REINFORCED) {
				drops.add(new ItemStack(ModBlocks.MACHINE_BLOCK_ADVANCED));				
			}
			else if (casing == ModBlocks.MACHINE_CASINGS_ADVANCED) {
				drops.add(new ItemStack(ModBlocks.MACHINE_BLOCK_ELITE));
			}
			else {
				drops.add(new ItemStack(ModBlocks.MACHINE_BLOCK_BASIC));
			}
		}
		else {
			super.getDrops(drops, world, pos, state, fortune);
		}
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand,
	                                EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(EnumHand.MAIN_HAND);
		TileEntity tileEntity = worldIn.getTileEntity(pos);

		// We extended BaseTileBlock. Thus we should always have tile entity. I hope.
		if (tileEntity == null) {
			return false;
		}

		if (!stack.isEmpty() && ToolManager.INSTANCE.canHandleTool(stack)) {
			if (WrenchHelper.handleWrench(stack, worldIn, pos, playerIn, side)) {
				return true;
			}
		}

		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
	}

//	@Override
//	public IBlockState getStateFromMeta(int meta) {
//		if (meta > types.length) {
//			meta = 0;
//		}
//		return getBlockState().getBaseState().withProperty(TYPE, typesList.get(meta));
//	}
//
//	@Override
//	public int getMetaFromState(IBlockState state) {
//		return typesList.indexOf(state.getValue(TYPE));
//	}
//
//	@Override
//	protected BlockStateContainer createBlockState() {
//		return new BlockStateContainer(this, TYPE);
//	}
//
//	@Override
//	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
//		return Item.getItemFromBlock(this);
//	}

//
//	@Override
//	public int damageDropped(IBlockState state) {
//		return getMetaFromState(state);
//	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int meta) {
		return new TileMachineCasing();
	}

}
