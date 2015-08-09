package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import techreborn.client.container.ContainerPlateCuttingMachine;
import techreborn.tiles.TilePlateCuttingMachine;

public class GuiPlateCuttingMachine extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/plate_cutting_machine.png");

    TilePlateCuttingMachine platecuttingmachine;

    ContainerPlateCuttingMachine containerPlateCuttingMachine;

    public GuiPlateCuttingMachine(EntityPlayer player, TilePlateCuttingMachine tileplatecuttingmachine) {
        super(new ContainerPlateCuttingMachine(tileplatecuttingmachine, player));
        containerPlateCuttingMachine = (ContainerPlateCuttingMachine) this.inventorySlots;
        this.xSize = 176;
        this.ySize = 167;
        platecuttingmachine = tileplatecuttingmachine;
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

        j = platecuttingmachine.getProgressScaled(20);
        if (j > 0) {
            this.drawTexturedModalRect(k + 83, l + 34, 176, 14, j, 16);
        }

        j = platecuttingmachine.getEnergyScaled(12);
        if (j > 0) {
            this.drawTexturedModalRect(k + 56, l + 36 + 12 - j, 176, 12 - j, 14, j + 2);
        }
    }

    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
        String name = StatCollector.translateToLocal("tile.techreborn.platecuttingmachine.name");
        this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }
}
