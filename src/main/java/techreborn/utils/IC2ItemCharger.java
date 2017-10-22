package techreborn.utils;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.item.ItemStack;
import reborncore.common.powerSystem.TilePowerAcceptor;

public class IC2ItemCharger {

	public static void chargeIc2Item(TilePowerAcceptor tilePowerAcceptor, ItemStack stack){
		if(stack.isEmpty()){
			return;
		}
		if(stack.getItem() instanceof IElectricItem){
			tilePowerAcceptor.useEnergy(ElectricItem.manager.charge(stack, tilePowerAcceptor.getEnergy(), 4, false, false));
		}
	}

}
