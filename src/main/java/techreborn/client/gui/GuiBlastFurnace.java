package techreborn.client.gui;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;

import reborncore.client.gui.GuiUtil;
import reborncore.client.multiblock.Multiblock;
import reborncore.client.multiblock.MultiblockRenderEvent;
import reborncore.client.multiblock.MultiblockSet;
import reborncore.common.misc.Location;
import reborncore.common.multiblock.CoordTriplet;

import techreborn.init.ModBlocks;
import techreborn.proxies.ClientProxy;
import techreborn.tiles.multiblock.TileBlastFurnace;

import java.io.IOException;

public class GuiBlastFurnace extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation("techreborn",
			"textures/gui/industrial_blast_furnace.png");

	TileBlastFurnace blastfurnace;

	boolean hasMultiBlock;

	public GuiBlastFurnace(final EntityPlayer player, final TileBlastFurnace blastFurnace) {
		super(blastFurnace.createContainer(player));
		this.xSize = 176;
		this.ySize = 167;
		this.blastfurnace = blastFurnace;
	}

	@Override
	public void initGui() {

		this.hasMultiBlock = this.blastfurnace.getCachedHeat() != 0;
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		final GuiButton button = new GuiButton(212, k + 4, l + 6, 20, 20, "");
		this.buttonList.add(button);
		super.initGui();
		final CoordTriplet coordinates = new CoordTriplet(
				this.blastfurnace.getPos().getX() - EnumFacing.getFront(this.blastfurnace.getFacingInt()).getFrontOffsetX() * 2,
				this.blastfurnace.getPos().getY() - 1, this.blastfurnace.getPos().getZ()
				- EnumFacing.getFront(this.blastfurnace.getFacingInt()).getFrontOffsetZ() * 2);
		if (coordinates.equals(MultiblockRenderEvent.anchor) && this.blastfurnace.getHeat() != 0) {
			ClientProxy.multiblockRenderEvent.setMultiblock(null);
			button.displayString = "B";
		} else {
			button.displayString = "A";
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float p_146976_1_, final int p_146976_2_, final int p_146976_3_) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiBlastFurnace.texture);
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		if (this.blastfurnace.getCachedHeat() == 0) {
			GuiUtil.drawTooltipBox(k + 30, l + 50 + 12 - 0, 114, 10);
			this.fontRendererObj.drawString(I18n.translateToLocal("techreborn.message.missingmultiblock"), k + 38,
					l + 52 + 12 - 0, -1);
		}

		int j = 0;
		this.mc.getTextureManager().bindTexture(GuiBlastFurnace.texture);
		j = this.blastfurnace.getProgressScaled(24);
		if (j > 0) {
			this.drawTexturedModalRect(k + 64, l + 37, 176, 14, j + 1, 16);
		}

		j = this.blastfurnace.getEnergyScaled(12);
		if (j > 0) {
			this.drawTexturedModalRect(k + 9, l + 36 + 12 - j, 176, 12 - j, 14, j + 2);
		}

	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int p_146979_1_, final int p_146979_2_) {
		super.drawGuiContainerForegroundLayer(p_146979_1_, p_146979_2_);
		final String name = I18n.translateToLocal("tile.techreborn.blastfurnace.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6,
				4210752);
		if (this.blastfurnace.getCachedHeat() != 0) {
			this.fontRendererObj.drawString("Current Heat: " + this.blastfurnace.getCachedHeat(), 40, 60, 4210752);
		}
		this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory", new Object[0]), 8,
				this.ySize - 96 + 2, 4210752);
	}

	@Override
	public void actionPerformed(final GuiButton button) throws IOException {
		super.actionPerformed(button);
		if (button.id == 212) {
			if (ClientProxy.multiblockRenderEvent.currentMultiblock == null) {
				{// This code here makes a basic multiblock and then sets to the
					// selected one.
					final Multiblock multiblock = new Multiblock();
					this.addComponent(0, 0, 0, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(1, 0, 0, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(0, 0, 1, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(-1, 0, 0, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(0, 0, -1, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(-1, 0, -1, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(-1, 0, 1, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(1, 0, -1, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(1, 0, 1, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);

					this.addComponent(1, 1, 0, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(0, 1, 1, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(-1, 1, 0, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(0, 1, -1, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(-1, 1, -1, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(-1, 1, 1, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(1, 1, -1, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(1, 1, 1, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);

					this.addComponent(1, 2, 0, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(0, 2, 1, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(-1, 2, 0, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(0, 2, -1, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(-1, 2, -1, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(-1, 2, 1, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(1, 2, -1, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(1, 2, 1, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);

					this.addComponent(0, 3, 0, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(1, 3, 0, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(0, 3, 1, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(-1, 3, 0, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(0, 3, -1, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(-1, 3, -1, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(-1, 3, 1, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(1, 3, -1, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);
					this.addComponent(1, 3, 1, ModBlocks.MACHINE_CASINGS.getDefaultState(), multiblock);

					final MultiblockSet set = new MultiblockSet(multiblock);
					ClientProxy.multiblockRenderEvent.setMultiblock(set);
					ClientProxy.multiblockRenderEvent.parent = new Location(this.blastfurnace.getPos().getX(),
							this.blastfurnace.getPos().getY(), this.blastfurnace.getPos().getZ(), this.blastfurnace.getWorld());
					MultiblockRenderEvent.anchor = new BlockPos(
							this.blastfurnace.getPos().getX()
							- EnumFacing.getFront(this.blastfurnace.getFacingInt()).getFrontOffsetX() * 2,
							this.blastfurnace.getPos().getY() - 1, this.blastfurnace.getPos().getZ()
							- EnumFacing.getFront(this.blastfurnace.getFacingInt()).getFrontOffsetZ() * 2);
				}
				button.displayString = "A";
			} else {
				ClientProxy.multiblockRenderEvent.setMultiblock(null);
				button.displayString = "B";
			}
		}
	}

	public void addComponent(final int x, final int y, final int z, final IBlockState blockState, final Multiblock multiblock) {
		multiblock.addComponent(new BlockPos(x, y, z), blockState);
	}

}
