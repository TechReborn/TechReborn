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

import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import reborncore.client.ClientNetworkManager;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.guibuilder.GuiBuilder;
import reborncore.common.screen.BuiltScreenHandler;
import techreborn.blockentity.machine.iron.IronFurnaceBlockEntity;
import techreborn.packets.ServerboundPackets;

public class GuiIronFurnace extends GuiBase<BuiltScreenHandler> {

	IronFurnaceBlockEntity blockEntity;
	private static final Identifier EXP_BUTTON_TEXTURE = new Identifier("minecraft", "textures/item/experience_bottle.png");


	public GuiIronFurnace(int syncID, PlayerEntity player, IronFurnaceBlockEntity furnace) {
		super(player, furnace, furnace.createScreenHandler(syncID, player));
		this.blockEntity = furnace;
	}

	public void onClick() {
		ClientNetworkManager.sendToServer(ServerboundPackets.createPacketExperience(blockEntity));
	}

	@Override
	public void init() {
		super.init();
		// TODO 1.19.3
//		ButtonWidget.TooltipSupplier tooltipSupplier = (button, matrices, mouseX, mouseY) -> {
//			PlayerEntity player = MinecraftClient.getInstance().player;
//			if (player == null) { return; }
//			String message = "Experience: ";
//
//			float furnaceExp = blockEntity.experience;
//			if (furnaceExp <= 0) {
//				message = message + "0";
//			} else {
//				float expTillLevel = (1.0F - player.experienceProgress) * player.getNextLevelExperience();
//				if (furnaceExp <= expTillLevel) {
//					int percentage = (int) (blockEntity.experience * 100 / player.getNextLevelExperience());
//					message = message + "+"
//							+ (percentage > 0 ? String.valueOf(percentage) : "<1")
//							+ "%";
//				} else {
//					int levels = 0;
//					furnaceExp -= expTillLevel;
//					while (furnaceExp > 0) {
//						furnaceExp -= PlayerUtils.getLevelExperience(player.experienceLevel);
//						++levels;
//					}
//					message = message + "+" + levels + "L";
//				}
//			}
//
//			renderTooltip(matrices, Text.literal(message), mouseX, mouseY);
//		};

		addDrawableChild(new TexturedButtonWidget(
				getGuiLeft() + 116,
				getGuiTop() + 58,
				16,
				16,
				0,
				0,
				1,
				EXP_BUTTON_TEXTURE,
				16,
				16,
				b -> onClick(),
				Text.empty()));
	}

	@Override
	protected void drawBackground(MatrixStack matrixStack, float lastFrameDuration, int mouseX, int mouseY) {
		super.drawBackground(matrixStack, lastFrameDuration, mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.BACKGROUND;

		// Input slot
		drawSlot(matrixStack, 56, 17, layer);
		// Fuel slot
		drawSlot(matrixStack, 56, 53, layer);

		drawOutputSlot(matrixStack, 116, 35, layer);
	}

	@Override
	protected void drawForeground(MatrixStack matrixStack, int mouseX, int mouseY) {
		super.drawForeground(matrixStack, mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.FOREGROUND;

		builder.drawProgressBar(matrixStack, this, blockEntity.getProgressScaled(100), 100, 85, 36, mouseX, mouseY, GuiBuilder.ProgressDirection.RIGHT, layer);
		builder.drawBurnBar(matrixStack, this, blockEntity.getBurnTimeRemainingScaled(100), 100, 56, 36, mouseX, mouseY, layer);
	}
}
