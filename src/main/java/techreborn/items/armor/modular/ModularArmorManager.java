package techreborn.items.armor.modular;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.apache.commons.lang3.Validate;
import reborncore.common.items.InventoryItem;
import techreborn.api.armor.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//Think of this class as the tile entity on the item. It handles sorting all the upgrades out, and the inventoryes, and the power
public class ModularArmorManager implements ICapabilityProvider, IModularArmorManager {

	ItemStack stack;
	ArmorSlot armorSlot;

	public ModularArmorManager(ItemStack stack) {
		Validate.isInstanceOf(ItemArmor.class, stack.getItem());
		this.stack = stack;
		ItemArmor itemArmor = (ItemArmor) stack.getItem();
		this.armorSlot = ArmorSlot.fromEntityEquipmentSlot(itemArmor.armorType);
	}

	@Override
	public ItemStack getArmorStack() {
		return stack;
	}

	@Override
	public IItemHandlerModifiable getInvetory() {
		return InventoryItem.getItemInvetory(stack, 64);
	}

	@Override
	public IEnergyStorage getEnergyStorage() {
		return null; //TODO power
	}

	@Override
	public List<IArmorUpgrade> getAllUprgades() {
		return IntStream.range(0, getInvetory().getSlots())
			.mapToObj(value -> getInvetory().getStackInSlot(value))
			.filter(ModularArmorUtils::isUprgade)
			.map(ModularArmorUtils::getArmorUprgade)
			.collect(Collectors.toList());
	}

	@Override
	public List<UpgradeHolder> getAllHolders() {
		return IntStream.range(0, getInvetory().getSlots())
			.mapToObj(value -> getInvetory().getStackInSlot(value))
			.filter(ModularArmorUtils::isUprgade)
			.map(stack -> ModularArmorUtils.getArmorUprgadeHolder(stack, this))
			.collect(Collectors.toList());
	}

	@Override
	public boolean hasCapability(
		@Nonnull
			Capability<?> capability,
		@Nullable
			EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityArmorUpgrade.ARMOR_MANAGER_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability(
		@Nonnull
			Capability<T> capability,
		@Nullable
			EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getInvetory());
		}
		if(capability == CapabilityArmorUpgrade.ARMOR_MANAGER_CAPABILITY){
			return CapabilityArmorUpgrade.ARMOR_MANAGER_CAPABILITY.cast(this);
		}
		return null;
	}
}
