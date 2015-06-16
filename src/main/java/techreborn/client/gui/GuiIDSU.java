package techreborn.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import techreborn.client.container.ContainerIDSU;
import techreborn.tiles.iesu.TileIDSU;

import java.awt.*;
import java.util.ArrayList;

public class GuiIDSU extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation(
			"techreborn", "textures/gui/aesu.png");

	TileIDSU idsu;

	ContainerIDSU containerIDSU;

	public ArrayList<String> names = new ArrayList<String>();

	int scrollpos = 0;

	int listSize = 5;

	public GuiIDSU(EntityPlayer player,
				   TileIDSU tileIDSU)
	{
		super(new ContainerIDSU(tileIDSU, player));
		this.xSize = 156;
		this.ySize = 200;
		idsu = tileIDSU;
		this.containerIDSU  = (ContainerIDSU) this.inventorySlots;

		for (int i = 0; i < 15; i++) {
			names.add(i + " name");
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.clear();
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.buttonList.add(new GuiButton(0, k + 96, l + 8, 18, 20, "++"));
		this.buttonList.add(new GuiButton(1, k + 96, l + 8 + 22, 18, 20, "+"));
		this.buttonList.add(new GuiButton(2, k + 96, l + 8 + (22*2), 18, 20, "-"));
		this.buttonList.add(new GuiButton(3, k + 96, l + 8 + (22*3), 18, 20, "--"));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
												   int p_146976_2_, int p_146976_3_)
	{
		this.mc.getTextureManager().bindTexture(texture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}

	protected void drawGuiContainerForegroundLayer(int p_146979_1_,
												   int p_146979_2_)
	{
		this.fontRendererObj.drawString(StatCollector.translateToLocal("tile.techreborn.aesu.name"), 40, 10, Color.WHITE.getRGB());

	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		int listX = k + 20;
		int listY = l + 20;

		{
			drawColourOnScreen(Color.lightGray.getRGB(), 255,  listX - 2, listY -2 , 54, listSize * 10 + 12, 0);
			for (int i = scrollpos; i < listSize; i++) {
				if(names.size() >= i){
					drawColourOnScreen(Color.gray.getRGB(), 255,  listX, listY + (12 * i), 50, 10, 0);
					fontRendererObj.drawString(names.get(i), listX + 1, listY + (12 * i), Color.WHITE.getRGB());
				}
			}
		}

//		int mouseScroll = Mouse.getDWheel();
//		System.out.println(mouseScroll);
//		if(mouseScroll > 0){
//			if(scrollpos + mouseScroll < names.size()){
//				scrollpos += Mouse.getDWheel();
//			}
//		} else {
//			if(!(scrollpos + mouseScroll < 0)){
//				scrollpos -= Mouse.getDWheel();
//			}
//		}
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);

	}

	public static void drawColourOnScreen(int colour, int alpha, double posX, double posY, double width, double height, double zLevel) {
		int r = (colour >> 16 & 0xff);
		int g = (colour >> 8 & 0xff);
		int b = (colour & 0xff);
		drawColourOnScreen(r, g, b, alpha, posX, posY, width, height, zLevel);
	}

	public static void drawColourOnScreen(int r, int g, int b, int alpha, double posX, double posY, double width, double height, double zLevel) {
		if (width <= 0 || height <= 0) {
			return;
		}
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA(r, g, b, alpha);
		tessellator.addVertex(posX, posY + height, zLevel);
		tessellator.addVertex(posX + width, posY + height, zLevel);
		tessellator.addVertex(posX + width, posY, zLevel);
		tessellator.addVertex(posX, posY, zLevel);
		tessellator.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}