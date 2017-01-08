package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fluids.FluidStack;

import techreborn.client.container.builder.ContainerBuilder;
import techreborn.tiles.multiblock.TileIndustrialSawmill;

public class GuiIndustrialSawmill extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation("techreborn",
			"textures/gui/industrial_sawmill.png");

	TileIndustrialSawmill sawmill;

	public GuiIndustrialSawmill(final EntityPlayer player, final TileIndustrialSawmill tilesawmill) {
		super(new ContainerBuilder("chemicalreactor").player(player.inventory).inventory(8, 84).hotbar(8, 142)
				.addInventory().tile(tilesawmill).slot(0, 70, 21).slot(1, 90, 21).outputSlot(2, 80, 51)
				.energySlot(3, 8, 51).upgradeSlot(4, 152, 8).upgradeSlot(5, 152, 26).upgradeSlot(6, 152, 44)
				.upgradeSlot(7, 152, 62).syncEnergyValue().syncCrafterValue().addInventory().create());
		this.xSize = 176;
		this.ySize = 167;
		this.sawmill = tilesawmill;
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
		this.mc.getTextureManager().bindTexture(GuiIndustrialSawmill.texture);
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;

		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		final int progress = this.sawmill.getProgressScaled(24);
		this.drawTexturedModalRect(k + 56, l + 38, 176, 14, progress - 1, 11);

		final int energy = 13 - (int) (this.sawmill.getEnergy() / this.sawmill.getMaxPower() * 13F);
		this.drawTexturedModalRect(k + 36, l + 66 + energy, 179, 1 + energy, 7, 13 - energy);

		if (!this.sawmill.tank.isEmpty()) {
			this.drawFluid(this.sawmill.tank.getFluid(), k + 11, l + 66, 12, 47, this.sawmill.tank.getCapacity());

			final int j = this.sawmill.getEnergyScaled(12);
			if (j > 0) {
				this.drawTexturedModalRect(k + 33, l + 65 + 12 - j, 176, 12 - j, 14, j + 2);
			}

			if (!this.sawmill.getMutliBlock()) {
				//GuiUtil.drawTooltipBox(k + 30, l + 50 + 12, 114, 10);
				this.fontRendererObj.drawString(I18n.translateToLocal("techreborn.message.missingmultiblock"), k + 38,
						l + 52 + 12, -1);
			}
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
		final String name = I18n.translateToLocal("tile.techreborn.industrialsawmill.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory"), 58, this.ySize - 96 + 2, 4210752);
	}

}
