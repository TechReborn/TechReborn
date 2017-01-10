package techreborn.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import techreborn.tiles.TileRollingMachine;

public class GuiRollingMachine extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation("techreborn",
			"textures/gui/rolling_machine.png");
	TileRollingMachine rollingMachine;

	public GuiRollingMachine(final EntityPlayer player, final TileRollingMachine tileRollingmachine) {
		super(tileRollingmachine.createContainer(player));
		this.xSize = 176;
		this.ySize = 167;
		this.rollingMachine = tileRollingmachine;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float p_146976_1_, final int p_146976_2_, final int p_146976_3_) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiRollingMachine.texture);
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		int j = this.rollingMachine.getBurnTimeRemainingScaled(24);
		this.drawTexturedModalRect(k + 91, l + 34, 176, 14, j + 1, 19);

		j = this.rollingMachine.getEnergyScaled(12);
		if (j > 0) {
			this.drawTexturedModalRect(k + 7, l + 33 + 12 - j, 176, 12 - j, 14, j + 2);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int p_146979_1_, final int p_146979_2_) {
		final String name = I18n.translateToLocal("tile.techreborn.rollingmachine.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6,
				4210752);
		this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory", new Object[0]), 8,
				this.ySize - 96 + 2, 4210752);
	}

	@Override
	public void initGui() {
		this.buttonList.clear();
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		this.buttonList.add(new GuiButton(0, k + 4, l + 4, 20, 20, "R"));
		super.initGui();
	}

}
