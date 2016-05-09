package techreborn.parts.powerCables;

import net.minecraft.util.IStringSerializable;
import reborncore.api.power.EnumPowerTier;
import techreborn.parts.powerCables.types.CopperCable;
import techreborn.parts.powerCables.types.GlassFiberCable;
import techreborn.parts.powerCables.types.GoldCable;
import techreborn.parts.powerCables.types.HVCable;
import techreborn.parts.powerCables.types.InsulatedCopperCable;
import techreborn.parts.powerCables.types.InsulatedGoldCable;
import techreborn.parts.powerCables.types.InsulatedHVCable;
import techreborn.parts.powerCables.types.TinCable;

public enum EnumCableType implements IStringSerializable
{
	COPPER("copper", "techreborn:blocks/cables/copper_cable", 128, 12.0, true, EnumPowerTier.LOW, CopperCable.class),
	TIN("tin", "techreborn:blocks/cables/tin_cable", 32, 12.0, true, EnumPowerTier.MEDIUM, TinCable.class),
	GOLD("gold", "techreborn:blocks/cables/gold_cable", 512, 12.0, true, EnumPowerTier.MEDIUM, GoldCable.class),
	HV("hv", "techreborn:blocks/cables/hv_cable", 2048, 12.0, true, EnumPowerTier.HIGH, HVCable.class),
	GLASSFIBER("glassfiber", "techreborn:blocks/cables/glass_fiber_cable", 8192, 12.0, false, EnumPowerTier.HIGH, GlassFiberCable.class),
	ICOPPER("insulatedcopper", "techreborn:blocks/cables/copper_insulated_cable", 128, 10.0, false, EnumPowerTier.LOW, InsulatedCopperCable.class),
	IGOLD("insulatedgold", "techreborn:blocks/cables/gold_insulated_cable", 512, 10.0, false, EnumPowerTier.MEDIUM, InsulatedGoldCable.class),
	IHV("insulatedhv", "techreborn:blocks/cables/hv_insulated_cable", 2048, 10.0, false, EnumPowerTier.HIGH, InsulatedHVCable.class);

	public String textureName = "minecraft:blocks/iron_block";
	public int transferRate = 128;
	public double cableThickness = 3.0;
	public boolean canKill = false;
	public Class<? extends CableMultipart> cableClass;
	public EnumPowerTier tier;
	private String friendlyName;

	EnumCableType(String friendlyName, String textureName, int transferRate, double cableThickness, boolean canKill,
			EnumPowerTier tier, Class<? extends CableMultipart> cableClass)
	{
		this.friendlyName = friendlyName;
		this.textureName = textureName;
		this.transferRate = transferRate;
		this.cableThickness = cableThickness / 2;
		this.canKill = canKill;
		this.cableClass = cableClass;
		this.tier = tier;
	}

	@Override
	public String getName()
	{
		return friendlyName.toLowerCase();
	}
}
