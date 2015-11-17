package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import reborncore.client.gui.GuiUtil;
import techreborn.client.container.ContainerVacuumFreezer;
import techreborn.tiles.TileVacuumFreezer;

public class GuiVacuumFreezer extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/vacuum_freezer.png");

    TileVacuumFreezer crafter;
    ContainerVacuumFreezer containerVacuumFreezer;

    public GuiVacuumFreezer(EntityPlayer player, TileVacuumFreezer tilealloysmelter) {
        super(new ContainerVacuumFreezer(tilealloysmelter, player));
        this.xSize = 176;
        this.ySize = 167;
        crafter = tilealloysmelter;
        this.containerVacuumFreezer = (ContainerVacuumFreezer) this.inventorySlots;
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

        j = crafter.getProgressScaled(24);
        if (j > 0) {
            this.drawTexturedModalRect(k + 79, l + 34, 176, 14, j + 1, 16);
        }

        j = crafter.getEnergyScaled(12);
        if (j > 0) {
            this.drawTexturedModalRect(k + 26, l + 36 + 12 - j, 176, 12 - j, 14, j + 2);
        }

        if (containerVacuumFreezer.machineStatus == 0) {
            GuiUtil.drawTooltipBox(k + 30, l + 50 + 12 - 0, 114, 10);
            this.fontRendererObj.drawString(StatCollector.translateToLocal("techreborn.message.missingmultiblock"), k + 38, l + 52 + 12 - 0, -1);
        }
    }

    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
        String name = StatCollector.translateToLocal("tile.techreborn.alloysmelter.name");
        this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }
}
