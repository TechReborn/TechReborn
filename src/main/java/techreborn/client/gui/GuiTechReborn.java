package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import reborncore.client.guibuilder.GuiBuilder;

/**
 * Created by Gigabit101 on 01/10/2016.
 */
public class GuiTechReborn extends GuiContainer
{
    GuiBuilder builder = new GuiBuilder(GuiBuilder.defaultTextureSheet);

    public GuiTechReborn(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {}

    public void drawBasicMachine(float partialTicks, int mouseX, int mouseY)
    {
        builder.drawDefaultBackground(this, guiLeft, guiTop, xSize , ySize);
        builder.drawPlayerSlots(this, guiLeft + xSize / 2, guiTop + 80, true);
    }
}
