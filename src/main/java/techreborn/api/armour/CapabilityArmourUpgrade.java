package techreborn.api.armour;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class CapabilityArmourUpgrade {

	@CapabilityInject(IArmourUpgrade.class)
	public static Capability<IArmourUpgrade> ARMOUR_UPRGRADE_CAPABILITY = null;

}
