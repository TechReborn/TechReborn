package techreborn.client.render.tiles;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * Created by mark on 15/06/2016.
 */
public class GlowInformation {

	//Null for front facing
	@Nullable
	EnumFacing dir;

	private ResourceLocation textureLocation;
	public TextureAtlasSprite textureAtlasSprite;

	@Nullable
	public PropertyBool isActive;

	public GlowInformation(EnumFacing dir, ResourceLocation texture, PropertyBool isActive) {
		this.dir = dir;
		this.textureLocation = texture;
		this.isActive = isActive;
	}

	public EnumFacing getDir() {
		return dir;
	}

	public ResourceLocation getTextureLocation() {
		return textureLocation;
	}

	public TextureAtlasSprite getTextureAtlasSprite() {
		return textureAtlasSprite;
	}

	public void setTextureAtlasSprite(TextureAtlasSprite textureAtlasSprite) {
		this.textureAtlasSprite = textureAtlasSprite;
	}
}
