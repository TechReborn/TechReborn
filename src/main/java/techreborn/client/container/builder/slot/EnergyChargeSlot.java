package techreborn.client.container.builder.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import reborncore.api.power.IEnergyInterfaceItem;

public class EnergyChargeSlot extends FilteredSlot {

	@SuppressWarnings("null")
	public EnergyChargeSlot(final IInventory inventory, final int index, final int xPosition, final int yPosition) {
		super(inventory, index, xPosition, yPosition);
		this.setFilter(stack -> stack.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.UP) || stack.getItem() instanceof IEnergyInterfaceItem);
	}

}
