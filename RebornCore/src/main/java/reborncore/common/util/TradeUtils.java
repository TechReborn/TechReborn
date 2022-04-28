package reborncore.common.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

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

	public static TradeOffer createSell(ItemConvertible item, int price, int count, int maxUses, int experience) {
		return new TradeOffer(new ItemStack(Items.EMERALD, price), new ItemStack(item, count), maxUses, experience, 0.05F);
	}

	public static TradeOffer createBuy(ItemConvertible item, int price, int count, int maxUses, int experience) {
		return new TradeOffer(new ItemStack(item, count), new ItemStack(Items.EMERALD, price), maxUses, experience, 0.05F);
	}

	@Contract("null -> null; !null -> new")
	public static TradeOffer copy(TradeOffer tradeOffer) {
		if (tradeOffer == null)
			return null;
		return new TradeOffer(tradeOffer.toNbt());
	}

	public static TradeOffers.Factory asFactory(TradeOffer tradeOffer) {
		return new TradeOffers.Factory() {
			private final TradeOffer offer = copy(tradeOffer);

			@Nullable
			@Override
			public TradeOffer create(Entity entity, Random random) {
				return copy(offer);
			}
		};
	}

	public static void registerTradesForLevel(VillagerProfession profession, Level level, boolean replace, TradeOffer... tradeOffers) {
		ExceptionUtils.requireNonNull(profession, "profession");
		ExceptionUtils.requireNonNull(level, "level");
		ExceptionUtils.requireNonNull(tradeOffers, "tradeOffers");
		ExceptionUtils.requireNonNullEntries(tradeOffers, "tradeOffers");

		Int2ObjectMap<TradeOffers.Factory[]> allTrades = TradeOffers.PROFESSION_TO_LEVELED_TRADE.getOrDefault(profession, new Int2ObjectArrayMap<>(Level.SIZE));
		TradeOffers.Factory[] oldLevelTrades = allTrades.getOrDefault(level.asInt(), new TradeOffers.Factory[0]);
		TradeOffers.Factory[] newLevelTrades = new TradeOffers.Factory[tradeOffers.length];
		newLevelTrades = Arrays.stream(tradeOffers).map(TradeUtils::asFactory).collect(Collectors.toList()).toArray(newLevelTrades);
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
