package techreborn.manual.pages;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import techreborn.manual.PageCollection;
import techreborn.manual.Reference;

import java.io.IOException;

public class BasePage extends GuiScreen {

	public static final ResourceLocation PAGE_TEXTURE = new ResourceLocation(
		"techreborn:textures/manual/gui/manual.png");
	private final int xSize = 200;
	private final int ySize = 180;
	// Name Displayed in the index page
	public String INDEX_NAME;
	public boolean hasIndexButton = false;
	protected PageCollection collection;
	// Name used to reference the page
	private String REFERENCE_NAME;

	public BasePage() {
	}

	public BasePage(String referenceName, PageCollection collection) {
		this.REFERENCE_NAME = referenceName;
		this.mc = Minecraft.getMinecraft();
		this.collection = collection;
		initGui();
	}

	public BasePage(String referenceName, boolean showInMenue, PageCollection collection) {
		this(referenceName, collection);
		this.hasIndexButton = showInMenue;
	}

	public int getXMin() {
		return (this.width - xSize) / 2;
	}

	public void setXMin(int x) {
		this.width = x;
	}

	public int getYMin() {
		return (this.height - ySize) / 2;
	}

	public void setYMin(int y) {
		this.height = y;
	}

	// Unlocalized Index Page Name
	public BasePage setIndexName(String unlocalizedName) {
		this.INDEX_NAME = ttl(unlocalizedName);
		return this;
	}

	public String getReferenceName() {
		return REFERENCE_NAME;
	}

	public void setReferenceName(String name) {
		REFERENCE_NAME = name;
	}

	public void renderBackgroundLayer(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {
		minecraft.renderEngine.bindTexture(PAGE_TEXTURE);
		drawTexturedModalRect(offsetX, offsetY, 0, 0, xSize, ySize);
	}

	public void renderOverlayComponents(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {
	}

	public void drawScreen(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {
		super.drawScreen(mouseX + offsetX, mouseY + offsetY, 0);
		renderOverlayComponents(minecraft, offsetX, offsetY, mouseX, mouseY);
	}

	@Override
	public void actionPerformed(GuiButton button) {
		if (button.id == 0)
			collection.changeActivePage(Reference.pageNames.CONTENTS_PAGE);
	}

	@Override
	public void mouseClicked(int par1, int par2, int par3) throws IOException {
		super.mouseClicked(par1, par2, par3);
	}

	// Translate To Local
	public String ttl(String unlocalizedName) {
		return I18n.translateToLocal(unlocalizedName);
	}
}
