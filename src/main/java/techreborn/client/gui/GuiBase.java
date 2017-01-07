package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Prospector
 */
public class GuiBase extends GuiContainer {

	public int xSize = 176;
	public int ySize = 176;
	public TRBuilder builder = new TRBuilder();
	public TileEntity tile;
	public Container container;

	public GuiBase(EntityPlayer player, TileEntity tile, Container container) {
		super(container);
		this.tile = tile;
		this.container = container;
	}

	protected void drawSlotForeground(int x, int y) {
		builder.drawSlot(this, x - 1, y - 1);
	}

	protected void drawSlotBackground(int x, int y) {
		drawSlotForeground(guiLeft + x, guiTop + y);
	}

	protected void drawOutputSlotForeground(int x, int y) {
		builder.drawOutputSlot(this, x - 1, y - 1);
	}

	protected void drawOutputSlotBackground(int x, int y) {
		drawOutputSlotForeground(guiLeft + x, guiTop + y);
	}

	protected void drawSelectedStackForeground(int x, int y) {
		builder.drawSelectedStack(this, x, y);
	}

	protected void drawSelectedStackBackground(int x, int y) {
		drawSelectedStackForeground(guiLeft + x, guiTop + y);
	}

	@Override
	public void initGui() {
		super.initGui();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		builder.drawDefaultBackground(this, guiLeft, guiTop, xSize, ySize);
		builder.drawPlayerSlots(this, guiLeft + xSize / 2, guiTop + 93, true);
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		drawTitle();
	}

	protected void drawTitle() {
		drawCentredString(I18n.translateToLocal(tile.getBlockType().getUnlocalizedName() + ".name"), 6, 4210752);
	}

	protected void drawCentredString(String string, int y, int colour) {
		drawString(string, (xSize / 2 - mc.fontRendererObj.getStringWidth(string) / 2), y, colour);
	}

	protected void drawString(String string, int x, int y, int colour) {
		mc.fontRendererObj.drawString(string, x, y, colour);
		GlStateManager.color(1, 1, 1, 1);
	}

	//TODO
	public enum SlotRender {
		STANDARD, OUTPUT, NONE, SPRITE;

	}
}
