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

package techreborn.blocks.generator.solarpanel;

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
import reborncore.api.tile.IMachineGuiHandler;
import reborncore.common.blocks.RebornMachineBlock;
import techreborn.utils.TechRebornCreativeTab;
import techreborn.lib.ModInfo;
import techreborn.tiles.generator.TileSolarPanel;

/**
 * Created by modmuss50 on 25/02/2016.
 */
public class BlockSolarPanel extends RebornMachineBlock {
	public static final String[] panes = new String[] {
		"basic", "hybrid", "advanced", "ultimate", "quantum"};
	public static PropertyBool ACTIVE = PropertyBool.create("active");
	public static final IProperty<EnumPanelType> TYPE = PropertyEnum.create("type", EnumPanelType.class);

	public BlockSolarPanel() {
		super(true);
		setCreativeTab(TechRebornCreativeTab.instance);
		this.setDefaultState(this.getBlockState().getBaseState().withProperty(ACTIVE, false).withProperty(TYPE, EnumPanelType.Basic));
		for (int i = 0; i < panes.length; i++) {
			ShootingStar.registerModel(new ModelCompound(ModInfo.MOD_ID, this, i, "machines/generators").setInvVariant("active=false,type=" + panes[i]));
		}
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int active = state.getValue(ACTIVE) ? EnumPanelType.values().length : 0;
		return state.getValue(TYPE).ordinal() + active;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, ACTIVE, TYPE);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		boolean active = false;
		if(meta >= EnumPanelType.values().length){
			active = true;
			meta -= EnumPanelType.values().length;
		}
		return getDefaultState().withProperty(ACTIVE, active).withProperty(TYPE, EnumPanelType.values()[meta]);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileSolarPanel(getStateFromMeta(meta).getValue(TYPE));
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getStateFromMeta(meta);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		//return new ItemStack(Item.getItemFromBlock(this), 1, getMetaFromState(world.getBlockState(pos)));
		return new ItemStack(Item.getItemFromBlock(this), 1, world.getBlockState(pos).getValue(TYPE).ordinal());
	}
	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(TYPE).ordinal();
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		for (EnumPanelType panelType : EnumPanelType.values()) {
			list.add(new ItemStack(this, 1, panelType.ordinal()));
		}
	}

	@Override
	public IMachineGuiHandler getGui() {
		return null;
	}
}
