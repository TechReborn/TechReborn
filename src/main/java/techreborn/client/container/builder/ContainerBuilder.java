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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;
import reborncore.common.tile.TileMachineBase;

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

	public ContainerTileInventoryBuilder tile(final TileMachineBase tile) {
		return new ContainerTileInventoryBuilder(this, tile);
	}

	void addPlayerInventoryRange(final Range<Integer> range) {
		this.playerInventoryRanges.add(range);
	}

	void addTileInventoryRange(final Range<Integer> range) {
		this.tileInventoryRanges.add(range);
	}

	public BuiltContainer create(final TileMachineBase tile) {
		final BuiltContainer built = new BuiltContainer(this.name, this.canInteract,
				this.playerInventoryRanges,
				this.tileInventoryRanges, tile);
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
