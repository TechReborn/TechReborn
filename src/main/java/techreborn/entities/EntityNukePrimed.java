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

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.common.explosion.RebornExplosion;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRContent;

/**
 * Created by Mark on 13/03/2016.
 */
public class EntityNukePrimed extends TntEntity {
	@Nullable LivingEntity owner;

	private final ServerBossBar bossBar = new ServerBossBar(Text.translatable("block.techreborn.nuke"), BossBar.Color.RED, BossBar.Style.PROGRESS);


	public EntityNukePrimed(EntityType<? extends EntityNukePrimed> type, World world) {
		super(type, world);
		setFuse(TechRebornConfig.nukeFuseTime);
	}

	public EntityNukePrimed(World world, double x, double y, double z, LivingEntity owner) {
		this(TRContent.ENTITY_NUKE, world);

		this.setPosition(x, y, z);
		double d = world.random.nextDouble() * 6.2831854820251465;
		this.setVelocity(-Math.sin(d) * 0.02, 0.2f, -Math.cos(d) * 0.02);
		this.setFuse(80);
		this.prevX = x;
		this.prevY = y;
		this.prevZ = z;
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
		if (!this.hasNoGravity()) {
			this.setVelocity(this.getVelocity().add(0.0D, -0.04D, 0.0D));
		}

		this.move(MovementType.SELF, this.getVelocity());
		this.setVelocity(this.getVelocity().multiply(0.98D));
		if (this.isOnGround()) {
			this.setVelocity(this.getVelocity().multiply(0.7D, -0.5D, 0.7D));
		}

		setFuse(getFuse() - 1);
		bossBar.setPercent((float) getFuse() / TechRebornConfig.nukeFuseTime);

		if (this.getFuse() <= 0) {
			this.remove(RemovalReason.KILLED);
			if (!this.getWorld().isClient) {
				this.explodeNuke();
			}
		} else {
			this.updateWaterState();
		}
	}

	public void explodeNuke() {
		if (!TechRebornConfig.nukeEnabled) {
			return;
		}
		RebornExplosion nukeExplosion = new RebornExplosion(getBlockPos(), getWorld(), TechRebornConfig.nukeRadius);
		nukeExplosion.setLivingBase(getOwner());
		nukeExplosion.applyExplosion();
	}

	@Override
	public void onStartedTrackingBy(ServerPlayerEntity player) {
		super.onStartedTrackingBy(player);
		this.bossBar.addPlayer(player);
	}

	@Override
	public void onStoppedTrackingBy(ServerPlayerEntity player) {
		super.onStoppedTrackingBy(player);
		this.bossBar.removePlayer(player);
	}
}
