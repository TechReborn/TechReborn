package techreborn.parts.fluidPipes;

import net.minecraft.util.IStringSerializable;

/**
 * Created by modmuss50 on 09/05/2016.
 */
public enum  EnumFluidPipeTypes implements IStringSerializable {

    EMPTY("empty","techreborn:blocks/fluidPipes/fluidpipe", false, false),
    INSERT("insert","techreborn:blocks/fluidPipes/fluidpipe_insert", false, true),
    EXTRACT("extract","techreborn:blocks/fluidPipes/fluidpipe_extract", true, false);

    private String name;
    public String textureName = "minecraft:blocks/iron_block";
    public boolean extract = false;
    public boolean insert = false;

    EnumFluidPipeTypes(String name,  String textureName, boolean extract, boolean insert) {
        this.name = name;
        this.textureName = textureName;
        this.extract = extract;
        this.insert = insert;
    }

    @Override
    public String getName() {
        return name;
    }
}
