package techreborn.client.container.builder;

import org.apache.commons.lang3.Range;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ContainerBuilder {
	private Predicate<EntityPlayer> canInteract = player -> true;

	final List<Slot> slots;
	final List<Range<Integer>> playerInventoryRanges, tileInventoryRanges;

	public ContainerBuilder() {
		this.slots = new ArrayList<>();
		this.playerInventoryRanges = new ArrayList<>();
		this.tileInventoryRanges = new ArrayList<>();
	}

	public ContainerBuilder interact(final Predicate<EntityPlayer> canInteract) {
		this.canInteract = canInteract;
		return this;
	}

	public ContainerPlayerInventoryBuilder player(final InventoryPlayer player) {
		return new ContainerPlayerInventoryBuilder(this, player);
	}

	public ContainerTileInventoryBuilder tile(final IInventory tile) {
		return new ContainerTileInventoryBuilder(this, tile);
	}

	void addPlayerInventoryRange(final Range<Integer> range) {
		this.playerInventoryRanges.add(range);
	}

	void addTileInventoryRange(final Range<Integer> range) {
		this.tileInventoryRanges.add(range);
	}

	public BuiltContainer create() {
		final BuiltContainer built = new BuiltContainer(this.canInteract, this.playerInventoryRanges,
				this.tileInventoryRanges);

		this.slots.forEach(built::addSlot);

		this.slots.clear();
		return built;
	}
}
