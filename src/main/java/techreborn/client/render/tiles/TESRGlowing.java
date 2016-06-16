package techreborn.client.render.tiles;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;
import reborncore.common.tile.TileMachineBase;
import techreborn.proxies.ClientProxy;

import java.util.List;

/**
 * Created by mark on 15/06/2016.
 */
public class TESRGlowing extends TileEntitySpecialRenderer<TileMachineBase> {

	@Override
	public void renderTileEntityAt(TileMachineBase te, double x, double y, double z, float partialTicks, int destroyStage) {

		if (ClientProxy.handler.informationHashMap.containsKey(te.getClass())) {
			if(!te.isActive()){
				return;
			}
			List<GlowInformation> informationList = ClientProxy.handler.informationHashMap.get(te.getClass());
			//Binds the blocks texture
			Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			float lastX = OpenGlHelper.lastBrightnessX;
			float lastY = OpenGlHelper.lastBrightnessY;
			//Disables lighting
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
			GlStateManager.disableLighting();

			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, z);
			GlStateManager.color(1, 1, 1);

			for (GlowInformation information : informationList) {
				//Get the texture uv
				TextureAtlasSprite tas = information.getTextureAtlasSprite();
				float minU = tas.getMinU();
				float maxU = tas.getMaxU();
				float minV = tas.getMinV();
				float maxV = tas.getMaxV();

				EnumFacing dir = information.getDir();
				if (dir == null) {
					dir = te.getFacingEnum();
				}

				Tessellator tess = Tessellator.getInstance();
				VertexBuffer wr = tess.getBuffer();
				wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

				switch (dir) {
					case NORTH:
						break;
					case WEST:
						GlStateManager.rotate(90, 0, 1, 0);
						GlStateManager.translate(-1 , 0, 0);
						break;
					case SOUTH:
						GlStateManager.rotate(180, 0, 1, 0);
						GlStateManager.translate(-1, 0, -1);
						break;
					case EAST:
						GlStateManager.rotate(270, 0, 1, 0);
						GlStateManager.translate(0, 0, -1);
						break;
					default:
						break;
				}

				wr.pos(1, 1, -0.001).tex(minU, minV).endVertex();
				wr.pos(1, 0, -0.001).tex(minU, maxV).endVertex();
				wr.pos(0, 0, -0.001).tex(maxU, maxV).endVertex();
				wr.pos(0, 1, -0.001).tex(maxU, minV).endVertex();


				tess.draw();
				wr.setTranslation(0, 0, 0);

			}

			//And make it dark again
			GlStateManager.enableLighting();
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastX, lastY);
			GlStateManager.popMatrix();

		}
	}
}
