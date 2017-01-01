package techreborn.manual.pages;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import techreborn.init.ModBlocks;
import techreborn.manual.PageCollection;
import techreborn.manual.Reference;
import techreborn.manual.util.ButtonUtil;
import techreborn.manual.util.GuiButtonItemTexture;

import java.awt.*;

public class BasicMachinesPage extends TitledPage {
	public BasicMachinesPage(String name, PageCollection collection) {
		super(name, false, collection, Reference.BASICMACHINES_KEY, Color.white.getRGB());
	}

	@Override
	public void initGui() {
		buttonList.clear();
		ButtonUtil.addBackButton(0, width / 2 - 60, height / 2 + 64, buttonList);
		buttonList.add(new GuiButtonItemTexture(1, getXMin() + 20, getYMin() + 20, 0, 46, 100, 20,
			new ItemStack(ModBlocks.GRINDER), ModBlocks.GRINDER.getUnlocalizedName(),
			ttl(ModBlocks.GRINDER.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(2, getXMin() + 20, getYMin() + 40, 0, 46, 100, 20,
			new ItemStack(ModBlocks.ELECTRIC_FURNACE), ModBlocks.ELECTRIC_FURNACE.getUnlocalizedName(),
			ttl(ModBlocks.ELECTRIC_FURNACE.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(3, getXMin() + 20, getYMin() + 60, 0, 46, 100, 20,
			new ItemStack(ModBlocks.ALLOY_SMELTER), ModBlocks.ALLOY_SMELTER.getUnlocalizedName(),
			ttl(ModBlocks.ALLOY_SMELTER.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(4, getXMin() + 20, getYMin() + 80, 0, 46, 100, 20,
			new ItemStack(ModBlocks.EXTRACTOR), ModBlocks.EXTRACTOR.getUnlocalizedName(),
			ttl(ModBlocks.EXTRACTOR.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(5, getXMin() + 20, getYMin() + 100, 0, 46, 100, 20,
			new ItemStack(ModBlocks.COMPRESSOR), ModBlocks.COMPRESSOR.getUnlocalizedName(),
			ttl(ModBlocks.COMPRESSOR.getLocalizedName())));
	}

	@Override
	public void actionPerformed(GuiButton button) {
		if (button.id == 0)
			collection.changeActivePage(Reference.pageNames.CONTENTS_PAGE);
		if (button.id == 1)
			collection.changeActivePage(ModBlocks.GRINDER.getLocalizedName());
		if (button.id == 2)
			collection.changeActivePage(ModBlocks.ELECTRIC_FURNACE.getLocalizedName());
		if (button.id == 3)
			collection.changeActivePage(ModBlocks.ALLOY_SMELTER.getLocalizedName());
		if (button.id == 4)
			collection.changeActivePage(ModBlocks.EXTRACTOR.getLocalizedName());
		if (button.id == 5)
			collection.changeActivePage(ModBlocks.COMPRESSOR.getLocalizedName());
	}
}
