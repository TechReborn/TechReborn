package techreborn.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import reborncore.client.gui.GuiUtil;
import reborncore.client.multiblock.Multiblock;
import reborncore.client.multiblock.MultiblockSet;
import reborncore.common.misc.Location;
import techreborn.client.container.ContainerBlastFurnace;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;
import techreborn.proxies.ClientProxy;
import techreborn.tiles.TileBlastFurnace;


public class GuiBlastFurnace extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/industrial_blast_furnace.png");

    TileBlastFurnace blastfurnace;

    ContainerBlastFurnace containerBlastFurnace;

    public GuiBlastFurnace(EntityPlayer player, TileBlastFurnace tileblastfurnace) {
        super(new ContainerBlastFurnace(tileblastfurnace, player));
        this.xSize = 176;
        this.ySize = 167;
        blastfurnace = tileblastfurnace;
        this.containerBlastFurnace = (ContainerBlastFurnace) this.inventorySlots;
    }

    @Override
    public void initGui() {
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        GuiButton button = new GuiButton(212, k + 4, l + 6, 20, 20, "");
        buttonList.add(button);
        super.initGui();
		ChunkCoordinates coordinates = new ChunkCoordinates(blastfurnace.xCoord - (ForgeDirection.getOrientation(blastfurnace.getRotation()).offsetX * 2), blastfurnace.yCoord - 1, blastfurnace.zCoord - (ForgeDirection.getOrientation(blastfurnace.getRotation()).offsetZ * 2));
		if(coordinates.equals(ClientProxy.multiblockRenderEvent.anchor) && blastfurnace.getHeat() != 0){
			ClientProxy.multiblockRenderEvent.setMultiblock(null);
			button.displayString = "B";
		} else {
			button.displayString = "A";
		}
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        this.mc.getTextureManager().bindTexture(texture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        if (containerBlastFurnace.heat == 0) {
            GuiUtil.drawTooltipBox(k + 30, l + 50 + 12 - 0, 114, 10);
            this.fontRendererObj.drawString(ModInfo.MISSING_MULTIBLOCK, k + 38, l + 52 + 12 - 0, -1);
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
        String name = StatCollector.translateToLocal("tile.techreborn.blastfurnace.name");
        this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
        if (containerBlastFurnace.heat != 0) {
            this.fontRendererObj.drawString("Current Heat: " + containerBlastFurnace.heat, 40, 60, 4210752);
        }
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }

	@Override
	public void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		if(button.id == 212){
			if(ClientProxy.multiblockRenderEvent.currentMultiblock == null){
				{//This code here makes a basic multiblock and then sets to the selected one.
					Multiblock multiblock = new Multiblock();
					multiblock.addComponent(0, 0, 0, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(1, 0, 0, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(0, 0, 1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(-1, 0, 0, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(0, 0, -1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(-1, 0, -1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(-1, 0, 1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(1, 0, -1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(1, 0, 1, ModBlocks.MachineCasing, 0);
					
					multiblock.addComponent(1, 1, 0, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(0, 1, 1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(-1, 1, 0, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(0, 1, -1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(-1, 1, -1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(-1, 1, 1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(1, 1, -1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(1, 1, 1, ModBlocks.MachineCasing, 0);
					
					multiblock.addComponent(1, 2, 0, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(0, 2, 1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(-1, 2, 0, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(0, 2, -1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(-1, 2, -1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(-1, 2, 1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(1, 2, -1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(1, 2, 1, ModBlocks.MachineCasing, 0);

					multiblock.addComponent(0, 3, 0, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(1, 3, 0, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(0, 3, 1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(-1, 3, 0, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(0, 3, -1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(-1, 3, -1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(-1, 3, 1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(1, 3, -1, ModBlocks.MachineCasing, 0);
					multiblock.addComponent(1, 3, 1, ModBlocks.MachineCasing, 0);
					
					MultiblockSet set = new MultiblockSet(multiblock);
					ClientProxy.multiblockRenderEvent.setMultiblock(set);
					ClientProxy.multiblockRenderEvent.partent = new Location(blastfurnace.xCoord, blastfurnace.yCoord, blastfurnace.zCoord, blastfurnace.getWorldObj());
					ClientProxy.multiblockRenderEvent.anchor = new ChunkCoordinates(blastfurnace.xCoord - (ForgeDirection.getOrientation(blastfurnace.getRotation()).offsetX * 2), blastfurnace.yCoord - 1, blastfurnace.zCoord - (ForgeDirection.getOrientation(blastfurnace.getRotation()).offsetZ * 2));
				}
				button.displayString = "A";
			} else {
				ClientProxy.multiblockRenderEvent.setMultiblock(null);
				button.displayString = "B";
			}
		}
	}
}
