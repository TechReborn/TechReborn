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

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import reborncore.client.multiblock.MultiblockRenderEvent;
import reborncore.client.multiblock.MultiblockSet;
import reborncore.common.misc.Location;
import reborncore.common.powerSystem.PowerSystem;
import techreborn.client.ClientMultiBlocks;
import techreborn.proxies.ClientProxy;
import techreborn.tiles.fusionReactor.TileFusionControlComputer;

import java.io.IOException;

public class GuiFusionReactor extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation("techreborn",
		"textures/gui/fusion_reactor.png");

	TileFusionControlComputer fusionController;

	public GuiFusionReactor(final EntityPlayer player, final TileFusionControlComputer fusion) {
		super(fusion.createContainer(player));
		this.fusionController = fusion;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int p_146979_1_, final int p_146979_2_) {
		final String name = I18n.translateToLocal("tile.techreborn:fusion_control_computer.name");
		this.fontRenderer.drawString(name, 87, 6, 4210752);
		this.fontRenderer.drawString(I18n.translateToLocalFormatted("container.inventory", new Object[0]), 8,
			this.ySize - 96 + 2, 4210752);

		this.fontRenderer.drawString(PowerSystem.getLocaliszedPower(this.fusionController.getEnergy()), 11, 8,
			16448255);
		this.fontRenderer.drawString("Coils: " + (this.fusionController.getCoilStatus() == 1 ? "Yes" : "No"), 11, 16,
			16448255);
		if (this.fusionController.getNeededPower() > 1 && this.fusionController.getCrafingTickTime() < 1)
			this.fontRenderer.drawString("Start: "
				+ this.percentage(this.fusionController.getNeededPower(), (int) this.fusionController.getEnergy())
				+ "%", 11, 24, 16448255);

	}

	@Override
	public void initGui() {
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		final GuiButton button = new GuiButton(212, k + this.xSize - 24, l + 4, 20, 20, "");
		this.buttonList.add(button);
		super.initGui();
		final BlockPos coordinates = new BlockPos(
			this.fusionController.getPos().getX()
				- EnumFacing.getFront(this.fusionController.getFacingInt()).getFrontOffsetX() * 2,
			this.fusionController.getPos().getY() - 1, this.fusionController.getPos().getZ()
			- EnumFacing.getFront(this.fusionController.getFacingInt()).getFrontOffsetZ() * 2);
		if (coordinates.equals(MultiblockRenderEvent.anchor)) {
			ClientProxy.multiblockRenderEvent.setMultiblock(null);
			button.displayString = "B";
		} else {
			button.displayString = "A";
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float p_146976_1_, final int p_146976_2_,
	                                               final int p_146976_3_) {
		this.drawDefaultBackground();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiFusionReactor.texture);
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		this.drawTexturedModalRect(k + 88, l + 36, 176, 0, 14, 14);

		// progressBar
		this.drawTexturedModalRect(k + 111, l + 34, 176, 14, this.fusionController.getProgressScaled(), 16);

	}

	public int percentage(final int MaxValue, final int CurrentValue) {
		if (CurrentValue == 0)
			return 0;
		return (int) (CurrentValue * 100.0f / MaxValue);
	}

	@Override
	public void actionPerformed(final GuiButton button) throws IOException {
		super.actionPerformed(button);
		if (button.id == 212) {
			if (ClientProxy.multiblockRenderEvent.currentMultiblock == null) {
				// This code here makes a basic multiblock and then sets to
				// theselected one.
				final MultiblockSet set = new MultiblockSet(ClientMultiBlocks.reactor);
				ClientProxy.multiblockRenderEvent.setMultiblock(set);
				ClientProxy.multiblockRenderEvent.parent = new Location(this.fusionController.getPos().getX(),
					this.fusionController.getPos().getY(), this.fusionController.getPos().getZ(),
					this.fusionController.getWorld());
				MultiblockRenderEvent.anchor = new BlockPos(this.fusionController.getPos().getX(),
					this.fusionController.getPos().getY() - 1, this.fusionController.getPos().getZ());

				button.displayString = "A";
			} else {
				ClientProxy.multiblockRenderEvent.setMultiblock(null);
				button.displayString = "B";
			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
}
