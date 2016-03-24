package techreborn.manual.pages;

import java.awt.*;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import techreborn.init.ModBlocks;
import techreborn.manual.PageCollection;
import techreborn.manual.Reference;
import techreborn.manual.util.ButtonUtil;
import techreborn.manual.util.GuiButtonItemTexture;

public class BasicMachinesPage extends TitledPage
{
	public BasicMachinesPage(String name, PageCollection collection)
	{
		super(name, false, collection, Reference.BASICMACHINES_KEY, Color.white.getRGB());
	}

	@Override
	public void initGui()
	{
		buttonList.clear();
		ButtonUtil.addBackButton(0, width / 2 - 60, height / 2 + 64, buttonList);
		buttonList.add(new GuiButtonItemTexture(1, getXMin() + 20, getYMin() + 20, 0, 46, 100, 20,
				new ItemStack(ModBlocks.Grinder), ModBlocks.Grinder.getUnlocalizedName(),
				ttl(ModBlocks.Grinder.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(2, getXMin() + 20, getYMin() + 40, 0, 46, 100, 20,
				new ItemStack(ModBlocks.ElectricFurnace), ModBlocks.ElectricFurnace.getUnlocalizedName(),
				ttl(ModBlocks.ElectricFurnace.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(3, getXMin() + 20, getYMin() + 60, 0, 46, 100, 20,
				new ItemStack(ModBlocks.AlloySmelter), ModBlocks.AlloySmelter.getUnlocalizedName(),
				ttl(ModBlocks.AlloySmelter.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(4, getXMin() + 20, getYMin() + 80, 0, 46, 100, 20,
				new ItemStack(ModBlocks.Extractor), ModBlocks.Extractor.getUnlocalizedName(),
				ttl(ModBlocks.Extractor.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(5, getXMin() + 20, getYMin() + 100, 0, 46, 100, 20,
				new ItemStack(ModBlocks.Compressor), ModBlocks.Compressor.getUnlocalizedName(),
				ttl(ModBlocks.Compressor.getLocalizedName())));
	}

	@Override
	public void actionPerformed(GuiButton button)
	{
		if (button.id == 0)
			collection.changeActivePage(Reference.pageNames.CONTENTS_PAGE);
		if (button.id == 1)
			collection.changeActivePage(ModBlocks.Grinder.getLocalizedName());
		if (button.id == 2)
			collection.changeActivePage(ModBlocks.ElectricFurnace.getLocalizedName());
		if (button.id == 3)
			collection.changeActivePage(ModBlocks.AlloySmelter.getLocalizedName());
		if (button.id == 4)
			collection.changeActivePage(ModBlocks.Extractor.getLocalizedName());
		if (button.id == 5)
			collection.changeActivePage(ModBlocks.Compressor.getLocalizedName());
	}
}
