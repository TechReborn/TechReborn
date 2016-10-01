package techreborn.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import techreborn.client.container.ContainerAlloyFurnace;
import techreborn.tiles.TileAlloyFurnace;

public class GuiAlloyFurnace extends GuiTechReborn {
	TileAlloyFurnace alloyfurnace;
	ContainerAlloyFurnace containerAlloyFurnace;

	public GuiAlloyFurnace(EntityPlayer player, TileAlloyFurnace tileAlloyFurnace) {
		super(new ContainerAlloyFurnace(tileAlloyFurnace, player));
		this.xSize = 176;
		this.ySize = 167;
		this.alloyfurnace = tileAlloyFurnace;
		this.containerAlloyFurnace = (ContainerAlloyFurnace) this.inventorySlots;
	}

	@Override
	public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawBasicMachine(partialTicks, mouseX, mouseY);
        builder.drawSlot(this, guiLeft + 46, guiTop + 16);
        builder.drawSlot(this, guiLeft + 64, guiTop + 16);
        builder.drawOutputSlot(this, guiLeft + 112, guiTop + 30);
        builder.drawSlot(this, guiLeft + 55, guiTop + 52);

        builder.drawProgressBar(this, alloyfurnace.getCookProgressScaled(24), guiLeft + 84, guiTop + 34);
        builder.drawBurnBar(this, alloyfurnace.getBurnTimeRemainingScaled(13), guiLeft + 57, guiTop + 34);
    }
}
