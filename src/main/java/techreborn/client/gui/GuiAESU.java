/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import reborncore.common.powerSystem.PowerSystem;
import techreborn.tiles.storage.TileAdjustableSU;

public class GuiAESU extends GuiBase {

	TileAdjustableSU tile;

	public GuiAESU(final EntityPlayer player, final TileAdjustableSU aesu) {
		super(player, aesu, aesu.createContainer(player));
		this.tile = aesu;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int mouseX, final int mouseY) {
		super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
		final Layer layer = Layer.BACKGROUND;

		this.drawSlot(62, 45, layer);
		this.drawSlot(98, 45, layer);
		this.drawArmourSlots(8, 18, layer);
		this.builder.drawEnergyOutput(this, 169, 61, (int) this.tile.getBaseMaxOutput(), layer);
		this.builder.drawUpDownButtons(this, 121, 79, layer);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		final Layer layer = Layer.FOREGROUND;
		
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.6, 0.6, 1);
		this.drawCentredString(PowerSystem.getLocaliszedPowerFormattedNoSuffix((int) this.tile.getEnergy()) + "/"
				+ PowerSystem.getLocaliszedPowerFormattedNoSuffix((int) this.tile.getMaxPower()) + " "
				+ PowerSystem.getDisplayPower().abbreviation, 35, 0, 58, layer);
		GlStateManager.popMatrix();
	
		this.builder.drawMultiEnergyBar(this, 81, 28, (int) this.tile.getEnergy(), (int) this.tile.getMaxPower(), mouseX, mouseY, 0, layer);
	}	
	
}

//	private static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/aesu.png");
//
//	TileAdjustableSU aesu;
//
//	ContainerAESU containerAesu;
//
//	public GuiAESU(EntityPlayer player, TileAdjustableSU tileaesu) {
//		super(new ContainerAESU(tileaesu, player));
//		this.xSize = 176;
//		this.ySize = 197;
//		aesu = tileaesu;
//		this.containerAesu = (ContainerAESU) this.inventorySlots;
//	}
//
//	@Override
//	public void initGui() {
//		super.initGui();
//		this.buttonList.clear();
//		int k = (this.width - this.xSize) / 2;
//		int l = (this.height - this.ySize) / 2;
//		this.buttonList.add(new GuiButton(0, k + 115, l + 5, 15, 20, "++"));
//		this.buttonList.add(new GuiButton(1, k + 115, l + 5 + 20, 15, 20, "+"));
//		this.buttonList.add(new GuiButton(2, k + 115, l + 5 + (20 * 2), 15, 20, "-"));
//		this.buttonList.add(new GuiButton(3, k + 115, l + 5 + (20 * 3), 15, 20, "--"));
//	}
//
//	@Override
//	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
//		this.drawDefaultBackground();
//		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//		this.mc.getTextureManager().bindTexture(texture);
//		int k = (this.width - this.xSize) / 2;
//		int l = (this.height - this.ySize) / 2;
//		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
//	}
//
//	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
//		this.fontRenderer.drawString(I18n.format("tile.techreborn:adjustable_su.name"), 40, 10,
//			Color.WHITE.getRGB());
//		this.fontRenderer.drawString(PowerSystem.getLocaliszedPower(containerAesu.euOut) + " /tick", 10, 20,
//			Color.WHITE.getRGB());
//		this.fontRenderer.drawString(PowerSystem.getLocaliszedPower(containerAesu.storedEu) + " ", 10, 30,
//			Color.WHITE.getRGB());
//		this.fontRenderer.drawString(PowerSystem.getLocaliszedPower(containerAesu.euChange) + " change", 10, 40,
//			Color.WHITE.getRGB());
//	}
//
//	@Override
//	protected void actionPerformed(GuiButton button) throws IOException {
//		super.actionPerformed(button);
//		NetworkManager.sendToServer(new PacketAesu(button.id, aesu));
//	}
//
//	@Override
//	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
//		super.drawScreen(mouseX, mouseY, partialTicks);
//		this.renderHoveredToolTip(mouseX, mouseY);
//	}
//}
