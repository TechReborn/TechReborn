package techreborn.manual;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import techreborn.manual.pages.ContentsPage;
import techreborn.manual.pages.DescriptionPage;
import techreborn.manual.pages.GettingStartedPage;

@SideOnly(Side.CLIENT)
public class GuiManual extends GuiScreen 
{
    protected final PageCollection root;
    protected int pageIndex = 0;
    protected int xSize = 0;
    protected int ySize = 0;
    public Container inventorySlots;
    protected int guiLeft;
    protected int guiTop;

    public GuiManual() 
    {
        this.xSize = 146;
        this.ySize = 180;
        root = createRoot();
    }

    protected PageCollection createRoot() 
    {
        pageIndex = 0;
        final PageCollection pageCollection = new PageCollection();
        pageCollection.addPage(new ContentsPage("CONTENTS", pageCollection));
        pageCollection.addPage(new GettingStartedPage(Reference.pageNames.GETTINGSTARTED_PAGE, pageCollection));
        pageCollection.addPage(new DescriptionPage(Reference.pageNames.GETTINGRUBBER_PAGE, pageCollection, true));
        
        

        return pageCollection;
    }

    private int getNextPageIndex() 
    {
        int i = pageIndex;
        pageIndex++;
        return i;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float par3) 
    {
        drawGuiBackgroundLayer(par3, mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, par3);

        prepareRenderState();
        GL11.glPushMatrix();

        root.drawScreen(this.mc, this.guiLeft, this.guiTop, mouseX - this.guiLeft, mouseY - this.guiTop);

        GL11.glPopMatrix();
        restoreRenderState();
    }

    protected void prepareRenderState() 
    {
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    protected void restoreRenderState() 
    {
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
    }

    protected void drawGuiBackgroundLayer(float p_146976_1_, int mouseX, int mouseY) 
    {
        GL11.glPushMatrix();
        GL11.glTranslated(this.guiLeft, this.guiTop, 0);
        root.renderBackgroundLayer(this.mc, 0, 0, mouseX - this.guiLeft, mouseY - this.guiTop);
        GL11.glPopMatrix();
    }

    @Override
    public void setWorldAndResolution(Minecraft minecraft, int x, int y) 
    {
        super.setWorldAndResolution(minecraft, x, y);
        root.setWorldAndResolution(minecraft, x, y);
    }

    @Override
    public void actionPerformed(GuiButton button) 
    {
        root.actionPerformed(button);
    }


    @Override
    public void mouseClicked(int par1, int par2, int par3) throws IOException 
    {
        root.mouseClicked(par1, par2, par3);
    }

    @Override
    public void handleInput() throws IOException 
    {
        super.handleInput();
    }

    @Override
    public void initGui() 
    {
        super.initGui();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
    }

    @Override
    public boolean doesGuiPauseGame() 
    {
        return false;
    }
}
