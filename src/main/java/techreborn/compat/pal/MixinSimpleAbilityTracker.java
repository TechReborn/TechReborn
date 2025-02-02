/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2025 TechReborn
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

package techreborn.compat.pal;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.ladysnake.pal.PlayerAbility;
import io.github.ladysnake.pal.SimpleAbilityTracker;
import io.github.ladysnake.pal.VanillaAbilities;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import techreborn.init.TRAbility;

@Mixin(SimpleAbilityTracker.class)
public abstract class MixinSimpleAbilityTracker {
	@Final
	@Shadow
	protected PlayerEntity player;

	@Unique
	public boolean isAbilityAllowFlying;

	@Inject(method = "<init>(Lio/github/ladysnake/pal/PlayerAbility;Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At("TAIL"))
	public void SimpleAbilityTracker(PlayerAbility ability, PlayerEntity player, CallbackInfo ci) {
		isAbilityAllowFlying = ability == VanillaAbilities.ALLOW_FLYING;
	}

	@ModifyReturnValue(method = "shouldBeEnabled()Z", at = @At("TAIL"))
	boolean checkAllowFlying(boolean enabled) {
		if (enabled) {
			TRAbility.allowFreeFlying(player);
		} else {
			TRAbility.dontAllowFreeFlying(player);
		}
		return this.isAbilityAllowFlying && TRAbility.ALLOW_FLYING.containsKey(player);
	}

	@ModifyExpressionValue(method = "addSource(Lio/github/ladysnake/pal/AbilitySource;)V", at = @At(value = "INVOKE", target = "Lio/github/ladysnake/pal/PlayerAbilityEnableCallback;allow(Lnet/minecraft/entity/player/PlayerEntity;Lio/github/ladysnake/pal/PlayerAbility;Lio/github/ladysnake/pal/AbilitySource;)Z"))
	boolean checkAdd(boolean empty) {
		return empty && !(isAbilityAllowFlying && TRAbility.ALLOW_FLYING.containsKey(player));
	}

	@ModifyExpressionValue(method = "removeSource(Lio/github/ladysnake/pal/AbilitySource;)V", at = @At(value = "INVOKE", target = "Ljava/util/SortedSet;isEmpty()Z"))
	boolean checkRemove(boolean empty) {
		return empty && !(isAbilityAllowFlying && TRAbility.ALLOW_FLYING.containsKey(player));
	}

	@Inject(method = "addSource(Lio/github/ladysnake/pal/AbilitySource;)V", at = @At(value = "INVOKE", target = "Lio/github/ladysnake/pal/SimpleAbilityTracker;updateState(Z)V"))
	void allowFreeFlying(CallbackInfo ci) {
		if (isAbilityAllowFlying) {
			TRAbility.allowFreeFlying(player);
		}
	}

	@Inject(method = "removeSource(Lio/github/ladysnake/pal/AbilitySource;)V", at = @At(value = "INVOKE", target = "Lio/github/ladysnake/pal/SimpleAbilityTracker;updateState(Z)V"))
	void dontAllowFreeFlying(CallbackInfo ci) {
		if (isAbilityAllowFlying) {
			TRAbility.dontAllowFreeFlying(player);
		}
	}
}
