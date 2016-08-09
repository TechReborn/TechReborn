package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fluids.FluidStack;
import techreborn.client.container.ContainerIndustrialSawmill;
import techreborn.tiles.multiblock.TileIndustrialSawmill;

public class GuiIndustrialSawmill extends GuiContainer
{

	public static final ResourceLocation texture = new ResourceLocation("techreborn",
			"textures/gui/industrial_sawmill.png");

	TileIndustrialSawmill sawmill;

	public GuiIndustrialSawmill(EntityPlayer player, TileIndustrialSawmill tilesawmill)
	{
		super(new ContainerIndustrialSawmill(tilesawmill, player));
		this.xSize = 176;
		this.ySize = 167;
        this.sawmill = tilesawmill;
	}

	@Override
	public void initGui()
	{
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		super.initGui();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(texture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;

		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		int progress = sawmill.getProgressScaled(24);
		this.drawTexturedModalRect(k + 56, l + 38, 176, 14, progress - 1, 11);

		int energy = 13 - (int) (sawmill.getEnergy() / sawmill.getMaxPower() * 13F);
		drawTexturedModalRect(k + 36, l + 66 + energy, 179, 1 + energy, 7, 13 - energy);

		if(!sawmill.tank.isEmpty()) {
			drawFluid(sawmill.tank.getFluid(), k + 11, l + 66, 12, 47, sawmill.tank.getCapacity());

<<<<<<< HEAD
			mc.renderEngine.bindTexture(texture);
			drawTexturedModalRect(k + 14, l + 24, 179, 88, 9, 37);
=======
		j = sawmill.getEnergyScaled(12);
		if (j > 0)
		{
			this.drawTexturedModalRect(k + 33, l + 65 + 12 - j, 176, 12 - j, 14, j + 2);
>>>>>>> parent of b292fdd... Rewrite to use new RebornCore Power API. Texture fixes.
		}

		if (!sawmill.getMutliBlock()) {
			//GuiUtil.drawTooltipBox(k + 30, l + 50 + 12, 114, 10);
			this.fontRendererObj.drawString(I18n.translateToLocal("techreborn.message.missingmultiblock"), k + 38,
					l + 52 + 12, -1);
		}

	}

	public void drawFluid(FluidStack fluid, int x, int y, int width, int height, int maxCapacity) {
		mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		ResourceLocation still = fluid.getFluid().getStill(fluid);
		TextureAtlasSprite sprite = mc.getTextureMapBlocks().getAtlasSprite(still.toString());

		int drawHeight = (int) ((fluid.amount / (maxCapacity * 1F)) * height);
		int iconHeight = sprite.getIconHeight();
		int offsetHeight = drawHeight;

		int iteration = 0;
		while(offsetHeight != 0) {
			int curHeight = offsetHeight < iconHeight ? offsetHeight : iconHeight;
			drawTexturedModalRect(x, y - offsetHeight, sprite, width, curHeight);
			offsetHeight -= curHeight;
			iteration++;
			if(iteration > 50) break;
		}

	}

	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		String name = I18n.translateToLocal("tile.techreborn.industrialsawmill.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory"), 58, this.ySize - 96 + 2, 4210752);
	}

}
