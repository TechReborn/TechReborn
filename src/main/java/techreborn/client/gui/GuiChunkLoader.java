package techreborn.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import techreborn.client.container.builder.ContainerBuilder;
import techreborn.tiles.TileChunkLoader;

public class GuiChunkLoader extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation("techreborn",
			"textures/gui/industrial_chunkloader.png");
	TileChunkLoader chunkloader;
	private GuiButton plusOneButton;
	private GuiButton plusTenButton;
	private GuiButton minusOneButton;
	private GuiButton minusTenButton;

	public GuiChunkLoader(final EntityPlayer player, final TileChunkLoader tilechunkloader) {
		super(new ContainerBuilder("chunkloader").player(player.inventory).inventory().hotbar().addInventory()
				.create());
		this.xSize = 176;
		this.ySize = 167;
		this.chunkloader = tilechunkloader;
	}

	@Override
	public void initGui() {
		super.initGui();
		this.guiLeft = this.width / 2 - this.xSize / 2;
		this.guiTop = this.height / 2 - this.ySize / 2;
		this.plusOneButton = new GuiButton(0, this.guiLeft + 5, this.guiTop + 37, 40, 20, "+1");
		this.plusTenButton = new GuiButton(0, this.guiLeft + 45, this.guiTop + 37, 40, 20, "+10");

		this.minusOneButton = new GuiButton(0, this.guiLeft + 90, this.guiTop + 37, 40, 20, "-1");
		this.minusTenButton = new GuiButton(0, this.guiLeft + 130, this.guiTop + 37, 40, 20, "-10");

		this.buttonList.add(this.plusOneButton);
		this.buttonList.add(this.plusTenButton);
		this.buttonList.add(this.minusOneButton);
		this.buttonList.add(this.minusTenButton);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float p_146976_1_, final int p_146976_2_, final int p_146976_3_) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiChunkLoader.texture);
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int p_146979_1_, final int p_146979_2_) {
		final String name = I18n.translateToLocal("tile.techreborn.chunkloader.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6,
				4210752);
		this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory", new Object[0]), 8,
				this.ySize - 96 + 2, 4210752);
	}

}
