package techreborn.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import reborncore.common.powerSystem.PowerSystem;
import techreborn.tiles.storage.TileMFE;

public class GuiMFE extends GuiBase {

	TileMFE mfe;

	public GuiMFE(final EntityPlayer player, final TileMFE mfe) {
		super(player, mfe, mfe.createContainer(player));
		this.mfe = mfe;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int mouseX, final int mouseY) {
		super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
		final Layer layer = Layer.BACKGROUND;

		this.drawSlot(62, 45, layer);
		this.drawSlot(98, 45, layer);
		this.drawArmourSlots(8, 18, layer);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		final Layer layer = Layer.FOREGROUND;

		GlStateManager.pushMatrix();
		GlStateManager.scale(0.6, 0.6, 1);
		this.drawCentredString(PowerSystem.getLocaliszedPowerFormattedNoSuffix((int) this.mfe.getEnergy()) + "/" + PowerSystem.getLocaliszedPowerFormattedNoSuffix((int) this.mfe.getMaxPower()) + " " + PowerSystem.getDisplayPower().abbreviation, 35, 0, 58, layer);
		GlStateManager.popMatrix();

		this.builder.drawMultiEnergyBar(this, 81, 28, (int) this.mfe.getEnergy(), (int) this.mfe.getMaxPower(), mouseX, mouseY, 0, layer);
	}
}
