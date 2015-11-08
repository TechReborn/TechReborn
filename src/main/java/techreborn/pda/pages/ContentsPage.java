package techreborn.pda.pages;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import techreborn.config.TechRebornConfigGui;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.pda.PageCollection;
import techreborn.pda.util.GuiButtonCustomTexture;

import java.awt.*;

public class ContentsPage extends TitledPage {

    public ContentsPage(String name, PageCollection collection) {
        super(name, false, collection, "techreborn.pda.contents", Color.white.getRGB());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        buttonList.clear();
        buttonList.add(new GuiButtonCustomTexture(0, getXMin() + 25, getYMin() + 20, 0, 46, 80, 20, new ItemStack(ModBlocks.AlloySmelter), "INDEX", "MACHINES"));
        buttonList.add(new GuiButtonCustomTexture(1, getXMin() + 160, getYMin() + 20, 0, 46, 80, 20, new ItemStack(ModItems.uuMatter), "INDEX", "ITEMS"));
        buttonList.add(new GuiButtonCustomTexture(2, getXMin() + 25, getYMin() + 40, 0, 46, 80, 20, new ItemStack(ModBlocks.DieselGenerator), "INDEX", "POWER GENERATION"));
        buttonList.add(new GuiButtonCustomTexture(3, getXMin() + 160, getYMin() + 40, 0, 46, 80, 20, new ItemStack(ModItems.advancedDrill), "INDEX", "TOOLS"));
        buttonList.add(new GuiButtonCustomTexture(4, getXMin() + 25, getYMin() + 60, 0, 46, 80, 20, new ItemStack(ModBlocks.Aesu), "INDEX", "POWER STORAGE"));
        buttonList.add(new GuiButtonCustomTexture(5, getXMin() + 25, getYMin() + 80, 0, 46, 80, 20, new ItemStack(ModBlocks.MachineCasing), "INDEX", "MULTIBLOCKS"));
        buttonList.add(new GuiButtonCustomTexture(6, getXMin() + 160, getYMin() + 60, 0, 46, 80, 20, new ItemStack(ModItems.upgrades), "INDEX", "UPGRADES"));

        buttonList.add(new GuiButtonCustomTexture(7, getXMin() + 160, getYMin() + 180, 0, 46, 80, 20, new ItemStack(ModItems.lapotronicOrb), "INDEX", "Changelog"));
        buttonList.add(new GuiButtonCustomTexture(8, getXMin() + 25, getYMin() + 180, 0, 46, 80, 20, new ItemStack(ModItems.lapotronicOrb), "INDEX", "Configs"));
    }

    @Override
    public void renderBackgroundLayer(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {
        super.renderBackgroundLayer(minecraft, offsetX, offsetY, mouseX, mouseY);
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button.id == 0) collection.changeActivePage("MACHINES");
        if (button.id == 1) collection.changeActivePage("ITEMS");
        if (button.id == 2) collection.changeActivePage("POWER_GENERATION");
        if (button.id == 3) collection.changeActivePage("TOOLS");
        if (button.id == 4) collection.changeActivePage("POWER_STORAGE");
        if (button.id == 5) collection.changeActivePage("MULTIBLOCKS");
        if (button.id == 6) collection.changeActivePage("UPGRADES");
        if (button.id == 7) collection.changeActivePage("VERSION");
        if (button.id == 8) mc.displayGuiScreen(new TechRebornConfigGui(this));

        if (button.id == 9) collection.changeActivePage("CONFIG");

    }
}
