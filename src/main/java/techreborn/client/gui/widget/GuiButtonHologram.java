package techreborn.client.gui.widget;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.math.BlockPos;
import reborncore.client.multiblock.Multiblock;
import techreborn.client.gui.GuiBase;

/**
 * Created by Prospector
 */
public class GuiButtonHologram extends GuiButton {

	GuiBase.Layer layer;
	GuiBase gui;

	public GuiButtonHologram(int buttonId, int x, int y, GuiBase gui, GuiBase.Layer layer) {
		super(buttonId, x, y, 20, 12, "");
		this.layer = layer;
		this.gui = gui;
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {

		if (layer == GuiBase.Layer.FOREGROUND) {
			mouseX -= gui.getGuiLeft();
			mouseY -= gui.getGuiTop();
		}

		if (this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height) {
			return true;
		}
		return false;
	}

	public void addComponent(final int x, final int y, final int z, final IBlockState blockState, final Multiblock multiblock) {
		multiblock.addComponent(new BlockPos(x, y, z), blockState);
	}

	@Override
	public void drawButtonForegroundLayer(int mouseX, int mouseY) {

	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {

	}
}
