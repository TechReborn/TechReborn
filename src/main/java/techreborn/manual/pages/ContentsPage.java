package techreborn.manual.pages;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import techreborn.config.TechRebornConfigGui;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.items.ItemPlates;
import techreborn.manual.PageCollection;
import techreborn.manual.Reference;
import techreborn.manual.util.GuiButtonCustomTexture;

public class ContentsPage extends TitledPage 
{
    public ContentsPage(String name, PageCollection collection) 
    {
        super(name, false, collection, Reference.CONTENTS_KEY, Color.white.getRGB());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() 
    {
        buttonList.clear();
        buttonList.add(new GuiButtonCustomTexture(0, getXMin() + 25, getYMin() + 20, 0, 46, 100, 20, ItemPlates.getPlateByName("iron"),
        		Reference.pageNames.GETTINGSTARTED_PAGE, ttl(Reference.GETTINGSTARTED_KEY)));
    }

    @Override
    public void renderBackgroundLayer(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) 
    {
        super.renderBackgroundLayer(minecraft, offsetX, offsetY, mouseX, mouseY);
    }

    @Override
    public void actionPerformed(GuiButton button) 
    {
        if (button.id == 0) collection.changeActivePage(Reference.pageNames.GETTINGSTARTED_PAGE);
    }
}
