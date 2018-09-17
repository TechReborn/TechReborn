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

package techreborn.client.gui.slot.elements;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.client.FMLClientHandler;
import reborncore.RebornCore;
import reborncore.client.gui.GuiUtil;
import reborncore.common.network.NetworkManager;
import reborncore.common.network.packet.PacketFluidConfigSave;
import reborncore.common.network.packet.PacketFluidIOSave;
import reborncore.common.tile.FluidConfiguration;
import reborncore.common.tile.TileMachineBase;
import reborncore.common.util.MachineFacing;
import techreborn.client.gui.GuiBase;

import java.awt.*;

public class FluidConfigPopupElement extends ElementBase {
	public boolean filter = false;

	ConfigFluidElement fluidElement;
	int lastMousex, lastMousey;

	public FluidConfigPopupElement(int x, int y, ConfigFluidElement fluidElement) {
		super(x, y, Sprite.SLOT_CONFIG_POPUP);
		this.fluidElement = fluidElement;
	}

	@Override
	public void draw(GuiBase gui) {
		drawDefaultBackground(gui, adjustX(gui, getX() - 8), adjustY(gui, getY() - 7), 84, 105 + (filter ? 15 : 0));
		super.draw(gui);

		TileMachineBase machine = ((TileMachineBase) gui.tile);
		IBlockAccess blockAccess = machine.getWorld();
		BlockPos pos = machine.getPos();
		IBlockState state = blockAccess.getBlockState(pos);
		IBlockState actualState = state.getBlock().getDefaultState().getActualState(blockAccess, pos);
		BlockRendererDispatcher dispatcher = FMLClientHandler.instance().getClient().getBlockRendererDispatcher();
		IBakedModel model = dispatcher.getBlockModelShapes().getModelForState(state.getBlock().getDefaultState());
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		drawState(gui, blockAccess, model, actualState, pos, dispatcher, 4, 23); //left
		drawState(gui, blockAccess, model, actualState, pos, dispatcher, 23, -12, -90F, 1F, 0F, 0F); //top
		drawState(gui, blockAccess, model, actualState, pos, dispatcher, 23, 23, -90F, 0F, 1F, 0F); //centre
		drawState(gui, blockAccess, model, actualState, pos, dispatcher, 23, 42, 90F, 1F, 0F, 0F); //bottom
		drawState(gui, blockAccess, model, actualState, pos, dispatcher, 26, 23, 180F, 0F, 1F, 0F); //right
		drawState(gui, blockAccess, model, actualState, pos, dispatcher, 26, 42, 90F, 0F, 1F, 0F); //back

		drawSateColor(gui.getMachine(), MachineFacing.UP.getFacing(machine), 22, -1, gui);
		drawSateColor(gui.getMachine(), MachineFacing.FRONT.getFacing(machine), 22, 18, gui);
		drawSateColor(gui.getMachine(), MachineFacing.DOWN.getFacing(machine), 22, 37, gui);
		drawSateColor(gui.getMachine(), MachineFacing.RIGHT.getFacing(machine), 41, 18, gui);
		drawSateColor(gui.getMachine(), MachineFacing.BACK.getFacing(machine), 41, 37, gui);
		drawSateColor(gui.getMachine(), MachineFacing.LEFT.getFacing(machine), 3, 18, gui);
	}

	@Override
	public boolean onRelease(TileMachineBase provider, GuiBase gui, int mouseX, int mouseY) {
		if (isInBox(23, 4, 16, 16, mouseX, mouseY, gui)) {
			cyleConfig(MachineFacing.UP.getFacing(provider), gui);
		} else if (isInBox(23, 23, 16, 16, mouseX, mouseY, gui)) {
			cyleConfig(MachineFacing.FRONT.getFacing(provider), gui);
		} else if (isInBox(42, 23, 16, 16, mouseX, mouseY, gui)) {
			cyleConfig(MachineFacing.RIGHT.getFacing(provider), gui);
		} else if (isInBox(4, 23, 16, 16, mouseX, mouseY, gui)) {
			cyleConfig(MachineFacing.LEFT.getFacing(provider), gui);
		} else if (isInBox(23, 42, 16, 16, mouseX, mouseY, gui)) {
			cyleConfig(MachineFacing.DOWN.getFacing(provider), gui);
		} else if (isInBox(42, 42, 16, 16, mouseX, mouseY, gui)) {
			cyleConfig(MachineFacing.BACK.getFacing(provider), gui);
		} else {
			return false;
		}
		return true;
	}

	public void cyleConfig(EnumFacing side, GuiBase guiBase) {
		FluidConfiguration.FluidConfig config = guiBase.getMachine().fluidConfiguration.getSideDetail(side);

		FluidConfiguration.ExtractConfig fluidIO = config.getIoConfig().getNext();
		FluidConfiguration.FluidConfig newConfig = new FluidConfiguration.FluidConfig(side, fluidIO);

		PacketFluidConfigSave packetSave = new PacketFluidConfigSave(guiBase.tile.getPos(), newConfig);
		NetworkManager.sendToServer(packetSave);
	}

	public void updateCheckBox(CheckBoxElement checkBoxElement, String type, GuiBase guiBase) {
		FluidConfiguration configHolder = guiBase.getMachine().fluidConfiguration;
		boolean input = configHolder.autoInput();
		boolean output = configHolder.autoOutput();
		if (type.equalsIgnoreCase("input")) {
			input = !configHolder.autoInput();
		}
		if (type.equalsIgnoreCase("output")) {
			output = !configHolder.autoOutput();
		}

		PacketFluidIOSave packetFluidIOSave = new PacketFluidIOSave(guiBase.tile.getPos(), input, output);
		NetworkManager.sendToServer(packetFluidIOSave);
	}

	@Override
	public boolean onHover(TileMachineBase provider, GuiBase gui, int mouseX, int mouseY) {
		lastMousex = mouseX;
		lastMousey = mouseY;
		return super.onHover(provider, gui, mouseX, mouseY);
	}

	private void drawSateColor(TileMachineBase machineBase, EnumFacing side, int inx, int iny, GuiBase gui) {
		iny += 4;
		int sx = inx + getX() + gui.guiLeft;
		int sy = iny + getY() + gui.guiTop;
		FluidConfiguration fluidConfiguration = machineBase.fluidConfiguration;
		if (fluidConfiguration == null) {
			RebornCore.LOGGER.debug("Humm, this isnt suppoed to happen");
			return;
		}
		FluidConfiguration.FluidConfig fluidConfig = fluidConfiguration.getSideDetail(side);
		Color color;
		switch (fluidConfig.getIoConfig()) {
			case NONE:
				color = new Color(0, 0, 0, 0);
				break;
			case INPUT:
				color = new Color(0, 0, 255, 128);
				break;
			case OUTPUT:
				color = new Color(255, 69, 0, 128);
				break;
			case ALL:
				color = new Color(52, 255, 30, 128);
				break;
			default:
				color = new Color(0, 0, 0, 0);
				break;
		}
		GlStateManager.color(255, 255, 255);
		GuiUtil.drawGradientRect(sx, sy, 18, 18, color.getRGB(), color.getRGB());
		GlStateManager.color(255, 255, 255);
	}

	private boolean isInBox(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY, GuiBase guiBase) {
		rectX += getX();
		rectY += getY();
		return isInRect(guiBase, rectX, rectY, rectWidth, rectHeight, pointX, pointY);
		//return (pointX - guiBase.getGuiLeft()) >= rectX - 1 && (pointX - guiBase.getGuiLeft()) < rectX + rectWidth + 1 && (pointY - guiBase.getGuiTop()) >= rectY - 1 && (pointY - guiBase.getGuiTop()) < rectY + rectHeight + 1;
	}

	public void drawState(GuiBase gui,
	                      IBlockAccess blockAccess,
	                      IBakedModel model,
	                      IBlockState actualState,
	                      BlockPos pos,
	                      BlockRendererDispatcher dispatcher,
	                      int x,
	                      int y,
	                      float rotAngle,
	                      float rotX,
	                      float rotY,
	                      float rotZ) {

		GlStateManager.pushMatrix();
		GlStateManager.enableDepth();
		GlStateManager.translate(8 + gui.guiLeft + this.x + x, 8 + gui.guiTop + this.y + y, 512);
		GlStateManager.scale(16F, 16F, 16F);
		GlStateManager.translate(0.5F, 0.5F, 0.5F);
		GlStateManager.scale(-1, -1, -1);
		if (rotAngle != 0) {
			GlStateManager.rotate(rotAngle, rotX, rotY, rotZ);
		}
		dispatcher.getBlockModelRenderer().renderModelBrightness(model, actualState, 1F, false);
		GlStateManager.disableDepth();
		GlStateManager.popMatrix();

/*		GlStateManager.pushMatrix();
		GlStateManager.enableDepth();
		//		GlStateManager.translate(8 + gui.xFactor + this.x + x, 8 + gui.yFactor + this.y + y, 1000);
		GlStateManager.translate(gui.xFactor + this.x + x, gui.yFactor + this.y + y, 512);
		if (rotAngle != 0) {
			GlStateManager.rotate(rotAngle, rotX, rotY, rotZ);
		}
		GlStateManager.scale(16F, 16F, 16F);
		GlStateManager.translate(-0.5F, -0.5F, -0.5F);
		GlStateManager.scale(-1, -1, -1);
		GlStateManager.disableDepth();
		GlStateManager.popMatrix();*/
	}

	public void drawState(GuiBase gui, IBlockAccess blockAccess, IBakedModel model, IBlockState actualState, BlockPos pos, BlockRendererDispatcher dispatcher, int x, int y) {
		drawState(gui, blockAccess, model, actualState, pos, dispatcher, x, y, 0, 0, 0, 0);
	}
}