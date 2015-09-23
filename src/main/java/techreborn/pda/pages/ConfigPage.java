package techreborn.pda.pages;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import techreborn.config.TechRebornConfigGui;
import techreborn.init.ModItems;
import techreborn.pda.PageCollection;
import techreborn.pda.util.GuiButtonCustomTexture;

public class ConfigPage extends TitledPage {
	public int GUI_ID = 0;
	public float scale = 0F;
    private GuiButton plusOneButton;
    private GuiButton minusOneButton;
    private GuiButton plusOneButtonScale;
    private GuiButton minusOneButtonScale;

	public ConfigPage(String name, PageCollection collection, String unlocalizedTitle) {
		super(name, true, collection, "techreborn.pda.pdaconfig", 518915);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		buttonList.clear();
        plusOneButton = new GuiButton(0, getXMin() + 85, getYMin() + 19, 10, 10, "+");
        minusOneButton = new GuiButton(1, getXMin() + 110, getYMin() + 19, 10, 10, "-");
        plusOneButtonScale = new GuiButton(2, getXMin() + 85, getYMin() + 39, 10, 10, "+");
        minusOneButtonScale = new GuiButton(3, getXMin() + 110, getYMin() + 39, 10, 10, "-");
        
        buttonList.add(plusOneButton);
        buttonList.add(minusOneButton);
        buttonList.add(plusOneButtonScale);
        buttonList.add(minusOneButtonScale);
	}
	
	@Override
	public void renderOverlayComponents(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {
		this.fontRendererObj.drawString("GUI_ID", getXMin() + 20, getYMin() + 20, 518915);
		this.fontRendererObj.drawString(GUI_ID + "", getXMin() + 100, getYMin() + 20, 518915);
		this.fontRendererObj.drawString("GUI_SCALE", getXMin() + 20, getYMin() + 40, 518915);
		this.fontRendererObj.drawString(scale + "", getXMin() + 100, getYMin() + 40, 518915);
	}
	
	@Override
	public void actionPerformed(GuiButton button) {
		if (button.id == 0) GUI_ID++;
		if (button.id == 1 && GUI_ID != 0) GUI_ID--;
		if (button.id == 2) scale++;
		if (button.id == 3 && scale != 0) scale--;
	}
}
