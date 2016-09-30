package techreborn.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import techreborn.client.container.ContainerRollingMachine;
import techreborn.tiles.TileRollingMachine;

public class GuiRollingMachine extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation("techreborn",
		"textures/gui/rolling_machine.png");
	TileRollingMachine rollingMachine;
	ContainerRollingMachine containerRollingMachine;

	public GuiRollingMachine(EntityPlayer player, TileRollingMachine tileRollingmachine) {
		super(new ContainerRollingMachine(tileRollingmachine, player));
		this.xSize = 176;
		this.ySize = 167;
		rollingMachine = tileRollingmachine;
		containerRollingMachine = (ContainerRollingMachine) this.inventorySlots;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(texture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		int j = this.containerRollingMachine.getBurnTimeRemainingScaled(24);
		this.drawTexturedModalRect(k + 91, l + 34, 176, 14, j + 1, 19);

		j = this.rollingMachine.getEnergyScaled(12);
		if (j > 0) {
			this.drawTexturedModalRect(k + 7, l + 33 + 12 - j, 176, 12 - j, 14, j + 2);
		}
	}

	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		String name = I18n.translateToLocal("tile.techreborn.rollingmachine.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6,
			4210752);
		this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory", new Object[0]), 8,
			this.ySize - 96 + 2, 4210752);
	}

	@Override
	public void initGui() {
		this.buttonList.clear();
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.buttonList.add(new GuiButton(0, k + 4, l + 4, 20, 20, "R"));
		super.initGui();
	}

}
