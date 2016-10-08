package techreborn.manual.pages;

import net.minecraft.client.Minecraft;
import techreborn.manual.PageCollection;

public class TitledPage extends BasePage {
	public boolean drawTitle = true;
	private String title;
	private int colour;

	public TitledPage(String name, boolean showInMenue, PageCollection collection, String unlocalizedTitle, int colour) {
		super(name, showInMenue, collection);
		this.title = unlocalizedTitle;
		this.colour = colour;
	}

	@Override
	public void renderOverlayComponents(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {
		if (title == null)
			title = INDEX_NAME;
		if (drawTitle)
			drawCenteredString(minecraft.fontRendererObj, ttl(title), offsetX + 70, offsetY + 10, colour);
	}
}
