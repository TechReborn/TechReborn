package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import techreborn.client.container.ContainerFusionReactor;
import techreborn.tiles.fusionReactor.TileEntityFusionController;


public class GuiFusionReactor extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/fusion_reactor.png");

    ContainerFusionReactor containerFusionReactor;

    public GuiFusionReactor(EntityPlayer player,
                            TileEntityFusionController tileaesu) {
        super(new ContainerFusionReactor(tileaesu, player));
        containerFusionReactor = (ContainerFusionReactor) this.inventorySlots;
    }

    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
        String name = StatCollector.translateToLocal("tile.techreborn.fusioncontrolcomputer.name");
        this.fontRendererObj.drawString(name, 87, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);

        this.fontRendererObj.drawString("EU: " + containerFusionReactor.energy, 11, 8, 16448255);
        this.fontRendererObj.drawString("Coils: " + (containerFusionReactor.coilStatus == 1 ? "Yes" : "No"), 11, 16, 16448255);
        this.fontRendererObj.drawString("Start EU: " + percentage(containerFusionReactor.neededEU, containerFusionReactor.energy), 11, 24, 16448255);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        this.mc.getTextureManager().bindTexture(texture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        drawTexturedModalRect(k + 88, l + 36, 176, 0, 14, 14);

        //progressBar
        drawTexturedModalRect(k + 111, l + 34, 176, 14, containerFusionReactor.getProgressScaled(), 16);

    }

    public int percentage(int MaxValue, int CurrentValue) {
        if (CurrentValue == 0)
            return 0;
        return (int) ((CurrentValue * 100.0f) / MaxValue);
    }
}
