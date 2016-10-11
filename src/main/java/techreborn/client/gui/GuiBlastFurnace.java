package techreborn.client.gui;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
import techreborn.client.container.ContainerBlastFurnace;
import techreborn.init.ModBlocks;
import techreborn.proxies.ClientProxy;
import techreborn.tiles.multiblock.TileBlastFurnace;

import java.io.IOException;

public class GuiBlastFurnace extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation("techreborn",
		"textures/gui/industrial_blast_furnace.png");

	TileBlastFurnace blastfurnace;

	ContainerBlastFurnace containerBlastFurnace;
	boolean hasMultiBlock;

	public GuiBlastFurnace(EntityPlayer player, TileBlastFurnace tileblastfurnace) {
		super(new ContainerBlastFurnace(tileblastfurnace, player));
		this.xSize = 176;
		this.ySize = 167;
		blastfurnace = tileblastfurnace;
		this.containerBlastFurnace = (ContainerBlastFurnace) this.inventorySlots;
	}

	@Override
	public void initGui() {

		hasMultiBlock = containerBlastFurnace.heat != 0;
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		GuiButton button = new GuiButton(212, k + 4, l + 6, 20, 20, "");
		buttonList.add(button);
		super.initGui();
		CoordTriplet coordinates = new CoordTriplet(
			blastfurnace.getPos().getX() - (EnumFacing.getFront(blastfurnace.getFacingInt()).getFrontOffsetX() * 2),
			blastfurnace.getPos().getY() - 1, blastfurnace.getPos().getZ()
			- (EnumFacing.getFront(blastfurnace.getFacingInt()).getFrontOffsetZ() * 2));
		if (coordinates.equals(ClientProxy.multiblockRenderEvent.anchor) && blastfurnace.getHeat() != 0) {
			ClientProxy.multiblockRenderEvent.setMultiblock(null);
			button.displayString = "B";
		} else {
			button.displayString = "A";
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(texture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		if (hasMultiBlock) {
			GuiUtil.drawTooltipBox(k + 30, l + 50 + 12 - 0, 114, 10);
			this.fontRendererObj.drawString(I18n.translateToLocal("techreborn.message.missingmultiblock"), k + 38,
				l + 52 + 12 - 0, -1);
		}

		int j = 0;
		this.mc.getTextureManager().bindTexture(texture);
		j = blastfurnace.getProgressScaled(24);
		if (j > 0) {
			this.drawTexturedModalRect(k + 64, l + 37, 176, 14, j + 1, 16);
		}

		j = blastfurnace.getEnergyScaled(12);
		if (j > 0) {
			this.drawTexturedModalRect(k + 9, l + 36 + 12 - j, 176, 12 - j, 14, j + 2);
		}

	}

	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		super.drawGuiContainerForegroundLayer(p_146979_1_, p_146979_2_);
		String name = I18n.translateToLocal("tile.techreborn.blastfurnace.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6,
			4210752);
		if (containerBlastFurnace.heat != 0) {
			this.fontRendererObj.drawString("Current Heat: " + containerBlastFurnace.heat, 40, 60, 4210752);
		}
		this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory", new Object[0]), 8,
			this.ySize - 96 + 2, 4210752);
	}

	@Override
	public void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if (button.id == 212) {
			if (ClientProxy.multiblockRenderEvent.currentMultiblock == null) {
				{
					//WTF was I doing again?
					Multiblock multiblock = new Multiblock();
					addComponent(0, 0, 0, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(1, 0, 0, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(0, 0, 1, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(-1, 0, 0, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(0, 0, -1, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(-1, 0, -1, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(-1, 0, 1, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(1, 0, -1, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(1, 0, 1, ModBlocks.MachineCasing.getDefaultState(), multiblock);

					addComponent(1, 1, 0, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(0, 1, 1, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(-1, 1, 0, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(0, 1, -1, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(-1, 1, -1, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(-1, 1, 1, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(1, 1, -1, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(1, 1, 1, ModBlocks.MachineCasing.getDefaultState(), multiblock);

					addComponent(1, 2, 0, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(0, 2, 1, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(-1, 2, 0, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(0, 2, -1, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(-1, 2, -1, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(-1, 2, 1, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(1, 2, -1, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(1, 2, 1, ModBlocks.MachineCasing.getDefaultState(), multiblock);

					addComponent(0, 3, 0, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(1, 3, 0, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(0, 3, 1, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(-1, 3, 0, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(0, 3, -1, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(-1, 3, -1, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(-1, 3, 1, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(1, 3, -1, ModBlocks.MachineCasing.getDefaultState(), multiblock);
					addComponent(1, 3, 1, ModBlocks.MachineCasing.getDefaultState(), multiblock);

					addComponent(1, 4, 0, ModBlocks.LesuStorage.getDefaultState(), multiblock);
					addComponent(1, 4, 1, ModBlocks.Lesu.getDefaultState(), multiblock);
					addComponent(1, 4, 2, ModBlocks.AlloyFurnace.getDefaultState(), multiblock);

					MultiblockSet set = new MultiblockSet(multiblock);
					ClientProxy.multiblockRenderEvent.setMultiblock(set);
					ClientProxy.multiblockRenderEvent.parent = new Location(blastfurnace.getPos().getX(),
						blastfurnace.getPos().getY(), blastfurnace.getPos().getZ(), blastfurnace.getWorld());
					MultiblockRenderEvent.anchor = new BlockPos(
						blastfurnace.getPos().getX()
							- (EnumFacing.getFront(blastfurnace.getFacingInt()).getFrontOffsetX() * 2),
						blastfurnace.getPos().getY() - 1, blastfurnace.getPos().getZ()
						- (EnumFacing.getFront(blastfurnace.getFacingInt()).getFrontOffsetZ() * 2));
				}
				button.displayString = "A";
			} else {
				ClientProxy.multiblockRenderEvent.setMultiblock(null);
				button.displayString = "B";
			}
		}
	}
	
	public void addComponent(int x, int y, int z, IBlockState blockState, Multiblock multiblock){
		multiblock.addComponent(new BlockPos(x, y, z), blockState);
	}
	
	
}
