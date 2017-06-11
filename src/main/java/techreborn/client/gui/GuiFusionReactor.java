package techreborn.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import reborncore.client.multiblock.Multiblock;
import reborncore.client.multiblock.MultiblockSet;
import reborncore.common.misc.Location;
import techreborn.client.ClientMultiBlocks;
import techreborn.client.container.ContainerFusionReactor;
import techreborn.init.ModBlocks;
import techreborn.proxies.ClientProxy;
import techreborn.tiles.fusionReactor.TileEntityFusionController;
import org.lwjgl.opengl.GL11;


public class GuiFusionReactor extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/fusion_reactor.png");

    ContainerFusionReactor containerFusionReactor;
    TileEntityFusionController fusionController;

    public GuiFusionReactor(EntityPlayer player,
                            TileEntityFusionController tileaesu) {
        super(new ContainerFusionReactor(tileaesu, player));
        containerFusionReactor = (ContainerFusionReactor) this.inventorySlots;
        this.fusionController = tileaesu;
    }

    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
        String name = StatCollector.translateToLocal("tile.techreborn.fusioncontrolcomputer.name");
        this.fontRendererObj.drawString(name, 87, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);

        this.fontRendererObj.drawString("EU: " + containerFusionReactor.energy, 11, 8, 16448255);
        this.fontRendererObj.drawString("Coils: " + (containerFusionReactor.coilStatus == 1 ? "Yes" : "No"), 11, 16, 16448255);
        if(containerFusionReactor.neededEU > 1 && containerFusionReactor.tickTime < 1)
            this.fontRendererObj.drawString("Start EU: " + percentage(containerFusionReactor.neededEU, containerFusionReactor.energy) + "%", 11, 24, 16448255);

    }

    @Override
    public void initGui() {
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        GuiButton button = new GuiButton(212, k + this.xSize - 24, l + 4, 20, 20, "");
        buttonList.add(button);
        super.initGui();
        ChunkCoordinates coordinates = new ChunkCoordinates(fusionController.xCoord - (ForgeDirection.getOrientation(fusionController.getRotation()).offsetX * 2), fusionController.yCoord - 1, fusionController.zCoord - (ForgeDirection.getOrientation(fusionController.getRotation()).offsetZ * 2));
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

        drawTexturedModalRect(k + 88, l + 36, 176, 0, 14, 14);

        //progressBar
        drawTexturedModalRect(k + 111, l + 34, 176, 14, containerFusionReactor.getProgressScaled(), 16);

    }

    public int percentage(int MaxValue, int CurrentValue) {
        if (CurrentValue == 0)
            return 0;
        return (int) ((CurrentValue * 100.0f) / MaxValue);
    }

    @Override
    public void actionPerformed(GuiButton button) {
        super.actionPerformed(button);
        if(button.id == 212){
            if(ClientProxy.multiblockRenderEvent.currentMultiblock == null){
                {//This code here makes a basic multiblock and then sets to the selected one.
                    MultiblockSet set = new MultiblockSet(ClientMultiBlocks.reactor);
                    ClientProxy.multiblockRenderEvent.setMultiblock(set);
                    ClientProxy.multiblockRenderEvent.partent = new Location(fusionController.xCoord, fusionController.yCoord, fusionController.zCoord, fusionController.getWorldObj());
                    ClientProxy.multiblockRenderEvent.anchor = new ChunkCoordinates(fusionController.xCoord , fusionController.yCoord -1 , fusionController.zCoord);
                }
                button.displayString = "A";
            } else {
                ClientProxy.multiblockRenderEvent.setMultiblock(null);
                button.displayString = "B";
            }
        }
    }
}
