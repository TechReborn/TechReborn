package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import reborncore.common.util.ItemUtils;

import techreborn.client.container.builder.ContainerBuilder;
import techreborn.tiles.TileQuantumChest;

public class GuiQuantumChest extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation("techreborn",
			"textures/gui/thermal_generator.png");

	TileQuantumChest tile;

	public GuiQuantumChest(final EntityPlayer player, final TileQuantumChest tile) {
		super(new ContainerBuilder("quantumchest").player(player.inventory).inventory(8, 64).hotbar(8, 152)
				.addInventory().tile(tile).slot(0, 80, 17).outputSlot(1, 80, 53).fakeSlot(2, 59, 42).addInventory()
				.create());
		this.xSize = 176;
		this.ySize = 167;
		this.tile = tile;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float p_146976_1_, final int p_146976_2_, final int p_146976_3_) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiQuantumChest.texture);
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int p_146979_1_, final int p_146979_2_) {
		final String name = I18n.translateToLocal("tile.techreborn.quantumChest.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6,
				4210752);
		this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory", new Object[0]), 8,
				this.ySize - 96 + 2, 4210752);
		this.fontRendererObj.drawString("Amount", 10, 20, 16448255);

		int count = 0;
		if (this.tile.storedItem != ItemStack.EMPTY || !this.tile.getStackInSlot(1).isEmpty()) {

			count = this.tile.storedItem.getCount();
			if (this.tile.getStackInSlot(1) != ItemStack.EMPTY && (this.tile.storedItem.isEmpty()
					|| ItemUtils.isItemEqual(this.tile.getStackInSlot(1), this.tile.getStackInSlot(2), true, true)))
				count += this.tile.getStackInSlot(1).getCount();
		}
		this.fontRendererObj.drawString(count + "", 10, 30, 16448255);
	}
}
