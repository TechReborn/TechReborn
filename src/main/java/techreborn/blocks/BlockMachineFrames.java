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

import net.minecraft.block.material.Material;
import reborncore.client.models.ModelCompound;
import reborncore.client.models.RebornModelRegistry;
import reborncore.common.BaseBlock;
import techreborn.TechReborn;
import techreborn.utils.TechRebornCreativeTab;

public class BlockMachineFrames extends BaseBlock {
//	public static final String[] types = new String[] { "basic", "advanced", "highly_advanced" };
//	public static final PropertyString TYPE = new PropertyString("type", types);
//	private static final List<String> typesList = Lists.newArrayList(ArrayUtils.arrayToLowercase(types));

	public BlockMachineFrames() {
		super(Material.IRON);
		setCreativeTab(TechRebornCreativeTab.instance);
		setHardness(1f);
		RebornModelRegistry.registerModel(new ModelCompound(TechReborn.MOD_ID, this, "machines/structure"));
	}

//	public static ItemStack getFrameByName(String name, int count) {
//		name = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
//		if (name.equals("machine")) {
//			return new ItemStack(ModBlocks.MACHINE_FRAMES, count, 0);
//		}
//		if (name.equals("advanced_machine")) {
//			return new ItemStack(ModBlocks.MACHINE_FRAMES, count, 1);
//		}
//		if (name.equals("highly_advanced_machine")) {
//			return new ItemStack(ModBlocks.MACHINE_FRAMES, count, 2);
//		}
//
//		for (int i = 0; i < types.length; i++) {
//			if (types[i].equalsIgnoreCase(name)) {
//				return new ItemStack(ModBlocks.MACHINE_FRAMES, count, i);
//			}
//		}
//		throw new InvalidParameterException("The part " + name + " could not be found.");
//	}
//
//	public static ItemStack getFrameByName(String name) {
//		return getFrameByName(name, 1);
//	}


//	@Override
//	public int damageDropped(IBlockState state) {
//		return getMetaFromState(state);
//	}
//
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

//	@Override
//	protected BlockStateContainer createBlockState() {
//		return new BlockStateContainer(this, TYPE);
//	}

}
