package techreborn.blocks.generator.solarpanel;

import net.minecraft.util.IStringSerializable;
import reborncore.api.power.EnumPowerTier;

public enum EnumPanelType implements IStringSerializable {
	Basic("basic", 128, 64, 50000, EnumPowerTier.LOW, 0),
	Hybrid("hybrid", 256, 128, 1000000, EnumPowerTier.MEDIUM, 1),
	Advanced("advanced", 512, 256, 5000000, EnumPowerTier.HIGH, 2),
	Ultimate("ultimate", 2048, 1042, 10000000,  EnumPowerTier.EXTREME, 3),
	Quantum("quantum", 16384, 8192, 100000000, EnumPowerTier.INSANE, 4);

	private int ID;
	private String friendlyName;

	public int generationRateD = 128;
	// Generation of FE during Day

	public int generationRateN = 64;
	// Generation of FE during Night

	public int internalCapacity = 0;
	// Internal FE storage of solar panel

	public EnumPowerTier powerTier;


	EnumPanelType(String friendlyName, int generationRateD, int generationRateN, int internalCapacity,  EnumPowerTier tier, int ID) {
		this.friendlyName = friendlyName;
		this.ID = ID;
		this.generationRateD = generationRateD;
		this.generationRateN = generationRateN;
		this.internalCapacity = internalCapacity;
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