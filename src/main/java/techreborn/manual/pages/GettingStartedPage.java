package techreborn.manual.pages;

import net.minecraft.client.gui.GuiButton;
import techreborn.items.ItemParts;
import techreborn.items.ItemPlates;
import techreborn.manual.PageCollection;
import techreborn.manual.Reference;
import techreborn.manual.util.ButtonUtil;
import techreborn.manual.util.GuiButtonItemTexture;

import java.awt.*;

public class GettingStartedPage extends TitledPage {
	public GettingStartedPage(String name, PageCollection collection) {
		super(name, false, collection, Reference.GETTINGSTARTED_KEY, Color.white.getRGB());
	}

	@Override
	public void initGui() {
		buttonList.clear();
		ButtonUtil.addBackButton(0, width / 2 - 60, height / 2 + 64, buttonList);
		buttonList.add(new GuiButtonItemTexture(1, getXMin() + 20, getYMin() + 20, 0, 46, 100, 20,
			ItemParts.getPartByName("rubberSap"), Reference.pageNames.GETTINGRUBBER_PAGE,
			ttl(Reference.GETTINGRUBBER_KEY)));
		buttonList.add(new GuiButtonItemTexture(2, getXMin() + 20, getYMin() + 40, 0, 46, 100, 20,
			ItemPlates.getPlateByName("iron"), Reference.pageNames.CRAFTINGPLATES_PAGE,
			ttl(Reference.CRAFTINGPLATES_KEY)));
	}

	@Override
	public void actionPerformed(GuiButton button) {
		if (button.id == 0)
			collection.changeActivePage(Reference.pageNames.CONTENTS_PAGE);
		if (button.id == 1)
			collection.changeActivePage(Reference.pageNames.GETTINGRUBBER_PAGE);
		if (button.id == 2)
			collection.changeActivePage(Reference.pageNames.CRAFTINGPLATES_PAGE);

	}
}
