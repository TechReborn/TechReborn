package techreborn.client.gui.upgrades;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.gui.GuiBase;

/**
 * Created by Mark on 12/04/2017.
 */
public class GuiSideConfig extends GuiBase {

	public GuiSideConfig(EntityPlayer player, TileEntity tile, BuiltContainer container) {
		super(player, tile, container);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int mouseX, final int mouseY) {
		super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.BACKGROUND;


	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.FOREGROUND;

	}

	@Override
	protected void drawTitle() {
		drawCentredString("Side Configuration", 6, 4210752, Layer.FOREGROUND);
	}

	@Override
	public boolean tryAddUpgrades() {
		return false;
	}
}
