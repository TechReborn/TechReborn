package techreborn.blockentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import techreborn.init.TRContent;

public class DigitalTankBlockEntity extends TankStorageBaseBlockEntity {

	@Override
	public ItemStack getToolDrop(PlayerEntity playerEntity) {
		return getDropWithNBT(TRContent.Machine.DIGITAL_TANK.getStack());
	}
}
