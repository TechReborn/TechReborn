package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import techreborn.client.container.ContainerDieselGenerator;
import techreborn.tiles.TileDieselGenerator;
import org.lwjgl.opengl.GL11;


public class GuiDieselGenerator extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation(
            "techreborn", "textures/gui/thermal_generator.png");

    TileDieselGenerator tile;

    ContainerDieselGenerator containerDieselGenerator;

    public GuiDieselGenerator(EntityPlayer player, TileDieselGenerator tile) {
        super(new ContainerDieselGenerator(tile, player));
        this.xSize = 176;
        this.ySize = 167;
        this.tile = tile;
        containerDieselGenerator = (ContainerDieselGenerator) this.inventorySlots;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
                                                   int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(texture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }

    protected void drawGuiContainerForegroundLayer(int p_146979_1_,
                                                   int p_146979_2_) {
        String name = StatCollector.translateToLocal("tile.techreborn.dieselgenerator.name");
        this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
        this.fontRendererObj.drawString(
                I18n.format("container.inventory", new Object[0]), 8,
                this.ySize - 96 + 2, 4210752);
        this.fontRendererObj.drawString("Liquid Amount", 10, 20, 16448255);
        this.fontRendererObj.drawString(containerDieselGenerator.fluid + "", 10,
                30, 16448255);

        this.fontRendererObj.drawString("EU Amount", 10, 40, 16448255);
        this.fontRendererObj.drawString(containerDieselGenerator.energy + "", 10,
                50, 16448255);
    }
}
