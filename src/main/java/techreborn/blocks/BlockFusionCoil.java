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

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;
import reborncore.api.ToolManager;
import techreborn.client.TechRebornCreativeTab;
import techreborn.init.ModBlocks;
import techreborn.init.ModSounds;
import techreborn.lib.ModInfo;

public class BlockFusionCoil extends Block {

	public BlockFusionCoil() {
		super(Material.IRON);
		setHardness(2f);
		setSoundType(SoundType.METAL);
		setCreativeTab(TechRebornCreativeTab.instance);
		ShootingStar.registerModel(new ModelCompound(ModInfo.MOD_ID, this, "machines/generators"));
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

		ItemStack tool = playerIn.getHeldItem(EnumHand.MAIN_HAND);
		if (!tool.isEmpty() && ToolManager.INSTANCE.canHandleTool(tool)) {
			if (ToolManager.INSTANCE.handleTool(tool, pos, worldIn, playerIn, side, false)) {
				if (playerIn.isSneaking()) {
					ItemStack drop = new ItemStack(ModBlocks.FUSION_COIL, 1);
					spawnAsEntity(worldIn, pos, drop);
					worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, ModSounds.BLOCK_DISMANTLE,
							SoundCategory.BLOCKS, 0.6F, 1F);
					if (!worldIn.isRemote) {
						worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
					}
					return true;
				}
			}
		}
		return false;
	}
}
