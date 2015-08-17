package techreborn.pda.pages;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import techreborn.init.ModBlocks;
import techreborn.pda.PageCollection;

public class MultiBlockPage extends TitledPage{
	
	public static ResourceLocation test = new ResourceLocation("techreborn:textures/pda/multiblocks/base.png");

	public MultiBlockPage(String name, PageCollection collection, String unlocalizedTitle) {
		super(name, false, collection, unlocalizedTitle, 777777);
	}
	
	@Override
	public void initGui() {
		super.initGui();
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
}
