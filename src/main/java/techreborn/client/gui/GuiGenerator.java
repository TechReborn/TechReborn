package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import techreborn.client.container.ContainerGenerator;
import techreborn.tiles.generator.TileGenerator;

public class GuiGenerator extends GuiContainer {

    public static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/generator.png");

    TileGenerator generator;

    public GuiGenerator(EntityPlayer player, TileGenerator generator) {
        super(new ContainerGenerator(generator, player));
        this.xSize = 176;
        this.ySize = 167;
        this.generator = generator;
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

        int j = 0;

        j = generator.getEnergyScaled(12);
        if (j > 0) {
            this.drawTexturedModalRect(k + 56, l + 36 + 12 - j, 176, 12 - j, 14, j + 2);
        }
    }

    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
        String name = StatCollector.translateToLocal("tile.techreborn.generator.name");
        this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }
}
