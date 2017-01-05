package techreborn.client.container.builder;

import org.apache.commons.lang3.Range;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;

import reborncore.client.gui.slots.BaseSlot;

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
				this.parent.slots.add(new BaseSlot(this.player, j + i * 9 + 9, xStart + j * 18, yStart + i * 18));
		this.main = Range.between(startIndex, this.parent.slots.size() - 1);
		return this;
	}

	public ContainerPlayerInventoryBuilder hotbar(final int xStart, final int yStart) {
		final int startIndex = this.parent.slots.size();
		for (int i = 0; i < 9; ++i)
			this.parent.slots.add(new BaseSlot(this.player, i, xStart + i * 18, yStart));
		this.hotbar = Range.between(startIndex, this.parent.slots.size() - 1);
		return this;
	}

	public ContainerPlayerInventoryBuilder inventory() {
		return this.inventory(8, 84);
	}

	public ContainerPlayerInventoryBuilder hotbar() {
		return this.hotbar(8, 142);
	}

	private ContainerPlayerInventoryBuilder armor(final int index, final int xStart, final int yStart,
			final EntityEquipmentSlot slotType) {
		this.parent.slots.add(new ArmorSlot(slotType, this.player, index, xStart, yStart));
		return this;
	}

	public ContainerPlayerInventoryBuilder helmet(final int xStart, final int yStart) {
		return this.armor(this.player.getSizeInventory() - 2, xStart, yStart, EntityEquipmentSlot.HEAD);
	}

	public ContainerPlayerInventoryBuilder chestplate(final int xStart, final int yStart) {
		return this.armor(this.player.getSizeInventory() - 3, xStart, yStart, EntityEquipmentSlot.CHEST);
	}

	public ContainerPlayerInventoryBuilder leggings(final int xStart, final int yStart) {
		return this.armor(this.player.getSizeInventory() - 4, xStart, yStart, EntityEquipmentSlot.LEGS);
	}

	public ContainerPlayerInventoryBuilder boots(final int xStart, final int yStart) {
		return this.armor(this.player.getSizeInventory() - 5, xStart, yStart, EntityEquipmentSlot.FEET);
	}

	public ContainerPlayerInventoryBuilder armor(final int xStart, final int yStart) {
		return this.helmet(xStart, yStart).chestplate(xStart, yStart + 19).leggings(xStart, yStart + 38).boots(xStart,
				yStart + 57);
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
				final EntityEquipmentSlot slotType) {
			this.parent.parent.slots.add(new ArmorSlot(slotType, this.parent.player, index, xStart, yStart));
			return this;
		}

		public ContainerPlayerArmorInventoryBuilder helmet(final int xStart, final int yStart) {
			return this.armor(this.parent.player.getSizeInventory() - 2, xStart, yStart, EntityEquipmentSlot.HEAD);
		}

		public ContainerPlayerArmorInventoryBuilder chestplate(final int xStart, final int yStart) {
			return this.armor(this.parent.player.getSizeInventory() - 3, xStart, yStart, EntityEquipmentSlot.CHEST);
		}

		public ContainerPlayerArmorInventoryBuilder leggings(final int xStart, final int yStart) {
			return this.armor(this.parent.player.getSizeInventory() - 4, xStart, yStart, EntityEquipmentSlot.LEGS);
		}

		public ContainerPlayerArmorInventoryBuilder boots(final int xStart, final int yStart) {
			return this.armor(this.parent.player.getSizeInventory() - 5, xStart, yStart, EntityEquipmentSlot.FEET);
		}

		public ContainerPlayerArmorInventoryBuilder complete(final int xStart, final int yStart) {
			return this.helmet(xStart, yStart).chestplate(xStart, yStart + 19).leggings(xStart, yStart + 38)
					.boots(xStart, yStart + 57);
		}

		public ContainerPlayerInventoryBuilder addArmor() {
			this.parent.armor = Range.between(this.startIndex, this.parent.parent.slots.size() - 1);
			return this.parent;
		}
	}
}