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

package reborncore.client.screen.builder;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.Range;
import reborncore.client.IconSupplier;
import reborncore.client.screen.builder.slot.PlayerInventorySlot;
import reborncore.client.screen.builder.slot.SpriteSlot;

public final class PlayerScreenHandlerBuilder {

	private final PlayerInventory player;
	private final ScreenHandlerBuilder parent;
	private Range<Integer> main;
	private Range<Integer> hotbar;
	private Range<Integer> armor;

	PlayerScreenHandlerBuilder(final ScreenHandlerBuilder parent, final PlayerInventory player) {
		this.player = player;
		this.parent = parent;
	}

	public PlayerScreenHandlerBuilder inventory(final int xStart, final int yStart) {
		final int startIndex = this.parent.slots.size();
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.parent.slots.add(new PlayerInventorySlot(this.player, j + i * 9 + 9, xStart + j * 18, yStart + i * 18));
			}
		}
		this.main = Range.between(startIndex, this.parent.slots.size() - 1);
		return this;
	}

	public PlayerScreenHandlerBuilder hotbar(final int xStart, final int yStart) {
		final int startIndex = this.parent.slots.size();
		for (int i = 0; i < 9; ++i) {
			this.parent.slots.add(new PlayerInventorySlot(this.player, i, xStart + i * 18, yStart));
		}
		this.hotbar = Range.between(startIndex, this.parent.slots.size() - 1);
		return this;
	}

	public PlayerScreenHandlerBuilder inventory() {
		return this.inventory(8, 94);
	}

	public PlayerScreenHandlerBuilder hotbar() {
		return this.hotbar(8, 152);
	}

	public PlayerArmorScreenHandlerBuilder armor() {
		return new PlayerArmorScreenHandlerBuilder(this);
	}

	public ScreenHandlerBuilder addInventory() {
		if (this.hotbar != null) {
			this.parent.addPlayerInventoryRange(this.hotbar);
		}
		if (this.main != null) {
			this.parent.addPlayerInventoryRange(this.main);
		}
		if (this.armor != null) {
			this.parent.addBlockEnityInventoryRange(this.armor);
		}

		return this.parent;
	}

	public static final class PlayerArmorScreenHandlerBuilder {
		private final PlayerScreenHandlerBuilder parent;
		private final int startIndex;

		public PlayerArmorScreenHandlerBuilder(final PlayerScreenHandlerBuilder parent) {
			this.parent = parent;
			this.startIndex = parent.parent.slots.size();
		}

		private PlayerArmorScreenHandlerBuilder armor(final int index, final int xStart, final int yStart,
													  final EquipmentSlot slotType, final Identifier sprite) {
			this.parent.parent.slots.add(new SpriteSlot(this.parent.player, index, xStart, yStart, sprite, 1)
					.setFilter(stack -> {
						if (stack.getItem() instanceof ArmorItem) {
							return ((ArmorItem) stack.getItem()).getSlotType() == slotType;
						}
						return false;
					}));
			return this;
		}

		public PlayerArmorScreenHandlerBuilder helmet(final int xStart, final int yStart) {
			return this.armor(this.parent.player.size() - 2, xStart, yStart, EquipmentSlot.HEAD, IconSupplier.armour_head_id);
		}

		public PlayerArmorScreenHandlerBuilder chestplate(final int xStart, final int yStart) {
			return this.armor(this.parent.player.size() - 3, xStart, yStart, EquipmentSlot.CHEST, IconSupplier.armour_chest_id);
		}

		public PlayerArmorScreenHandlerBuilder leggings(final int xStart, final int yStart) {
			return this.armor(this.parent.player.size() - 4, xStart, yStart, EquipmentSlot.LEGS, IconSupplier.armour_legs_id);
		}

		public PlayerArmorScreenHandlerBuilder boots(final int xStart, final int yStart) {
			return this.armor(this.parent.player.size() - 5, xStart, yStart, EquipmentSlot.FEET, IconSupplier.armour_feet_id);
		}

		public PlayerArmorScreenHandlerBuilder complete(final int xStart, final int yStart) {
			return this.helmet(xStart, yStart).chestplate(xStart, yStart + 18).leggings(xStart, yStart + 18 + 18)
					.boots(xStart, yStart + 18 + 18 + 18);
		}

		public PlayerScreenHandlerBuilder addArmor() {
			this.parent.armor = Range.between(this.startIndex - 1, this.parent.parent.slots.size() - 2);
			return this.parent;
		}
	}
}