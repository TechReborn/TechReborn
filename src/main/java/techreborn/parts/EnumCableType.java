package techreborn.parts;

import net.minecraft.util.IStringSerializable;
import techreborn.parts.types.*;

public enum EnumCableType implements IStringSerializable {
    COPPER("copper", "techreborn:blocks/cables/copper_cable", 128, 12.0, true, CopperCable.class),
    TIN("tin", "minecraft:blocks/iron_block", 32, 4.0, true, TinCable.class),
    GOLD("gold", "minecraft:blocks/iron_block", 512, 3.0, true, GoldCable.class),
    HV("hv", "minecraft:blocks/iron_block", 2048, 6.0, true, HVCable.class),
    GLASSFIBER("glassfiber", "minecraft:blocks/iron_block", 8192, 4.0, false, GlassFiberCable.class),
    ICOPPER("insulatedcopper", "techreborn:blocks/cables/copper_insulated_cable", 128, 10.0, false, InsulatedCopperCable.class),
    IGOLD("insulatedgold", "minecraft:blocks/iron_block", 512, 6.0, false, InsulatedGoldCable.class),
    IHV("insulatedhv", "minecraft:blocks/iron_block", 2048, 10.0, false, InsulatedHVCable.class);

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
