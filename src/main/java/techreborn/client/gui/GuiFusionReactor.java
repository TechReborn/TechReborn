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

import net.minecraft.block.BlockState;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.Pair;
import reborncore.RebornCoreClient;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.builder.widget.GuiButtonExtended;
import reborncore.client.gui.builder.widget.GuiButtonUpDown;
import reborncore.client.gui.builder.widget.GuiButtonUpDown.UpDownButtonType;
import reborncore.client.gui.guibuilder.GuiBuilder;
import reborncore.client.multiblock.Multiblock;
import reborncore.common.network.NetworkManager;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.Color;
import reborncore.common.util.Torus;
import techreborn.init.TRContent;
import techreborn.packets.ServerboundPackets;
import techreborn.blockentity.fusionReactor.FusionControlComputerBlockEntity;

import java.util.List;
import java.util.Optional;

public class GuiFusionReactor extends GuiBase<BuiltContainer> {
	FusionControlComputerBlockEntity blockEntity;

	public GuiFusionReactor(int syncID, final PlayerEntity player, final FusionControlComputerBlockEntity blockEntity) {
		super(player, blockEntity, blockEntity.createContainer(syncID, player));
		this.blockEntity = blockEntity;
	}
	
	@Override
	public void init() {
		super.init();
		addButton(new GuiButtonUpDown(left + 121, top + 79, this, (ButtonWidget buttonWidget) -> sendSizeChange(5), UpDownButtonType.FASTFORWARD));
		addButton(new GuiButtonUpDown(left + 121 + 12, top + 79, this, (ButtonWidget buttonWidget) -> sendSizeChange(1), UpDownButtonType.FORWARD));
		addButton(new GuiButtonUpDown(left + 121 + 24, top + 79, this, (ButtonWidget buttonWidget) -> sendSizeChange(-5), UpDownButtonType.REWIND));
		addButton(new GuiButtonUpDown(left + 121 + 36, top + 79, this, (ButtonWidget buttonWidget) -> sendSizeChange(-1), UpDownButtonType.FASTREWIND));
	}

	@Override
	protected void drawBackground(final float partialTicks, final int mouseX, final int mouseY) {
		super.drawBackground(partialTicks, mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.BACKGROUND;

		drawSlot(34, 47, layer);
		drawSlot(126, 47, layer);
		drawOutputSlot(80, 47, layer);

		builder.drawJEIButton(this, 158, 5, layer);
		if (blockEntity.getCoilStatus() > 0) {
			builder.drawHologramButton(this, 6, 4, mouseX, mouseY, layer);
		}

	}

	@Override
	protected void drawForeground(final int mouseX, final int mouseY) {
		super.drawForeground(mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.FOREGROUND;

		builder.drawProgressBar(this, blockEntity.getProgressScaled(100), 100, 55, 51, mouseX, mouseY, GuiBuilder.ProgressDirection.RIGHT, layer);
		builder.drawProgressBar(this, blockEntity.getProgressScaled(100), 100, 105, 51, mouseX, mouseY, GuiBuilder.ProgressDirection.LEFT, layer);
		if (blockEntity.getCoilStatus() > 0) {
			addHologramButton(6, 4, 212, layer).clickHandler(this::hologramToggle);
			drawCentredString(blockEntity.getStateString(), 20, Color.BLUE.darker().getColor(), layer);
			if(blockEntity.state == 2){
				drawCentredString( PowerSystem.getLocaliszedPowerFormatted((int) blockEntity.getPowerChange()) + "/t", 30, Color.GREEN.darker().getColor(), layer);
			}
		} else {
			builder.drawMultiblockMissingBar(this, layer);
			addHologramButton(76, 56, 212, layer).clickHandler(this::hologramToggle);
			builder.drawHologramButton(this, 76, 56, mouseX, mouseY, layer);

			Optional<Pair<Integer, Integer>> stackSize = getCoilStackCount();
			if(stackSize.isPresent()){
				if(stackSize.get().getLeft() > 0){
					drawCentredString("Required Coils: " + stackSize.get().getLeft() + "x64 +" + stackSize.get().getRight(), 25, 0xFFFFFF,  layer);
				} else {
					drawCentredString("Required Coils: " + stackSize.get().getRight(), 25, 0xFFFFFF, layer);
				}

			}
		}
		drawString("Size: " + blockEntity.size, 83, 81, 0xFFFFFF, layer);
		drawString("" + blockEntity.getPowerMultiplier() + "x", 10, 81, 0xFFFFFF, layer);

		builder.drawMultiEnergyBar(this, 9, 19, (int) this.blockEntity.getEnergy(), (int) this.blockEntity.getMaxPower(), mouseX, mouseY, 0, layer);
	}

	public void hologramToggle(GuiButtonExtended button, double x, double y){
		if (GuiBase.slotConfigType == SlotConfigType.NONE) {
			if (blockEntity.renderMultiblock == null) {
				updateMultiBlockRender();
			} else {
				blockEntity.renderMultiblock = null;
			}
		}
	}

	private void sendSizeChange(int sizeDelta){
		NetworkManager.sendToServer(ServerboundPackets.createPacketFusionControlSize(sizeDelta, blockEntity.getPos()));
		//Reset the multiblock as it will be wrong now.
		if(blockEntity.renderMultiblock != null){
			updateMultiBlockRender();
		}
	}

	private void updateMultiBlockRender(){
		final Multiblock multiblock = new Multiblock();
		BlockState coil = TRContent.Machine.FUSION_COIL.block.getDefaultState();

		List<BlockPos> coils = Torus.generate(new BlockPos(0, 0, 0), blockEntity.size);
		coils.forEach(pos -> addComponent(pos.getX(), pos.getY(), pos.getZ(), coil, multiblock));

		blockEntity.renderMultiblock = multiblock;
	}
	
	public void addComponent(final int x, final int y, final int z, final BlockState blockState, final Multiblock multiblock) {
		multiblock.addComponent(new BlockPos(x, y, z), blockState);
	}

	public Optional<Pair<Integer, Integer>> getCoilStackCount(){
		if(!Torus.TORUS_SIZE_MAP.containsKey(blockEntity.size)){
			return Optional.empty();
		}
		int count = Torus.TORUS_SIZE_MAP.get(blockEntity.size);
		return Optional.of(Pair.of(count / 64, count % 64));
	}
}
