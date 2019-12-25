package techreborn.blockentity.machine.tier3;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import techreborn.blockentity.TankStorageBaseBlockEntity;
import techreborn.init.TRContent;

public class QuantumTankBlockEntity extends TankStorageBaseBlockEntity {

	public QuantumTankBlockEntity(){
		super();
	}

	@Override
	public ItemStack getToolDrop(PlayerEntity playerEntity) {
		return getDropWithNBT(TRContent.Machine.QUANTUM_TANK.getStack());
	}
}
