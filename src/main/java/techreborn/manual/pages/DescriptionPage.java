package techreborn.manual.pages;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import techreborn.manual.PageCollection;
import techreborn.manual.Reference;
import techreborn.manual.util.ButtonUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DescriptionPage extends TitledPage {
	public boolean hasImage;
	public String secondpage;
	public String imageprefix = "techreborn:textures/manual/screenshots/";
	private String rawDescription;
	private List<String> formattedDescription;
	private float descriptionScale = 0.88f;

	public DescriptionPage(String name, PageCollection collection, boolean hasImage, String secondPage) {
		super(name, false, collection, Reference.GETTINGSTARTED_KEY, Color.white.getRGB());
		this.hasImage = hasImage;
		this.rawDescription = "techreborn.manual." + this.getReferenceName() + ".description";
		this.secondpage = secondPage;
	}

	@Override
	public void renderOverlayComponents(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {
		if (hasImage) {
			renderImage(offsetX, offsetY);
			addDescription(mc, offsetX, offsetY + 60);
		} else
			addDescription(mc, offsetX, offsetY);
	}

	@Override
	public void initGui() {
		buttonList.clear();
		ButtonUtil.addBackButton(0, width / 2 - 60, height / 2 + 64, buttonList);
		if (secondpage != null) {
			ButtonUtil.addNextButton(1, width / 2 + 40, height / 2 + 64, buttonList);
		}
	}

	public void renderImage(int offsetX, int offsetY) {
		TextureManager render = Minecraft.getMinecraft().renderEngine;
		render.bindTexture(new ResourceLocation(imageprefix + this.getReferenceName() + ".png"));

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		drawTexturedModalRect(offsetX, offsetY - 14, 0, 0, 120, this.height);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public void addDescription(Minecraft minecraft, int offsetX, int offsetY) {
		GL11.glPushMatrix();
		GL11.glTranslated(offsetX + 15, offsetY + 40, 1);
		GL11.glScalef(descriptionScale, descriptionScale, descriptionScale);
		int offset = 0;
		for (String s : getFormattedText(fontRendererObj)) {
			if (s == null)
				break;
			if (s.contains("\\%") && s.substring(0, 2).equals("\\%")) {
				s = s.substring(2);
				offset += fontRendererObj.FONT_HEIGHT / 2;
			}
			fontRendererObj.drawString(s, 0, offset, Color.black.getRGB());
			offset += fontRendererObj.FONT_HEIGHT;
		}
		GL11.glPopMatrix();
	}

	@SuppressWarnings("unchecked")
	public List<String> getFormattedText(FontRenderer fr) {
		if (formattedDescription == null) {
			formattedDescription = new ArrayList<>();

			if (Strings.isNullOrEmpty(rawDescription)) {
				formattedDescription = ImmutableList.of();
				return formattedDescription;
			}
			if (!rawDescription.contains("\\n")) {
				formattedDescription = ImmutableList.copyOf(fr.listFormattedStringToWidth(rawDescription, 130));
				return formattedDescription;
			}

			List<String> segments = new ArrayList();
			String raw = rawDescription;

			for (String s : segments)
				formattedDescription.addAll(ImmutableList.copyOf(fr.listFormattedStringToWidth(s, 370)));
		}
		return formattedDescription;
	}

	@Override
	public void actionPerformed(GuiButton button) {
		if (button.id == 0)
			collection.changeActivePage(Reference.pageNames.GETTINGSTARTED_PAGE);

		if (secondpage != null) {
			if (button.id == 1)
				collection.changeActivePage(secondpage);
		}
	}
}
