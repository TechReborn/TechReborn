/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2025 TeamReborn
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

package reborncore.client.mixin;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import reborncore.common.EntityAccessor;
import reborncore.common.fluid.RebornFluid;

@Mixin(Entity.class)
public class MixinEntity {
	@Redirect(method = "onSwimmingStart()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"))
	private void addParticle(World world, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		if (!(world instanceof ClientWorld clientWorld)) return;
		int color = ((EntityAccessor) this).reborncore$getParticleColor();
		if (color == -1) {
			world.addParticle(parameters, x, y, z, velocityX, velocityY, velocityZ);
			return;
		}

		Particle particle = clientWorld.worldRenderer.spawnParticle(
			parameters == ParticleTypes.BUBBLE ? RebornFluid.BUBBLE : RebornFluid.SPLASH,
			false, x, y, z, velocityX, velocityY, velocityZ
		);
		if (particle != null) {
			particle.setColor(
				(color >> 16 & 0xFF) / 255.0F,
				(color >> 8 & 0xFF) / 255.0F,
				(color & 0xFF) / 255.0F
			);
		}
	}
}
