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

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.GuiBuilder;
import reborncore.common.screen.BuiltScreenHandler;
import techreborn.blockentity.machine.iron.IronFurnaceBlockEntity;
import techreborn.packets.serverbound.ExperiencePayload;
import techreborn.utils.PlayerUtils;

import java.util.Objects;

public class GuiIronFurnace extends GuiBase<BuiltScreenHandler> {
	final IronFurnaceBlockEntity blockEntity;
	private static final ItemStack EXP_BUTTON_STACK = new ItemStack(Items.EXPERIENCE_BOTTLE);

	public GuiIronFurnace(int syncID, Player player, IronFurnaceBlockEntity furnace) {
		super(player, furnace, furnace.createScreenHandler(syncID, player));
		this.blockEntity = furnace;
	}

	public void onClick(Button buttonWidget) {
		ClientPlayNetworking.send(new ExperiencePayload(blockEntity.getBlockPos()));
	}

	@Override
	public void init() {
		super.init();
		addRenderableWidget(new XpButtonWidget(this::onClick));
	}

	private class XpButtonWidget extends Button {
		public XpButtonWidget(OnPress pressAction) {
			super(getGuiLeft() + 116,
				getGuiTop() + 58,
				16,
				16,
				Component.empty(),
				pressAction,
				DEFAULT_NARRATION);
		}

		@Override
		public void extractContents(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
			context.item(EXP_BUTTON_STACK, getX(), getY());

			if (isHovered) {
				context.setTooltipForNextFrame(getFont(), getTooltipText(), mouseX, mouseY);
			}
		}

		private Component getTooltipText() {
			Player player = Minecraft.getInstance().player;
			Objects.requireNonNull(player);
			String message = ": ";

			float furnaceExp = blockEntity.experience;
			if (furnaceExp <= 0) {
				message = message + "0";
			} else {
				float expTillLevel = (1.0F - player.experienceProgress) * player.getXpNeededForNextLevel();
				if (furnaceExp <= expTillLevel) {
					int percentage = (int) (blockEntity.experience * 100 / player.getXpNeededForNextLevel());
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
					message = message + "+" + levels + "L";
				}
			}

			return Component.translatable("techreborn.tooltip.experience")
				.append(message);
		}
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor drawContext, final int mouseX, final int mouseY, final float lastFrameDuration) {
		super.extractBackground(drawContext, mouseX, mouseY, lastFrameDuration);
		final GuiBase.Layer layer = GuiBase.Layer.BACKGROUND;

		// Input slot
		drawSlot(drawContext, 56, 17, layer);
		// Fuel slot
		drawSlot(drawContext, 56, 53, layer);

		drawOutputSlot(drawContext, 116, 35, layer);
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor drawContext, int mouseX, int mouseY) {
		super.extractLabels(drawContext, mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.FOREGROUND;

		builder.drawProgressBar(drawContext, this, blockEntity.getProgressScaled(100), 100, 85, 36, mouseX, mouseY, GuiBuilder.ProgressDirection.RIGHT, layer);
		builder.drawBurnBar(drawContext, this, blockEntity.getBurnTimeRemainingScaled(100), 100, 56, 36, mouseX, mouseY, layer);
	}
}
