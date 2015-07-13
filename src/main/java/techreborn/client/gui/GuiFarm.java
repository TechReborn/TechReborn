package techreborn.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import techreborn.client.container.ContainerFarm;

public class GuiFarm extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation(
            "techreborn", "textures/gui/farm.png");

    ContainerFarm containerFarm;

    public GuiFarm(ContainerFarm container) {
        super(container);
        this.xSize = 203;
        this.ySize = 166;
        container = (ContainerFarm) this.inventorySlots;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float arg0, int arg1, int arg2) {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int arg0, int arg1) {
        String name = StatCollector.translateToLocal("tile.techreborn.farm.name");
        fontRendererObj.drawString(name, xSize / 2 - fontRendererObj.getStringWidth(name) / 2 + 68, 5, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 60,
                this.ySize - 96 + 2, 4210752);
        super.drawGuiContainerForegroundLayer(arg0, arg1);
    }
}
