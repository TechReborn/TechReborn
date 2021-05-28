/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore.client.gui.builder.slot.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.Packet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import reborncore.RebornCore;
import reborncore.client.gui.GuiUtil;
import reborncore.client.gui.builder.GuiBase;
import reborncore.common.blockentity.FluidConfiguration;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.network.IdentifiedPacket;
import reborncore.common.network.NetworkManager;
import reborncore.common.network.ServerBoundPackets;
import reborncore.common.util.Color;
import reborncore.common.util.MachineFacing;

public class FluidConfigPopupElement extends ElementBase {
	public boolean filter = false;

	ConfigFluidElement fluidElement;
	double lastMousex, lastMousey;

	public FluidConfigPopupElement(int x, int y, ConfigFluidElement fluidElement) {
		super(x, y, Sprite.SLOT_CONFIG_POPUP);
		this.fluidElement = fluidElement;
	}

	@Override
	public void draw(MatrixStack matrixStack, GuiBase<?> gui) {
		drawDefaultBackground(matrixStack, gui, adjustX(gui, getX() - 8), adjustY(gui, getY() - 7), 84, 105 + (filter ? 15 : 0));
		super.draw(matrixStack, gui);

		MachineBaseBlockEntity machine = ((MachineBaseBlockEntity) gui.be);
		World world = machine.getWorld();
		BlockPos pos = machine.getPos();
		BlockState state = world.getBlockState(pos);
		BlockState actualState = state.getBlock().getDefaultState();
		BlockRenderManager dispatcher = MinecraftClient.getInstance().getBlockRenderManager();
		BakedModel model = dispatcher.getModels().getModel(state.getBlock().getDefaultState());
		MinecraftClient.getInstance().getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
		drawState(gui, world, model, actualState, pos, dispatcher, 4, 23, Vec3f.POSITIVE_Y.getDegreesQuaternion(90F)); //left
		drawState(gui, world, model, actualState, pos, dispatcher, 23, 4, Vec3f.NEGATIVE_X.getDegreesQuaternion(90F)); //top
		drawState(gui, world, model, actualState, pos, dispatcher, 23, 23, null); //centre
		drawState(gui, world, model, actualState, pos, dispatcher, 23, 26, Vec3f.POSITIVE_X.getDegreesQuaternion(90F)); //bottom
		drawState(gui, world, model, actualState, pos, dispatcher, 42, 23, Vec3f.POSITIVE_Y.getDegreesQuaternion(90F)); //right
		drawState(gui, world, model, actualState, pos, dispatcher, 26, 42, Vec3f.POSITIVE_Y.getDegreesQuaternion(180F)); //back

		drawSateColor(gui.getMachine(), MachineFacing.UP.getFacing(machine), 22, -1, gui);
		drawSateColor(gui.getMachine(), MachineFacing.FRONT.getFacing(machine), 22, 18, gui);
		drawSateColor(gui.getMachine(), MachineFacing.DOWN.getFacing(machine), 22, 37, gui);
		drawSateColor(gui.getMachine(), MachineFacing.RIGHT.getFacing(machine), 41, 18, gui);
		drawSateColor(gui.getMachine(), MachineFacing.BACK.getFacing(machine), 41, 37, gui);
		drawSateColor(gui.getMachine(), MachineFacing.LEFT.getFacing(machine), 3, 18, gui);
	}

	@Override
	public boolean onRelease(MachineBaseBlockEntity provider, GuiBase<?> gui, double mouseX, double mouseY) {
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

	public void cyleConfig(Direction side, GuiBase<?> guiBase) {
		FluidConfiguration.FluidConfig config = guiBase.getMachine().fluidConfiguration.getSideDetail(side);

		FluidConfiguration.ExtractConfig fluidIO = config.getIoConfig().getNext();
		FluidConfiguration.FluidConfig newConfig = new FluidConfiguration.FluidConfig(side, fluidIO);

		IdentifiedPacket packetSave = ServerBoundPackets.createPacketFluidConfigSave(guiBase.be.getPos(), newConfig);
		NetworkManager.sendToServer(packetSave);
	}

	public void updateCheckBox(CheckBoxElement checkBoxElement, String type, GuiBase<?> guiBase) {
		FluidConfiguration configHolder = guiBase.getMachine().fluidConfiguration;
		boolean input = configHolder.autoInput();
		boolean output = configHolder.autoOutput();
		if (type.equalsIgnoreCase("input")) {
			input = !configHolder.autoInput();
		}
		if (type.equalsIgnoreCase("output")) {
			output = !configHolder.autoOutput();
		}

		IdentifiedPacket packetFluidIOSave = ServerBoundPackets.createPacketFluidIOSave(guiBase.be.getPos(), input, output);
		NetworkManager.sendToServer(packetFluidIOSave);
	}

	@Override
	public boolean onHover(MachineBaseBlockEntity provider, GuiBase<?> gui, double mouseX, double mouseY) {
		lastMousex = mouseX;
		lastMousey = mouseY;
		return super.onHover(provider, gui, mouseX, mouseY);
	}

	private void drawSateColor(MachineBaseBlockEntity machineBase, Direction side, int inx, int iny, GuiBase<?> gui) {
		iny += 4;
		int sx = inx + getX() + gui.getGuiLeft();
		int sy = iny + getY() + gui.getGuiTop();
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
		RenderSystem.color3f(255, 255, 255);
		GuiUtil.drawGradientRect(sx, sy, 18, 18, color.getColor(), color.getColor());
		RenderSystem.color3f(255, 255, 255);
	}

	private boolean isInBox(int rectX, int rectY, int rectWidth, int rectHeight, double pointX, double pointY, GuiBase<?> guiBase) {
		rectX += getX();
		rectY += getY();
		return isInRect(guiBase, rectX, rectY, rectWidth, rectHeight, pointX, pointY);
		//return (pointX - guiBase.getGuiLeft()) >= rectX - 1 && (pointX - guiBase.getGuiLeft()) < rectX + rectWidth + 1 && (pointY - guiBase.getGuiTop()) >= rectY - 1 && (pointY - guiBase.getGuiTop()) < rectY + rectHeight + 1;
	}

	public void drawState(GuiBase<?> gui,
						  World world,
						  BakedModel model,
						  BlockState actualState,
						  BlockPos pos,
						  BlockRenderManager dispatcher,
						  int x,
						  int y,
						  Quaternion quaternion) {

		MatrixStack matrixStack = new MatrixStack();
		matrixStack.push();
		matrixStack.translate(8 + gui.getGuiLeft() + this.x + x, 8 + gui.getGuiTop() + this.y + y, 512);
		matrixStack.scale(16F, 16F, 16F);
		matrixStack.translate(0.5F, 0.5F, 0.5F);
		matrixStack.scale(-1, -1, -1);

		if (quaternion != null) {
			matrixStack.multiply(quaternion);
		}

		VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
		dispatcher.getModelRenderer().render(matrixStack.peek(), immediate.getBuffer(RenderLayer.getSolid()), actualState, model, 1F, 1F, 1F, OverlayTexture.getU(15F), OverlayTexture.DEFAULT_UV);
		immediate.draw();
		matrixStack.pop();
	}

}