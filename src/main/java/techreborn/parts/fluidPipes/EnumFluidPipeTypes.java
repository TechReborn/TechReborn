package techreborn.parts.fluidPipes;

import net.minecraft.util.IStringSerializable;

/**
 * Created by modmuss50 on 09/05/2016.
 */
public enum  EnumFluidPipeTypes implements IStringSerializable {

    EMPTY("empty","techreborn:blocks/fluidPipes/fluidpipe", false, false),
    INSERT("insert","techreborn:blocks/fluidPipes/fluidpipe_insert", false, true),
    EXTRACT("extract","techreborn:blocks/fluidPipes/fluidpipe_extract", true, false),
    FLV1("flv1","techreborn:blocks/fluidPipes/fluidpipe", false, false),
    FLV2("flv2","techreborn:blocks/fluidPipes/fluidpipe", false, false),
    FLV3("flv3","techreborn:blocks/fluidPipes/fluidpipe", false, false),
    FLV4("flv4","techreborn:blocks/fluidPipes/fluidpipe", false, false),
    FLV5("flv5","techreborn:blocks/fluidPipes/fluidpipe", false, false),
    FLV6("flv6","techreborn:blocks/fluidPipes/fluidpipe", false, false),
    FLV7("flv7","techreborn:blocks/fluidPipes/fluidpipe", false, false),
    FLV8("flv8","techreborn:blocks/fluidPipes/fluidpipe", false, false),
    FLV9("flv9","techreborn:blocks/fluidPipes/fluidpipe", false, false),
    FLV10("flv10","techreborn:blocks/fluidPipes/fluidpipe", false, false);


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
