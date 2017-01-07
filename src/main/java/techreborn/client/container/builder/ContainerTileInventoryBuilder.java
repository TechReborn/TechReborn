package techreborn.client.container.builder;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;

import reborncore.api.power.IEnergyInterfaceItem;
import reborncore.client.gui.slots.BaseSlot;
import reborncore.client.gui.slots.SlotFake;
import reborncore.client.gui.slots.SlotOutput;

import techreborn.client.container.builder.slot.FilteredSlot;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

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

	public ContainerTileInventoryBuilder outputSlot(final int index, final int x, final int y) {
		this.parent.slots.add(new SlotOutput(this.tile, index, x, y));
		return this;
	}

	public ContainerTileInventoryBuilder fakeSlot(final int index, final int x, final int y) {
		this.parent.slots.add(new SlotFake(this.tile, index, x, y, false, false, Integer.MAX_VALUE));
		return this;
	}

	@SuppressWarnings("null")
	public ContainerTileInventoryBuilder energySlot(final int index, final int x, final int y) {
		this.parent.slots.add(new FilteredSlot(this.tile, index, x, y)
				.setFilter(stack -> stack.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.UP)
						|| stack.getItem() instanceof IEnergyInterfaceItem));
		return this;
	}

	/**
	 *
	 * @param supplier
	 *            The supplier must supply a variable holding inside a Short, it
	 *            will be truncated by force.
	 * @param setter
	 *            The setter to call when the variable has been updated.
	 */
	public ContainerTileInventoryBuilder syncShortValue(final IntSupplier supplier, final IntConsumer setter) {
		this.parent.shortValues.add(Pair.of(supplier, setter));
		return this;
	}

	/**
	 *
	 * @param supplier
	 *            The supplier it can supply a variable holding in an Integer it
	 *            will be split inside multiples shorts.
	 * @param setter
	 *            The setter to call when the variable has been updated.
	 */
	public ContainerTileInventoryBuilder syncIntegerValue(final IntSupplier supplier, final IntConsumer setter) {
		this.parent.integerValues.add(Pair.of(supplier, setter));
		return this;
	}

	public ContainerBuilder addInventory() {
		this.parent.tileInventoryRanges.add(Range.between(this.rangeStart, this.parent.slots.size()));
		return this.parent;
	}
}
