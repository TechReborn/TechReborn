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

package techreborn.blocks.tier2;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;
import reborncore.api.tile.IMachineGuiHandler;
import reborncore.common.blocks.RebornMachineBlock;
import reborncore.common.util.ItemUtils;
import techreborn.client.EGui;
import techreborn.items.IBlastFurnaceCoil;
import techreborn.lib.ModInfo;
import techreborn.tiles.multiblock.TileIndustrialBlastFurnace;
import techreborn.utils.TechRebornCreativeTab;

/**
 * @author modmuss50, estebes, drcrazy
 */
public class BlockIndustrialBlastFurnace extends RebornMachineBlock {

	public BlockIndustrialBlastFurnace() {
		super();
		setCreativeTab(TechRebornCreativeTab.instance);
		ShootingStar.registerModel(new ModelCompound(ModInfo.MOD_ID, this, "machines/tier2_machines"));
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int meta) {
		return new TileIndustrialBlastFurnace();
	}

	@Override
	public IMachineGuiHandler getGui() {
		return EGui.BLAST_FURNACE;
	}

	@Override
	public boolean isAdvanced() {
		return true;
	}

	// Handle coils >>
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(hand);

		if (ItemUtils.getSize(stack) >= 4) {
			if (stack.getItem() instanceof IBlastFurnaceCoil) {
				if (((IBlastFurnaceCoil) stack.getItem()).isValid(stack)) {
					TileEntity tileEntity = worldIn.getTileEntity(pos);
					if (tileEntity instanceof TileIndustrialBlastFurnace) {
						boolean ret = ((TileIndustrialBlastFurnace) tileEntity).addCoils(stack);
						if (ret) playerIn.setHeldItem(EnumHand.MAIN_HAND, ItemUtils.getSize(stack) == 4 ?
						                                                  ItemStack.EMPTY : ItemUtils.decreaseSize(stack, 4));
						return ret;
					}
				}
			}
		}

		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof TileIndustrialBlastFurnace) {
			NBTTagCompound nbt = ItemUtils.getStackNbtData(stack);
			((TileIndustrialBlastFurnace) tileEntity).coils = nbt.getByte("coils");
		}
	}

	// << Handle coils
}
