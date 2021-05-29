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
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.SlotConfiguration;
import reborncore.common.network.IdentifiedPacket;
import reborncore.common.network.NetworkManager;
import reborncore.common.network.ServerBoundPackets;
import reborncore.common.util.Color;
import reborncore.common.util.MachineFacing;

public class SlotConfigPopupElement extends ElementBase {
	int id;
	public boolean filter = false;

	ConfigSlotElement slotElement;

	boolean allowInput = true;

	public SlotConfigPopupElement(int slotId, int x, int y, ConfigSlotElement slotElement, boolean allowInput) {
		super(x, y, Sprite.SLOT_CONFIG_POPUP);
		this.id = slotId;
		this.slotElement = slotElement;
		this.allowInput = allowInput;
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

		drawSlotSateColor(matrixStack, gui.getMachine(), MachineFacing.UP.getFacing(machine), id, 22, -1, gui);
		drawSlotSateColor(matrixStack, gui.getMachine(), MachineFacing.FRONT.getFacing(machine), id, 22, 18, gui);
		drawSlotSateColor(matrixStack, gui.getMachine(), MachineFacing.DOWN.getFacing(machine), id, 22, 37, gui);
		drawSlotSateColor(matrixStack, gui.getMachine(), MachineFacing.RIGHT.getFacing(machine), id, 41, 18, gui);
		drawSlotSateColor(matrixStack, gui.getMachine(), MachineFacing.BACK.getFacing(machine), id, 41, 37, gui);
		drawSlotSateColor(matrixStack, gui.getMachine(), MachineFacing.LEFT.getFacing(machine), id, 3, 18, gui);
	}

	@Override
	public boolean onRelease(MachineBaseBlockEntity provider, GuiBase<?> gui, double mouseX, double mouseY) {
		if (isInBox(23, 4, 16, 16, mouseX, mouseY, gui)) {
			cyleSlotConfig(MachineFacing.UP.getFacing(provider), gui);
		} else if (isInBox(23, 23, 16, 16, mouseX, mouseY, gui)) {
			cyleSlotConfig(MachineFacing.FRONT.getFacing(provider), gui);
		} else if (isInBox(42, 23, 16, 16, mouseX, mouseY, gui)) {
			cyleSlotConfig(MachineFacing.RIGHT.getFacing(provider), gui);
		} else if (isInBox(4, 23, 16, 16, mouseX, mouseY, gui)) {
			cyleSlotConfig(MachineFacing.LEFT.getFacing(provider), gui);
		} else if (isInBox(23, 42, 16, 16, mouseX, mouseY, gui)) {
			cyleSlotConfig(MachineFacing.DOWN.getFacing(provider), gui);
		} else if (isInBox(42, 42, 16, 16, mouseX, mouseY, gui)) {
			cyleSlotConfig(MachineFacing.BACK.getFacing(provider), gui);
		} else {
			return false;
		}
		return true;
	}

	public void cyleSlotConfig(Direction side, GuiBase<?> guiBase) {
		SlotConfiguration.SlotConfig currentSlot = guiBase.getMachine().getSlotConfiguration().getSlotDetails(id).getSideDetail(side);

		//Bit of a mess, in the future have a way to remove config options from this list
		SlotConfiguration.ExtractConfig nextConfig = currentSlot.getSlotIO().getIoConfig().getNext();
		if (!allowInput && nextConfig == SlotConfiguration.ExtractConfig.INPUT) {
			nextConfig = SlotConfiguration.ExtractConfig.OUTPUT;
		}

		SlotConfiguration.SlotIO slotIO = new SlotConfiguration.SlotIO(nextConfig);
		SlotConfiguration.SlotConfig newConfig = new SlotConfiguration.SlotConfig(side, slotIO, id);
		IdentifiedPacket packetSlotSave = ServerBoundPackets.createPacketSlotSave(guiBase.be.getPos(), newConfig);
		NetworkManager.sendToServer(packetSlotSave);
	}

	public void updateCheckBox(CheckBoxElement checkBoxElement, String type, GuiBase<?> guiBase) {
		SlotConfiguration.SlotConfigHolder configHolder = guiBase.getMachine().getSlotConfiguration().getSlotDetails(id);
		boolean input = configHolder.autoInput();
		boolean output = configHolder.autoOutput();
		boolean filter = configHolder.filter();
		if (type.equalsIgnoreCase("input")) {
			input = !configHolder.autoInput();
		}
		if (type.equalsIgnoreCase("output")) {
			output = !configHolder.autoOutput();
		}
		if (type.equalsIgnoreCase("filter")) {
			filter = !configHolder.filter();
		}

		IdentifiedPacket packetSlotSave = ServerBoundPackets.createPacketIOSave(guiBase.be.getPos(), id, input, output, filter);
		NetworkManager.sendToServer(packetSlotSave);
	}

	private void drawSlotSateColor(MatrixStack matrices, MachineBaseBlockEntity machineBase, Direction side, int slotID, int inx, int iny, GuiBase<?> gui) {
		iny += 4;
		int sx = inx + getX() + gui.getGuiLeft();
		int sy = iny + getY() + gui.getGuiTop();
		SlotConfiguration.SlotConfigHolder slotConfigHolder = machineBase.getSlotConfiguration().getSlotDetails(slotID);
		if (slotConfigHolder == null) {
			RebornCore.LOGGER.debug("Humm, this isnt suppoed to happen");
			return;
		}
		SlotConfiguration.SlotConfig slotConfig = slotConfigHolder.getSideDetail(side);
		Color color;
		switch (slotConfig.getSlotIO().getIoConfig()) {
			case INPUT:
				color = new Color(0, 0, 255, 128);
				break;
			case OUTPUT:
				color = new Color(255, 69, 0, 128);
				break;
			default:
				color = new Color(0, 0, 0, 0);
				break;
		}
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		GuiUtil.drawGradientRect(matrices, sx, sy, 18, 18, color.getColor(), color.getColor());
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

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

	public void drawState(GuiBase<?> gui, World world, BakedModel model, BlockState actualState, BlockPos pos, BlockRenderManager dispatcher, int x, int y) {
		drawState(gui, world, model, actualState, pos, dispatcher, x, y, null);
	}
}