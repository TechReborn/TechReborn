package techreborn.items.armor.modular.upgrades;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import reborncore.common.powerSystem.forge.IEnergyStorageModifiable;
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

	//Move all of the power out of the power storage item being added and put it into the armor
	@Override
	public void onAdded(UpgradeHolder holder, TileEntity workstation) {
		if(!holder.getUpgradeStack().hasCapability(CapabilityEnergy.ENERGY, null)){
			return;
		}
		IEnergyStorage sourceEnergy = holder.getUpgradeStack().getCapability(CapabilityEnergy.ENERGY, null);
		IEnergyStorage targetEneergy =  holder.getArmorManager().getEnergyStorage();
		//TODO is there another way around the power input limt?
		if(targetEneergy instanceof IEnergyStorageModifiable && sourceEnergy instanceof IEnergyStorageModifiable){
			((IEnergyStorageModifiable) targetEneergy).setEnergy(sourceEnergy.getEnergyStored());
			((IEnergyStorageModifiable) sourceEnergy).setEnergy(0);
		}

	}

	@Override
	public void onRemoved(UpgradeHolder holder, TileEntity workstation) {
		if(holder.getUpgradeStack().getCapability(CapabilityEnergy.ENERGY, null) != null){
			return;
		}
		IEnergyStorage targetEneergy = holder.getUpgradeStack().getCapability(CapabilityEnergy.ENERGY, null);
		IEnergyStorage sourceEnergy =  holder.getArmorManager().getEnergyStorage();
		if(targetEneergy instanceof IEnergyStorageModifiable && sourceEnergy instanceof IEnergyStorageModifiable){
			//TODO caculate the excess energy that will be left over
//			((IEnergyStorageModifiable) targetEneergy).setEnergy(sourceEnergy.getEnergyStored());
//			((IEnergyStorageModifiable) sourceEnergy).setEnergy(0);
		}
	}
}
