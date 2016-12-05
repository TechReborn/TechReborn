package techreborn.manual.pages;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.items.ItemPlates;
import techreborn.manual.PageCollection;
import techreborn.manual.Reference;
import techreborn.manual.util.GuiButtonItemTexture;

import java.awt.*;

public class ContentsPage extends TitledPage {
	public ContentsPage(String name, PageCollection collection) {
		super(name, false, collection, Reference.CONTENTS_KEY, Color.white.getRGB());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		buttonList.clear();
		buttonList.add(new GuiButtonItemTexture(0, getXMin() + 20, getYMin() + 20, 0, 46, 100, 20,
			ItemPlates.getPlateByName("iron"), Reference.pageNames.GETTINGSTARTED_PAGE,
			ttl(Reference.GETTINGSTARTED_KEY)));
		buttonList.add(new GuiButtonItemTexture(1, getXMin() + 20, getYMin() + 40, 0, 46, 100, 20,
			new ItemStack(ModBlocks.generator), Reference.pageNames.GENERATINGPOWER_PAGE,
			ttl(Reference.GENERATINGPOWER_KEY)));
		buttonList.add(new GuiButtonItemTexture(2, getXMin() + 20, getYMin() + 60, 0, 46, 100, 20,
			new ItemStack(ModBlocks.electricFurnace), Reference.pageNames.BASICMACHINES_PAGE,
			ttl(Reference.BASICMACHINES_KEY)));
		buttonList.add(new GuiButtonItemTexture(3, getXMin() + 20, getYMin() + 80, 0, 46, 100, 20,
			new ItemStack(ModBlocks.blastFurnace), Reference.pageNames.ADVANCEDMACHINES_PAGE,
			ttl(Reference.ADVANCEDMACHINES_KEY)));
		buttonList.add(new GuiButtonItemTexture(4, getXMin() + 20, getYMin() + 100, 0, 46, 100, 20,
			new ItemStack(ModItems.ironDrill), Reference.pageNames.TOOLS_PAGE, ttl(Reference.TOOLS_KEY)));
	}

	@Override
	public void renderBackgroundLayer(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {
		super.renderBackgroundLayer(minecraft, offsetX, offsetY, mouseX, mouseY);
	}

	@Override
	public void actionPerformed(GuiButton button) {
		if (button.id == 0)
			collection.changeActivePage(Reference.pageNames.GETTINGSTARTED_PAGE);
		if (button.id == 1)
			collection.changeActivePage(Reference.pageNames.GENERATINGPOWER_PAGE);
		if (button.id == 2)
			collection.changeActivePage(Reference.pageNames.BASICMACHINES_PAGE);
		if (button.id == 3)
			collection.changeActivePage(Reference.pageNames.ADVANCEDMACHINES_PAGE);
		if (button.id == 4)
			collection.changeActivePage(Reference.pageNames.TOOLS_PAGE);
	}
}
