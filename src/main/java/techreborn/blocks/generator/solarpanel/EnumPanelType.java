package techreborn.blocks.generator.solarpanel;

import net.minecraft.util.IStringSerializable;
import reborncore.api.power.EnumPowerTier;

public enum EnumPanelType implements IStringSerializable {
	Basic("basic", 128, EnumPowerTier.LOW, 0),
	Hybrid("hybrid", 32, EnumPowerTier.MEDIUM, 1),
	Advanced("advanced", 512, EnumPowerTier.MEDIUM, 2),
	Ultimate("ultimate", 2048, EnumPowerTier.HIGH, 3),
	Quantum("quantum", 2048, EnumPowerTier.EXTREME, 4);

	private int ID;
	public int generationRate = 128;
	private String friendlyName;

	EnumPanelType(String friendlyName, int generationRate,  EnumPowerTier tier, int ID) {
		this.friendlyName = friendlyName;
		this.generationRate = generationRate;
		this.ID = ID;
	}



	@Override
	public String getName() {
		return friendlyName.toLowerCase();
	}


	public int getID(){
		return ID;
	}
}
