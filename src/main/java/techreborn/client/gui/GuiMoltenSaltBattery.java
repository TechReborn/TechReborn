package techreborn.client.gui;

import java.awt.Color;
import java.util.Optional;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import org.apache.commons.lang3.tuple.Pair;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.builder.widget.GuiButtonExtended;
import reborncore.client.gui.builder.widget.GuiButtonUpDown;
import reborncore.client.gui.builder.widget.GuiButtonUpDown.UpDownButtonType;
import reborncore.client.gui.guibuilder.GuiBuilder;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.common.network.NetworkManager;
import reborncore.common.powerSystem.PowerSystem;
import techreborn.blockentity.storage.energy.MoltenSaltBatteryBlockEntity;
import techreborn.packets.ServerboundPackets;

public class GuiMoltenSaltBattery extends GuiBase<BuiltScreenHandler> {

	private final MoltenSaltBatteryBlockEntity blockEntity;

	public GuiMoltenSaltBattery(int syncID, final PlayerEntity player, final MoltenSaltBatteryBlockEntity blockEntity) {
		super(player, blockEntity, blockEntity.createScreenHandler(syncID, player));
		this.blockEntity = blockEntity;
	}

	@Override
	public void init() {
		super.init();

		addDrawableChild(new GuiButtonUpDown(x + 82, y + 19, this, (ButtonWidget buttonWidget) -> sendRadiusDelta(1), UpDownButtonType.FORWARD));
		addDrawableChild(new GuiButtonUpDown(x + 94, y + 19, this, (ButtonWidget buttonWidget) -> sendRadiusDelta(-1), UpDownButtonType.REWIND));

		addDrawableChild(new GuiButtonUpDown(x + 82, y + 32, this, (ButtonWidget buttonWidget) -> sendLayersDelta(1), UpDownButtonType.FORWARD));
		addDrawableChild(new GuiButtonUpDown(x + 94, y + 32, this, (ButtonWidget buttonWidget) -> sendLayersDelta(-1), UpDownButtonType.REWIND));
	}

	@Override
	protected void drawBackground(MatrixStack matrixStack, final float partialTicks, final int mouseX, final int mouseY) {
		super.drawBackground(matrixStack, partialTicks, mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.BACKGROUND;

		builder.drawJEIButton(matrixStack, this, 158, 5, layer);

		builder.drawHologramButton(matrixStack, this, 6, 4, mouseX, mouseY, layer);
		addHologramButton(6, 4, 212, layer).clickHandler(this::hologramToggle);
	}

	@Override
	protected void drawForeground(MatrixStack matrixStack, final int mouseX, final int mouseY) {
		super.drawForeground(matrixStack, mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.FOREGROUND;

		drawText(matrixStack, new LiteralText("Radius: ").append(String.valueOf(blockEntity.getRadius())), 28, 21, Color.BLACK.getRGB(), layer);
		drawText(matrixStack, new LiteralText("Layers: ").append(String.valueOf(blockEntity.getLayers())), 28, 35, Color.BLACK.getRGB(), layer);

		drawText(matrixStack, new LiteralText("Cells: ").append(String.valueOf(blockEntity.getCells())), 28, 49, Color.BLACK.getRGB(), layer);

		if (blockEntity.isFormed()) {
			String capacity = PowerSystem.getLocalizedPower(blockEntity.getBaseMaxPower());
			drawText(matrixStack, new LiteralText("Capacity: ").append(capacity),
				  28, 63, Color.BLACK.getRGB(), layer);
		} else {
			String estCapacity = PowerSystem.getLocalizedPower(blockEntity.getEstimatedCapacity());
			drawText(matrixStack, new LiteralText("Est. Capacity: ").append(estCapacity), 28, 62, Color.BLACK.getRGB(), layer);
			drawCentredText(matrixStack, new LiteralText("Multi-block is not valid!"), 75, Color.RED.getRGB(), layer);
		}

		builder.drawMultiEnergyBar(matrixStack, this, 9, 19, (int) this.blockEntity.getEnergy(), (int) this.blockEntity.getMaxStoredPower(), mouseX, mouseY, 0, layer);
	}

	public void hologramToggle(GuiButtonExtended button, double x, double y) {
		blockEntity.renderMultiblock ^= !hideGuiElements();
	}

	private void sendLayersDelta(int delta) {
		NetworkManager.sendToServer(ServerboundPackets.createPacketBatteryDimensions(blockEntity, 0, delta));
	}

	private void sendRadiusDelta(int delta) {
		NetworkManager.sendToServer(ServerboundPackets.createPacketBatteryDimensions(blockEntity, delta, 0));
	}
}
