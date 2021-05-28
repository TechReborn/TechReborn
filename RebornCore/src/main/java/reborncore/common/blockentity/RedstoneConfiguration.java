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

package reborncore.common.blockentity;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.client.screen.builder.Syncable;
import reborncore.common.util.BooleanFunction;
import reborncore.common.util.NBTSerializable;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RedstoneConfiguration implements NBTSerializable, Syncable {

	//Set in TR to be a better item such as a battery or a cell
	public static ItemStack powerStack = new ItemStack(Items.CARROT_ON_A_STICK);
	public static ItemStack fluidStack = new ItemStack(Items.BUCKET);

	private static List<Element> ELEMENTS = new ArrayList<>();
	private static Map<String, Element> ELEMENT_MAP = new HashMap<>();

	public static Element ITEM_IO = newBuilder()
										.name("item_io")
										.icon(() -> new ItemStack(Blocks.HOPPER))
										.build();

	public static Element POWER_IO = newBuilder()
										.name("power_io")
										.icon(() -> powerStack)
										.build();

	public static Element FLUID_IO = newBuilder()
										.name("fluid_io")
										.canApply(type -> type.getTank() != null)
										.icon(() -> fluidStack)
										.build();

	public static Element RECIPE_PROCESSING = newBuilder()
										.name("recipe_processing")
										.canApply(type -> type instanceof IRecipeCrafterProvider)
										.icon(() -> new ItemStack(Blocks.CRAFTING_TABLE))
										.build();


	private static Element.Builder newBuilder() {
		return Element.Builder.getInstance();
	}

	private final MachineBaseBlockEntity blockEntity;
	private List<Element> activeElements;
	private Map<Element, State> stateMap;

	public RedstoneConfiguration(MachineBaseBlockEntity blockEntity) {
		this.blockEntity = blockEntity;
	}

	public List<Element> getElements() {
		if (activeElements != null) {
			return activeElements;
		}
		return activeElements = ELEMENTS.stream()
				.filter(element -> element.isApplicable(blockEntity))
				.collect(Collectors.toList());
	}

	public void refreshCache() {
		activeElements = null;

		if (stateMap != null) {
			for (Element element : getElements()) {
				if (!stateMap.containsKey(element)) {
					stateMap.put(element, State.IGNORED);
				}
			}
		}
	}

	public State getState(Element element) {
		if (stateMap == null) {
			populateStateMap();
		}
		State state = stateMap.get(element);
		Validate.notNull(state, "Unsupported element " + element.getName() + " for machine: " + blockEntity.getClass().getName());
		return state;
	}

	public void setState(Element element, State state) {
		if (stateMap == null) {
			populateStateMap();
		}
		Validate.isTrue(stateMap.containsKey(element));
		stateMap.replace(element, state);
	}

	public boolean isActive(Element element) {
		State state = getState(element);
		if (state == State.IGNORED) {
			return true;
		}
		boolean hasRedstonePower = blockEntity.getWorld().isReceivingRedstonePower(blockEntity.getPos());
		boolean enabledState = state == State.ENABLED_ON;
		return enabledState == hasRedstonePower;
	}

	private void populateStateMap() {
		Validate.isTrue(stateMap == null);
		stateMap = new HashMap<>();
		for (Element element : getElements()) {
			stateMap.put(element, State.IGNORED);
		}
	}

	@NotNull
	@Override
	public NbtCompound write() {
		NbtCompound tag = new NbtCompound();
		for (Element element : getElements()) {
			tag.putInt(element.getName(), getState(element).ordinal());
		}
		return tag;
	}

	@Override
	public void read(@NotNull NbtCompound tag) {
		stateMap = new HashMap<>();
		for (String key : tag.getKeys()) {
			Element element = ELEMENT_MAP.get(key);
			if (element == null) {
				System.out.println("Unknown element type: " + key);
				continue;
			}
			State state = State.values()[tag.getInt(key)];
			stateMap.put(element, state);
		}

		//Ensure all active states are in the map, will happen if a new state is added when the world is upgraded
		for (Element element : getElements()) {
			if (!stateMap.containsKey(element)) {
				stateMap.put(element, State.IGNORED);
			}
		}
	}

	@Override
	public void getSyncPair(List<Pair<Supplier, Consumer>> pairList) {
		pairList.add(Pair.of(this::write, (Consumer<NbtCompound>) this::read));
	}

	public static Element getElementByName(String name) {
		return ELEMENT_MAP.get(name);
	}

	//Could be power input/output, item/fluid io, machine processing
	public static class Element {
		private final String name;
		private final BooleanFunction<MachineBaseBlockEntity> isApplicable;
		private final Supplier<ItemStack> icon;

		public Element(String name, BooleanFunction<MachineBaseBlockEntity> isApplicable, Supplier<ItemStack> icon) {
			this.name = name;
			this.isApplicable = isApplicable;
			this.icon = icon;
		}

		public boolean isApplicable(MachineBaseBlockEntity blockEntity) {
			return isApplicable.get(blockEntity);
		}

		public String getName() {
			return name;
		}

		public ItemStack getIcon() {
			return icon.get();
		}

		public static class Builder {

			private String name;
			private BooleanFunction<MachineBaseBlockEntity> isApplicable = (be) -> true;
			private Supplier<ItemStack> icon = () -> ItemStack.EMPTY;

			public Builder name(String name) {
				this.name = name;
				return this;
			}

			public Builder canApply(BooleanFunction<MachineBaseBlockEntity> isApplicable) {
				this.isApplicable = isApplicable;
				return this;
			}

			public Builder icon(Supplier<ItemStack> stack) {
				this.icon = stack;
				return this;
			}

			public Element build() {
				Validate.isTrue(!StringUtils.isEmpty(name));
				Element element = new Element(name, isApplicable, icon);
				ELEMENTS.add(element);
				ELEMENT_MAP.put(element.getName(), element);
				return element;
			}

			public static Builder getInstance() {
				return new Builder();
			}
		}
	}

	public enum State {
		IGNORED,
		ENABLED_ON,
		ENABLED_OFF
	}
}
