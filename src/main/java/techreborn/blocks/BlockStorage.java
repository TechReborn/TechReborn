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

import com.google.common.collect.Lists;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;
import reborncore.common.BaseBlock;
import reborncore.common.blocks.PropertyString;
import reborncore.common.util.ArrayUtils;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.events.TRRecipeHandler;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;

import java.util.List;
import java.util.Random;

public class BlockStorage extends BaseBlock {

	public static final String[] types = new String[] { "silver", "aluminum", "titanium", "chrome", "steel", "brass",
		"lead", "electrum", "zinc", "platinum", "tungsten", "nickel", "invar", "iridium" };
	public static final PropertyString TYPE = new PropertyString("type", types);
	private static final List<String> typesList = Lists.newArrayList(ArrayUtils.arrayToLowercase(types));

	public BlockStorage() {
		super(Material.IRON);
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		setHardness(2f);
		this.setDefaultState(this.getDefaultState().withProperty(TYPE, "silver"));
		for (int i = 0; i < types.length; i++) {
			ShootingStar.registerModel(new ModelCompound(ModInfo.MOD_ID, this, i).setInvVariant("type=" + types[i]).setFileName("storage"));
		}
		TRRecipeHandler.hideEntry(this);
	}

	public static ItemStack getStorageBlockByName(String name, int count) {
		for (int i = 0; i < types.length; i++) {
			if (types[i].equals(name)) {
				return new ItemStack(ModBlocks.STORAGE, count, i);
			}
		}
		return BlockStorage2.getStorageBlockByName(name, count);
	}

	public static ItemStack getStorageBlockByName(String name) {
		return getStorageBlockByName(name, 1);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(CreativeTabs creativeTabs, NonNullList list) {
		for (int meta = 0; meta < types.length; meta++) {
			list.add(new ItemStack(this, 1, meta));
		}
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
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

}
