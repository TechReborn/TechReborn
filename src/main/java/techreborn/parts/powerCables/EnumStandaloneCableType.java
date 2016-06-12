package techreborn.parts.powerCables;

import net.minecraft.util.IStringSerializable;
import reborncore.api.power.EnumPowerTier;

public enum EnumStandaloneCableType implements IStringSerializable {
	COPPER("copper", "techreborn:blocks/cables/copper_cable", 128, 12.0, true, EnumPowerTier.LOW),
	TIN("tin", "techreborn:blocks/cables/tin_cable", 32, 12.0, true, EnumPowerTier.MEDIUM),
	GOLD("gold", "techreborn:blocks/cables/gold_cable", 512, 12.0, true, EnumPowerTier.MEDIUM),
	HV("hv", "techreborn:blocks/cables/hv_cable", 2048, 12.0, true, EnumPowerTier.HIGH),
	GLASSFIBER("glassfiber", "techreborn:blocks/cables/glass_fiber_cable", 8192, 12.0, false, EnumPowerTier.HIGH),
	ICOPPER("insulatedcopper", "techreborn:blocks/cables/copper_insulated_cable", 128, 10.0, false, EnumPowerTier.LOW),
	IGOLD("insulatedgold", "techreborn:blocks/cables/gold_insulated_cable", 512, 10.0, false, EnumPowerTier.MEDIUM),
	IHV("insulatedhv", "techreborn:blocks/cables/hv_insulated_cable", 2048, 10.0, false, EnumPowerTier.HIGH),
	SUPERCONDUCTOR("superconductor", "techreborn:blocks/cables/superconductor", 2048, 10.0, false, EnumPowerTier.HIGH);


	public String textureName = "minecraft:blocks/iron_block";
	public int transferRate = 128;
	public double cableThickness = 3.0;
	public boolean canKill = false;
	public EnumPowerTier tier;
	private String friendlyName;

	EnumStandaloneCableType(String friendlyName, String textureName, int transferRate, double cableThickness,
			boolean canKill, EnumPowerTier tier) {
		this.friendlyName = friendlyName;
		this.textureName = textureName;
		this.transferRate = transferRate;
		this.cableThickness = cableThickness / 2;
		this.canKill = canKill;
		this.tier = tier;
	}

	@Override
	public String getName() {
		return friendlyName.toLowerCase();
	}
}
