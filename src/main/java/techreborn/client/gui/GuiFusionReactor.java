package techreborn.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;

import reborncore.client.multiblock.MultiblockRenderEvent;
import reborncore.client.multiblock.MultiblockSet;
import reborncore.common.misc.Location;
import reborncore.common.powerSystem.PowerSystem;

import techreborn.client.ClientMultiBlocks;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.proxies.ClientProxy;
import techreborn.tiles.fusionReactor.TileEntityFusionController;

import java.io.IOException;

public class GuiFusionReactor extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation("techreborn",
			"textures/gui/fusion_reactor.png");

	TileEntityFusionController fusionController;

	public GuiFusionReactor(final EntityPlayer player, final TileEntityFusionController fusion) {
		super(new ContainerBuilder("fusionreactor").player(player.inventory).inventory(8, 84).hotbar(8, 142)
				.addInventory().tile(fusion).slot(0, 88, 17).slot(1, 88, 53).outputSlot(2, 148, 35).syncEnergyValue()
				.syncIntegerValue(fusion::getCoilStatus, fusion::setCoilStatus)
				.syncIntegerValue(fusion::getCrafingTickTime, fusion::setCrafingTickTime)
				.syncIntegerValue(fusion::getFinalTickTime, fusion::setFinalTickTime)
				.syncIntegerValue(fusion::getNeededPower, fusion::setNeededPower).addInventory().create());
		this.fusionController = fusion;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int p_146979_1_, final int p_146979_2_) {
		final String name = I18n.translateToLocal("tile.techreborn.fusioncontrolcomputer.name");
		this.fontRendererObj.drawString(name, 87, 6, 4210752);
		this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory", new Object[0]), 8,
				this.ySize - 96 + 2, 4210752);

		this.fontRendererObj.drawString(PowerSystem.getLocaliszedPower(this.fusionController.getEnergy()), 11, 8,
				16448255);
		this.fontRendererObj.drawString("Coils: " + (this.fusionController.getCoilStatus() == 1 ? "Yes" : "No"), 11, 16,
				16448255);
		if (this.fusionController.getNeededPower() > 1 && this.fusionController.getCrafingTickTime() < 1)
			this.fontRendererObj.drawString("Start: "
					+ this.percentage(this.fusionController.getNeededPower(), (int) this.fusionController.getEnergy())
					+ "%", 11, 24, 16448255);

	}

	@Override
	public void initGui() {
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		final GuiButton button = new GuiButton(212, k + this.xSize - 24, l + 4, 20, 20, "");
		this.buttonList.add(button);
		super.initGui();
		final BlockPos coordinates = new BlockPos(
				this.fusionController.getPos().getX()
				- EnumFacing.getFront(this.fusionController.getFacingInt()).getFrontOffsetX() * 2,
				this.fusionController.getPos().getY() - 1, this.fusionController.getPos().getZ()
				- EnumFacing.getFront(this.fusionController.getFacingInt()).getFrontOffsetZ() * 2);
		if (coordinates.equals(MultiblockRenderEvent.anchor)) {
			ClientProxy.multiblockRenderEvent.setMultiblock(null);
			button.displayString = "B";
		} else {
			button.displayString = "A";
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float p_146976_1_, final int p_146976_2_,
			final int p_146976_3_) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiFusionReactor.texture);
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		this.drawTexturedModalRect(k + 88, l + 36, 176, 0, 14, 14);

		// progressBar
		this.drawTexturedModalRect(k + 111, l + 34, 176, 14, this.fusionController.getProgressScaled(), 16);

	}

	public int percentage(final int MaxValue, final int CurrentValue) {
		if (CurrentValue == 0)
			return 0;
		return (int) (CurrentValue * 100.0f / MaxValue);
	}

	@Override
	public void actionPerformed(final GuiButton button) throws IOException {
		super.actionPerformed(button);
		if (button.id == 212) {
			if (ClientProxy.multiblockRenderEvent.currentMultiblock == null) {
				// This code here makes a basic multiblock and then sets to
				// theselected one.
				final MultiblockSet set = new MultiblockSet(ClientMultiBlocks.reactor);
				ClientProxy.multiblockRenderEvent.setMultiblock(set);
				ClientProxy.multiblockRenderEvent.parent = new Location(this.fusionController.getPos().getX(),
						this.fusionController.getPos().getY(), this.fusionController.getPos().getZ(),
						this.fusionController.getWorld());
				MultiblockRenderEvent.anchor = new BlockPos(this.fusionController.getPos().getX(),
						this.fusionController.getPos().getY() - 1, this.fusionController.getPos().getZ());

				button.displayString = "A";
			} else {
				ClientProxy.multiblockRenderEvent.setMultiblock(null);
				button.displayString = "B";
			}
		}
	}
}
