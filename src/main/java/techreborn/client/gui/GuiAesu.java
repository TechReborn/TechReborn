package techreborn.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import techreborn.client.container.ContainerAesu;
import techreborn.packets.PacketAesu;
import techreborn.packets.PacketHandler;
import techreborn.tiles.TileAesu;

import java.awt.*;

public class GuiAesu extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation(
			"techreborn", "textures/gui/aesu.png");

	TileAesu aesu;

	ContainerAesu containerAesu;

	public GuiAesu(EntityPlayer player,
			TileAesu tileaesu)
	{
		super(new ContainerAesu(tileaesu, player));
		this.xSize = 156;
		this.ySize = 200;
		aesu = tileaesu;
		this.containerAesu  = (ContainerAesu) this.inventorySlots;
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.clear();
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.buttonList.add(new GuiButton(0, k + 96, l + 8, 18, 20, "++"));
		this.buttonList.add(new GuiButton(1, k + 96, l + 8 + 22, 18, 20, "+"));
		this.buttonList.add(new GuiButton(2, k + 96, l + 8 + (22*2), 18, 20, "-"));
		this.buttonList.add(new GuiButton(3, k + 96, l + 8 + (22*3), 18, 20, "--"));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
			int p_146976_2_, int p_146976_3_)
	{
		this.mc.getTextureManager().bindTexture(texture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}

	protected void drawGuiContainerForegroundLayer(int p_146979_1_,
			int p_146979_2_)
	{
		this.fontRendererObj.drawString(StatCollector.translateToLocal("tile.techreborn.aesu.name"), 40, 10, Color.WHITE.getRGB());
		this.fontRendererObj.drawString((int)containerAesu.euOut + " eu/tick", 10, 20, Color.WHITE.getRGB());
		this.fontRendererObj.drawString((int)containerAesu.storedEu + " eu", 10, 30, Color.WHITE.getRGB());
        this.fontRendererObj.drawString((int)containerAesu.euChange + " eu change", 10, 40, Color.WHITE.getRGB());
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		PacketHandler.sendPacketToServer(new PacketAesu(button.id, aesu));
	}
}
