package techreborn.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import techreborn.client.container.ContainerChunkloader;
import techreborn.tiles.TileChunkLoader;
import org.lwjgl.opengl.GL11;

public class GuiChunkLoader extends GuiContainer {

    private GuiButton plusOneButton;
    private GuiButton plusTenButton;
    private GuiButton minusOneButton;
    private GuiButton minusTenButton;

    private static final ResourceLocation texture = new ResourceLocation(
            "techreborn", "textures/gui/industrial_chunkloader.png");

    TileChunkLoader chunkloader;

    public GuiChunkLoader(EntityPlayer player, TileChunkLoader tilechunkloader) {
        super(new ContainerChunkloader(tilechunkloader, player));
        this.xSize = 176;
        this.ySize = 167;
        chunkloader = tilechunkloader;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.guiLeft = this.width / 2 - this.xSize / 2;
        this.guiTop = this.height / 2 - this.ySize / 2;
        plusOneButton = new GuiButton(0, guiLeft + 5, guiTop + 37, 40, 20, "+1");
        plusTenButton = new GuiButton(0, guiLeft + 45, guiTop + 37, 40, 20, "+10");

        minusOneButton = new GuiButton(0, guiLeft + 90, guiTop + 37, 40, 20, "-1");
        minusTenButton = new GuiButton(0, guiLeft + 130, guiTop + 37, 40, 20, "-10");

        buttonList.add(plusOneButton);
        buttonList.add(plusTenButton);
        buttonList.add(minusOneButton);
        buttonList.add(minusTenButton);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
                                                   int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(texture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }

    protected void drawGuiContainerForegroundLayer(int p_146979_1_,
                                                   int p_146979_2_) {
        String name = StatCollector.translateToLocal("tile.techreborn.chunkloader.name");
        this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
        this.fontRendererObj.drawString(
                I18n.format("container.inventory", new Object[0]), 8,
                this.ySize - 96 + 2, 4210752);
    }

}
