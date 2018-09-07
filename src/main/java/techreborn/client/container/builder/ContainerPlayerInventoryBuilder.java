/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.client.container.builder;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.apache.commons.lang3.Range;
import techreborn.client.IconSupplier;
import techreborn.client.container.builder.slot.SpriteSlot;

public final class ContainerPlayerInventoryBuilder {

	private final InventoryPlayer player;
	private final ContainerBuilder parent;
	private Range<Integer> main;
	private Range<Integer> hotbar;
	private Range<Integer> armor;

	ContainerPlayerInventoryBuilder(final ContainerBuilder parent, final InventoryPlayer player) {
		this.player = player;
		this.parent = parent;
	}

	public ContainerPlayerInventoryBuilder inventory(final int xStart, final int yStart) {
		final int startIndex = this.parent.slots.size();
		for (int i = 0; i < 3; ++i)
			for (int j = 0; j < 9; ++j)
				this.parent.slots.add(new Slot(this.player, j + i * 9 + 9, xStart + j * 18, yStart + i * 18));
		this.main = Range.between(startIndex, this.parent.slots.size() - 1);
		return this;
	}

	public ContainerPlayerInventoryBuilder hotbar(final int xStart, final int yStart) {
		final int startIndex = this.parent.slots.size();
		for (int i = 0; i < 9; ++i)
			this.parent.slots.add(new Slot(this.player, i, xStart + i * 18, yStart));
		this.hotbar = Range.between(startIndex, this.parent.slots.size() - 1);
		return this;
	}

	public ContainerPlayerInventoryBuilder inventory() {
		return this.inventory(8, 94);
	}

	public ContainerPlayerInventoryBuilder hotbar() {
		return this.hotbar(8, 152);
	}

	public ContainerPlayerArmorInventoryBuilder armor() {
		return new ContainerPlayerArmorInventoryBuilder(this);
	}

	public ContainerBuilder addInventory() {
		if (this.hotbar != null)
			this.parent.addPlayerInventoryRange(this.hotbar);
		if (this.main != null)
			this.parent.addPlayerInventoryRange(this.main);
		if (this.armor != null)
			this.parent.addTileInventoryRange(this.armor);

		return this.parent;
	}

	public static final class ContainerPlayerArmorInventoryBuilder {
		private final ContainerPlayerInventoryBuilder parent;
		private final int startIndex;

		public ContainerPlayerArmorInventoryBuilder(final ContainerPlayerInventoryBuilder parent) {
			this.parent = parent;
			this.startIndex = parent.parent.slots.size();
		}

		private ContainerPlayerArmorInventoryBuilder armor(final int index, final int xStart, final int yStart,
		                                                   final EntityEquipmentSlot slotType, final String sprite) {
			this.parent.parent.slots.add(new SpriteSlot(new InvWrapper(this.parent.player), index, xStart, yStart, sprite, 1)
					.setFilter(stack -> stack.getItem().isValidArmor(stack, slotType, this.parent.player.player)));
			return this;
		}

		public ContainerPlayerArmorInventoryBuilder helmet(final int xStart, final int yStart) {
			return this.armor(this.parent.player.getSizeInventory() - 2, xStart, yStart, EntityEquipmentSlot.HEAD, IconSupplier.armour_head_name);
		}

		public ContainerPlayerArmorInventoryBuilder chestplate(final int xStart, final int yStart) {
			return this.armor(this.parent.player.getSizeInventory() - 3, xStart, yStart, EntityEquipmentSlot.CHEST, IconSupplier.armour_chest_name);
		}

		public ContainerPlayerArmorInventoryBuilder leggings(final int xStart, final int yStart) {
			return this.armor(this.parent.player.getSizeInventory() - 4, xStart, yStart, EntityEquipmentSlot.LEGS, IconSupplier.armour_legs_name);
		}

		public ContainerPlayerArmorInventoryBuilder boots(final int xStart, final int yStart) {
			return this.armor(this.parent.player.getSizeInventory() - 5, xStart, yStart, EntityEquipmentSlot.FEET, IconSupplier.armour_feet_name);
		}

		public ContainerPlayerArmorInventoryBuilder complete(final int xStart, final int yStart) {
			return this.helmet(xStart, yStart).chestplate(xStart, yStart + 18).leggings(xStart, yStart + 18 + 18)
				.boots(xStart, yStart + 18 + 18 + 18);
		}

		public ContainerPlayerInventoryBuilder addArmor() {
			this.parent.armor = Range.between(this.startIndex, this.parent.parent.slots.size() - 1);
			return this.parent;
		}
	}
}