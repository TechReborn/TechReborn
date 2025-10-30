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

import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import reborncore.api.items.InventoryBase;

public class InventoryItem extends InventoryBase implements DataComponentHolder {

	// ItemStack of InventoryItem
	@NotNull
	ItemStack stack;
	private final DataComponentMap components = DataComponentMap.EMPTY;

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
		components.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(this.getStacks());
	}

	/**
	 *  Save {@link net.minecraft.world.Container} to ContainerComponent
	 *
	 **/
	@Override
	public void setChanged() {
		DataComponentMap.Builder builder = DataComponentMap.builder();
		builder.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.getStacks()));
		this.getContainerStack().applyComponents(builder.build());
	}

	@Override
	public DataComponentMap getComponents() {
		return !this.isEmpty() ? this.components : DataComponentMap.EMPTY;
	}
}
