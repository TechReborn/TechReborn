package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import reborncore.client.gui.GuiUtil;
import techreborn.client.container.ContainerImplosionCompressor;
import techreborn.tiles.TileImplosionCompressor;

public class GuiImplosionCompressor extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/implosion_compressor.png");

    TileImplosionCompressor compresser;
    ContainerImplosionCompressor containerImplosionCompressor;

    public GuiImplosionCompressor(EntityPlayer player, TileImplosionCompressor tilecompresser) {
        super(new ContainerImplosionCompressor(tilecompresser, player));
        containerImplosionCompressor = (ContainerImplosionCompressor) this.inventorySlots;
        this.xSize = 176;
        this.ySize = 167;
        compresser = tilecompresser;
    }

    @Override
    public void initGui() {
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        super.initGui();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        this.mc.getTextureManager().bindTexture(texture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        if (containerImplosionCompressor.multiblockmeta == 0) {
            GuiUtil.drawTooltipBox(k + 30, l + 50 + 12 - 0, 114, 10);
            this.fontRendererObj.drawString(StatCollector.translateToLocal("techreborn.message.missingmultiblock"), k + 38, l + 52 + 12 - 0, -1);
        }
        
        int j = 0;
        this.mc.getTextureManager().bindTexture(texture);
        j = compresser.getProgressScaled(24);
        if (j > 0) {
            this.drawTexturedModalRect(k + 60, l + 37, 176, 14, j + 1, 16);
        }

        j = compresser.getEnergyScaled(12);
        if (j > 0) {
            this.drawTexturedModalRect(k + 14, l + 36 + 12 - j, 176, 12 - j, 14, j + 2);
        }
    }

    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
        String name = StatCollector.translateToLocal("tile.techreborn.implosioncompressor.name");
        this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }

}
