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

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import reborncore.api.recipe.IRecipeCrafterProvider;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public record RedstoneConfiguration(Map<Element, State> stateMap) {
	// Set in TR to be a better item such as a battery or a cell
	public static ItemStack powerStack = new ItemStack(Items.CARROT_ON_A_STICK);
	public static ItemStack fluidStack = new ItemStack(Items.BUCKET);

	public static final MapCodec<RedstoneConfiguration> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.unboundedMap(Element.CODEC, State.CODEC).fieldOf("elements").forGetter(RedstoneConfiguration::stateMap)
	).apply(instance, RedstoneConfiguration::new));
	private static final StreamCodec<ByteBuf, Map<Element, State>> STATE_MAP_CODEC = ByteBufCodecs.map(HashMap::new, Element.PACKET_CODEC, State.PACKET_CODEC);
	public static final StreamCodec<ByteBuf, RedstoneConfiguration> PACKET_CODEC = STATE_MAP_CODEC.map(RedstoneConfiguration::new, RedstoneConfiguration::stateMap);

	public RedstoneConfiguration() {
		this(Collections.emptyMap());
	}

	public static List<Element> getValidElements(MachineBaseBlockEntity blockEntity) {
		return Element.ELEMENTS.stream()
			.filter(element -> element.isApplicable(blockEntity))
			.collect(Collectors.toList());
	}

	public State getState(Element element) {
		State state = stateMap.get(element);

		if (state == null) {
			return State.IGNORED;
		}

		return state;
	}

	public RedstoneConfiguration withState(Element element, State state) {
		Map<Element, State> elements = new HashMap<>(this.stateMap);
		elements.put(element, state);
		return new RedstoneConfiguration(Collections.unmodifiableMap(elements));
	}

	public boolean isActive(Element element, MachineBaseBlockEntity blockEntity) {
		State state = getState(element);
		if (state == State.IGNORED) {
			return true;
		}
		boolean hasRedstonePower = blockEntity.getLevel().hasNeighborSignal(blockEntity.getBlockPos());
		boolean enabledState = state == State.ENABLED_ON;
		return enabledState == hasRedstonePower;
	}

	// Could be power input/output, item/fluid io, machine processing
	public record Element(String name, Predicate<MachineBaseBlockEntity> isApplicable, Supplier<ItemStack> icon) implements StringRepresentable {
		public static Element ITEM_IO = new Element("item_io", () -> new ItemStack(Blocks.HOPPER));
		public static Element POWER_IO = new Element("power_io", () -> powerStack);
		public static Element FLUID_IO = new Element("fluid_io", type -> type.getTank() != null, () -> fluidStack);
		public static Element RECIPE_PROCESSING = new Element("recipe_processing", type -> type instanceof IRecipeCrafterProvider, () -> new ItemStack(Blocks.CRAFTING_TABLE));
		private static final List<Element> ELEMENTS = List.of(
			ITEM_IO, POWER_IO, FLUID_IO, RECIPE_PROCESSING
		);
		private static final Map<String, Element> ELEMENT_MAP = ELEMENTS.stream()
			.collect(Collectors.toMap(Element::name, Function.identity()));

		public static final Codec<Element> CODEC = StringRepresentable.fromValues(() -> ELEMENTS.toArray(Element[]::new));
		public static final StreamCodec<ByteBuf, Element> PACKET_CODEC = ByteBufCodecs.STRING_UTF8
			.map(ELEMENT_MAP::get, Element::name);

		public Element(String name, Supplier<ItemStack> icon) {
			this(name, (be) -> true, icon);
		}

		public boolean isApplicable(MachineBaseBlockEntity blockEntity) {
			return isApplicable.test(blockEntity);
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}

	public enum State implements StringRepresentable {
		IGNORED,
		ENABLED_ON,
		ENABLED_OFF;
		public static final Codec<State> CODEC = StringRepresentable.fromEnum(State::values);
		public static final StreamCodec<ByteBuf, State> PACKET_CODEC = ByteBufCodecs.INT
			.map(integer -> State.values()[integer], Enum::ordinal);

		@Override
		public String getSerializedName() {
			return name();
		}
	}
}
