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
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;
import techreborn.lib.ModInfo;
import techreborn.utils.TechRebornCreativeTab;

import java.util.Random;

//Thanks @Prospector you saved me me some time :)
public abstract class BlockRubberPlankSlab extends BlockSlab {
	public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("variant", BlockRubberPlankSlab.Variant.class);
	public final String name;
	public Block halfslab;

	public BlockRubberPlankSlab(String name) {
		super(Material.WOOD, Material.WOOD.getMaterialMapColor());
		this.name = name;
		IBlockState iblockstate = this.blockState.getBaseState();
		if (!this.isDouble()) {
			iblockstate = iblockstate.withProperty(HALF, EnumBlockHalf.BOTTOM);
			halfslab = this;
		}
		setCreativeTab(TechRebornCreativeTab.instance);
		setHarvestLevel("axe", 0);
		setHardness(2.0F);
		setResistance(15);
		setSoundType(SoundType.WOOD);
		this.setDefaultState(iblockstate.withProperty(VARIANT, BlockRubberPlankSlab.Variant.DEFAULT));
		useNeighborBrightness = true;
	}

	@Override
	public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return true;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(halfslab);
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(halfslab);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState iblockstate = this.getDefaultState().withProperty(VARIANT, BlockRubberPlankSlab.Variant.DEFAULT);

		if (!this.isDouble()) {
			iblockstate = iblockstate.withProperty(HALF, (meta & 8) == 0 ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
		}

		return iblockstate;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int i = 0;

		if (!this.isDouble() && state.getValue(HALF) == EnumBlockHalf.TOP) {
			i |= 8;
		}

		return i;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return this.isDouble() ? new BlockStateContainer(this, new IProperty[] { VARIANT }) : new BlockStateContainer(this, new IProperty[] { HALF, VARIANT });
	}

	@Override
	public String getUnlocalizedName(int meta) {
		return super.getUnlocalizedName();
	}

	@Override
	public IProperty<?> getVariantProperty() {
		return VARIANT;
	}

	@Override
	public Comparable<?> getTypeForItem(ItemStack stack) {
		return BlockRubberPlankSlab.Variant.DEFAULT;
	}

	public static enum Variant implements IStringSerializable {
		DEFAULT;

		@Override
		public String getName() {
			return "default";
		}
	}

	public static class BlockDouble extends BlockRubberPlankSlab {
		public BlockDouble(String name, Block half) {
			super(name);
			this.halfslab = half;
		}

		@Override
		public boolean isDouble() {
			return true;
		}
	}

	public static class BlockHalf extends BlockRubberPlankSlab {
		public BlockHalf(String name) {
			super(name);
			ShootingStar.registerModel(new ModelCompound(ModInfo.MOD_ID, this));
		}

		@Override
		public boolean isDouble() {
			return false;
		}
	}
}
