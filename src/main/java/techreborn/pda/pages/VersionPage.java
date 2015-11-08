package techreborn.pda.pages;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import techreborn.Core;
import techreborn.lib.ModInfo;
import techreborn.pda.PageCollection;

import java.awt.*;
import java.util.ArrayList;

public class VersionPage extends TitledPage {

    public VersionPage(String name, PageCollection collection, String unlocalizedTitle, int colour) {
        super(name, false, collection, unlocalizedTitle, Color.white.getRGB());
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void renderOverlayComponents(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {
        super.renderOverlayComponents(minecraft, offsetX, offsetY, mouseX, mouseY);
        addDescription(mc, offsetX, offsetY);
        addChangelog(mc, offsetX, offsetY);
        addChangelog2(mc, offsetX, offsetY);
    }

    @Override
    public void renderBackgroundLayer(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {
        super.renderBackgroundLayer(minecraft, offsetX, offsetY, mouseX, mouseY);
    }

    public void addDescription(Minecraft minecraft, int offsetX, int offsetY) {
        GL11.glPushMatrix();
        this.drawCenteredString(minecraft.fontRenderer, "INSTALLED VERSION  " + ModInfo.MOD_VERSION, offsetX + 120, offsetY + 20, 7777777);
        this.drawCenteredString(minecraft.fontRenderer, "LATEST VERSION      " + "TODO", offsetX + 120, offsetY + 40, 7777777);
        GL11.glPopMatrix();
    }

    public void addChangelog(Minecraft minecraft, int offsetX, int offsetY) {
        GL11.glPushMatrix();
        this.drawCenteredString(minecraft.fontRenderer, "CHANGELOG", offsetX + 120, getYMin() + 50, 7777777);
        GL11.glPopMatrix();
    }

    public void addChangelog2(Minecraft minecraft, int offsetX, int offsetY) {
        ArrayList<String> changeLog = Core.INSTANCE.versionChecker.getChangeLogSinceCurrentVersion();

        GL11.glPushMatrix();
        GL11.glScalef(0.7F, 0.7F, 0.7F);

        int y = offsetY + 105;
        for (String change : changeLog) {
            drawCenteredString(minecraft.fontRenderer, change, offsetX + 230, y, Color.white.getRGB());
            y += 10;
        }
        GL11.glPopMatrix();
    }
}
