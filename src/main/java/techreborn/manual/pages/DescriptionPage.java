package techreborn.manual.pages;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import techreborn.manual.PageCollection;
import techreborn.manual.Reference;

public class DescriptionPage extends TitledPage
{
	public boolean hasImage;
    private String rawDescription;
    private List<String> formattedDescription;
    private float descriptionScale = 0.88f;
	
	public String imageprefix = "techreborn:textures/manual/screenshots/";
	
    public DescriptionPage(String name, PageCollection collection, boolean hasImage) 
    {
        super(name, false, collection, Reference.GETTINGSTARTED_KEY, Color.white.getRGB());
        this.hasImage = hasImage;
        this.rawDescription = "techreborn.manual." + this.getReferenceName() + ".description";
    }
    
    @Override
    public void renderOverlayComponents(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) 
    {
    	if(hasImage)
    	{
    		renderImage(offsetX, offsetY);
    		addDescription(mc, offsetX, offsetY + 60);
    	}
    	else
    		addDescription(mc, offsetX, offsetY);
    }
    
    public void renderImage(int offsetX, int offsetY)
    {
		TextureManager render = Minecraft.getMinecraft().renderEngine;
		render.bindTexture(new ResourceLocation(imageprefix + this.getReferenceName() + ".png"));

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		drawTexturedModalRect(offsetX, offsetY - 14, 0, 0, 120, this.height);
		GL11.glDisable(GL11.GL_BLEND);
    }
    
    public void addDescription(Minecraft minecraft, int offsetX, int offsetY) 
    {
        GL11.glPushMatrix();
        GL11.glTranslated(offsetX + 15, offsetY + 40, 1);
        GL11.glScalef(descriptionScale, descriptionScale, descriptionScale);
        int offset = 0;
        for (String s : getFormattedText(fontRendererObj)) 
        {
            if (s == null) break;
            if (s.contains("\\%") && s.substring(0, 2).equals("\\%")) 
            {
                s = s.substring(2);
                offset += fontRendererObj.FONT_HEIGHT / 2;
            }
            fontRendererObj.drawString(s, 0, offset, Color.black.getRGB());
            offset += fontRendererObj.FONT_HEIGHT;
        }
        GL11.glPopMatrix();
    }
    
    @SuppressWarnings("unchecked")
    public List<String> getFormattedText(FontRenderer fr) 
    {
        if (formattedDescription == null) 
        {
            formattedDescription = new ArrayList<String>();

            if (Strings.isNullOrEmpty(rawDescription)) 
            {
                formattedDescription = ImmutableList.of();
                return formattedDescription;
            }
            if (!rawDescription.contains("\\n")) 
            {
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
}
