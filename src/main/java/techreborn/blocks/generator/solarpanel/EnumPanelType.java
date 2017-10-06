package techreborn.blocks.generator.solarpanel;

import net.minecraft.util.IStringSerializable;
import reborncore.api.power.EnumPowerTier;

public enum EnumPanelType implements IStringSerializable {
	Basic("basic", 1, 0, EnumPowerTier.MICRO, 0),
	Hybrid("hybrid", 16, 0, EnumPowerTier.LOW, 1),
	Advanced("advanced", 64, 6,  EnumPowerTier.MEDIUM, 2),
	Ultimate("ultimate", 256, 26, EnumPowerTier.HIGH, 3),
	Quantum("quantum", 1024, 102, EnumPowerTier.EXTREME, 4);

	private int ID;
	private String friendlyName;

	public int generationRateD = 10;
	// Generation of FE during Day

	public int generationRateN = 0;
	// Generation of FE during Night

	public int internalCapacity = 1000;
	// Internal FE storage of solar panel

	public EnumPowerTier powerTier;


	EnumPanelType(String friendlyName, int generationRateD, int generationRateN,  EnumPowerTier tier, int ID) {
		this.friendlyName = friendlyName;
		this.ID = ID;
		this.generationRateD = generationRateD;
		this.generationRateN = generationRateN;
		this.internalCapacity = (generationRateD * 1000);
		this.powerTier = tier;
	}



	@Override
	public String getName() {
		return friendlyName;
	}


	public int getID(){
		return ID;
	}
}