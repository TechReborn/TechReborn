package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import techreborn.client.container.builder.ContainerBuilder;
import techreborn.tiles.TileAssemblingMachine;

public class GuiAssemblingMachine extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation("techreborn",
			"textures/gui/assembling_machine.png");

	TileAssemblingMachine assemblingmachine;

	public GuiAssemblingMachine(final EntityPlayer player, final TileAssemblingMachine assemblingMachine) {
		super(new ContainerBuilder("assemblingmachine").player(player.inventory).inventory(8, 84).hotbar(8, 142)
				.addInventory().tile(assemblingMachine).slot(0, 47, 17).slot(1, 65, 17).outputSlot(2, 116, 35)
				.energySlot(3, 56, 53).upgradeSlot(4, 152, 8).upgradeSlot(5, 152, 26).upgradeSlot(6, 152, 44)
				.upgradeSlot(7, 152, 62).syncEnergyValue().syncCrafterValue().addInventory().create());
		this.xSize = 176;
		this.ySize = 167;
		this.assemblingmachine = assemblingMachine;
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
		this.mc.getTextureManager().bindTexture(GuiAssemblingMachine.texture);
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		int j = 0;

		j = this.assemblingmachine.getProgressScaled(20);
		if (j > 0) {
			this.drawTexturedModalRect(k + 86, l + 34, 176, 14, j + 1, 16);
		}

		j = this.assemblingmachine.getEnergyScaled(12);
		if (j > 0) {
			this.drawTexturedModalRect(k + 56, l + 36 + 12 - j, 176, 12 - j, 14, j + 2);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int p_146979_1_, final int p_146979_2_) {
		final String name = I18n.translateToLocal("tile.techreborn.assemblinmachine.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6,
				4210752);
		this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory", new Object[0]), 8,
				this.ySize - 96 + 2, 4210752);
	}
}
