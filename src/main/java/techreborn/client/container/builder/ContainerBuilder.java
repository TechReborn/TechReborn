package techreborn.client.container.builder;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

public class ContainerBuilder {

	private final String name;

	private Predicate<EntityPlayer> canInteract = player -> true;

	final List<Slot> slots;
	final List<Range<Integer>> playerInventoryRanges, tileInventoryRanges;

	final List<Pair<IntSupplier, IntConsumer>> shortValues;
	final List<Pair<IntSupplier, IntConsumer>> integerValues;

	final List<Consumer<InventoryCrafting>> craftEvents;

	public ContainerBuilder(final String name) {

		this.name = name;

		this.slots = new ArrayList<>();
		this.playerInventoryRanges = new ArrayList<>();
		this.tileInventoryRanges = new ArrayList<>();

		this.shortValues = new ArrayList<>();
		this.integerValues = new ArrayList<>();

		this.craftEvents = new ArrayList<>();
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
		final BuiltContainer built = new BuiltContainer(this.name, this.canInteract,
				this.playerInventoryRanges,
				this.tileInventoryRanges);
		if (!this.shortValues.isEmpty())
			built.addShortSync(this.shortValues);
		if (!this.integerValues.isEmpty())
			built.addIntegerSync(this.integerValues);
		if (!this.craftEvents.isEmpty())
			built.addCraftEvents(this.craftEvents);

		this.slots.forEach(built::addSlot);

		this.slots.clear();
		return built;
	}
}
