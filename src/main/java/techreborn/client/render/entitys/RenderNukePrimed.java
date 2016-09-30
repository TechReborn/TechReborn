package techreborn.client.render.entitys;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import techreborn.blocks.BlockNuke;
import techreborn.entitys.EntityNukePrimed;
import techreborn.init.ModBlocks;

/**
 * Created by Mark on 13/03/2016.
 */
public class RenderNukePrimed extends Render<EntityNukePrimed> {

	public RenderNukePrimed(RenderManager renderManager) {
		super(renderManager);
		this.shadowSize = 0.5F;
	}

	@Override
	public void doRender(EntityNukePrimed entity, double x, double y, double z, float entityYaw, float partialTicks) {
		BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y + 0.5F, (float) z);
		if ((float) entity.fuse - partialTicks + 1.0F < 10.0F) {
			float f = 1.0F - ((float) entity.fuse - partialTicks + 1.0F) / 10.0F;
			f = MathHelper.clamp_float(f, 0.0F, 1.0F);
			f = f * f;
			f = f * f;
			float f1 = 1.0F + f * 0.3F;
			GlStateManager.scale(f1, f1, f1);
		}
		float f2 = (1.0F - ((float) entity.fuse - partialTicks + 1.0F) / 100.0F) * 0.8F;
		this.bindEntityTexture(entity);
		GlStateManager.translate(-0.5F, -0.5F, 0.5F);
		blockrendererdispatcher.renderBlockBrightness(ModBlocks.nuke.getDefaultState(),
			entity.getBrightness(partialTicks));
		GlStateManager.translate(0.0F, 0.0F, 1.0F);
		if (entity.fuse / 5 % 2 == 0) {
			GlStateManager.disableLighting();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(770, 772);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1F);
			GlStateManager.doPolygonOffset(-3.0F, -3.0F);
			GlStateManager.enablePolygonOffset();
			blockrendererdispatcher.renderBlockBrightness(
				ModBlocks.nuke.getDefaultState().withProperty(BlockNuke.OVERLAY, true), 1.0F);
			GlStateManager.doPolygonOffset(0.0F, 0.0F);
			GlStateManager.disablePolygonOffset();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.disableBlend();
			GlStateManager.enableLighting();
		}
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityNukePrimed entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}
}
