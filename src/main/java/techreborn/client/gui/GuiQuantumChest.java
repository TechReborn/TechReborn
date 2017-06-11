package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import techreborn.client.container.ContainerQuantumChest;
import techreborn.tiles.TileQuantumChest;
import org.lwjgl.opengl.GL11;

public class GuiQuantumChest extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation(
            "techreborn", "textures/gui/thermal_generator.png");

    TileQuantumChest tile;

    public GuiQuantumChest(EntityPlayer player, TileQuantumChest tile) {
        super(new ContainerQuantumChest(tile, player));
        this.xSize = 176;
        this.ySize = 167;
        this.tile = tile;
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
        String name = StatCollector.translateToLocal("tile.techreborn.quantumChest.name");
        this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
        this.fontRendererObj.drawString(
                I18n.format("container.inventory", new Object[0]), 8,
                this.ySize - 96 + 2, 4210752);
        this.fontRendererObj.drawString("Amount", 10, 20, 16448255);
        if (tile.storedItem != null && tile.getStackInSlot(1) != null)
            this.fontRendererObj.drawString(tile.storedItem.stackSize + tile.getStackInSlot(1).stackSize + "", 10, 30, 16448255);
       	if (tile.storedItem == null && tile.getStackInSlot(1) != null)
        	this.fontRendererObj.drawString(tile.getStackInSlot(1).stackSize + "", 10, 30, 16448255);
    }
}
