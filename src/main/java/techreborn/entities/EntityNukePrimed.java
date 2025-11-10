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

package techreborn.entities;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import reborncore.common.explosion.RebornExplosion;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRContent;

/**
 * Created by Mark on 13/03/2016.
 */
public class EntityNukePrimed extends PrimedTnt {
	@Nullable LivingEntity owner;

	private final ServerBossEvent bossBar = new ServerBossEvent(Component.translatable("block.techreborn.nuke"), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);


	public EntityNukePrimed(EntityType<? extends EntityNukePrimed> type, Level world) {
		super(type, world);
		setFuse(TechRebornConfig.nukeFuseTime);
	}

	public EntityNukePrimed(Level world, double x, double y, double z, @Nullable LivingEntity owner) {
		this(TRContent.ENTITY_NUKE, world);

		this.setPos(x, y, z);
		double d = world.random.nextDouble() * 6.2831854820251465;
		this.setDeltaMovement(-Math.sin(d) * 0.02, 0.2f, -Math.cos(d) * 0.02);
		this.setFuse(80);
		this.xo = x;
		this.yo = y;
		this.zo = z;
		this.owner = owner;
		setFuse(TechRebornConfig.nukeFuseTime);
	}

	@Nullable
	@Override
	public LivingEntity getOwner() {
		return owner;
	}

	@Override
	public void tick() {
		if (!this.isNoGravity()) {
			this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
		}

		this.move(MoverType.SELF, this.getDeltaMovement());
		this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
		if (this.onGround()) {
			this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
		}

		setFuse(getFuse() - 1);
		bossBar.setProgress((float) getFuse() / TechRebornConfig.nukeFuseTime);

		if (this.getFuse() <= 0) {
			this.remove(RemovalReason.KILLED);
			if (!this.level().isClientSide()) {
				this.explodeNuke();
			}
		} else {
			this.updateInWaterStateAndDoFluidPushing();
		}
	}

	public void explodeNuke() {
		if (!TechRebornConfig.nukeEnabled) {
			return;
		}
		RebornExplosion nukeExplosion = new RebornExplosion(blockPosition(), (ServerLevel)level(), TechRebornConfig.nukeRadius);
		nukeExplosion.setLivingBase(getOwner());
		nukeExplosion.explode();
	}

	@Override
	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		this.bossBar.addPlayer(player);
	}

	@Override
	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		this.bossBar.removePlayer(player);
	}
}
