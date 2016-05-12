package techreborn.parts.fluidPipes;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.IStringSerializable;

/**
 * Created by modmuss50 on 09/05/2016.
 */
public enum EnumFluidPipeTypes implements IStringSerializable
{

	EMPTY("empty", "techreborn:blocks/fluidPipes/fluidpipe", false, false, ChatFormatting.DARK_GRAY),
	INSERT("insert", "techreborn:blocks/fluidPipes/fluidpipe_insert", false, true, ChatFormatting.BLUE),
	EXTRACT("extract", "techreborn:blocks/fluidPipes/fluidpipe_extract", true, false, ChatFormatting.GOLD);

	public String textureName = "minecraft:blocks/iron_block";
	public boolean extract = false;
	public boolean insert = false;
	public ChatFormatting colour = ChatFormatting.WHITE;
	private String name;

	EnumFluidPipeTypes(String name, String textureName, boolean extract, boolean insert, ChatFormatting colour)
	{
		this.name = name;
		this.textureName = textureName;
		this.extract = extract;
		this.insert = insert;
		this.colour = colour;
	}

	@Override public String getName()
	{
		return name;
	}
}
