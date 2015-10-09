package techreborn.pda.pages;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.init.Blocks;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import techreborn.client.multiblock.Multiblock;
import techreborn.client.multiblock.MultiblockSet;
import techreborn.init.ModBlocks;
import techreborn.pda.PageCollection;
import techreborn.proxies.ClientProxy;

public class MultiBlockPage extends TitledPage{
	
	public static ResourceLocation test = new ResourceLocation("techreborn:textures/pda/multiblocks/base.png");

	public MultiBlockPage(String name, PageCollection collection, String unlocalizedTitle) {
		super(name, false, collection, unlocalizedTitle, Color.white.getRGB());
	}
	
	@Override
	public void initGui() {
		super.initGui();
		GuiButton button = new GuiButton(212, getXMin() + 30, getYMin() + 140, "Show multiblock in world");
		buttonList.add(button);
		if(ClientProxy.multiblockRenderEvent.currentMultiblock != null){
			button.displayString = "Hide multiblock in world";
		}
	}
	
	@Override
	public void drawBackground(int p_146278_1_) {
		super.drawBackground(p_146278_1_);
	}
	
	@Override
	public void renderOverlayComponents(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {
		super.renderOverlayComponents(minecraft, offsetX, offsetY, mouseX, mouseY);
	}

	@Override
	public void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		if(button.id == 212){
			if(ClientProxy.multiblockRenderEvent.currentMultiblock == null){
				{//This code here makes a basic multiblock and then sets to the selected one.
					Multiblock multiblock = new Multiblock();
					multiblock.addComponent(0, 0, 0, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(1, 0, 0, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(0, 0, 1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(-1, 0, 0, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(0, 0, -1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(-1, 0, -1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(-1, 0, 1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(1, 0, -1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(1, 0, 1, ModBlocks.MachineCasing, 0);
					
					multiblock.addComponent(1, 1, 0, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(0, 1, 1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(-1, 1, 0, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(0, 1, -1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(-1, 1, -1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(-1, 1, 1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(1, 1, -1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(1, 1, 1, ModBlocks.MachineCasing, 0);
					
					multiblock.addComponent(1, 2, 0, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(0, 2, 1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(-1, 2, 0, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(0, 2, -1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(-1, 2, -1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(-1, 2, 1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(1, 2, -1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(1, 2, 1, ModBlocks.MachineCasing, 0);
					
					multiblock.addComponent(1, 3, 0, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(0, 3, 1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(-1, 3, 0, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(0, 3, -1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(-1, 3, -1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(-1, 3, 1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(1, 3, -1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(1, 3, 1, ModBlocks.MachineCasing, 0);
					
					MultiblockSet set = new MultiblockSet(multiblock);
					ClientProxy.multiblockRenderEvent.setMultiblock(set);
				}
				button.displayString = "Hide multiblock in world";
			} else {
				ClientProxy.multiblockRenderEvent.setMultiblock(null);
				button.displayString = "Show multiblock in world";
			}
		}
	}
}
