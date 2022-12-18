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
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ColoredItem {

	BLACK(Items.BLACK_DYE, Items.BLACK_WOOL, Items.BLACK_CARPET, Items.BLACK_BED, Items.BLACK_STAINED_GLASS, Items.BLACK_STAINED_GLASS_PANE, Items.BLACK_TERRACOTTA, Items.BLACK_GLAZED_TERRACOTTA, Items.BLACK_CONCRETE_POWDER, Items.BLACK_CONCRETE, Items.BLACK_SHULKER_BOX, Items.BLACK_BANNER, Items.BLACK_CANDLE),
	BLUE(Items.BLUE_DYE, Items.BLUE_WOOL, Items.BLUE_CARPET, Items.BLUE_BED, Items.BLUE_STAINED_GLASS, Items.BLUE_STAINED_GLASS_PANE, Items.BLUE_TERRACOTTA, Items.BLUE_GLAZED_TERRACOTTA, Items.BLUE_CONCRETE_POWDER, Items.BLUE_CONCRETE, Items.BLUE_SHULKER_BOX, Items.BLUE_BANNER, Items.BLUE_CANDLE),
	BROWN(Items.BROWN_DYE, Items.BROWN_WOOL, Items.BROWN_CARPET, Items.BROWN_BED, Items.BROWN_STAINED_GLASS, Items.BROWN_STAINED_GLASS_PANE, Items.BROWN_TERRACOTTA, Items.BROWN_GLAZED_TERRACOTTA, Items.BROWN_CONCRETE_POWDER, Items.BROWN_CONCRETE, Items.BROWN_SHULKER_BOX, Items.BROWN_BANNER, Items.BROWN_CANDLE),
	CYAN(Items.CYAN_DYE, Items.CYAN_WOOL, Items.CYAN_CARPET, Items.CYAN_BED, Items.CYAN_STAINED_GLASS, Items.CYAN_STAINED_GLASS_PANE, Items.CYAN_TERRACOTTA, Items.CYAN_GLAZED_TERRACOTTA, Items.CYAN_CONCRETE_POWDER, Items.CYAN_CONCRETE, Items.CYAN_SHULKER_BOX, Items.CYAN_BANNER, Items.CYAN_CANDLE),
	GRAY(Items.GRAY_DYE, Items.GRAY_WOOL, Items.GRAY_CARPET, Items.GRAY_BED, Items.GRAY_STAINED_GLASS, Items.GRAY_STAINED_GLASS_PANE, Items.GRAY_TERRACOTTA, Items.GRAY_GLAZED_TERRACOTTA, Items.GRAY_CONCRETE_POWDER, Items.GRAY_CONCRETE, Items.GRAY_SHULKER_BOX, Items.GRAY_BANNER, Items.GRAY_CANDLE),
	GREEN(Items.GREEN_DYE, Items.GREEN_WOOL, Items.GREEN_CARPET, Items.GREEN_BED, Items.GREEN_STAINED_GLASS, Items.GREEN_STAINED_GLASS_PANE, Items.GREEN_TERRACOTTA, Items.GREEN_GLAZED_TERRACOTTA, Items.GREEN_CONCRETE_POWDER, Items.GREEN_CONCRETE, Items.GREEN_SHULKER_BOX, Items.GREEN_BANNER, Items.GREEN_CANDLE),
	LIGHT_BLUE(Items.LIGHT_BLUE_DYE, Items.LIGHT_BLUE_WOOL, Items.LIGHT_BLUE_CARPET, Items.LIGHT_BLUE_BED, Items.LIGHT_BLUE_STAINED_GLASS, Items.LIGHT_BLUE_STAINED_GLASS_PANE, Items.LIGHT_BLUE_TERRACOTTA, Items.LIGHT_BLUE_GLAZED_TERRACOTTA, Items.LIGHT_BLUE_CONCRETE_POWDER, Items.LIGHT_BLUE_CONCRETE, Items.LIGHT_BLUE_SHULKER_BOX, Items.LIGHT_BLUE_BANNER, Items.LIGHT_BLUE_CANDLE),
	LIGHT_GRAY(Items.LIGHT_GRAY_DYE, Items.LIGHT_GRAY_WOOL, Items.LIGHT_GRAY_CARPET, Items.LIGHT_GRAY_BED, Items.LIGHT_GRAY_STAINED_GLASS, Items.LIGHT_GRAY_STAINED_GLASS_PANE, Items.LIGHT_GRAY_TERRACOTTA, Items.LIGHT_GRAY_GLAZED_TERRACOTTA, Items.LIGHT_GRAY_CONCRETE_POWDER, Items.LIGHT_GRAY_CONCRETE, Items.LIGHT_GRAY_SHULKER_BOX, Items.LIGHT_GRAY_BANNER, Items.LIGHT_GRAY_CANDLE),
	LIME(Items.LIME_DYE, Items.LIME_WOOL, Items.LIME_CARPET, Items.LIME_BED, Items.LIME_STAINED_GLASS, Items.LIME_STAINED_GLASS_PANE, Items.LIME_TERRACOTTA, Items.LIME_GLAZED_TERRACOTTA, Items.LIME_CONCRETE_POWDER, Items.LIME_CONCRETE, Items.LIME_SHULKER_BOX, Items.LIME_BANNER, Items.LIME_CANDLE),
	MAGENTA(Items.MAGENTA_DYE, Items.MAGENTA_WOOL, Items.MAGENTA_CARPET, Items.MAGENTA_BED, Items.MAGENTA_STAINED_GLASS, Items.MAGENTA_STAINED_GLASS_PANE, Items.MAGENTA_TERRACOTTA, Items.MAGENTA_GLAZED_TERRACOTTA, Items.MAGENTA_CONCRETE_POWDER, Items.MAGENTA_CONCRETE, Items.MAGENTA_SHULKER_BOX, Items.MAGENTA_BANNER, Items.MAGENTA_CANDLE),
	NEUTRAL(null, null, null, null, Items.GLASS, Items.GLASS_PANE, Items.TERRACOTTA, null, null, null, Items.SHULKER_BOX, null, Items.CANDLE),
	ORANGE(Items.ORANGE_DYE, Items.ORANGE_WOOL, Items.ORANGE_CARPET, Items.ORANGE_BED, Items.ORANGE_STAINED_GLASS, Items.ORANGE_STAINED_GLASS_PANE, Items.ORANGE_TERRACOTTA, Items.ORANGE_GLAZED_TERRACOTTA, Items.ORANGE_CONCRETE_POWDER, Items.ORANGE_CONCRETE, Items.ORANGE_SHULKER_BOX, Items.ORANGE_BANNER, Items.ORANGE_CANDLE),
	PINK(Items.PINK_DYE, Items.PINK_WOOL, Items.PINK_CARPET, Items.PINK_BED, Items.PINK_STAINED_GLASS, Items.PINK_STAINED_GLASS_PANE, Items.PINK_TERRACOTTA, Items.PINK_GLAZED_TERRACOTTA, Items.PINK_CONCRETE_POWDER, Items.PINK_CONCRETE, Items.PINK_SHULKER_BOX, Items.PINK_BANNER, Items.PINK_CANDLE),
	PURPLE(Items.PURPLE_DYE, Items.PURPLE_WOOL, Items.PURPLE_CARPET, Items.PURPLE_BED, Items.PURPLE_STAINED_GLASS, Items.PURPLE_STAINED_GLASS_PANE, Items.PURPLE_TERRACOTTA, Items.PURPLE_GLAZED_TERRACOTTA, Items.PURPLE_CONCRETE_POWDER, Items.PURPLE_CONCRETE, Items.PURPLE_SHULKER_BOX, Items.PURPLE_BANNER, Items.PURPLE_CANDLE),
	RED(Items.RED_DYE, Items.RED_WOOL, Items.RED_CARPET, Items.RED_BED, Items.RED_STAINED_GLASS, Items.RED_STAINED_GLASS_PANE, Items.RED_TERRACOTTA, Items.RED_GLAZED_TERRACOTTA, Items.RED_CONCRETE_POWDER, Items.RED_CONCRETE, Items.RED_SHULKER_BOX, Items.RED_BANNER, Items.RED_CANDLE),
	WHITE(Items.WHITE_DYE, Items.WHITE_WOOL, Items.WHITE_CARPET, Items.WHITE_BED, Items.WHITE_STAINED_GLASS, Items.WHITE_STAINED_GLASS_PANE, Items.WHITE_TERRACOTTA, Items.WHITE_GLAZED_TERRACOTTA, Items.WHITE_CONCRETE_POWDER, Items.WHITE_CONCRETE, Items.WHITE_SHULKER_BOX, Items.WHITE_BANNER, Items.WHITE_CANDLE),
	YELLOW(Items.YELLOW_DYE, Items.YELLOW_WOOL, Items.YELLOW_CARPET, Items.YELLOW_BED, Items.YELLOW_STAINED_GLASS, Items.YELLOW_STAINED_GLASS_PANE, Items.YELLOW_TERRACOTTA, Items.YELLOW_GLAZED_TERRACOTTA, Items.YELLOW_CONCRETE_POWDER, Items.YELLOW_CONCRETE, Items.YELLOW_SHULKER_BOX, Items.YELLOW_BANNER, Items.YELLOW_CANDLE);

	private Item dye;
	private Item wool;
	private Item carpet;
	private Item bed;
	private Item glass;
	private Item glassPane;
	private Item terracotta;
	private Item glazedTerracotta;
	private Item concretePowder;
	private Item concrete;
	private Item shulkerBox;
	private Item banner;
	private Item candle;

	private static Map<Pair<ColoredItem, ColoredItem>, ColoredItem> vanillaMixingMap = null;
	private static Map<Pair<ColoredItem, ColoredItem>, ColoredItem> extendedMixingMap = null;
	private static Map<Pair<ColoredItem, ColoredItem>, ColoredItem> extendedMixingMapNoNeutral = null;

	ColoredItem(Item dye, Item wool, Item carpet, Item bed, Item glass, Item glassPane, Item terracotta, Item glazedTerracotta, Item concretePowder, Item concrete, Item shulkerBox, Item banner, Item candle) {
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
