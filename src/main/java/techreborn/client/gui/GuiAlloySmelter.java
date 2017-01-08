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
import techreborn.tiles.TileAlloySmelter;

public class GuiAlloySmelter extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation("techreborn",
			"textures/gui/electric_alloy_furnace.png");

	TileAlloySmelter alloysmelter;

	public GuiAlloySmelter(final EntityPlayer player, final TileAlloySmelter tilealloysmelter) {
		super(new ContainerBuilder("alloysmelter").player(player.inventory).inventory(8, 84).hotbar(8, 142)
				.addInventory().tile(tilealloysmelter)
				.filterSlot(0, 47, 17,
						stack -> RecipeHandler.recipeList.stream()
						.anyMatch(recipe -> recipe instanceof AlloySmelterRecipe
								&& ItemUtils.isItemEqual(recipe.getInputs().get(0), stack, true, true, true)))
				.filterSlot(1, 65, 17,
						stack -> RecipeHandler.recipeList.stream()
						.anyMatch(recipe -> recipe instanceof AlloySmelterRecipe
								&& ItemUtils.isItemEqual(recipe.getInputs().get(1), stack, true, true, true)))
				.outputSlot(2, 116, 35).energySlot(3, 56, 53).upgradeSlot(4, 152, 8).upgradeSlot(5, 152, 26)
				.upgradeSlot(6, 152, 44).upgradeSlot(7, 152, 62).syncEnergyValue().syncCrafterValue().addInventory()
				.create());
		this.xSize = 176;
		this.ySize = 167;
		this.alloysmelter = tilealloysmelter;
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
		this.mc.getTextureManager().bindTexture(GuiAlloySmelter.texture);
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		int j = 0;

		j = this.alloysmelter.getProgressScaled(24);
		if (j > 0) {
			this.drawTexturedModalRect(k + 79, l + 34, 176, 14, j + 1, 16);
		}

		j = this.alloysmelter.getEnergyScaled(12);
		if (j > 0) {
			this.drawTexturedModalRect(k + 56, l + 36 + 12 - j, 176, 12 - j, 14, j + 2);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int p_146979_1_, final int p_146979_2_) {
		final String name = I18n.translateToLocal("tile.techreborn.alloysmelter.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6,
				4210752);
		this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory", new Object[0]), 8,
				this.ySize - 96 + 2, 4210752);
	}
}
