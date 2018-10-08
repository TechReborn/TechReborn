package techreborn.api.armour;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.Validate;
import techreborn.items.armor.modular.ModularArmourManager;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ModularArmourUtils {

	public static boolean isUprgade(ItemStack stack) {
		return stack.hasCapability(CapabilityArmourUpgrade.ARMOUR_UPRGRADE_CAPABILITY, null);
	}

	public static IArmourUpgrade getArmourUprgade(ItemStack stack) {
		return stack.getCapability(CapabilityArmourUpgrade.ARMOUR_UPRGRADE_CAPABILITY, null);
	}

	public static UpgradeHolder getArmourUprgadeHolder(ItemStack stack, IModularArmourManager armourManager) {
		return new UpgradeHolder(stack, armourManager);
	}

	public static IModularArmourManager getManager(ItemStack stack) {
		Validate.isTrue(isModularArmor(stack));
		return new ModularArmourManager(stack); //TODO move out of api package
	}

	public static boolean isModularArmor(ItemStack stack){
		return false; //TODO
	}

	public static List<IModularArmourManager> getArmourOnPlayer(EntityPlayer player){
		return StreamSupport
			.stream(player.getArmorInventoryList().spliterator(), false)
			.filter(ModularArmourUtils::isModularArmor)
			.map(ModularArmourUtils::getManager)
			.collect(Collectors.toList());
	}

	public static List<IArmourUpgrade> getUpgradesOnPlayer(EntityPlayer player){
		return getArmourOnPlayer(player).stream()
			.flatMap((Function<IModularArmourManager, Stream<IArmourUpgrade>>) iModularArmourManager -> iModularArmourManager.getAllUprgades().stream())
			.collect(Collectors.toList());
	}

	//Similar to above, just returns the UpgradeHolder to allow for more things to be done
	public static List<UpgradeHolder> getHoldersOnPlayer(EntityPlayer player){
		return getArmourOnPlayer(player).stream()
			.flatMap((Function<IModularArmourManager, Stream<UpgradeHolder>>) iModularArmourManager -> iModularArmourManager.getAllHolders().stream())
			.collect(Collectors.toList());
	}

}
