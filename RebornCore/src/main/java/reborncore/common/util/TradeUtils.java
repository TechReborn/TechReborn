package reborncore.common.util;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class TradeUtils {

	private TradeUtils() {/* No instantiation.*/}

	public static TradeOffer create(ItemConvertible item, int price, int count, int maxUses, int experience) {
		return new TradeOffer(new ItemStack(Items.EMERALD, price), new ItemStack(item, count), maxUses, experience, 0.05F);
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

}
