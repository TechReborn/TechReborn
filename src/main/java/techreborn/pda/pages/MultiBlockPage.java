package techreborn.pda.pages;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.init.Blocks;
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
		super(name, false, collection, unlocalizedTitle, 777777);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		GuiButton button = new GuiButton(212, 10, 10, "Show multiblock in world");
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
		this.drawCenteredString(fontRendererObj, "TODO", offsetX + 120, offsetY + 130, 777777);
	}

	@Override
	public void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		if(button.id == 212){
			if(ClientProxy.multiblockRenderEvent.currentMultiblock == null){
				{//This code here makes a basic multiblock and then sets to the selected one.
					Multiblock multiblock = new Multiblock();
					multiblock.addComponent(0, 0, 0, Blocks.brick_block, 0);
					multiblock.addComponent(1, 0, 0, Blocks.cobblestone, 0);
					multiblock.addComponent(0, 0, 1, Blocks.cobblestone, 0);
					multiblock.addComponent(-1, 0, 0, Blocks.cobblestone, 0);
					multiblock.addComponent(0, 0, -1, Blocks.cobblestone, 0);
					multiblock.addComponent(-1, 0, -1, Blocks.cobblestone, 0);
					multiblock.addComponent(1, 0, 1, Blocks.cobblestone, 0);
					multiblock.addComponent(0, 1, 0, Blocks.diamond_block, 0);
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
