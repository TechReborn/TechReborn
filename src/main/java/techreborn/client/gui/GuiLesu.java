package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import reborncore.common.powerSystem.PowerSystem;
import techreborn.client.container.ContainerLesu;
import techreborn.tiles.lesu.TileLesu;

import java.awt.*;

public class GuiLesu extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/aesu.png");
    TileLesu aesu;
    ContainerLesu containerLesu;

    public GuiLesu(EntityPlayer player, TileLesu tileaesu) {
        super(new ContainerLesu(tileaesu, player));
        this.xSize = 176;
        this.ySize = 197;
        aesu = tileaesu;
        this.containerLesu = (ContainerLesu) this.inventorySlots;
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
        this.fontRendererObj.drawString(I18n.translateToLocal("tile.techreborn.lesu.name"), 40, 10, Color.WHITE.getRGB());
        this.fontRendererObj.drawString(PowerSystem.getLocaliszedPower(containerLesu.euOut) + "/t", 10, 20, Color.WHITE.getRGB());
        this.fontRendererObj.drawString(PowerSystem.getLocaliszedPower(containerLesu.storedEu), 10, 30, Color.WHITE.getRGB());
        this.fontRendererObj.drawString(PowerSystem.getLocaliszedPower(containerLesu.euChange) + " change", 10, 40, Color.WHITE.getRGB());
        this.fontRendererObj.drawString(containerLesu.connectedBlocks + " blocks", 10, 50, Color.WHITE.getRGB());
        this.fontRendererObj.drawString(PowerSystem.getLocaliszedPower(containerLesu.euStorage) + " max", 10, 60, Color.WHITE.getRGB());
    }
}
