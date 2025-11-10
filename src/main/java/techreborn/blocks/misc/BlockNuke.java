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

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import org.jetbrains.annotations.Nullable;
import reborncore.common.BaseBlock;
import techreborn.config.TechRebornConfig;
import techreborn.entities.EntityNukePrimed;
import techreborn.init.TRBlockSettings;

/**
 * Created by Mark on 13/03/2016.
 */
public class BlockNuke extends BaseBlock {
	public static final BooleanProperty OVERLAY = BooleanProperty.create("overlay");

	public BlockNuke(String name) {
		super(TRBlockSettings.nuke(name));
		this.registerDefaultState(this.getStateDefinition().any().setValue(OVERLAY, false));
	}

	public void ignite(Level worldIn, BlockPos pos, BlockState state, LivingEntity igniter) {
		if (!worldIn.isClientSide()) {
			EntityNukePrimed entitynukeprimed = new EntityNukePrimed(worldIn, (float) pos.getX() + 0.5F,
					pos.getY(), (float) pos.getZ() + 0.5F, igniter);
			worldIn.addFreshEntity(entitynukeprimed);
			worldIn.playSound(null, entitynukeprimed.getX(), entitynukeprimed.getY(), entitynukeprimed.getZ(),
					SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
		}
	}

	@Override
	public void wasExploded(ServerLevel worldIn, BlockPos pos, Explosion explosionIn) {
		EntityNukePrimed entitynukeprimed = new EntityNukePrimed(worldIn, (float) pos.getX() + 0.5F,
				pos.getY(), (float) pos.getZ() + 0.5F, explosionIn.getIndirectSourceEntity());
		entitynukeprimed.setFuse(worldIn.random.nextInt(TechRebornConfig.nukeFuseTime / 4) + TechRebornConfig.nukeFuseTime / 8);
		worldIn.addFreshEntity(entitynukeprimed);
	}

	@Override
	protected void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn, InsideBlockEffectApplier handler) {
		if (!worldIn.isClientSide() && entityIn instanceof Projectile projectileEntity) {
			LivingEntity shooter = null;
			if (projectileEntity.getOwner() instanceof LivingEntity) {
				shooter = (LivingEntity) projectileEntity.getOwner();
			}
			if (projectileEntity.isOnFire()) {
				ignite(worldIn, pos, state, shooter);
				worldIn.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			}
		}
	}

	@Override
	public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean b) {
		super.onPlace(state, worldIn, pos, oldState, b);
		if (worldIn.hasNeighborSignal(pos)) {
			ignite(worldIn, pos, state, null);
			worldIn.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		}
	}

	@Override
	protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, @Nullable Orientation wireOrientation, boolean notify) {
		if (world.hasNeighborSignal(pos)) {
			ignite(world, pos, state, null);
			world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(OVERLAY);
	}
}
