package techreborn.api.armour;

import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.List;

public interface IModularArmourManager {

	public ItemStack getArmourStack();

	public IItemHandlerModifiable getInvetory();

	public List<IArmourUpgrade> getAllUprgades();

	//Same as above just contains more infomation related to the upgrade
	public List<UpgradeHolder> getAllHolders();

	public IEnergyStorage getEnergyStorage();
}
