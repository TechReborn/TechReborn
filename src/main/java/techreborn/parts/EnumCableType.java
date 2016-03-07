package techreborn.parts;

import net.minecraft.util.IStringSerializable;
import techreborn.parts.types.*;

public enum EnumCableType implements IStringSerializable {
    COPPER("copper", "techreborn:blocks/cables/copper_cable", 128, 12.0, true, CopperCable.class),
    TIN("tin", "techreborn:blocks/cables/tin_cable", 32, 12.0, true, TinCable.class),
    GOLD("gold", "techreborn:blocks/cables/gold_cable", 512, 12.0, true, GoldCable.class),
    HV("hv", "techreborn:blocks/cables/hv_cable", 2048, 12.0, true, HVCable.class),
    GLASSFIBER("glassfiber", "techreborn:blocks/cables/glass_fiber_cable", 8192, 12.0, false, GlassFiberCable.class),
    ICOPPER("insulatedcopper", "techreborn:blocks/cables/copper_insulated_cable", 128, 10.0, false, InsulatedCopperCable.class),
    IGOLD("insulatedgold", "techreborn:blocks/cables/gold_insulated_cable", 512, 10.0, false, InsulatedGoldCable.class),
    IHV("insulatedhv", "techreborn:blocks/cables/hv_insulated_cable", 2048, 10.0, false, InsulatedHVCable.class);

    private String friendlyName;
    public String textureName = "minecraft:blocks/iron_block";
    public int transferRate= 128;
    public double cableThickness = 3.0;
    public boolean canKill = false;
    public Class<? extends CableMultipart> cableClass;

    EnumCableType(String friendlyName, String textureName, int transferRate, double cableThickness, boolean canKill, Class<? extends CableMultipart> cableClass) {
        this.friendlyName = friendlyName;
        this.textureName = textureName;
        this.transferRate = transferRate;
        this.cableThickness = cableThickness /2;
        this.canKill = canKill;
        this.cableClass = cableClass;
    }

    @Override
    public String getName() {
        return friendlyName.toLowerCase();
    }
}
