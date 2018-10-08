package techreborn.api.armor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.Validate;
import techreborn.items.armor.modular.ModularArmorManager;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ModularArmorUtils {

	public static boolean isUprgade(ItemStack stack) {
		return stack.hasCapability(CapabilityArmorUpgrade.ARMOR_UPRGRADE_CAPABILITY, null);
	}

	public static IArmorUpgrade getArmorUprgade(ItemStack stack) {
		return stack.getCapability(CapabilityArmorUpgrade.ARMOR_UPRGRADE_CAPABILITY, null);
	}

	public static UpgradeHolder getArmorUprgadeHolder(ItemStack stack, IModularArmorManager armorManager) {
		return new UpgradeHolder(stack, armorManager);
	}

	public static IModularArmorManager getManager(ItemStack stack) {
		Validate.isTrue(isModularArmor(stack));
		return new ModularArmorManager(stack); //TODO move out of api package
	}

	public static boolean isModularArmor(ItemStack stack){
		return false; //TODO
	}

	public static List<IModularArmorManager> getArmorOnPlayer(EntityPlayer player){
		return StreamSupport
			.stream(player.getArmorInventoryList().spliterator(), false)
			.filter(ModularArmorUtils::isModularArmor)
			.map(ModularArmorUtils::getManager)
			.collect(Collectors.toList());
	}

	public static List<IArmorUpgrade> getUpgradesOnPlayer(EntityPlayer player){
		return getArmorOnPlayer(player).stream()
			.flatMap((Function<IModularArmorManager, Stream<IArmorUpgrade>>) iModularArmorManager -> iModularArmorManager.getAllUprgades().stream())
			.collect(Collectors.toList());
	}

	//Similar to above, just returns the UpgradeHolder to allow for more things to be done
	public static List<UpgradeHolder> getHoldersOnPlayer(EntityPlayer player){
		return getArmorOnPlayer(player).stream()
			.flatMap((Function<IModularArmorManager, Stream<UpgradeHolder>>) iModularArmorManager -> iModularArmorManager.getAllHolders().stream())
			.collect(Collectors.toList());
	}

}
