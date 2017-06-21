package techreborn.client.gui.autocrafting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.lwjgl.opengl.GL11;
import reborncore.common.network.NetworkManager;
import techreborn.client.gui.GuiBase;
import techreborn.client.gui.TRBuilder;
import techreborn.packets.PacketSetRecipe;
import techreborn.tiles.TileAutoCraftingTable;

import java.io.IOException;
import java.util.Iterator;

import static net.minecraft.item.ItemStack.EMPTY;

/**
 * Created by modmuss50 on 20/06/2017.
 */
public class GuiAutoCrafting extends GuiBase {

	static final ResourceLocation RECIPE_BOOK_TEXTURE = new ResourceLocation("textures/gui/recipe_book.png");
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
		if (recipe != null) {
			renderItemStack(recipe.getRecipeOutput(), 95, 42);

			int x = mouseX -= getGuiLeft();
			int y = mouseY -= getGuiTop();
			renderRecipe(recipe, 91, 66);
		}
		final Layer layer = Layer.FOREGROUND;
		this.builder.drawMultiEnergyBar(this, 9, 26, (int) this.tileAutoCraftingTable.getEnergy(), (int) this.tileAutoCraftingTable.getMaxPower(), mouseX, mouseY, 0, layer);
		this.builder.drawProgressBar(this, 50, 100, 120, 44, mouseX, mouseY, TRBuilder.ProgressDirection.RIGHT, layer);
	}

	//Based of vanilla code
	public void renderRecipe(IRecipe recipe, int x, int y) {
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.enableAlpha();
		mc.getTextureManager().bindTexture(RECIPE_BOOK_TEXTURE);

		this.drawTexturedModalRect(x, y, 152, 78, 24, 24);

		int recipeWidth = 3;
		int recipeHeight = 3;
		if (recipe instanceof ShapedRecipes) {
			ShapedRecipes shapedrecipes = (ShapedRecipes) recipe;
			recipeWidth = shapedrecipes.getWidth();
			recipeHeight = shapedrecipes.getHeight();
		}
		if (recipe instanceof ShapedOreRecipe) {
			ShapedOreRecipe shapedrecipes = (ShapedOreRecipe) recipe;
			recipeWidth = shapedrecipes.getWidth();
			recipeHeight = shapedrecipes.getHeight();
		}
		Iterator<Ingredient> ingredients = recipe.getIngredients().iterator();
		for (int rHeight = 0; rHeight < recipeHeight; ++rHeight) {
			int j1 = 3 + rHeight * 7;
			for (int rWidth = 0; rWidth < recipeWidth; ++rWidth) {
				if (ingredients.hasNext()) {
					ItemStack[] aitemstack = ingredients.next().getMatchingStacks();
					if (aitemstack.length != 0) {
						int l1 = 3 + rWidth * 7;
						GlStateManager.pushMatrix();
						int i2 = (int) ((float) (x + l1) / 0.42F - 3.0F);
						int j2 = (int) ((float) (y + j1) / 0.42F - 3.0F);
						GlStateManager.scale(0.42F, 0.42F, 1.0F);
						GlStateManager.enableLighting();
						mc.getRenderItem().renderItemAndEffectIntoGUI(aitemstack[0], i2, j2);
						GlStateManager.disableLighting();
						GlStateManager.popMatrix();
					}
				}
			}
		}
		GlStateManager.disableAlpha();
		RenderHelper.disableStandardItemLighting();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int mouseX, final int mouseY) {
		super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
		final Layer layer = Layer.BACKGROUND;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				drawSlot(28 + (i * 18), 25 + (j * 18), layer);
			}
		}
		drawOutputSlot(145, 42, layer);
		drawOutputSlot(95, 42, layer);
		drawString("Inventory", 8, 82, 4210752, layer);
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
			super.mouseClicked(mouseX, mouseY, mouseButton);
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

	public void setRecipe(IRecipe recipe) {
		tileAutoCraftingTable.setCurrentRecipe(recipe.getRegistryName());
		NetworkManager.sendToServer(new PacketSetRecipe(tileAutoCraftingTable, recipe.getRegistryName()));
	}

}
