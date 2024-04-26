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

package reborncore.common.util;

import net.minecraft.component.*;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import reborncore.api.items.InventoryBase;

public class InventoryItem extends InventoryBase implements ComponentHolder {

	// ItemStack of InventoryItem
	@NotNull
	ItemStack stack;
	private final ComponentMap components = ComponentMap.EMPTY;

	private InventoryItem(@NotNull ItemStack stack, int size) {
		super(size);
		Validate.notNull(stack, "Stack is empty");
		Validate.isTrue(!stack.isEmpty());
		this.stack = stack;
	}

	public @NotNull ItemStack getContainerStack() {
		return stack;
	}

	/**
	 * Copy inventory stacks from ContainerComponent to inventory. Call this in screenhandler.
	 */
	public final void readComponents(){
		components.getOrDefault(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT).copyTo(this.getStacks());
	}

	/**
	 *  Save {@link net.minecraft.inventory.Inventory} to ContainerComponent
	 *
	 **/
	@Override
	public void markDirty() {
		ComponentMap.Builder builder = ComponentMap.builder();
		builder.add(DataComponentTypes.CONTAINER, ContainerComponent.fromStacks(this.getStacks()));
		this.getContainerStack().applyComponentsFrom(builder.build());
	}

	@Override
	public ComponentMap getComponents() {
		return !this.isEmpty() ? this.components : ComponentMap.EMPTY;
	}
}
