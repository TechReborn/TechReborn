package techreborn.items.armor.modular.upgrades;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;
import techreborn.api.armor.UpgradeHolder;
import techreborn.items.armor.modular.BaseArmorUprgade;
import techreborn.lib.ModInfo;

public class PowerStorageUprgade extends BaseArmorUprgade {
	public PowerStorageUprgade() {
		super(new ResourceLocation(ModInfo.MOD_ID, "power_storage"));
	}

	@Override
	public int getPowerStorage(UpgradeHolder holder) {
		if(holder.getUpgradeStack().hasCapability(CapabilityEnergy.ENERGY, null)){
			return holder.getUpgradeStack().getCapability(CapabilityEnergy.ENERGY, null).getMaxEnergyStored();
		}
		return 0;
	}
}
