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
			new ItemStack(ModBlocks.grinder), ModBlocks.grinder.getUnlocalizedName(),
			ttl(ModBlocks.grinder.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(2, getXMin() + 20, getYMin() + 40, 0, 46, 100, 20,
			new ItemStack(ModBlocks.electricFurnace), ModBlocks.electricFurnace.getUnlocalizedName(),
			ttl(ModBlocks.electricFurnace.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(3, getXMin() + 20, getYMin() + 60, 0, 46, 100, 20,
			new ItemStack(ModBlocks.alloySmelter), ModBlocks.alloySmelter.getUnlocalizedName(),
			ttl(ModBlocks.alloySmelter.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(4, getXMin() + 20, getYMin() + 80, 0, 46, 100, 20,
			new ItemStack(ModBlocks.extractor), ModBlocks.extractor.getUnlocalizedName(),
			ttl(ModBlocks.extractor.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(5, getXMin() + 20, getYMin() + 100, 0, 46, 100, 20,
			new ItemStack(ModBlocks.compressor), ModBlocks.compressor.getUnlocalizedName(),
			ttl(ModBlocks.compressor.getLocalizedName())));
	}

	@Override
	public void actionPerformed(GuiButton button) {
		if (button.id == 0)
			collection.changeActivePage(Reference.pageNames.CONTENTS_PAGE);
		if (button.id == 1)
			collection.changeActivePage(ModBlocks.grinder.getLocalizedName());
		if (button.id == 2)
			collection.changeActivePage(ModBlocks.electricFurnace.getLocalizedName());
		if (button.id == 3)
			collection.changeActivePage(ModBlocks.alloySmelter.getLocalizedName());
		if (button.id == 4)
			collection.changeActivePage(ModBlocks.extractor.getLocalizedName());
		if (button.id == 5)
			collection.changeActivePage(ModBlocks.compressor.getLocalizedName());
	}
}
