package techreborn.compat.trinkets;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;

import java.util.function.Predicate;

public class Trinkets {

	public static Predicate<PlayerEntity> isElytraEquipped() {
		return playerEntity -> {
			TrinketComponent component = TrinketsApi.getTrinketComponent(playerEntity);
			for (int i = 0; i < component.getInventory().size(); i++) {
				if (component.getInventory().getStack(i).getItem() == Items.ELYTRA) {
					return true;
				}
			}
			return false;
		};
	}
}
