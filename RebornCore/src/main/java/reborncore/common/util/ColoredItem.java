/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2022 TeamReborn
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

package reborncore.common.util;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.ColorCollection;
import org.jspecify.annotations.Nullable;

public enum ColoredItem {

	BLACK(DyeColor.BLACK, Items.DYE, Items.WOOL, Items.CARPET, Items.BED, Items.STAINED_GLASS, Items.STAINED_GLASS_PANE, Items.DYED_TERRACOTTA, Items.GLAZED_TERRACOTTA, Items.CONCRETE_POWDER, Items.CONCRETE, Items.DYED_SHULKER_BOX, Items.BANNER, Items.DYED_CANDLE),
	BLUE(DyeColor.BLUE, Items.DYE, Items.WOOL, Items.CARPET, Items.BED, Items.STAINED_GLASS, Items.STAINED_GLASS_PANE, Items.DYED_TERRACOTTA, Items.GLAZED_TERRACOTTA, Items.CONCRETE_POWDER, Items.CONCRETE, Items.DYED_SHULKER_BOX, Items.BANNER, Items.DYED_CANDLE),
	BROWN(DyeColor.BROWN, Items.DYE, Items.WOOL, Items.CARPET, Items.BED, Items.STAINED_GLASS, Items.STAINED_GLASS_PANE, Items.DYED_TERRACOTTA, Items.GLAZED_TERRACOTTA, Items.CONCRETE_POWDER, Items.CONCRETE, Items.DYED_SHULKER_BOX, Items.BANNER, Items.DYED_CANDLE),
	CYAN(DyeColor.CYAN, Items.DYE, Items.WOOL, Items.CARPET, Items.BED, Items.STAINED_GLASS, Items.STAINED_GLASS_PANE, Items.DYED_TERRACOTTA, Items.GLAZED_TERRACOTTA, Items.CONCRETE_POWDER, Items.CONCRETE, Items.DYED_SHULKER_BOX, Items.BANNER, Items.DYED_CANDLE),
	GRAY(DyeColor.GRAY, Items.DYE, Items.WOOL, Items.CARPET, Items.BED, Items.STAINED_GLASS, Items.STAINED_GLASS_PANE, Items.DYED_TERRACOTTA, Items.GLAZED_TERRACOTTA, Items.CONCRETE_POWDER, Items.CONCRETE, Items.DYED_SHULKER_BOX, Items.BANNER, Items.DYED_CANDLE),
	GREEN(DyeColor.GREEN, Items.DYE, Items.WOOL, Items.CARPET, Items.BED, Items.STAINED_GLASS, Items.STAINED_GLASS_PANE, Items.DYED_TERRACOTTA, Items.GLAZED_TERRACOTTA, Items.CONCRETE_POWDER, Items.CONCRETE, Items.DYED_SHULKER_BOX, Items.BANNER, Items.DYED_CANDLE),
	LIGHT_BLUE(DyeColor.LIGHT_BLUE, Items.DYE, Items.WOOL, Items.CARPET, Items.BED, Items.STAINED_GLASS, Items.STAINED_GLASS_PANE, Items.DYED_TERRACOTTA, Items.GLAZED_TERRACOTTA, Items.CONCRETE_POWDER, Items.CONCRETE, Items.DYED_SHULKER_BOX, Items.BANNER, Items.DYED_CANDLE),
	LIGHT_GRAY(DyeColor.LIGHT_GRAY, Items.DYE, Items.WOOL, Items.CARPET, Items.BED, Items.STAINED_GLASS, Items.STAINED_GLASS_PANE, Items.DYED_TERRACOTTA, Items.GLAZED_TERRACOTTA, Items.CONCRETE_POWDER, Items.CONCRETE, Items.DYED_SHULKER_BOX, Items.BANNER, Items.DYED_CANDLE),
	LIME(DyeColor.LIME, Items.DYE, Items.WOOL, Items.CARPET, Items.BED, Items.STAINED_GLASS, Items.STAINED_GLASS_PANE, Items.DYED_TERRACOTTA, Items.GLAZED_TERRACOTTA, Items.CONCRETE_POWDER, Items.CONCRETE, Items.DYED_SHULKER_BOX, Items.BANNER, Items.DYED_CANDLE),
	MAGENTA(DyeColor.MAGENTA, Items.DYE, Items.WOOL, Items.CARPET, Items.BED, Items.STAINED_GLASS, Items.STAINED_GLASS_PANE, Items.DYED_TERRACOTTA, Items.GLAZED_TERRACOTTA, Items.CONCRETE_POWDER, Items.CONCRETE, Items.DYED_SHULKER_BOX, Items.BANNER, Items.DYED_CANDLE),
	NEUTRAL(null, null, null, null, Items.GLASS, Items.GLASS_PANE, Items.TERRACOTTA, null, null, null, Items.SHULKER_BOX, null, Items.CANDLE),
	ORANGE(DyeColor.ORANGE, Items.DYE, Items.WOOL, Items.CARPET, Items.BED, Items.STAINED_GLASS, Items.STAINED_GLASS_PANE, Items.DYED_TERRACOTTA, Items.GLAZED_TERRACOTTA, Items.CONCRETE_POWDER, Items.CONCRETE, Items.DYED_SHULKER_BOX, Items.BANNER, Items.DYED_CANDLE),
	PINK(DyeColor.PINK, Items.DYE, Items.WOOL, Items.CARPET, Items.BED, Items.STAINED_GLASS, Items.STAINED_GLASS_PANE, Items.DYED_TERRACOTTA, Items.GLAZED_TERRACOTTA, Items.CONCRETE_POWDER, Items.CONCRETE, Items.DYED_SHULKER_BOX, Items.BANNER, Items.DYED_CANDLE),
	PURPLE(DyeColor.PURPLE, Items.DYE, Items.WOOL, Items.CARPET, Items.BED, Items.STAINED_GLASS, Items.STAINED_GLASS_PANE, Items.DYED_TERRACOTTA, Items.GLAZED_TERRACOTTA, Items.CONCRETE_POWDER, Items.CONCRETE, Items.DYED_SHULKER_BOX, Items.BANNER, Items.DYED_CANDLE),
	RED(DyeColor.RED, Items.DYE, Items.WOOL, Items.CARPET, Items.BED, Items.STAINED_GLASS, Items.STAINED_GLASS_PANE, Items.DYED_TERRACOTTA, Items.GLAZED_TERRACOTTA, Items.CONCRETE_POWDER, Items.CONCRETE, Items.DYED_SHULKER_BOX, Items.BANNER, Items.DYED_CANDLE),
	WHITE(DyeColor.WHITE, Items.DYE, Items.WOOL, Items.CARPET, Items.BED, Items.STAINED_GLASS, Items.STAINED_GLASS_PANE, Items.DYED_TERRACOTTA, Items.GLAZED_TERRACOTTA, Items.CONCRETE_POWDER, Items.CONCRETE, Items.DYED_SHULKER_BOX, Items.BANNER, Items.DYED_CANDLE),
	YELLOW(DyeColor.YELLOW, Items.DYE, Items.WOOL, Items.CARPET, Items.BED, Items.STAINED_GLASS, Items.STAINED_GLASS_PANE, Items.DYED_TERRACOTTA, Items.GLAZED_TERRACOTTA, Items.CONCRETE_POWDER, Items.CONCRETE, Items.DYED_SHULKER_BOX, Items.BANNER, Items.DYED_CANDLE);

	private final @Nullable Item dye;
	private final @Nullable Item wool;
	private final @Nullable Item carpet;
	private final @Nullable Item bed;
	private final @Nullable Item glass;
	private final @Nullable Item glassPane;
	private final @Nullable Item terracotta;
	private final @Nullable Item glazedTerracotta;
	private final @Nullable Item concretePowder;
	private final @Nullable Item concrete;
	private final @Nullable Item shulkerBox;
	private final @Nullable Item banner;
	private final @Nullable Item candle;

	private static @Nullable Map<Pair<ColoredItem, ColoredItem>, ColoredItem> vanillaMixingMap = null;
	private static @Nullable Map<Pair<ColoredItem, ColoredItem>, ColoredItem> extendedMixingMap = null;
	private static @Nullable Map<Pair<ColoredItem, ColoredItem>, ColoredItem> extendedMixingMapNoNeutral = null;

	ColoredItem(
		DyeColor color,
		ColorCollection<Item> dye,
		ColorCollection<Item> wool,
		ColorCollection<Item> carpet,
		ColorCollection<Item> bed,
		ColorCollection<Item> glass,
		ColorCollection<Item> glassPane,
		ColorCollection<Item> terracotta,
		ColorCollection<Item> glazedTerracotta,
		ColorCollection<Item> concretePowder,
		ColorCollection<Item> concrete,
		ColorCollection<Item> shulkerBox,
		ColorCollection<Item> banner,
		ColorCollection<Item> candle
	) {
		this(
			dye.pick(color),
			wool.pick(color),
			carpet.pick(color),
			bed.pick(color),
			glass.pick(color),
			glassPane.pick(color),
			terracotta.pick(color),
			glazedTerracotta.pick(color),
			concretePowder.pick(color),
			concrete.pick(color),
			shulkerBox.pick(color),
			banner.pick(color),
			candle.pick(color)
		);
	}

	ColoredItem(
		@Nullable Item dye,
		@Nullable Item wool,
		@Nullable Item carpet,
		@Nullable Item bed,
		@Nullable Item glass,
		@Nullable Item glassPane,
		@Nullable Item terracotta,
		@Nullable Item glazedTerracotta,
		@Nullable Item concretePowder,
		@Nullable Item concrete,
		@Nullable Item shulkerBox,
		@Nullable Item banner,
		@Nullable Item candle
	) {
		this.dye = dye;
		this.wool = wool;
		this.carpet = carpet;
		this.bed = bed;
		this.glass = glass;
		this.glassPane = glassPane;
		this.terracotta = terracotta;
		this.glazedTerracotta = glazedTerracotta;
		this.concretePowder = concretePowder;
		this.concrete = concrete;
		this.shulkerBox = shulkerBox;
		this.banner = banner;
		this.candle = candle;
	}

	private static void putSym(ImmutableMap.Builder<Pair<ColoredItem, ColoredItem>, ColoredItem> builder, ColoredItem item1, ColoredItem item2, ColoredItem result) {
		Objects.requireNonNull(builder);
		Objects.requireNonNull(item1);
		Objects.requireNonNull(item2);
		Objects.requireNonNull(result);
		builder.put(Pair.of(item1, item2), result);
		builder.put(Pair.of(item2, item1), result);
	}

	/**
	 * Returns an immutable map that encodes the vanilla color mixing recipes with 2 inputs + a neutral mixing ingredient.
	 * <p>
	 *     The key is a pair of the two inputs and the value is the mixing result.
	 *     Note that the map is symmetric and irreflexive in the key, i.e. when ((a,b),c) in the map, then also ((b,a),c), but never a = b.
	 *     The {@link #NEUTRAL} mixing ingredient means that the other pair entry equals the result.
	 *     The map will be filled upon the first call of this method.
	 * </p>
	 * @see #createVanillaMixingColorStream(ColoredItem)
	 * @see #getExtendedMixingMap(boolean)
	 */
	public static Map<Pair<ColoredItem, ColoredItem>, ColoredItem> getVanillaMixingMap() {
		if (vanillaMixingMap != null)
			return vanillaMixingMap;

		var builder = new ImmutableMap.Builder<Pair<ColoredItem, ColoredItem>, ColoredItem>();
		for (ColoredItem item : values()) {
			if (item != NEUTRAL) {
				putSym(builder, NEUTRAL, item, item);
			}
		}
		// ordered by result
		putSym(builder, BLUE, GREEN, CYAN);
		putSym(builder, BLACK, WHITE, GRAY);
		putSym(builder, PURPLE, PINK, MAGENTA);
		putSym(builder, BLUE, WHITE, LIGHT_BLUE);
		putSym(builder, GRAY, WHITE, LIGHT_GRAY);
		putSym(builder, GREEN, WHITE, LIME);
		putSym(builder, RED, WHITE, PINK);
		putSym(builder, RED, BLUE, PURPLE);

		vanillaMixingMap = builder.build();
		return vanillaMixingMap;
	}

	/**
	 * Curries the map returned by {@link #getVanillaMixingMap()} into a stream, leaving the map itself unchanged.
	 * <p>
	 *     If this method is f, and the map is m, then f(a) is a stream s such that s(b) = c = m(a,b), with ((a,b),c)
	 *     being an element in the map m.
	 * </p>
	 * @see #createVanillaMixingColorMap(ColoredItem)
	 * @see #createExtendedMixingColorStream(ColoredItem, boolean, boolean)
	 */
	public static Stream<Pair<ColoredItem, ColoredItem>> createVanillaMixingColorStream(ColoredItem color) {
		Objects.requireNonNull(color);
		return getVanillaMixingMap().entrySet().stream()
			.filter(entry -> entry.getKey().getLeft() == color)
			.map(entry -> Pair.of(entry.getKey().getRight(),entry.getValue()));
	}

	/**
	 * Consumes a stream returned by {@link #createVanillaMixingColorStream(ColoredItem)} into a map object.
	 * @see #createExtendedMixingColorMap(ColoredItem, boolean, boolean)
	 */
	public static Map<ColoredItem, ColoredItem> createVanillaMixingColorMap(ColoredItem color) {
		Objects.requireNonNull(color);
		return createVanillaMixingColorStream(color).collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
	}

	/**
	 * Returns an immutable map that encodes extended color mixing recipes with 2 inputs + possibly a neutral mixing ingredient.
	 * <p>
	 *     The key is a pair of the two inputs and the value is the mixing result.
	 *     Note that the map is symmetric and irreflexive in the key, i.e. when ((a,b),c) in the map, then also ((b,a),c), but never a = b.
	 *     The {@link #NEUTRAL} mixing ingredient means that the other pair entry equals the result.
	 *     The map will be filled upon the first call of this method.
	 * </p>
	 * @param withNeutral If the {@link #NEUTRAL} ingredient is provided. If not, the color {@link #WHITE} will be handled as the neutral ingredient.
	 * @see #createExtendedMixingColorStream(ColoredItem, boolean, boolean)
	 * @see #getVanillaMixingMap()
	 */
	public static Map<Pair<ColoredItem, ColoredItem>, ColoredItem> getExtendedMixingMap(boolean withNeutral) {
		if (withNeutral && extendedMixingMap != null)
			return extendedMixingMap;
		if (!withNeutral && extendedMixingMapNoNeutral != null)
			return extendedMixingMapNoNeutral;

		var builder = new ImmutableMap.Builder<Pair<ColoredItem, ColoredItem>, ColoredItem>();
		builder.putAll(getVanillaMixingMap());
		// ordered by result
		putSym(builder, BLACK, GRAY, BLACK);
		putSym(builder, BLUE, LIGHT_BLUE, BLUE);
		putSym(builder, BLUE, CYAN, BLUE);
		putSym(builder, GRAY, CYAN, BLUE);
		putSym(builder, BLACK, ORANGE, BROWN);
		putSym(builder, BROWN, ORANGE, BROWN);
		putSym(builder, GREEN, RED, BROWN);
		putSym(builder, LIGHT_BLUE, CYAN, CYAN);
		putSym(builder, GRAY, LIGHT_GRAY, GRAY);
		putSym(builder, BLACK, LIGHT_GRAY, GRAY);
		putSym(builder, BLUE, YELLOW, GREEN);
		putSym(builder, LIME, GRAY, GREEN);
		putSym(builder, LIME, GREEN, GREEN);
		putSym(builder, PINK, GRAY, MAGENTA);
		putSym(builder, BLUE, LIGHT_GRAY, LIGHT_BLUE);
		putSym(builder, CYAN, LIGHT_GRAY, LIGHT_BLUE);
		putSym(builder, LIGHT_GRAY, LIGHT_BLUE, LIGHT_BLUE);
		putSym(builder, WHITE, LIGHT_GRAY, LIGHT_GRAY);
		putSym(builder, YELLOW, LIGHT_BLUE, LIME);
		putSym(builder, YELLOW, CYAN, LIME);
		putSym(builder, YELLOW, ORANGE, ORANGE);
		putSym(builder, PINK, ORANGE, ORANGE);
		putSym(builder, MAGENTA, LIGHT_GRAY, PINK);
		putSym(builder, MAGENTA, WHITE, PINK);
		putSym(builder, MAGENTA, BLUE, PURPLE);
		putSym(builder, MAGENTA, PURPLE, PURPLE);
		putSym(builder, MAGENTA, RED, RED);
		putSym(builder, ORANGE, RED, RED);
		putSym(builder, PINK, RED, RED);

		extendedMixingMap = builder.build();

		builder = new ImmutableMap.Builder<>();
		for (var entry : extendedMixingMap.entrySet()) {
			if (entry.getKey().getLeft() != NEUTRAL && entry.getKey().getRight() != NEUTRAL &&
				entry.getKey().getLeft() != WHITE && entry.getKey().getRight() != WHITE)
				builder.put(entry);
		}
		for (ColoredItem item : values()) {
			if (item != WHITE && item != NEUTRAL) {
				putSym(builder, WHITE, item, item);
			}
		}
		extendedMixingMapNoNeutral = builder.build();

		if (withNeutral)
			return extendedMixingMap;
		return extendedMixingMapNoNeutral;
	}

	/**
	 * Curries the map returned by {@link #getExtendedMixingMap(boolean)} into a stream, leaving the map itself unchanged.
	 * <p>
	 *     If this method is f, and the map is m, then f(a) is a stream s such that s(b) = c = m(a,b), with ((a,b),c)
	 *     being an element in the map m.
	 * </p>
	 * @param withNeutral If the {@link #NEUTRAL} ingredient is provided. If not, the color {@link #WHITE} will be handled as the neutral ingredient.
	 * @param differentResults If {@code true}, values like s(b) = b will be sorted out.
	 * @see #createExtendedMixingColorMap(ColoredItem, boolean, boolean)
	 * @see #createVanillaMixingColorStream(ColoredItem)
	 */
	public static Stream<Pair<ColoredItem, ColoredItem>> createExtendedMixingColorStream(ColoredItem color, boolean withNeutral, boolean differentResults) {
		Objects.requireNonNull(color);
		return getExtendedMixingMap(withNeutral).entrySet().stream()
			.filter(entry -> entry.getKey().getLeft() == color && (!differentResults || entry.getKey().getRight() != entry.getValue()))
			.map(entry -> Pair.of(entry.getKey().getRight(),entry.getValue()));
	}

	/**
	 * Consumes a stream returned by {@link #createExtendedMixingColorStream(ColoredItem, boolean, boolean)} into a map object.
	 * @see #createVanillaMixingColorMap(ColoredItem)
	 */
	public static Map<ColoredItem, ColoredItem> createExtendedMixingColorMap(ColoredItem color, boolean withNeutral, boolean differentResults) {
		Objects.requireNonNull(color);
		return createExtendedMixingColorStream(color, withNeutral, differentResults)
			.collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
	}

	public Item getDye() {
		return dye;
	}

	public Item getWool() {
		return wool;
	}

	public Item getCarpet() {
		return carpet;
	}

	public Item getBed() {
		return bed;
	}

	public Item getGlass() {
		return glass;
	}

	public Item getGlassPane() {
		return glassPane;
	}

	public Item getTerracotta() {
		return terracotta;
	}

	public Item getGlazedTerracotta() {
		return glazedTerracotta;
	}

	public Item getConcretePowder() {
		return concretePowder;
	}

	public Item getConcrete() {
		return concrete;
	}

	public Item getShulkerBox() {
		return shulkerBox;
	}

	public Item getBanner() {
		return banner;
	}

	public Item getCandle() {
		return candle;
	}
}
