package techreborn.manual.pages;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import techreborn.init.ModBlocks;
import techreborn.manual.PageCollection;
import techreborn.manual.Reference;
import techreborn.manual.util.ButtonUtil;
import techreborn.manual.util.GuiButtonItemTexture;

import java.awt.*;

public class AdvancedMachines extends TitledPage {
	public AdvancedMachines(String name, PageCollection collection) {
		super(name, false, collection, Reference.ADVANCEDMACHINES_KEY, Color.white.getRGB());
	}

	@Override
	public void initGui() {
		buttonList.clear();
		ButtonUtil.addBackButton(0, width / 2 - 60, height / 2 + 64, buttonList);
		buttonList.add(new GuiButtonItemTexture(1, getXMin() + 10, getYMin() + 20, 0, 46, 100, 20,
			new ItemStack(ModBlocks.blastFurnace), ModBlocks.blastFurnace.getUnlocalizedName(),
			ttl(ModBlocks.blastFurnace.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(2, getXMin() + 10, getYMin() + 40, 0, 46, 100, 20,
			new ItemStack(ModBlocks.industrialSawmill), ModBlocks.industrialSawmill.getUnlocalizedName(),
			ttl(ModBlocks.industrialSawmill.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(3, getXMin() + 10, getYMin() + 60, 0, 46, 100, 20,
			new ItemStack(ModBlocks.industrialElectrolyzer), ModBlocks.industrialElectrolyzer.getUnlocalizedName(),
			ttl(ModBlocks.industrialElectrolyzer.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(4, getXMin() + 10, getYMin() + 80, 0, 46, 100, 20,
			new ItemStack(ModBlocks.industrialGrinder), ModBlocks.industrialGrinder.getUnlocalizedName(),
			ttl(ModBlocks.industrialGrinder.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(5, getXMin() + 10, getYMin() + 100, 0, 46, 100, 20,
			new ItemStack(ModBlocks.implosionCompressor), ModBlocks.implosionCompressor.getUnlocalizedName(),
			ttl(ModBlocks.implosionCompressor.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(6, getXMin() + 10, getYMin() + 120, 0, 46, 100, 20,
			new ItemStack(ModBlocks.centrifuge), ModBlocks.centrifuge.getUnlocalizedName(),
			ttl(ModBlocks.centrifuge.getLocalizedName())));
	}

	@Override
	public void actionPerformed(GuiButton button) {
		if (button.id == 0)
			collection.changeActivePage(Reference.pageNames.CONTENTS_PAGE);
		if (button.id == 1)
			collection.changeActivePage(ModBlocks.blastFurnace.getLocalizedName());
		if (button.id == 2)
			collection.changeActivePage(ModBlocks.industrialSawmill.getLocalizedName());
		if (button.id == 3)
			collection.changeActivePage(ModBlocks.industrialElectrolyzer.getLocalizedName());
		if (button.id == 4)
			collection.changeActivePage(ModBlocks.industrialGrinder.getLocalizedName());
		if (button.id == 5)
			collection.changeActivePage(ModBlocks.implosionCompressor.getLocalizedName());
		if (button.id == 6)
			collection.changeActivePage(ModBlocks.centrifuge.getLocalizedName());
	}
}
