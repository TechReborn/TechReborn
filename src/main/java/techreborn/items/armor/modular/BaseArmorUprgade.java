package techreborn.items.armor.modular;

import net.minecraft.util.ResourceLocation;
import techreborn.api.armor.IArmorUpgrade;
import techreborn.api.armor.UpgradeHolder;

public abstract class BaseArmorUprgade implements IArmorUpgrade {

	ResourceLocation name;

	public BaseArmorUprgade(ResourceLocation name) {
		this.name = name;
	}

	@Override
	public ResourceLocation getName() {
		return name;
	}

	public boolean canUsePower(UpgradeHolder holder, int value){
		return holder.getArmorManager().getEnergyStorage().getEnergyStored() >= value;
	}
}
