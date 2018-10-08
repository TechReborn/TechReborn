package techreborn.api.armor;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class CapabilityArmorUpgrade {

	@CapabilityInject(IArmorUpgrade.class)
	public static Capability<IArmorUpgrade> ARMOR_UPRGRADE_CAPABILITY = null;

}
