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
import techreborn.cofhLib.gui.GuiBase;
import techreborn.cofhLib.gui.element.ElementListBox;
import techreborn.cofhLib.gui.element.listbox.ListBoxElementText;
import techreborn.tiles.iesu.TileIDSU;

import java.awt.*;
import java.util.ArrayList;

public class GuiIDSU extends GuiBase {


	TileIDSU idsu;

	ContainerIDSU containerIDSU;

	ElementListBox listBox;


	public GuiIDSU(EntityPlayer player,
				   TileIDSU tileIDSU)
	{
		super(new ContainerIDSU(tileIDSU, player));
		this.xSize = 156;
		this.ySize = 200;
		idsu = tileIDSU;
		this.containerIDSU  = (ContainerIDSU) this.inventorySlots;
		texture = new ResourceLocation(
				"techreborn", "textures/gui/aesu.png");
		drawTitle = true;
		drawInventory = true;
		name = StatCollector.translateToLocal("tile.techreborn.aesu.name");
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
		listBox = new ElementListBox(this, k + 20, l + 20, 60, 60);
		for (int i = 0; i < 15; i++) {
			listBox.add(new ListBoxElementText("Name " + i));
		}
		addElement(listBox);
	}

}