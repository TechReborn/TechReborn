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
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import reborncore.common.explosion.RebornExplosion;
import techreborn.config.TechRebornConfig;

/**
 * Created by Mark on 13/03/2016.
 */
public class EntityNukePrimed extends TntEntity {

	public EntityNukePrimed(EntityType<EntityNukePrimed> entityType, World world) {
		super(entityType, world);
		setFuse(TechRebornConfig.nukeFuseTime);
	}

	public EntityNukePrimed(World world, double x, double y, double z, LivingEntity tntPlacedBy) {
		super(world, x, y, z, tntPlacedBy);
		setFuse(TechRebornConfig.nukeFuseTime);
	}

	@Override
	public void tick() {
		if (!this.hasNoGravity()) {
			this.setVelocity(this.getVelocity().add(0.0D, -0.04D, 0.0D));
		}

		this.move(MovementType.SELF, this.getVelocity());
		this.setVelocity(this.getVelocity().multiply(0.98D));
		if (this.onGround) {
			this.setVelocity(this.getVelocity().multiply(0.7D, -0.5D, 0.7D));
		}

		setFuse(getFuseTimer() - 1);
		if (this.getFuseTimer() <= 0) {
			this.remove();
			if (!this.world.isClient) {
				this.explodeNuke();
			}
		} else {
			this.updateWaterState();
			if (this.world.isClient) {
				this.world.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
			}
		}
	}

	public void explodeNuke() {
		if (!TechRebornConfig.nukeEnabled) {
			return;
		}
		RebornExplosion nukeExplosion = new RebornExplosion(getBlockPos(), world, TechRebornConfig.nukeRadius);
		nukeExplosion.setLivingBase(getCausingEntity());
		nukeExplosion.explode();
	}
}
