/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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
import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.builder.widget.GuiButtonSimple;
import reborncore.client.gui.guibuilder.GuiBuilder;
import reborncore.common.network.NetworkManager;
import techreborn.blockentity.machine.iron.IronFurnaceBlockEntity;
import techreborn.packets.ServerboundPackets;
import techreborn.utils.PlayerUtils;

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
		addButton(new GuiButtonSimple(getGuiLeft() + 116, getGuiTop() + 57, 18, 18, "", b -> onClick()) {

			@Override
			public void renderToolTip(int mouseX, int mouseY) {
				PlayerEntity player = playerInventory.player;
				if (player == null) {
					return;
				}
				String message = "Experience: ";

				float furnaceExp = blockEntity.experience;
				if (furnaceExp <= 0) {
					message = message + "0";
				} else {
					float expTillLevel = (1.0F - player.experienceProgress) * player.getNextLevelExperience();
					if (furnaceExp <= expTillLevel) {
						int percentage = (int) (blockEntity.experience * 100 / player.getNextLevelExperience());
						message = message + "+"
								+ (percentage > 0 ? String.valueOf(percentage) : "<1")
								+ "%";
					} else {
						int levels = 0;
						furnaceExp -= expTillLevel;
						while (furnaceExp > 0) {
							furnaceExp -= PlayerUtils.getLevelExperience(player.experienceLevel);
							++levels;
						}
						message = message + "+" + String.valueOf(levels) + "L";
					}
				}

				List<String> list = new ArrayList<>();				
				list.add(message);
				renderTooltip(list, mouseX, mouseY);
				GlStateManager.disableLighting();
				GlStateManager.color4f(1, 1, 1, 1);				
					
			}
			
			@Override
			public void renderBg(MinecraftClient mc, int mouseX, int mouseY) {
				mc.getItemRenderer().renderGuiItem(new ItemStack(Items.EXPERIENCE_BOTTLE), x, y);
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

		builder.drawProgressBar(this, blockEntity.getProgressScaled(100), 100, 85, 36, mouseX, mouseY, GuiBuilder.ProgressDirection.RIGHT, layer);
		builder.drawBurnBar(this, blockEntity.getBurnTimeRemainingScaled(100), 100, 56, 36, mouseX, mouseY, layer);
	}
}
