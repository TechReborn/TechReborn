package techreborn.blocks.generator.solarpanel;

import net.minecraft.util.IStringSerializable;
import reborncore.api.power.EnumPowerTier;

public enum EnumPanelType implements IStringSerializable {
	Basic("basic", 1, 0, EnumPowerTier.MICRO),
	Hybrid("hybrid", 16, 0, EnumPowerTier.LOW),
	Advanced("advanced", 64, 6,  EnumPowerTier.MEDIUM),
	Ultimate("ultimate", 256, 26, EnumPowerTier.HIGH),
	Quantum("quantum", 1024, 102, EnumPowerTier.EXTREME);

	private int ID;
	private String friendlyName;

	public int generationRateD = 10;
	// Generation of EU during Day

	public int generationRateN = 0;
	// Generation of EU during Night

	public int internalCapacity = 1000;
	// Internal EU storage of solar panel

	public EnumPowerTier powerTier;


	EnumPanelType(String friendlyName, int generationRateD, int generationRateN,  EnumPowerTier tier) {
		this.friendlyName = friendlyName;
		this.generationRateD = generationRateD;
		this.generationRateN = generationRateN;
		this.internalCapacity = (generationRateD * 1000);
		this.powerTier = tier;
	}

	@Override
	public String getName() {
		return friendlyName;
	}

}