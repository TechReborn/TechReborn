/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import reborncore.api.events.ItemCraftCallback;
import reborncore.common.recipes.ExtendedRecipeRemainder;

@Mixin(CraftingResultSlot.class)
public abstract class MixinCraftingResultSlot {

	@Shadow
	@Final
	private CraftingInventory input;

	@Shadow
	@Final
	private PlayerEntity player;

	@ModifyVariable(method = "onTakeItem", at = @At(value = "INVOKE"), index = 3)
	private DefaultedList<ItemStack> defaultedList(DefaultedList<ItemStack> list) {
		for (int i = 0; i < input.size(); i++) {
			ItemStack invStack = input.getStack(i);
			if (invStack.getItem() instanceof ExtendedRecipeRemainder) {
				ItemStack remainder = ((ExtendedRecipeRemainder) invStack.getItem()).getRemainderStack(invStack.copy());
				if (!remainder.isEmpty()) {
					list.set(i, remainder);
				}
			}
		}
		return list;
	}

	@Inject(method = "onCrafted(Lnet/minecraft/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;onCraft(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;I)V", shift = At.Shift.AFTER))
	private void onCrafted(ItemStack itemStack, CallbackInfo info) {
		ItemCraftCallback.EVENT.invoker().onCraft(itemStack, input, player);
	}

}