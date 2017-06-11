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
import reborncore.client.multiblock.MultiblockSet;
import reborncore.common.misc.Location;
import techreborn.client.ClientMultiBlocks;
import techreborn.client.container.ContainerVacuumFreezer;
import techreborn.proxies.ClientProxy;
import techreborn.tiles.TileVacuumFreezer;
import org.lwjgl.opengl.GL11;

public class GuiVacuumFreezer extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/vacuum_freezer.png");

    TileVacuumFreezer crafter;
    ContainerVacuumFreezer containerVacuumFreezer;

    public GuiVacuumFreezer(EntityPlayer player, TileVacuumFreezer tilealloysmelter) {
        super(new ContainerVacuumFreezer(tilealloysmelter, player));
        this.xSize = 176;
        this.ySize = 167;
        crafter = tilealloysmelter;
        this.containerVacuumFreezer = (ContainerVacuumFreezer) this.inventorySlots;
    }

    @Override
    public void initGui() {
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        GuiButton button = new GuiButton(212, k + this.xSize - 24, l + 4, 20, 20, "");
        buttonList.add(button);
        super.initGui();
        ChunkCoordinates coordinates = new ChunkCoordinates(crafter.xCoord, crafter.yCoord - 5, crafter.zCoord);
        if(coordinates.equals(ClientProxy.multiblockRenderEvent.anchor)){
            ClientProxy.multiblockRenderEvent.setMultiblock(null);
            button.displayString = "B";
        } else {
            button.displayString = "A";
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(texture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        int j = 0;

        j = crafter.getProgressScaled(24);
        if (j > 0) {
            this.drawTexturedModalRect(k + 79, l + 37, 176, 14, j + 1, 16);
        }

        j = crafter.getEnergyScaled(12);
        if (j > 0) {
            this.drawTexturedModalRect(k + 26, l + 36 + 12 - j, 176, 12 - j, 14, j + 2);
        }

        if (containerVacuumFreezer.machineStatus == 0) {
            GuiUtil.drawTooltipBox(k + 30, l + 50 + 12 - 0, 114, 10);
            this.fontRendererObj.drawString(StatCollector.translateToLocal("techreborn.message.missingmultiblock"), k + 38, l + 52 + 12 - 0, -1);
        }
    }

    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
        String name = StatCollector.translateToLocal("tile.techreborn.vacuumfreezer.name");
        this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    public void actionPerformed(GuiButton button) {
        super.actionPerformed(button);
        if(button.id == 212){
            if(ClientProxy.multiblockRenderEvent.currentMultiblock == null){
                {//This code here makes a basic multiblock and then sets to the selected one.
                    MultiblockSet set = new MultiblockSet(ClientMultiBlocks.frezzer);
                    ClientProxy.multiblockRenderEvent.setMultiblock(set);
                    ClientProxy.multiblockRenderEvent.partent = new Location(crafter.xCoord, crafter.yCoord, crafter.zCoord, crafter.getWorldObj());
                    ClientProxy.multiblockRenderEvent.anchor = new ChunkCoordinates(crafter.xCoord , crafter.yCoord -5 , crafter.zCoord);
                }
                button.displayString = "A";
            } else {
                ClientProxy.multiblockRenderEvent.setMultiblock(null);
                button.displayString = "B";
            }
        }
    }
}
