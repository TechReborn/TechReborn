package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import reborncore.api.recipe.RecipeHandler;
import reborncore.common.util.ItemUtils;

import techreborn.api.recipe.machines.AlloySmelterRecipe;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.tiles.TileAlloyFurnace;

public class GuiAlloyFurnace extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation("techreborn",
			"textures/gui/alloy_furnace.png");

	TileAlloyFurnace alloyfurnace;

	public GuiAlloyFurnace(final EntityPlayer player, final TileAlloyFurnace tileAlloyFurnace) {
		super(new ContainerBuilder("alloyfurnace").player(player.inventory).inventory(8, 84).hotbar(8, 142)
				.addInventory().tile(tileAlloyFurnace)
				.filterSlot(0, 47, 17,
						stack -> RecipeHandler.recipeList.stream()
						.anyMatch(recipe -> recipe instanceof AlloySmelterRecipe
								&& ItemUtils.isItemEqual(recipe.getInputs().get(0), stack, true, true, true)))
				.filterSlot(1, 65, 17,
						stack -> RecipeHandler.recipeList.stream()
						.anyMatch(recipe -> recipe instanceof AlloySmelterRecipe
								&& ItemUtils.isItemEqual(recipe.getInputs().get(1), stack, true, true, true)))
				.outputSlot(2, 116, 35).fuelSlot(3, 56, 53)
				.syncIntegerValue(tileAlloyFurnace::getBurnTime, tileAlloyFurnace::setBurnTime)
				.syncIntegerValue(tileAlloyFurnace::getCookTime, tileAlloyFurnace::setCookTime)
				.syncIntegerValue(tileAlloyFurnace::getCurrentItemBurnTime, tileAlloyFurnace::setCurrentItemBurnTime)
				.addInventory().create());
		this.xSize = 176;
		this.ySize = 167;
		this.alloyfurnace = tileAlloyFurnace;
	}

	@Override
	public void initGui() {
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		super.initGui();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float p_146976_1_, final int p_146976_2_, final int p_146976_3_) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiAlloyFurnace.texture);
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		if (this.alloyfurnace.isBurning()) {
			int i1 = this.alloyfurnace.getBurnTimeRemainingScaled(13);
			this.drawTexturedModalRect(k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 1);
			i1 = this.alloyfurnace.getCookProgressScaled(24);
			this.drawTexturedModalRect(k + 79, l + 34, 176, 14, i1 + 1, 16);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int p_146979_1_, final int p_146979_2_) {
		final String name = I18n.translateToLocal("tile.techreborn.alloyfurnace.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6,
				4210752);
		this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory", new Object[0]), 8,
				this.ySize - 96 + 2, 4210752);
	}
}
