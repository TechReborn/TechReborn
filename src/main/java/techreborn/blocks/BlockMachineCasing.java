/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;
import reborncore.common.blocks.PropertyString;
import reborncore.common.multiblock.BlockMultiblockBase;
import reborncore.common.util.ArrayUtils;
import techreborn.Core;
import techreborn.client.TechRebornCreativeTab;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;
import techreborn.tiles.TileMachineCasing;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Random;

public class BlockMachineCasing extends BlockMultiblockBase {

	public static final String[] types = new String[] { "standard", "reinforced", "advanced" };
	public static final PropertyString TYPE = new PropertyString("type", types);
	private static final List<String> typesList = Lists.newArrayList(ArrayUtils.arrayToLowercase(types));

	public BlockMachineCasing() {
		super(Material.IRON);
		setCreativeTab(TechRebornCreativeTab.instance);
		setHardness(2F);
		this.setDefaultState(this.getDefaultState().withProperty(TYPE, "standard"));
		for (int i = 0; i > types.length; i++) {
			if (Core.proxy.isCTMAvailable()) {
				ShootingStar.registerModel(new ModelCompound(ModInfo.MOD_ID, this, i, "machines/structure/ctm").setInvVariant(types[i]));
			} else {
				ShootingStar.registerModel(new ModelCompound(ModInfo.MOD_ID, this, i, "machines/structure").setInvVariant(types[i]));
			}
		}
	}

	public static ItemStack getStackByName(String name, int count) {
		name = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name);
		for (int i = 0; i < types.length; i++) {
			if (types[i].equalsIgnoreCase(name)) {
				return new ItemStack(ModBlocks.MACHINE_CASINGS, count, i);
			}
		}
		throw new InvalidParameterException("The machine casing " + name + " could not be found.");
	}

	public static ItemStack getStackByName(String name) {
		return getStackByName(name, 1);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta > types.length) {
			meta = 0;
		}
		return getBlockState().getBaseState().withProperty(TYPE, typesList.get(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return typesList.indexOf(state.getValue(TYPE));
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE);
	}

	public int getHeatFromState(IBlockState state) {
		switch (getMetaFromState(state)) {
			case 0:
				return 1020 / 25;
			case 1:
				return 1700 / 25;
			case 2:
				return 2380 / 25;
		}
		return 0;
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
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileMachineCasing();
	}

}
