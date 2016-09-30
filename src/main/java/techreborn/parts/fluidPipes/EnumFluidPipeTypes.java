package techreborn.parts.fluidPipes;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;

/**
 * Created by modmuss50 on 09/05/2016.
 */
public enum EnumFluidPipeTypes implements IStringSerializable {

	EMPTY("empty", "techreborn:blocks/fluidPipes/fluidpipe", false, false, TextFormatting.DARK_GRAY),
	INSERT("insert", "techreborn:blocks/fluidPipes/fluidpipe_insert", false, true, TextFormatting.BLUE),
	EXTRACT("extract", "techreborn:blocks/fluidPipes/fluidpipe_extract", true, false, TextFormatting.GOLD);

	public String textureName = "minecraft:blocks/iron_block";
	public boolean extract = false;
	public boolean insert = false;
	public TextFormatting colour = TextFormatting.WHITE;
	private String name;

	EnumFluidPipeTypes(String name, String textureName, boolean extract, boolean insert, TextFormatting colour) {
		this.name = name;
		this.textureName = textureName;
		this.extract = extract;
		this.insert = insert;
		this.colour = colour;
	}

	@Override
	public String getName() {
		return name;
	}
}
