package techreborn.client.gui.autocrafting;

import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import techreborn.client.gui.GuiBase;
import techreborn.tiles.TileAutoCraftingTable;

import java.io.IOException;

/**
 * Created by modmuss50 on 20/06/2017.
 */
public class GuiAutoCrafting extends GuiBase {

	GuiAutoCraftingRecipeSlector recipeSlector = new GuiAutoCraftingRecipeSlector();
	private GuiButtonImage recipeButton;
	boolean showGui = true;
	InventoryCrafting dummyInv;

	public GuiAutoCrafting(EntityPlayer player, TileAutoCraftingTable tile) {
		super(player, tile, tile.createContainer(player));
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		recipeSlector.func_193957_d();
	}

	@Override
	public void initGui() {
		super.initGui();
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
			this.recipeSlector.func_191861_a(mouseX, mouseY, partialTicks);
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

}
