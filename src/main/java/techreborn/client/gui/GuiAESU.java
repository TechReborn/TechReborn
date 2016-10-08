package techreborn.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import reborncore.common.packets.PacketHandler;
import reborncore.common.powerSystem.PowerSystem;
import techreborn.client.container.ContainerAESU;
import techreborn.packets.PacketAesu;
import techreborn.tiles.TileAesu;

import java.awt.*;
import java.io.IOException;

public class GuiAESU extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/aesu.png");

	TileAesu aesu;

	ContainerAESU containerAesu;

	public GuiAESU(EntityPlayer player, TileAesu tileaesu) {
		super(new ContainerAESU(tileaesu, player));
		this.xSize = 176;
		this.ySize = 197;
		aesu = tileaesu;
		this.containerAesu = (ContainerAESU) this.inventorySlots;
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.clear();
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.buttonList.add(new GuiButton(0, k + 115, l + 5, 15, 20, "++"));
		this.buttonList.add(new GuiButton(1, k + 115, l + 5 + 20, 15, 20, "+"));
		this.buttonList.add(new GuiButton(2, k + 115, l + 5 + (20 * 2), 15, 20, "-"));
		this.buttonList.add(new GuiButton(3, k + 115, l + 5 + (20 * 3), 15, 20, "--"));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(texture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}

	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		this.fontRendererObj.drawString(I18n.translateToLocal("tile.techreborn.aesu.name"), 40, 10,
			Color.WHITE.getRGB());
		this.fontRendererObj.drawString(PowerSystem.getLocaliszedPower(containerAesu.euOut) + " /tick", 10, 20,
			Color.WHITE.getRGB());
		this.fontRendererObj.drawString(PowerSystem.getLocaliszedPower(containerAesu.storedEu) + " ", 10, 30,
			Color.WHITE.getRGB());
		this.fontRendererObj.drawString(PowerSystem.getLocaliszedPower(containerAesu.euChange) + " change", 10, 40,
			Color.WHITE.getRGB());
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		PacketHandler.sendPacketToServer(new PacketAesu(button.id, aesu));
	}
}
