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

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.item.ItemConvertible;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

public final class TradeUtils {

	public enum Level {
		NOVICE, APPRENTICE, JOURNEYMAN, EXPERT, MASTER;

		public static final int SIZE = MASTER.ordinal() + 1;

		/**
		 * The number value of the job level as used internally.
		 * @return the level of the job as an int
		 */
		public int asInt() {
			return ordinal() + 1; // internally job levels start with 1, but ordinal() starts with 0
		}
	}

	private TradeUtils() {/* No instantiation. */}

	public static TradeOffers.Factory createSell(ItemConvertible item, int price, int count, int maxUses, int experience) {
		return new TradeOffers.SellItemFactory(item.asItem(), price, count, maxUses, experience);
	}

	public static TradeOffers.Factory createBuy(ItemConvertible item, int price, int count, int maxUses, int experience) {
		return new TradeOffers.BuyItemFactory(item, count, maxUses, experience, price);
	}

	public static void registerTradesForLevel(VillagerProfession profession, Level level, boolean replace, TradeOffers.Factory... newLevelTrades) {
		ExceptionUtils.requireNonNull(profession, "profession");
		ExceptionUtils.requireNonNull(level, "level");
		ExceptionUtils.requireNonNull(newLevelTrades, "newLevelTrades");
		ExceptionUtils.requireNonNullEntries(newLevelTrades, "newLevelTrades");

		Int2ObjectMap<TradeOffers.Factory[]> allTrades = TradeOffers.PROFESSION_TO_LEVELED_TRADE.getOrDefault(profession, new Int2ObjectArrayMap<>(Level.SIZE));
		TradeOffers.Factory[] oldLevelTrades = allTrades.getOrDefault(level.asInt(), new TradeOffers.Factory[0]);
		TradeOffers.Factory[] allLevelTrades;

		if (replace)
			allLevelTrades = newLevelTrades;
		else {
			allLevelTrades = new TradeOffers.Factory[oldLevelTrades.length+newLevelTrades.length];
			System.arraycopy(oldLevelTrades,0,allLevelTrades,0,oldLevelTrades.length);
			System.arraycopy(newLevelTrades,0,allLevelTrades,oldLevelTrades.length,newLevelTrades.length);
		}

		allTrades.put(level.asInt(), allLevelTrades);
		TradeOffers.PROFESSION_TO_LEVELED_TRADE.put(profession, allTrades);
	}

}
