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

package reborncore.mixin.common;

import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import reborncore.common.EntityAccessor;
import reborncore.common.fluid.RebornFluid;

import java.util.Set;

@Mixin(Entity.class)
public class MixinEntity implements EntityAccessor {
	@Unique
	private int particleColor = -1;
	@Override
	public int reborncore$getParticleColor() {
		return particleColor;
	}

	@Inject(method = "updateMovementInFluid(Lnet/minecraft/registry/tag/TagKey;D)Z", at = @At(value = "NEW", target = "Lnet/minecraft/util/math/BlockPos$Mutable;"))
	private void resetParticleColor(TagKey<Fluid> tag, double speed, CallbackInfoReturnable<Boolean> cir) {
		if (tag == FluidTags.WATER) {
			particleColor = -1;
		}
	}

	@Redirect(method = "updateMovementInFluid(Lnet/minecraft/registry/tag/TagKey;D)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
	private boolean isIn(FluidState fluidState, TagKey<Fluid> tag) {
		if (fluidState.isIn(tag)) {
			return true;
		}
		if (tag == FluidTags.WATER && fluidState.isIn(RebornFluid.WATER)) {
			if (particleColor == -1) {
				particleColor = ((RebornFluid) fluidState.getFluid()).getColor();
			}
			return true;
		}
		return false;
	}

	@Redirect(method = "updateSwimming()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
	private boolean isInWater(FluidState fluidState, TagKey<Fluid> tag) {
		return fluidState.isIn(tag) || fluidState.isIn(RebornFluid.WATER);
	}

	@Redirect(method = "isSubmergedIn(Lnet/minecraft/registry/tag/TagKey;)Z", at = @At(value = "INVOKE", target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z"))
	private boolean isSubmergedIn(Set<TagKey<Fluid>> submergedFluidTag, Object tag) {
		return submergedFluidTag.contains(tag) || (tag == FluidTags.WATER && submergedFluidTag.contains(RebornFluid.WATER));
	}
}
