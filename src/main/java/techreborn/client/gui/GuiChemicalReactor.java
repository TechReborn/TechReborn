package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import techreborn.client.container.ContainerChemicalReactor;
import techreborn.tiles.TileChemicalReactor;

public class GuiChemicalReactor extends GuiContainer {

    public static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/chemical_reactor.png");

    TileChemicalReactor chemicalReactor;
    ContainerChemicalReactor containerChemicalReactor;

    public GuiChemicalReactor(EntityPlayer player, TileChemicalReactor tilechemicalReactor) {
        super(new ContainerChemicalReactor(tilechemicalReactor, player));
        containerChemicalReactor = (ContainerChemicalReactor) this.inventorySlots;
        this.xSize = 176;
        this.ySize = 167;
        chemicalReactor = tilechemicalReactor;
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

        j = chemicalReactor.getProgressScaled(11);
        if (j > 0) {
            this.drawTexturedModalRect(k + 73, l + 39, 177, 15, 30, j);
        }

        j = chemicalReactor.getEnergyScaled(12);
        if (j > 0) {
            this.drawTexturedModalRect(k + 9, l + 32 + 12 - j, 176, 12 - j, 14, j + 2);
        }

    }

    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
        String name = StatCollector.translateToLocal("tile.techreborn.chemicalreactor.name");
        this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }
}
