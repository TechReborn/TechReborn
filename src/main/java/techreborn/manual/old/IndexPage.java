package techreborn.manual.old;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import techreborn.manual.PageCollection;
import techreborn.manual.pages.BasePage;
import techreborn.manual.pages.TitledPage;
import techreborn.manual.util.GuiButtonTextOnly;

public class IndexPage extends TitledPage {

    public IndexPage(String name, PageCollection collection) {
        super(name, false, collection, "techreborn.pda.index", 518915);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        buttonList.clear();
        int row = 0;
        int collum = 0;
        for (BasePage page : collection.pages) {
            if (page.hasIndexButton) {
                String indexName = page.INDEX_NAME;
                if (indexName == null && page instanceof CraftingInfoPage)
                    indexName = ttl(((CraftingInfoPage) page).result.getUnlocalizedName() + ".name");
                else if (indexName == null) indexName = page.getReferenceName();
                int colour = 0000000;

                buttonList.add(new GuiButtonTextOnly(999, getXMin() + 5 + collum * 81, getYMin() + 20 + (row * 7), 82, 7, indexName, page.getReferenceName(), 6666666));
                row++;
                if (row > 21) {
                    row = 0;
                    collum++;
                }
            }
        }
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button instanceof GuiButtonTextOnly)
            collection.changeActivePage(((GuiButtonTextOnly) button).LINKED_PAGE);
        if (button.id == 0) collection.changeActivePage("CONTENTS");
    }

    @Override
    public void renderBackgroundLayer(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {
        super.renderBackgroundLayer(minecraft, offsetX, offsetY, mouseX, mouseY);
    }

    @Override
    public void drawScreen(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {
        super.drawScreen(minecraft, offsetX, offsetY, mouseX, mouseY);
        for (int k = 0; k < this.buttonList.size(); ++k) {
            if (buttonList.get(k) instanceof GuiButtonTextOnly && ((GuiButtonTextOnly) buttonList.get(k)).getIsHovering()) {
                ((GuiButtonTextOnly) this.buttonList.get(k)).drawButton(this.mc, mouseX + offsetX, mouseY + offsetY);
            }
        }
        buttonList.add(new GuiButton(0, offsetX + 20, offsetY + 180, ttl("techreborn.pda.backbutton")));
    }
}
