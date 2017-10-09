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

package techreborn.blocks.generator.solarpanel;

import com.google.common.collect.Lists;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;
import reborncore.common.BaseTileBlock;
import techreborn.client.TechRebornCreativeTab;
import techreborn.lib.ModInfo;
import techreborn.tiles.generator.TileSolarPanel;

import java.util.List;

/**
 * Created by modmuss50 on 25/02/2016.
 */
public class BlockSolarPanel extends BaseTileBlock {
	public static final String[] panes = new String[] {
		"Basic", "Hybrid", "Advanced", "Ultimate", "Quantum"};
	public static final IProperty<EnumPanelType> TYPE = PropertyEnum.create("type", EnumPanelType.class);
	public static PropertyBool ACTIVE = PropertyBool.create("active");
	private static final List<String> paneList = Lists.newArrayList(panes);

	public BlockSolarPanel() {
		super(Material.IRON);
		setCreativeTab(TechRebornCreativeTab.instance);
		this.setDefaultState(this.getBlockState().getBaseState().withProperty(TYPE, EnumPanelType.Basic).withProperty(ACTIVE, false));
		setHardness(2.0F);
		this.setDefaultState(this.getStateFromMeta(0));
		for (int i = 0; i < panes.length; i++) {
			ShootingStar.registerModel(new ModelCompound(ModInfo.MOD_ID, this, i, "machines/generators").setInvVariant("type=" + panes[i] + ",active=false"));
		}
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int active = state.getValue(ACTIVE) ? EnumPanelType.values().length : 0;
		return state.getValue(TYPE).ordinal() + active;
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE, ACTIVE);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		boolean active = false;
		if(meta >= EnumPanelType.values().length){
			active = true;
			meta -= EnumPanelType.values().length;
		}
		return getDefaultState().withProperty(TYPE, EnumPanelType.values()[meta]).withProperty(ACTIVE, active);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileSolarPanel(getStateFromMeta(meta).getValue(TYPE));
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getStateFromMeta(placer.getHeldItem(hand).getItemDamage());
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(Item.getItemFromBlock(this),1,getMetaFromState(world.getBlockState(pos)));
	}
	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		for (EnumPanelType panelType : EnumPanelType.values()) {
			list.add(new ItemStack(this, 1, panelType.ordinal()));
		}
	}
}
