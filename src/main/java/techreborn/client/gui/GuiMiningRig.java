package techreborn.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.text.LiteralText;
import reborncore.client.RenderUtil;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.builder.widget.GuiButtonExtended;
import reborncore.client.gui.guibuilder.GuiBuilder;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.common.fluid.FluidValue;
import reborncore.common.fluid.container.FluidInstance;
import reborncore.common.util.Tank;
import techreborn.blockentity.machine.multiblock.MiningRigBlockEntity;

import java.util.Random;

public class GuiMiningRig extends GuiBase<BuiltScreenHandler> {

	private final MiningRigBlockEntity blockEntity;
	private boolean isValid = false;

	private int tempInt = 0;

	public GuiMiningRig(int syncID, final PlayerEntity player, final MiningRigBlockEntity blockEntity) {
		super(player, blockEntity, blockEntity.createScreenHandler(syncID, player));
		this.blockEntity = blockEntity;

		if(blockEntity.isMultiblockValid()){
			isValid = true;
			blockEntity.renderMultiblock = false;
		}
	}

	@Override
	protected void drawBackground(MatrixStack matrixStack, final float f, final int mouseX, final int mouseY) {
		super.drawBackground(matrixStack, f, mouseX, mouseY);
		final GuiBase.Layer layer = Layer.BACKGROUND;

		if(!isValid){
			return;
		}

		// Fluid slot
		drawSlot(matrixStack,31,72, layer);

		// Energy slot
		drawSlot(matrixStack, 8, 72, layer);

		// Drill head slot
		drawSlot(matrixStack,140,30, layer);

		// Singular inventory slot
		drawSlot(matrixStack,140,70, layer);
	}

	@Override
	protected void drawForeground(MatrixStack matrixStack, final int mouseX, final int mouseY) {
		super.drawForeground(matrixStack, mouseX, mouseY);
		final GuiBase.Layer layer = Layer.FOREGROUND;

		if(this.hideGuiElements()){
			return;
		}

		if (!isValid) {
			builder.drawMultiblockMissingBar(matrixStack, this, layer);
			addHologramButton(76, 56, 212, layer).clickHandler(this::onClick);
			builder.drawHologramButton(matrixStack, this, 76, 56, mouseX, mouseY, layer);
			return;
		}

		// Valid block and working

		// Black background, should probably move to background
		RenderUtil.drawGradientRect(0, 50, 15, 120,  89, 0xFF000000, 0xFF000000);


		drawText(matrixStack,new LiteralText("Drill"), 138,19,4210752, layer);

		// Status screen
		matrixStack.push();
		matrixStack.scale(0.6f,0.6f,0.6f);
		drawText(matrixStack,new LiteralText("Status: Running"), 87,30,0xFFFFFFFF, layer );
		matrixStack.pop();

		// Tank and energy meters
		Tank tank = blockEntity.getTank();
		if(tank != null) {
			builder.drawTank(matrixStack, this, 27, 13, mouseX, mouseY,
					tank.getFluidInstance(),
					tank.getCapacity(),
					tank.isEmpty(), layer);
		}

		builder.drawMultiEnergyBar(matrixStack, this, 9, 19, (int) blockEntity.getEnergy(), (int) blockEntity.getMaxPower(), mouseX, mouseY, 0, layer);

		// Mining progress
		builder.drawProgressBar(matrixStack, this, tempInt, 100, 143, 49, mouseX, mouseY, GuiBuilder.ProgressDirection.DOWN, layer);
		tempInt++;
		if(tempInt > 100){
			tempInt = 0;
		}


	}

	public void onClick(GuiButtonExtended button, Double mouseX, Double mouseY) {
		blockEntity.renderMultiblock ^= !hideGuiElements();
	}
}