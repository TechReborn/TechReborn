package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import techreborn.client.container.builder.ContainerBuilder;
import techreborn.tiles.TileChemicalReactor;

public class GuiChemicalReactor extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation("techreborn",
			"textures/gui/chemical_reactor.png");

	TileChemicalReactor chemicalReactor;

	public GuiChemicalReactor(final EntityPlayer player, final TileChemicalReactor tilechemicalReactor) {
		super(new ContainerBuilder("chemicalreactor").player(player.inventory).inventory(8, 84).hotbar(8, 142)
				.addInventory().tile(tilechemicalReactor).slot(0, 70, 21).slot(1, 90, 21).outputSlot(2, 80, 51)
				.energySlot(6, 8, 51).upgradeSlot(7, 152, 8).upgradeSlot(8, 152, 26).upgradeSlot(9, 152, 44)
				.upgradeSlot(10, 152, 62).syncEnergyValue().syncCrafterValue().addInventory().create());
		this.xSize = 176;
		this.ySize = 167;
		this.chemicalReactor = tilechemicalReactor;
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
		this.mc.getTextureManager().bindTexture(GuiChemicalReactor.texture);
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		int j = 0;

		j = this.chemicalReactor.getProgressScaled(11);
		if (j > 0) {
			this.drawTexturedModalRect(k + 73, l + 39, 177, 15, 30, j);
		}

		j = this.chemicalReactor.getEnergyScaled(12);
		if (j > 0) {
			this.drawTexturedModalRect(k + 9, l + 32 + 12 - j, 176, 12 - j, 14, j + 2);
		}

	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int p_146979_1_, final int p_146979_2_) {
		final String name = I18n.translateToLocal("tile.techreborn.chemicalreactor.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6,
				4210752);
		this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory", new Object[0]), 8,
				this.ySize - 96 + 2, 4210752);
	}
}
