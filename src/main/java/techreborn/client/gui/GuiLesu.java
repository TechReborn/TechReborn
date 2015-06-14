package techreborn.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import techreborn.client.container.ContainerAesu;
import techreborn.client.container.ContainerLesu;
import techreborn.packets.PacketAesu;
import techreborn.packets.PacketHandler;
import techreborn.tiles.TileAesu;
import techreborn.tiles.lesu.TileLesu;

import java.awt.*;

public class GuiLesu extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation(
            "techreborn", "textures/gui/aesu.png");

    TileLesu aesu;

    ContainerLesu containerLesu;

    public GuiLesu(EntityPlayer player,
                   TileLesu tileaesu)
    {
        super(new ContainerLesu(tileaesu, player));
        this.xSize = 156;
        this.ySize = 200;
        aesu = tileaesu;
        this.containerLesu  = (ContainerLesu) this.inventorySlots;
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
        this.fontRendererObj.drawString(StatCollector.translateToLocal("tile.techreborn.lesu.name"), 40, 10, Color.WHITE.getRGB());
        this.fontRendererObj.drawString(containerLesu.euOut + " eu/tick", 10, 20, Color.WHITE.getRGB());
        this.fontRendererObj.drawString(containerLesu.storedEu + " eu", 10, 30, Color.WHITE.getRGB());
        this.fontRendererObj.drawString(containerLesu.euChange + " eu change", 10, 40, Color.WHITE.getRGB());
        this.fontRendererObj.drawString(containerLesu.connectedBlocks + " blocks", 10, 50, Color.WHITE.getRGB());
        this.fontRendererObj.drawString(containerLesu.euStorage + " max eu", 10, 60, Color.WHITE.getRGB());
    }
}
