package techreborn.parts;

import net.minecraft.util.IStringSerializable;
import reborncore.api.power.EnumPowerTier;
import techreborn.parts.types.CopperCable;
import techreborn.parts.types.GlassFiberCable;
import techreborn.parts.types.GoldCable;
import techreborn.parts.types.HVCable;
import techreborn.parts.types.InsulatedCopperCable;
import techreborn.parts.types.InsulatedGoldCable;
import techreborn.parts.types.InsulatedHVCable;
import techreborn.parts.types.TinCable;

public enum EnumCableType implements IStringSerializable {
    COPPER("copper", "techreborn:blocks/cables/copper_cable", 128, 12.0, true, CopperCable.class, EnumPowerTier.LOW),
    TIN("tin", "techreborn:blocks/cables/tin_cable", 32, 12.0, true, TinCable.class, EnumPowerTier.MEDIUM),
    GOLD("gold", "techreborn:blocks/cables/gold_cable", 512, 12.0, true, GoldCable.class, EnumPowerTier.MEDIUM),
    HV("hv", "techreborn:blocks/cables/hv_cable", 2048, 12.0, true, HVCable.class, EnumPowerTier.HIGH),
    GLASSFIBER("glassfiber", "techreborn:blocks/cables/glass_fiber_cable", 8192, 12.0, false, GlassFiberCable.class, EnumPowerTier.HIGH),
    ICOPPER("insulatedcopper", "techreborn:blocks/cables/copper_insulated_cable", 128, 10.0, false, InsulatedCopperCable.class, EnumPowerTier.LOW),
    IGOLD("insulatedgold", "techreborn:blocks/cables/gold_insulated_cable", 512, 10.0, false, InsulatedGoldCable.class, EnumPowerTier.HIGH),
    IHV("insulatedhv", "techreborn:blocks/cables/hv_insulated_cable", 2048, 10.0, false, InsulatedHVCable.class, EnumPowerTier.HIGH);

    private String friendlyName;
    public String textureName = "minecraft:blocks/iron_block";
    public int transferRate = 128;
    public double cableThickness = 3.0;
    public boolean canKill = false;
    public Class<? extends CableMultipart> cableClass;
    public EnumPowerTier tier;

    EnumCableType(String friendlyName, String textureName, int transferRate, double cableThickness, boolean canKill, Class<? extends CableMultipart> cableClass, EnumPowerTier tier) {
        this.friendlyName = friendlyName;
        this.textureName = textureName;
        this.transferRate = transferRate;
        this.cableThickness = cableThickness / 2;
        this.canKill = canKill;
        this.cableClass = cableClass;
        this.tier = tier;
    }

    @Override
    public String getName() {
        return friendlyName.toLowerCase();
    }
}
