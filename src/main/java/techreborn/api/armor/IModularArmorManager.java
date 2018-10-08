package techreborn.api.armor;

import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.List;

public interface IModularArmorManager {

	public ItemStack getArmorStack();

	public IItemHandlerModifiable getInvetory();

	public List<IArmorUpgrade> getAllUprgades();

	//Same as above just contains more infomation related to the upgrade
	public List<UpgradeHolder> getAllHolders();

	public IEnergyStorage getEnergyStorage();
}
