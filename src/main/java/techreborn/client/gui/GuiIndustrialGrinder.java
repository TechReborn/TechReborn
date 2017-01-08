package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fluids.FluidStack;

import techreborn.tiles.multiblock.TileIndustrialGrinder;

public class GuiIndustrialGrinder extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation("techreborn",
			"textures/gui/industrial_grinder.png");

	TileIndustrialGrinder grinder;

	public GuiIndustrialGrinder(final EntityPlayer player, final TileIndustrialGrinder grinder) {
		super(grinder.createContainer(player));
		this.xSize = 176;
		this.ySize = 167;
		this.grinder = grinder;
	}

	@Override
	public void initGui() {
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		super.initGui();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float p_146976_1_, final int p_146976_2_, final int p_146976_3_) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiIndustrialGrinder.texture);
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		final int progress = this.grinder.getProgressScaled(24);
		if (progress > 0) {
			this.drawTexturedModalRect(k + 50, l + 35, 176, 14, progress + 1, 16);
		}

		final int energy = (int) (this.grinder.getEnergy() * 12f / this.grinder.getMaxPower());
		if (energy > 0) {
			this.drawTexturedModalRect(k + 132, l + 63 + 12 - energy, 176, 12 - energy, 14, energy + 2);
		}

		if (!this.grinder.tank.isEmpty()) {
			this.drawFluid(this.grinder.tank.getFluid(), k + 11, l + 66, 12, 47, this.grinder.tank.getCapacity());

			this.mc.renderEngine.bindTexture(GuiIndustrialGrinder.texture);
			this.drawTexturedModalRect(k + 14, l + 24, 179, 88, 9, 37);
		}

		if (!this.grinder.getMutliBlock()) {
			this.fontRendererObj.drawString(I18n.translateToLocal("techreborn.message.missingmultiblock"), k + 38, l + 52 + 12, -1);
		}

	}

	public void drawFluid(final FluidStack fluid, final int x, final int y, final int width, final int height, final int maxCapacity) {
		this.mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		final ResourceLocation still = fluid.getFluid().getStill(fluid);
		final TextureAtlasSprite sprite = this.mc.getTextureMapBlocks().getAtlasSprite(still.toString());

		final int drawHeight = (int) (fluid.amount / (maxCapacity * 1F) * height);
		final int iconHeight = sprite.getIconHeight();
		int offsetHeight = drawHeight;

		int iteration = 0;
		while (offsetHeight != 0) {
			final int curHeight = offsetHeight < iconHeight ? offsetHeight : iconHeight;
			this.drawTexturedModalRect(x, y - offsetHeight, sprite, width, curHeight);
			offsetHeight -= curHeight;
			iteration++;
			if (iteration > 50)
				break;
		}

	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int p_146979_1_, final int p_146979_2_) {
		final String name = I18n.translateToLocal("tile.techreborn.industrialgrinder.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6,
				4210752);
		this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory", new Object[0]), 8,
				this.ySize - 96 + 2, 4210752);
	}

}
