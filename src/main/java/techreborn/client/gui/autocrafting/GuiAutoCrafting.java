package techreborn.client.gui.autocrafting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import org.lwjgl.opengl.GL11;
import reborncore.common.network.NetworkManager;
import techreborn.client.gui.GuiBase;
import techreborn.packets.PacketSetRecipe;
import techreborn.tiles.TileAutoCraftingTable;

import java.io.IOException;

import static net.minecraft.item.ItemStack.EMPTY;

/**
 * Created by modmuss50 on 20/06/2017.
 */
public class GuiAutoCrafting extends GuiBase {

	GuiAutoCraftingRecipeSlector recipeSlector = new GuiAutoCraftingRecipeSlector();
	private GuiButtonImage recipeButton;
	boolean showGui = true;
	InventoryCrafting dummyInv;
	TileAutoCraftingTable tileAutoCraftingTable;

	public GuiAutoCrafting(EntityPlayer player, TileAutoCraftingTable tile) {
		super(player, tile, tile.createContainer(player));
		this.tileAutoCraftingTable = tile;
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		recipeSlector.func_193957_d();
	}

	public void renderItemStack(ItemStack stack, int x, int y) {
		if (stack != EMPTY) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			RenderHelper.enableGUIStandardItemLighting();

			RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
			itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);

			GL11.glDisable(GL11.GL_LIGHTING);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		IRecipe recipe = tileAutoCraftingTable.getIRecipe();
		if(recipe != null){
			renderItemStack(recipe.getRecipeOutput(), 10, 25);
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		recipeSlector.setGuiAutoCrafting(this);
		dummyInv = new InventoryCrafting(new Container() {
			@Override
			public boolean canInteractWith(EntityPlayer playerIn) {
				return false;
			}
		}, 3, 3);
		for (int i = 0; i < 9; i++) {
			dummyInv.setInventorySlotContents(i, ItemStack.EMPTY);
		}
		this.recipeSlector.func_191856_a(this.width, this.height, this.mc, false, this.inventorySlots, dummyInv);
		this.guiLeft = this.recipeSlector.func_193011_a(false, this.width, this.xSize);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (showGui) {
			this.recipeSlector.func_191861_a(mouseX, mouseY, 0.1F);
			super.drawScreen(mouseX, mouseY, partialTicks);
			this.recipeSlector.func_191864_a(this.guiLeft, this.guiTop, false, partialTicks);
		} else {
			super.drawScreen(mouseX, mouseY, partialTicks);
		}
		this.recipeSlector.func_191876_c(this.guiLeft, this.guiTop, mouseX, mouseY);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (!this.recipeSlector.func_191862_a(mouseX, mouseY, mouseButton)) {
			if (!showGui || !this.recipeSlector.func_191878_b()) {
				super.mouseClicked(mouseX, mouseY, mouseButton);
			}
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (!this.recipeSlector.func_191859_a(typedChar, keyCode)) {
			super.keyTyped(typedChar, keyCode);
		}
	}

	@Override
	protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
		super.handleMouseClick(slotIn, slotId, mouseButton, type);
		this.recipeSlector.func_191874_a(slotIn);
	}

	public void onGuiClosed() {
		this.recipeSlector.func_191871_c();
		super.onGuiClosed();
	}

	public void setRecipe(IRecipe recipe){
		tileAutoCraftingTable.setCurrentRecipe(recipe.getRegistryName());
		NetworkManager.sendToServer(new PacketSetRecipe(tileAutoCraftingTable, recipe.getRegistryName()));
	}


}
