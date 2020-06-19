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

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.common.fluid.FluidUtil;
import reborncore.common.fluid.container.FluidInstance;
import techreborn.blockentity.storage.fluid.TankUnitBaseBlockEntity;

public class GuiTankUnit extends GuiBase<BuiltScreenHandler> {

	TankUnitBaseBlockEntity tankEntity;

	public GuiTankUnit(int syncID, final PlayerEntity player, final TankUnitBaseBlockEntity tankEntity) {
		super(player, tankEntity, tankEntity.createScreenHandler(syncID, player));
		this.tankEntity = tankEntity;
	}

	@Override
	protected void drawBackground(MatrixStack matrixStack, final float f, final int mouseX, final int mouseY) {
		super.drawBackground(matrixStack, f, mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.BACKGROUND;

		// Draw slots
		drawSlot(matrixStack, 100, 53, layer);
		drawSlot(matrixStack, 140, 53, layer);
	}

	@Override
	protected void drawForeground(MatrixStack matrixStack, final int mouseX, final int mouseY) {
		super.drawForeground(matrixStack, mouseX, mouseY);

		// Draw input/out
		builder.drawText(matrixStack, this, new TranslatableText("gui.techreborn.unit.in"), 100, 43, 4210752);
		builder.drawText(matrixStack, this, new TranslatableText("gui.techreborn.unit.out"), 140, 43, 4210752);


		FluidInstance fluid = tankEntity.getTank().getFluidInstance();

		if (fluid.isEmpty()) {
			textRenderer.draw(matrixStack, new TranslatableText("techreborn.tooltip.unit.empty"), 10, 20, 4210752);
		} else {
			textRenderer.draw(matrixStack, new TranslatableText("gui.techreborn.tank.type"), 10, 20, 4210752);
			textRenderer.draw(matrixStack, FluidUtil.getFluidName(fluid).replace("_", " "), 10, 30, 4210752);


			textRenderer.draw(matrixStack, new TranslatableText("gui.techreborn.tank.amount"), 10, 50, 4210752);
			textRenderer.draw(matrixStack, fluid.getAmount().toString(), 10, 60, 4210752);

			String percentFilled = String.valueOf((int) ((double) fluid.getAmount().getRawValue() / (double) tankEntity.getTank().getCapacity().getRawValue() * 100));

			textRenderer.draw(matrixStack, new TranslatableText("gui.techreborn.unit.used").append(percentFilled + "%"), 10, 70, 4210752);

			textRenderer.draw(matrixStack, new TranslatableText("gui.techreborn.unit.wrenchtip"), 10, 80, 16711680);
		}
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		drawMouseoverTooltip(matrixStack, mouseX, mouseY);
	}
}
