/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

package techreborn.blocks.misc;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import reborncore.common.BaseBlock;
import techreborn.config.TechRebornConfig;
import techreborn.entities.EntityNukePrimed;

/**
 * Created by Mark on 13/03/2016.
 */
public class BlockNuke extends BaseBlock {
	public static BooleanProperty OVERLAY = BooleanProperty.of("overlay");

	public BlockNuke() {
		super(Block.Settings.of(Material.TNT));
		this.setDefaultState(this.getStateManager().getDefaultState().with(OVERLAY, false));
	}

	public void ignite(World worldIn, BlockPos pos, BlockState state, LivingEntity igniter) {
		if (!worldIn.isClient) {
			EntityNukePrimed entitynukeprimed = new EntityNukePrimed(worldIn, (float) pos.getX() + 0.5F,
					pos.getY(), (float) pos.getZ() + 0.5F, igniter);
			worldIn.spawnEntity(entitynukeprimed);
			worldIn.playSound(null, entitynukeprimed.getX(), entitynukeprimed.getY(), entitynukeprimed.getZ(),
					SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
	}

	@Override
	public void onDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
		if (!worldIn.isClient) {
			EntityNukePrimed entitynukeprimed = new EntityNukePrimed(worldIn, (float) pos.getX() + 0.5F,
					pos.getY(), (float) pos.getZ() + 0.5F, explosionIn.getCausingEntity());
			entitynukeprimed.setFuse(worldIn.random.nextInt(TechRebornConfig.nukeFuseTime / 4) + TechRebornConfig.nukeFuseTime / 8);
			worldIn.spawnEntity(entitynukeprimed);
		}
	}

	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		if (!worldIn.isClient && entityIn instanceof ProjectileEntity) {
			ProjectileEntity entityarrow = (ProjectileEntity) entityIn;
			LivingEntity shooter = null;
			if (entityarrow.getOwner() instanceof LivingEntity) {
				shooter = (LivingEntity) entityarrow.getOwner();
			}
			if (entityarrow.isOnFire()) {
				ignite(worldIn, pos, state, shooter);
				worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
			}
		}
	}

	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean b) {
		super.onBlockAdded(state, worldIn, pos, oldState, b);
		if (worldIn.isReceivingRedstonePower(pos)) {
			ignite(worldIn, pos, state, null);
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
		}
	}

	@Override
	public void neighborUpdate(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_, boolean bo) {
		if (worldIn.isReceivingRedstonePower(pos)) {
			ignite(worldIn, pos, state, null);
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(OVERLAY);
	}
}
