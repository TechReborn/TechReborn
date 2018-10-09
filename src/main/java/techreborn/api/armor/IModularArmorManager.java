package techreborn.api.armor;

import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.List;
import java.util.function.Consumer;

public interface IModularArmorManager {

	public ItemStack getArmorStack();

	public IItemHandlerModifiable getInvetory();

	public List<IArmorUpgrade> getAllUprgades();

	//Same as above just contains more infomation related to the upgrade
	public List<UpgradeHolder> getAllHolders();

	public IEnergyStorage getEnergyStorage();

	public default void tooltip(List<String> list){
		getAllHolders().forEach(holder -> holder.getUpgrade().tooltip(holder, list));
	}
}
