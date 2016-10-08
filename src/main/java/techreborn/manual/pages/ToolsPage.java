package techreborn.manual.pages;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import techreborn.init.ModItems;
import techreborn.manual.PageCollection;
import techreborn.manual.Reference;
import techreborn.manual.util.ButtonUtil;
import techreborn.manual.util.GuiButtonItemTexture;

import java.awt.*;

public class ToolsPage extends TitledPage {
	public ToolsPage(String name, PageCollection collection) {
		super(name, false, collection, Reference.TOOLS_KEY, Color.white.getRGB());
	}

	@Override
	public void initGui() {
		buttonList.clear();
		ButtonUtil.addBackButton(0, width / 2 - 60, height / 2 + 64, buttonList);
		ButtonUtil.addNextButton(1, width / 2 + 40, height / 2 + 64, buttonList);
		buttonList.add(new GuiButtonItemTexture(2, getXMin() + 20, getYMin() + 20, 0, 46, 100, 20,
			new ItemStack(ModItems.ironDrill), ModItems.ironDrill.getUnlocalizedName(),
			ttl(ModItems.ironDrill.getUnlocalizedName() + ".name")));
		buttonList.add(new GuiButtonItemTexture(3, getXMin() + 20, getYMin() + 40, 0, 46, 100, 20,
			new ItemStack(ModItems.diamondDrill), ModItems.diamondDrill.getUnlocalizedName(),
			ttl(ModItems.diamondDrill.getUnlocalizedName() + ".name")));
		buttonList.add(new GuiButtonItemTexture(4, getXMin() + 20, getYMin() + 60, 0, 46, 100, 20,
			new ItemStack(ModItems.advancedDrill), ModItems.advancedDrill.getUnlocalizedName(),
			ttl(ModItems.advancedDrill.getUnlocalizedName() + ".name")));
		buttonList.add(new GuiButtonItemTexture(5, getXMin() + 20, getYMin() + 80, 0, 46, 100, 20,
			new ItemStack(ModItems.ironChainsaw), ModItems.ironChainsaw.getUnlocalizedName(),
			ttl(ModItems.ironChainsaw.getUnlocalizedName() + ".name")));
		buttonList.add(new GuiButtonItemTexture(6, getXMin() + 20, getYMin() + 100, 0, 46, 100, 20,
			new ItemStack(ModItems.diamondChainsaw), ModItems.diamondChainsaw.getUnlocalizedName(),
			ttl(ModItems.diamondChainsaw.getUnlocalizedName() + ".name")));
		buttonList.add(new GuiButtonItemTexture(7, getXMin() + 20, getYMin() + 120, 0, 46, 100, 20,
			new ItemStack(ModItems.advancedChainsaw), ModItems.advancedChainsaw.getUnlocalizedName(),
			ttl(ModItems.advancedChainsaw.getUnlocalizedName() + ".name")));
	}

	@Override
	public void actionPerformed(GuiButton button) {
		if (button.id == 0)
			collection.changeActivePage(Reference.pageNames.CONTENTS_PAGE);
		if (button.id == 1) {
			buttonList.clear();
			ButtonUtil.addBackButton(0, width / 2 - 60, height / 2 + 64, buttonList);
			buttonList.add(new GuiButtonItemTexture(8, getXMin() + 20, getYMin() + 20, 0, 46, 100, 20,
				new ItemStack(ModItems.omniTool), ModItems.omniTool.getUnlocalizedName(),
				ttl(ModItems.omniTool.getUnlocalizedName() + ".name")));
			buttonList.add(new GuiButtonItemTexture(9, getXMin() + 20, getYMin() + 40, 0, 46, 100, 20,
				new ItemStack(ModItems.treeTap), ModItems.treeTap.getUnlocalizedName(),
				ttl(ModItems.treeTap.getUnlocalizedName() + ".name")));
		}
		if (button.id == 2)
			collection.changeActivePage(ModItems.ironDrill.getUnlocalizedName() + ".name");
		if (button.id == 3)
			collection.changeActivePage(ModItems.diamondDrill.getUnlocalizedName() + ".name");
		if (button.id == 4)
			collection.changeActivePage(ModItems.advancedDrill.getUnlocalizedName() + ".name");
		if (button.id == 5)
			collection.changeActivePage(ModItems.ironChainsaw.getUnlocalizedName() + ".name");
		if (button.id == 6)
			collection.changeActivePage(ModItems.diamondChainsaw.getUnlocalizedName() + ".name");
		if (button.id == 7)
			collection.changeActivePage(ModItems.advancedChainsaw.getUnlocalizedName() + ".name");
		if (button.id == 8)
			collection.changeActivePage(ModItems.omniTool.getUnlocalizedName() + ".name");
		if (button.id == 9)
			collection.changeActivePage(ModItems.treeTap.getUnlocalizedName() + ".name");
	}
}
