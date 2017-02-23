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
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.BaseBlock;
import techreborn.client.TechRebornCreativeTab;
import techreborn.init.ModBlocks;

import java.security.InvalidParameterException;

public class BlockMachineFrame extends BaseBlock {
	public static final String[] types = new String[] { "machine", "advancedMachine", "highlyAdvancedMachine" };
	public static final PropertyInteger METADATA = PropertyInteger.create("type", 0, types.length - 1);

	public BlockMachineFrame(Material material) {
		super(material);
		setUnlocalizedName("techreborn.machineFrame");
		setCreativeTab(TechRebornCreativeTab.instance);
		setHardness(1f);
		this.setDefaultState(this.getDefaultState().withProperty(METADATA, 0));
	}

	public static ItemStack getFrameByName(String name, int count) {
		name = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name);
		for (int i = 0; i < types.length; i++) {
			if (types[i].equalsIgnoreCase(name)) {
				return new ItemStack(ModBlocks.MACHINE_FRAMES, count, i);
			}
		}
		throw new InvalidParameterException("The part " + name + " could not be found.");
	}

	public static ItemStack getFrameByName(String name) {
		return getFrameByName(name, 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
		for (int meta = 0; meta < types.length; meta++) {
			list.add(new ItemStack(item, 1, meta));
		}
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(METADATA);
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, METADATA);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(METADATA, meta);
	}

}
