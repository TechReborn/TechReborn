package techreborn.client.container.builder.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;

import reborncore.api.power.IEnergyInterfaceItem;

import techreborn.lib.ModInfo;

public class EnergyChargeSlot extends FilteredSlot {

	private static final ResourceLocation TEX = new ResourceLocation(ModInfo.MOD_ID,
			"textures/gui/slots/energy_charge.png");

	@SuppressWarnings("null")
	public EnergyChargeSlot(final IInventory inventory, final int index, final int xPosition, final int yPosition) {
		super(inventory, index, xPosition, yPosition);

		this.setFilter(stack -> stack.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.UP)
				|| stack.getItem() instanceof IEnergyInterfaceItem);
		this.setBackgroundLocation(EnergyChargeSlot.TEX);
	}

}
