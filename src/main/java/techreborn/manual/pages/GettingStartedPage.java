package techreborn.manual.pages;

import java.awt.Color;

import net.minecraft.client.gui.GuiButton;
import techreborn.items.ItemParts;
import techreborn.items.ItemPlates;
import techreborn.manual.PageCollection;
import techreborn.manual.Reference;
import techreborn.manual.util.GuiButtonCustomTexture;

public class GettingStartedPage extends TitledPage
{
    public GettingStartedPage(String name, PageCollection collection) 
    {
        super(name, false, collection, Reference.GETTINGSTARTED_KEY, Color.white.getRGB());
    }
    
    @Override
    public void initGui() 
    {
    	buttonList.clear();
        buttonList.add(new GuiButtonCustomTexture(0, getXMin() + 25, getYMin() + 20, 0, 46, 100, 20, ItemParts.getPartByName("rubberSap"),
        	Reference.pageNames.GETTINGRUBBER_PAGE, ttl(Reference.GETTINGRUBBER_KEY)));   
    }
    
    @Override
    public void actionPerformed(GuiButton button) 
    {
        if (button.id == 0) collection.changeActivePage(Reference.pageNames.GETTINGRUBBER_PAGE);
    }
}
