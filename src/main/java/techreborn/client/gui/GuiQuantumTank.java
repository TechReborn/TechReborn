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

import net.minecraft.entity.player.PlayerEntity;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.gui.builder.GuiBase;
import reborncore.common.fluid.FluidUtil;
import reborncore.common.fluid.container.FluidInstance;
import techreborn.blockentity.machine.tier3.QuantumTankBlockEntity;

public class GuiQuantumTank extends GuiBase<BuiltContainer> {

	QuantumTankBlockEntity quantumTank;

	public GuiQuantumTank(int syncID, final PlayerEntity player, final QuantumTankBlockEntity quantumTank) {
		super(player, quantumTank, quantumTank.createContainer(syncID, player));
		this.quantumTank = quantumTank;
	}

	@Override
	protected void drawBackground(final float f, final int mouseX, final int mouseY) {
		super.drawBackground(f, mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.BACKGROUND;

		drawSlot(80, 17, layer);
		drawSlot(80, 53, layer);

	}

	@Override
	protected void drawForeground(final int mouseX, final int mouseY) {
		super.drawForeground(mouseX, mouseY);

		FluidInstance fluid = quantumTank.tank.getFluidInstance();
		if(fluid != null){
			font.draw( "Fluid Type:", 10, 20, 4210752);
			font.draw(FluidUtil.getFluidName(fluid) + "", 10, 30, 4210752);

			font.draw("Fluid Amount:", 10, 50, 4210752);
			font.draw(quantumTank.tank.getFluidAmount() + "mb", 10, 60, 4210752);
		}

	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		drawMouseoverTooltip(mouseX, mouseY);
	}
}
