package techreborn.client.render.tiles;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

/**
 * Created by mark on 15/06/2016.
 */
public class GlowInformation {

	//Null for front facing
	EnumFacing dir;

	private ResourceLocation textureLocation;
	public TextureAtlasSprite textureAtlasSprite;

	public GlowInformation(EnumFacing dir, ResourceLocation texture) {
		this.dir = dir;
		this.textureLocation = texture;
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
