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

public enum ColoredItem {

	BLACK(colorItem(Items.DYE, DyeColor.BLACK), colorItem(Items.WOOL, DyeColor.BLACK), colorItem(Items.CARPET, DyeColor.BLACK), colorItem(Items.BED, DyeColor.BLACK), colorItem(Items.STAINED_GLASS, DyeColor.BLACK), colorItem(Items.STAINED_GLASS_PANE, DyeColor.BLACK), colorItem(Items.DYED_TERRACOTTA, DyeColor.BLACK), colorItem(Items.GLAZED_TERRACOTTA, DyeColor.BLACK), colorItem(Items.CONCRETE_POWDER, DyeColor.BLACK), colorItem(Items.CONCRETE, DyeColor.BLACK), colorItem(Items.DYED_SHULKER_BOX, DyeColor.BLACK), colorItem(Items.BANNER, DyeColor.BLACK), colorItem(Items.DYED_CANDLE, DyeColor.BLACK)),
	BLUE(colorItem(Items.DYE, DyeColor.BLUE), colorItem(Items.WOOL, DyeColor.BLUE), colorItem(Items.CARPET, DyeColor.BLUE), colorItem(Items.BED, DyeColor.BLUE), colorItem(Items.STAINED_GLASS, DyeColor.BLUE), colorItem(Items.STAINED_GLASS_PANE, DyeColor.BLUE), colorItem(Items.DYED_TERRACOTTA, DyeColor.BLUE), colorItem(Items.GLAZED_TERRACOTTA, DyeColor.BLUE), colorItem(Items.CONCRETE_POWDER, DyeColor.BLUE), colorItem(Items.CONCRETE, DyeColor.BLUE), colorItem(Items.DYED_SHULKER_BOX, DyeColor.BLUE), colorItem(Items.BANNER, DyeColor.BLUE), colorItem(Items.DYED_CANDLE, DyeColor.BLUE)),
	BROWN(colorItem(Items.DYE, DyeColor.BROWN), colorItem(Items.WOOL, DyeColor.BROWN), colorItem(Items.CARPET, DyeColor.BROWN), colorItem(Items.BED, DyeColor.BROWN), colorItem(Items.STAINED_GLASS, DyeColor.BROWN), colorItem(Items.STAINED_GLASS_PANE, DyeColor.BROWN), colorItem(Items.DYED_TERRACOTTA, DyeColor.BROWN), colorItem(Items.GLAZED_TERRACOTTA, DyeColor.BROWN), colorItem(Items.CONCRETE_POWDER, DyeColor.BROWN), colorItem(Items.CONCRETE, DyeColor.BROWN), colorItem(Items.DYED_SHULKER_BOX, DyeColor.BROWN), colorItem(Items.BANNER, DyeColor.BROWN), colorItem(Items.DYED_CANDLE, DyeColor.BROWN)),
	CYAN(colorItem(Items.DYE, DyeColor.CYAN), colorItem(Items.WOOL, DyeColor.CYAN), colorItem(Items.CARPET, DyeColor.CYAN), colorItem(Items.BED, DyeColor.CYAN), colorItem(Items.STAINED_GLASS, DyeColor.CYAN), colorItem(Items.STAINED_GLASS_PANE, DyeColor.CYAN), colorItem(Items.DYED_TERRACOTTA, DyeColor.CYAN), colorItem(Items.GLAZED_TERRACOTTA, DyeColor.CYAN), colorItem(Items.CONCRETE_POWDER, DyeColor.CYAN), colorItem(Items.CONCRETE, DyeColor.CYAN), colorItem(Items.DYED_SHULKER_BOX, DyeColor.CYAN), colorItem(Items.BANNER, DyeColor.CYAN), colorItem(Items.DYED_CANDLE, DyeColor.CYAN)),
	GRAY(colorItem(Items.DYE, DyeColor.GRAY), colorItem(Items.WOOL, DyeColor.GRAY), colorItem(Items.CARPET, DyeColor.GRAY), colorItem(Items.BED, DyeColor.GRAY), colorItem(Items.STAINED_GLASS, DyeColor.GRAY), colorItem(Items.STAINED_GLASS_PANE, DyeColor.GRAY), colorItem(Items.DYED_TERRACOTTA, DyeColor.GRAY), colorItem(Items.GLAZED_TERRACOTTA, DyeColor.GRAY), colorItem(Items.CONCRETE_POWDER, DyeColor.GRAY), colorItem(Items.CONCRETE, DyeColor.GRAY), colorItem(Items.DYED_SHULKER_BOX, DyeColor.GRAY), colorItem(Items.BANNER, DyeColor.GRAY), colorItem(Items.DYED_CANDLE, DyeColor.GRAY)),
	GREEN(colorItem(Items.DYE, DyeColor.GREEN), colorItem(Items.WOOL, DyeColor.GREEN), colorItem(Items.CARPET, DyeColor.GREEN), colorItem(Items.BED, DyeColor.GREEN), colorItem(Items.STAINED_GLASS, DyeColor.GREEN), colorItem(Items.STAINED_GLASS_PANE, DyeColor.GREEN), colorItem(Items.DYED_TERRACOTTA, DyeColor.GREEN), colorItem(Items.GLAZED_TERRACOTTA, DyeColor.GREEN), colorItem(Items.CONCRETE_POWDER, DyeColor.GREEN), colorItem(Items.CONCRETE, DyeColor.GREEN), colorItem(Items.DYED_SHULKER_BOX, DyeColor.GREEN), colorItem(Items.BANNER, DyeColor.GREEN), colorItem(Items.DYED_CANDLE, DyeColor.GREEN)),
	LIGHT_BLUE(colorItem(Items.DYE, DyeColor.LIGHT_BLUE), colorItem(Items.WOOL, DyeColor.LIGHT_BLUE), colorItem(Items.CARPET, DyeColor.LIGHT_BLUE), colorItem(Items.BED, DyeColor.LIGHT_BLUE), colorItem(Items.STAINED_GLASS, DyeColor.LIGHT_BLUE), colorItem(Items.STAINED_GLASS_PANE, DyeColor.LIGHT_BLUE), colorItem(Items.DYED_TERRACOTTA, DyeColor.LIGHT_BLUE), colorItem(Items.GLAZED_TERRACOTTA, DyeColor.LIGHT_BLUE), colorItem(Items.CONCRETE_POWDER, DyeColor.LIGHT_BLUE), colorItem(Items.CONCRETE, DyeColor.LIGHT_BLUE), colorItem(Items.DYED_SHULKER_BOX, DyeColor.LIGHT_BLUE), colorItem(Items.BANNER, DyeColor.LIGHT_BLUE), colorItem(Items.DYED_CANDLE, DyeColor.LIGHT_BLUE)),
	LIGHT_GRAY(colorItem(Items.DYE, DyeColor.LIGHT_GRAY), colorItem(Items.WOOL, DyeColor.LIGHT_GRAY), colorItem(Items.CARPET, DyeColor.LIGHT_GRAY), colorItem(Items.BED, DyeColor.LIGHT_GRAY), colorItem(Items.STAINED_GLASS, DyeColor.LIGHT_GRAY), colorItem(Items.STAINED_GLASS_PANE, DyeColor.LIGHT_GRAY), colorItem(Items.DYED_TERRACOTTA, DyeColor.LIGHT_GRAY), colorItem(Items.GLAZED_TERRACOTTA, DyeColor.LIGHT_GRAY), colorItem(Items.CONCRETE_POWDER, DyeColor.LIGHT_GRAY), colorItem(Items.CONCRETE, DyeColor.LIGHT_GRAY), colorItem(Items.DYED_SHULKER_BOX, DyeColor.LIGHT_GRAY), colorItem(Items.BANNER, DyeColor.LIGHT_GRAY), colorItem(Items.DYED_CANDLE, DyeColor.LIGHT_GRAY)),
	LIME(colorItem(Items.DYE, DyeColor.LIME), colorItem(Items.WOOL, DyeColor.LIME), colorItem(Items.CARPET, DyeColor.LIME), colorItem(Items.BED, DyeColor.LIME), colorItem(Items.STAINED_GLASS, DyeColor.LIME), colorItem(Items.STAINED_GLASS_PANE, DyeColor.LIME), colorItem(Items.DYED_TERRACOTTA, DyeColor.LIME), colorItem(Items.GLAZED_TERRACOTTA, DyeColor.LIME), colorItem(Items.CONCRETE_POWDER, DyeColor.LIME), colorItem(Items.CONCRETE, DyeColor.LIME), colorItem(Items.DYED_SHULKER_BOX, DyeColor.LIME), colorItem(Items.BANNER, DyeColor.LIME), colorItem(Items.DYED_CANDLE, DyeColor.LIME)),
	MAGENTA(colorItem(Items.DYE, DyeColor.MAGENTA), colorItem(Items.WOOL, DyeColor.MAGENTA), colorItem(Items.CARPET, DyeColor.MAGENTA), colorItem(Items.BED, DyeColor.MAGENTA), colorItem(Items.STAINED_GLASS, DyeColor.MAGENTA), colorItem(Items.STAINED_GLASS_PANE, DyeColor.MAGENTA), colorItem(Items.DYED_TERRACOTTA, DyeColor.MAGENTA), colorItem(Items.GLAZED_TERRACOTTA, DyeColor.MAGENTA), colorItem(Items.CONCRETE_POWDER, DyeColor.MAGENTA), colorItem(Items.CONCRETE, DyeColor.MAGENTA), colorItem(Items.DYED_SHULKER_BOX, DyeColor.MAGENTA), colorItem(Items.BANNER, DyeColor.MAGENTA), colorItem(Items.DYED_CANDLE, DyeColor.MAGENTA)),
	NEUTRAL(null, null, null, null, Items.GLASS, Items.GLASS_PANE, Items.TERRACOTTA, null, null, null, Items.SHULKER_BOX, null, Items.CANDLE),
	ORANGE(colorItem(Items.DYE, DyeColor.ORANGE), colorItem(Items.WOOL, DyeColor.ORANGE), colorItem(Items.CARPET, DyeColor.ORANGE), colorItem(Items.BED, DyeColor.ORANGE), colorItem(Items.STAINED_GLASS, DyeColor.ORANGE), colorItem(Items.STAINED_GLASS_PANE, DyeColor.ORANGE), colorItem(Items.DYED_TERRACOTTA, DyeColor.ORANGE), colorItem(Items.GLAZED_TERRACOTTA, DyeColor.ORANGE), colorItem(Items.CONCRETE_POWDER, DyeColor.ORANGE), colorItem(Items.CONCRETE, DyeColor.ORANGE), colorItem(Items.DYED_SHULKER_BOX, DyeColor.ORANGE), colorItem(Items.BANNER, DyeColor.ORANGE), colorItem(Items.DYED_CANDLE, DyeColor.ORANGE)),
	PINK(colorItem(Items.DYE, DyeColor.PINK), colorItem(Items.WOOL, DyeColor.PINK), colorItem(Items.CARPET, DyeColor.PINK), colorItem(Items.BED, DyeColor.PINK), colorItem(Items.STAINED_GLASS, DyeColor.PINK), colorItem(Items.STAINED_GLASS_PANE, DyeColor.PINK), colorItem(Items.DYED_TERRACOTTA, DyeColor.PINK), colorItem(Items.GLAZED_TERRACOTTA, DyeColor.PINK), colorItem(Items.CONCRETE_POWDER, DyeColor.PINK), colorItem(Items.CONCRETE, DyeColor.PINK), colorItem(Items.DYED_SHULKER_BOX, DyeColor.PINK), colorItem(Items.BANNER, DyeColor.PINK), colorItem(Items.DYED_CANDLE, DyeColor.PINK)),
	PURPLE(colorItem(Items.DYE, DyeColor.PURPLE), colorItem(Items.WOOL, DyeColor.PURPLE), colorItem(Items.CARPET, DyeColor.PURPLE), colorItem(Items.BED, DyeColor.PURPLE), colorItem(Items.STAINED_GLASS, DyeColor.PURPLE), colorItem(Items.STAINED_GLASS_PANE, DyeColor.PURPLE), colorItem(Items.DYED_TERRACOTTA, DyeColor.PURPLE), colorItem(Items.GLAZED_TERRACOTTA, DyeColor.PURPLE), colorItem(Items.CONCRETE_POWDER, DyeColor.PURPLE), colorItem(Items.CONCRETE, DyeColor.PURPLE), colorItem(Items.DYED_SHULKER_BOX, DyeColor.PURPLE), colorItem(Items.BANNER, DyeColor.PURPLE), colorItem(Items.DYED_CANDLE, DyeColor.PURPLE)),
	RED(colorItem(Items.DYE, DyeColor.RED), colorItem(Items.WOOL, DyeColor.RED), colorItem(Items.CARPET, DyeColor.RED), colorItem(Items.BED, DyeColor.RED), colorItem(Items.STAINED_GLASS, DyeColor.RED), colorItem(Items.STAINED_GLASS_PANE, DyeColor.RED), colorItem(Items.DYED_TERRACOTTA, DyeColor.RED), colorItem(Items.GLAZED_TERRACOTTA, DyeColor.RED), colorItem(Items.CONCRETE_POWDER, DyeColor.RED), colorItem(Items.CONCRETE, DyeColor.RED), colorItem(Items.DYED_SHULKER_BOX, DyeColor.RED), colorItem(Items.BANNER, DyeColor.RED), colorItem(Items.DYED_CANDLE, DyeColor.RED)),
	WHITE(colorItem(Items.DYE, DyeColor.WHITE), colorItem(Items.WOOL, DyeColor.WHITE), colorItem(Items.CARPET, DyeColor.WHITE), colorItem(Items.BED, DyeColor.WHITE), colorItem(Items.STAINED_GLASS, DyeColor.WHITE), colorItem(Items.STAINED_GLASS_PANE, DyeColor.WHITE), colorItem(Items.DYED_TERRACOTTA, DyeColor.WHITE), colorItem(Items.GLAZED_TERRACOTTA, DyeColor.WHITE), colorItem(Items.CONCRETE_POWDER, DyeColor.WHITE), colorItem(Items.CONCRETE, DyeColor.WHITE), colorItem(Items.DYED_SHULKER_BOX, DyeColor.WHITE), colorItem(Items.BANNER, DyeColor.WHITE), colorItem(Items.DYED_CANDLE, DyeColor.WHITE)),
	YELLOW(colorItem(Items.DYE, DyeColor.YELLOW), colorItem(Items.WOOL, DyeColor.YELLOW), colorItem(Items.CARPET, DyeColor.YELLOW), colorItem(Items.BED, DyeColor.YELLOW), colorItem(Items.STAINED_GLASS, DyeColor.YELLOW), colorItem(Items.STAINED_GLASS_PANE, DyeColor.YELLOW), colorItem(Items.DYED_TERRACOTTA, DyeColor.YELLOW), colorItem(Items.GLAZED_TERRACOTTA, DyeColor.YELLOW), colorItem(Items.CONCRETE_POWDER, DyeColor.YELLOW), colorItem(Items.CONCRETE, DyeColor.YELLOW), colorItem(Items.DYED_SHULKER_BOX, DyeColor.YELLOW), colorItem(Items.BANNER, DyeColor.YELLOW), colorItem(Items.DYED_CANDLE, DyeColor.YELLOW));

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

	private static Item colorItem(ColorCollection<Item> items, DyeColor color) {
		return items.pick(color);
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
