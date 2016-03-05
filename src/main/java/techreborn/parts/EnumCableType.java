package techreborn.parts;

import net.minecraft.util.IStringSerializable;

public enum EnumCableType implements IStringSerializable {
    COPPER("copper"),
    TIN("tin"),
    GOLD("gold"),
    HV("hv"),
    GLASSFIBER("glassfiber"),
    ICOPPER("insulatedcopper"),
    IGOLD("insulatedgold"),
    IHV("insulatedhv");

    private String friendlyName;
    public String textureName = "minecraft:blocks/iron_block";
    public int transferRate= 128;
    public double cableThickness = 3.0;
    public boolean canKill = false;

    EnumCableType(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public String getName() {
        return friendlyName;
    }
}
