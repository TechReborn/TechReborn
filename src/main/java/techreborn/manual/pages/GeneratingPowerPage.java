package techreborn.manual.pages;

import java.awt.*;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import techreborn.init.ModBlocks;
import techreborn.manual.PageCollection;
import techreborn.manual.Reference;
import techreborn.manual.util.ButtonUtil;
import techreborn.manual.util.GuiButtonItemTexture;

public class GeneratingPowerPage extends TitledPage
{
	public GeneratingPowerPage(String name, PageCollection collection)
	{
		super(name, false, collection, Reference.GENERATINGPOWER_KEY, Color.white.getRGB());
	}

	@Override
	public void initGui()
	{
		buttonList.clear();
		ButtonUtil.addBackButton(0, width / 2 - 60, height / 2 + 64, buttonList);
		buttonList.add(new GuiButtonItemTexture(1, getXMin() + 20, getYMin() + 20, 0, 46, 100, 20,
				new ItemStack(ModBlocks.Generator), ModBlocks.Generator.getUnlocalizedName(),
				ttl(ModBlocks.Generator.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(2, getXMin() + 20, getYMin() + 40, 0, 46, 100, 20,
				new ItemStack(ModBlocks.thermalGenerator), ModBlocks.thermalGenerator.getUnlocalizedName(),
				ttl(ModBlocks.thermalGenerator.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(3, getXMin() + 20, getYMin() + 60, 0, 46, 100, 20,
				new ItemStack(ModBlocks.solarPanel), ModBlocks.solarPanel.getUnlocalizedName(),
				ttl(ModBlocks.solarPanel.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(5, getXMin() + 20, getYMin() + 100, 0, 46, 100, 20,
				new ItemStack(ModBlocks.LightningRod), ModBlocks.LightningRod.getUnlocalizedName(),
				ttl(ModBlocks.LightningRod.getLocalizedName())));
	}

	@Override
	public void actionPerformed(GuiButton button)
	{
		if (button.id == 0)
			collection.changeActivePage(Reference.pageNames.CONTENTS_PAGE);
		if (button.id == 1)
			collection.changeActivePage(ModBlocks.Generator.getLocalizedName());
		if (button.id == 2)
			collection.changeActivePage(ModBlocks.thermalGenerator.getLocalizedName());
		if (button.id == 3)
			collection.changeActivePage(ModBlocks.solarPanel.getLocalizedName());
		if (button.id == 4)
			collection.changeActivePage(ModBlocks.LightningRod.getLocalizedName());
	}
}
