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
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;
import reborncore.common.BaseBlock;
import techreborn.entities.EntityNukePrimed;
import techreborn.lib.ModInfo;
import techreborn.utils.TechRebornCreativeTab;

/**
 * Created by Mark on 13/03/2016.
 */
public class BlockNuke extends BaseBlock {
	public static final PropertyBool OVERLAY = PropertyBool.create("overlay");

	public BlockNuke() {
		super(Material.TNT);
		setTranslationKey("techreborn.nuke");
		setCreativeTab(TechRebornCreativeTab.instance);
		this.setDefaultState(this.blockState.getBaseState().withProperty(OVERLAY, false));
		ShootingStar.registerModel(new ModelCompound(ModInfo.MOD_ID, this));
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
	public void onBlockExploded(World worldIn, BlockPos pos, Explosion explosionIn) {
		if (!worldIn.isRemote) {
			EntityNukePrimed entitynukeprimed = new EntityNukePrimed(worldIn, (double) ((float) pos.getX() + 0.5F),
					(double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), explosionIn.getExplosivePlacedBy());
			entitynukeprimed.setFuse(worldIn.rand.nextInt(EntityNukePrimed.fuseTime / 4) + EntityNukePrimed.fuseTime / 8);
			worldIn.spawnEntity(entitynukeprimed);
		}
	}

	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (!worldIn.isRemote && entityIn instanceof EntityArrow) {
			EntityArrow entityarrow = (EntityArrow) entityIn;
			EntityLivingBase shooter = null;
			if (entityarrow.shootingEntity instanceof EntityLivingBase) {
				shooter = (EntityLivingBase) entityarrow.shootingEntity;
			}
			if (entityarrow.isBurning()) {
				ignite(worldIn, pos, state, shooter);
				worldIn.setBlockToAir(pos);
			}
		}
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
		if (worldIn.isBlockPowered(pos)) {
			ignite(worldIn, pos, state, null);
			worldIn.setBlockToAir(pos);
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
		if (worldIn.isBlockPowered(pos)) {
			ignite(worldIn, pos, state, null);
			worldIn.setBlockToAir(pos);
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		return false;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(OVERLAY) ? 1 : 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(OVERLAY, (meta & 1) > 0);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, OVERLAY);
	}

}
