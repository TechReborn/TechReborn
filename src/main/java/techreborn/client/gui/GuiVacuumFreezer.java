package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import techreborn.client.container.ContainerVacuumFreezer;
import techreborn.tiles.multiblock.TileVacuumFreezer;

public class GuiVacuumFreezer extends GuiContainer {

    public static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/vacuum_freezer.png");

    TileVacuumFreezer crafter;

    public GuiVacuumFreezer(EntityPlayer player, TileVacuumFreezer freezer) {
        super(new ContainerVacuumFreezer(freezer, player));
        this.xSize = 176;
        this.ySize = 167;
        crafter = freezer;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(texture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        int j = crafter.getProgressScaled(24);
        if (j > 0) {
            this.drawTexturedModalRect(k + 80, l + 37, 176, 14, j + 1, 16);
        }

        j = (int) (crafter.getEnergy() * 12f / crafter.getMaxPower());
        if (j > 0) {
            this.drawTexturedModalRect(k + 26, l + 36 + 12 - j, 176, 12 - j, 14, j + 2);
        }

        if (!crafter.getMultiBlock()) {
            this.fontRendererObj.drawString(I18n.translateToLocal("techreborn.message.missingmultiblock"), k + 38, l + 52 + 12, -1);
        }


        j = crafter.getEnergyScaled(12);
        if (j > 0) {
            this.drawTexturedModalRect(k + 26, l + 36 + 12 - j, 176, 12 - j, 14, j + 2);
        }
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = I18n.translateToLocal("tile.techreborn.vacuumfreezer.name");
        this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }


}
