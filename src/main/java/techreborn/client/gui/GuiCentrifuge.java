package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import techreborn.client.container.ContainerCentrifuge;
import techreborn.tiles.TileCentrifuge;

public class GuiCentrifuge extends GuiContainer {

    public static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/industrial_centrifuge.png");

    TileCentrifuge centrifuge;

    public GuiCentrifuge(EntityPlayer player, TileCentrifuge tileCentrifuge) {
        super(new ContainerCentrifuge(tileCentrifuge, player));
        this.xSize = 176;
        this.ySize = 167;
        centrifuge = tileCentrifuge;
    }

    @Override
    public void initGui() {
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        super.initGui();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(texture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        int j = 0;

        j = centrifuge.getProgressScaled(11);
        if (j > 0) {
            this.drawTexturedModalRect(k + 83, l + 23 + 10 - j, 177, 15 + 10 - j, 10, j);
            this.drawTexturedModalRect(k + 98, l + 38, 177, 51, j, 10);
            this.drawTexturedModalRect(k + 83, l + 53, 177, 39, 10, j);
            this.drawTexturedModalRect(k + 68 + 10 - j, l + 38, 177 + 10 - j, 27, j, 10);
        }

        j = centrifuge.getEnergyScaled(12);

        if (j > 0) {
            this.drawTexturedModalRect(k + 9, l + 32 + 12 - j, 176, 12 - j, 14, j + 2);
        }
    }

    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
        String namePt1 = I18n.translateToLocal("tile.techreborn.industrialBlock.name");
        String namePt2 = I18n.translateToLocal("tile.techreborn.centrifuge.name").replace(namePt1 + " ", "");
        this.fontRendererObj.drawString(namePt1, 98, 6, 4210752);
        this.fontRendererObj.drawString(namePt2, 98, 14, 4210752);
        this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
        this.fontRendererObj.drawString(centrifuge.getProgressScaled(100) + "%", 98, this.ySize - 96 + 2, 4210752);
    }
}
