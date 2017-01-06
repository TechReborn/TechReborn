package techreborn.client.container.builder;

import org.apache.commons.lang3.Range;

import net.minecraft.inventory.IInventory;

import reborncore.client.gui.slots.BaseSlot;
import reborncore.client.gui.slots.SlotFake;
import reborncore.client.gui.slots.SlotOutput;

import techreborn.client.container.builder.slot.EnergyChargeSlot;

public class ContainerTileInventoryBuilder {

	private final IInventory tile;
	private final ContainerBuilder parent;
	private final int rangeStart;

	ContainerTileInventoryBuilder(final ContainerBuilder parent, final IInventory tile) {
		this.tile = tile;
		this.parent = parent;
		this.rangeStart = parent.slots.size();
	}

	public ContainerTileInventoryBuilder slot(final int index, final int x, final int y) {
		this.parent.slots.add(new BaseSlot(this.tile, index, x, y));
		return this;
	}

	public ContainerTileInventoryBuilder output(final int index, final int x, final int y) {
		this.parent.slots.add(new SlotOutput(this.tile, index, x, y));
		return this;
	}

	public ContainerTileInventoryBuilder fake(final int index, final int x, final int y) {
		this.parent.slots.add(new SlotFake(this.tile, index, x, y, false, false, Integer.MAX_VALUE));
		return this;
	}

	public ContainerTileInventoryBuilder energy(final int index, final int x, final int y) {
		this.parent.slots.add(new EnergyChargeSlot(this.tile, index, x, y));
		return this;
	}

	public ContainerBuilder addInventory() {
		this.parent.tileInventoryRanges.add(Range.between(this.rangeStart, this.parent.slots.size() - 1));
		return this.parent;
	}
}
