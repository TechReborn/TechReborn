package techreborn.parts;

import net.minecraft.util.IStringSerializable;
import techreborn.parts.types.*;

public enum EnumCableType implements IStringSerializable {
    COPPER("copper", "minecraft:blocks/iron_block", 128, 4.0F, true, CopperCable.class),
    TIN("tin", "minecraft:blocks/iron_block", 32, 4.0F, true, TinCable.class),
    GOLD("gold", "minecraft:blocks/iron_block", 512, 3.0F, true, GoldCable.class),
    HV("hv", "minecraft:blocks/iron_block", 2048, 6.0F, true, HVCable.class),
    GLASSFIBER("glassfiber", "minecraft:blocks/iron_block", 8192, 4.0F, false, GlassFiberCable.class),
    ICOPPER("insulatedcopper", "minecraft:blocks/iron_block", 128, 6.0F, false, InsulatedCopperCable.class),
    IGOLD("insulatedgold", "minecraft:blocks/iron_block", 512, 6.0F, false, InsulatedGoldCable.class),
    IHV("insulatedhv", "minecraft:blocks/iron_block", 2048, 10.0F, false, InsulatedHVCable.class);

    private String friendlyName;
    public String textureName = "minecraft:blocks/iron_block";
    public int transferRate= 128;
    public float cableThickness = 3.0F;
    public boolean canKill = false;
    public Class<? extends CableMultipart> cableClass;

    EnumCableType(String friendlyName, String textureName, int transferRate, float cableThickness, boolean canKill, Class<? extends CableMultipart> cableClass) {
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
