package techreborn.pda.pages;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import techreborn.init.ModItems;
import techreborn.pda.PageCollection;
import techreborn.pda.util.GuiButtonAHeight;
import techreborn.pda.util.GuiButtonCustomTexture;
import techreborn.pda.util.GuiButtonTextOnly;

public class ItemsPage extends TitledPage{
	public String PAGE;
	
	public ItemsPage(String name, PageCollection collection, String page) {
		super(name, false, collection, page, Color.white.getRGB());
		PAGE = page;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		buttonList.clear();
		int row = 0;
		int collum = 0;
		for (BasePage page : collection.pages){
			if (page.hasIndexButton){
				String indexName = page.INDEX_NAME;
				if (page.getReferenceName() != null && page.getReferenceName().contains(PAGE)){
					if (indexName==null && page instanceof CraftingInfoPage) indexName = ttl(((CraftingInfoPage)page).result.getUnlocalizedName()+".name");
					else if (indexName==null) indexName = page.getReferenceName();
					int colour = 77777777;					
					buttonList.add(new GuiButtonTextOnly(999, getXMin()+20+collum*120, getYMin()+20+(row*7), 82, 7, indexName, page.getReferenceName(), 6666666));
					row++;
					if (row > 21){
						row = 0;
						collum++;
					}
				}
			}
		}
		buttonList.add(new GuiButton(1, getXMin() + 20, getYMin() + 180, ttl("techreborn.pda.backbutton")));
	}

	@Override
	public void actionPerformed(GuiButton button) {
		if (button instanceof GuiButtonTextOnly)
			collection.changeActivePage(((GuiButtonTextOnly)button).LINKED_PAGE);
		if (button.id == 1) collection.changeActivePage("CONTENTS");
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
	}
}
