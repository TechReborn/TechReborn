/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.entity.player.PlayerEntity;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.builder.widget.GuiButtonSimple;
import reborncore.client.gui.guibuilder.GuiBuilder;
import reborncore.common.network.NetworkManager;
import techreborn.blockentity.machine.iron.IronFurnaceBlockEntity;
import techreborn.packets.ServerboundPackets;

public class GuiIronFurnace extends GuiBase<BuiltContainer> {

	IronFurnaceBlockEntity blockEntity;

	public GuiIronFurnace(int syncID, PlayerEntity player, IronFurnaceBlockEntity furnace) {
		super(player, furnace,  furnace.createContainer(syncID, player));
		this.blockEntity = furnace;
	}
	
	public void onClick(){
		NetworkManager.sendToServer(ServerboundPackets.createPacketExperience(blockEntity));
	}	
	
	@Override
	public void init() {
		super.init();
		addButton(new GuiButtonSimple(getGuiLeft() + 116, getGuiTop() + 57, 18, 18, "Exp", b -> onClick()) {

			
			@Override
			public void renderToolTip(int mouseX, int mouseY) {
					List<String> list = new ArrayList<>();
					list.add("Expirience accumulated: " + blockEntity.experience);
					renderTooltip(list, mouseX - getGuiLeft(), mouseY - getGuiTop());
					GlStateManager.disableLighting();
					GlStateManager.color4f(1, 1, 1, 1);			
			}
		});		
	}
	
	@Override
	protected void drawBackground(float lastFrameDuration, int mouseX, int mouseY) {
		super.drawBackground(lastFrameDuration, mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.BACKGROUND;

		// Input slot
		drawSlot(56, 17, layer);
		// Fuel slot
		drawSlot(56, 53, layer);

		drawOutputSlot(116, 35, layer);
	}
	
	@Override
	protected void drawForeground(int mouseX, int mouseY) {
		super.drawForeground(mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.FOREGROUND;

		builder.drawProgressBar(this, blockEntity.gaugeProgressScaled(100), 100, 85, 36, mouseX, mouseY, GuiBuilder.ProgressDirection.RIGHT, layer);
		builder.drawBurnBar(this, blockEntity.gaugeFuelScaled(100), 100, 56, 36, mouseX, mouseY, layer);
		Iterator<AbstractButtonWidget> buttonsList = this.buttons.iterator();

		while (buttonsList.hasNext()) {
			AbstractButtonWidget abstractButtonWidget = (AbstractButtonWidget) buttonsList.next();
			if (abstractButtonWidget.isHovered()) {
				abstractButtonWidget.renderToolTip(mouseX, mouseY);
				break;
			}
		}
	}
}
