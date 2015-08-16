package techreborn.pda.pages;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.lib.ModInfo;
import techreborn.pda.PageCollection;
import techreborn.pda.util.GuiButtonCustomTexture;
import techreborn.pda.util.GuiButtonTextOnly;

public class ContentsPage extends TitledPage{

	public ContentsPage(String name, PageCollection collection) {
		super(name, false, collection, "techreborn.pda.contents", 518915);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		buttonList.clear();
		buttonList.add(new GuiButton(0, getXMin() + 25, getYMin() + 20, "ITEMS"));
		buttonList.add(new GuiButton(1, getXMin() + 25, getYMin() + 40, "BLOCKS"));
		buttonList.add(new GuiButton(2, getXMin() + 25, getYMin() + 160, "INDEX"));
		buttonList.add(new GuiButtonCustomTexture(3, getXMin() + 25, getYMin() + 60, 0, 46, 60, 20, new ItemStack(ModBlocks.Aesu), "INDEX"));
	}

	@Override
	public void actionPerformed(GuiButton button) {
		if (button.id == 0)collection.changeActivePage("ITEMS");
		if (button.id == 1)collection.changeActivePage("BLOCKS");
		if (button.id == 2)collection.changeActivePage("INDEX");
	}

	@Override
	public void renderBackgroundLayer(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {
		super.renderBackgroundLayer(minecraft, offsetX, offsetY, mouseX, mouseY);
	}

	@Override
	public void drawScreen(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {
		super.drawScreen(minecraft, offsetX, offsetY, mouseX, mouseY);
		for (int k = 0; k < this.buttonList.size(); ++k){
			if (buttonList.get(k) instanceof GuiButtonTextOnly && ((GuiButtonTextOnly) buttonList.get(k)).getIsHovering()) {
				((GuiButtonTextOnly) this.buttonList.get(k)).drawButton(this.mc, mouseX + offsetX, mouseY + offsetY);
			}
		}
		drawCenteredString(minecraft.fontRenderer, ttl(ModInfo.MOD_NAME + "   " + ModInfo.MOD_VERSION), offsetX + 128, offsetY + 185, 848465);
	}

}
