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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import reborncore.client.models.ModelCompound;
import reborncore.client.models.RebornModelRegistry;
import reborncore.common.BaseBlock;
import techreborn.TechReborn;
import techreborn.entities.EntityNukePrimed;

/**
 * Created by Mark on 13/03/2016.
 */
public class BlockNuke extends BaseBlock {
	public static BooleanProperty OVERLAY = BooleanProperty.create("overlay");

	public BlockNuke() {
		super(Block.Properties.create(Material.TNT));
		this.setDefaultState(this.stateContainer.getBaseState().with(OVERLAY, false));
		RebornModelRegistry.registerModel(new ModelCompound(TechReborn.MOD_ID, this));
	}

	public void ignite(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase igniter) {
		if (!worldIn.isRemote) {
			EntityNukePrimed entitynukeprimed = new EntityNukePrimed(worldIn, (double) ((float) pos.getX() + 0.5F),
					(double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter);
			worldIn.spawnEntity(entitynukeprimed);
			worldIn.playSound((EntityPlayer) null, entitynukeprimed.posX, entitynukeprimed.posY, entitynukeprimed.posZ,
					SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
	}

	@Override
	public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn) {
		if (!worldIn.isRemote) {
			EntityNukePrimed entitynukeprimed = new EntityNukePrimed(worldIn, (double) ((float) pos.getX() + 0.5F),
					(double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), explosionIn.getExplosivePlacedBy());
			entitynukeprimed.setFuse(worldIn.rand.nextInt(EntityNukePrimed.fuseTime / 4) + EntityNukePrimed.fuseTime / 8);
			worldIn.spawnEntity(entitynukeprimed);
		}
	}

	@Override
	public void onEntityCollision(IBlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		if (!worldIn.isRemote && entityIn instanceof EntityArrow) {
			EntityArrow entityarrow = (EntityArrow) entityIn;
			EntityLivingBase shooter = null;
			if (entityarrow.func_212360_k() instanceof EntityLivingBase) {
				shooter = (EntityLivingBase) entityarrow.func_212360_k();
			}
			if (entityarrow.isBurning()) {
				ignite(worldIn, pos, state, shooter);
				worldIn.removeBlock(pos);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onBlockAdded(IBlockState state, World worldIn, BlockPos pos, IBlockState oldState) {
		super.onBlockAdded(state, worldIn, pos, oldState);
		if (worldIn.isBlockPowered(pos)) {
			ignite(worldIn, pos, state, null);
			worldIn.removeBlock(pos);
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
		if (worldIn.isBlockPowered(pos)) {
			ignite(worldIn, pos, state, null);
			worldIn.removeBlock(pos);
		}
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
		builder.add(OVERLAY);
	}
}
