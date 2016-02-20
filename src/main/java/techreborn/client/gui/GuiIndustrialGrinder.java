package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import techreborn.client.container.ContainerIndustrialGrinder;
import techreborn.tiles.TileIndustrialGrinder;

public class GuiIndustrialGrinder extends GuiContainer {

    public static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/industrial_grinder.png");

    TileIndustrialGrinder grinder;
    ContainerIndustrialGrinder containerGrinder;

    public GuiIndustrialGrinder(EntityPlayer player, TileIndustrialGrinder tilegrinder) {
        super(new ContainerIndustrialGrinder(tilegrinder, player));
        this.xSize = 176;
        this.ySize = 167;
        grinder = tilegrinder;
        containerGrinder = (ContainerIndustrialGrinder) this.inventorySlots;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        this.mc.getTextureManager().bindTexture(texture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        int j = 0;

        j = grinder.getProgressScaled(24);
        if (j > 0) {
            this.drawTexturedModalRect(k + 50, l + 36, 176, 14, j + 1, 16);
        }

        j = grinder.getEnergyScaled(12);
        if (j > 0) {
            this.drawTexturedModalRect(k + 132, l + 63 + 12 - j, 176, 12 - j, 14, j + 2);
        }

        if (containerGrinder.connectionStatus != 1) {
           // GuiDraw.drawTooltipBox(k + 30, l + 50 + 12 - j, 114, 10);
            this.fontRendererObj.drawString(StatCollector.translateToLocal("techreborn.message.missingmultiblock"), k + 38, l + 52 + 12 - j, -1);
        }

        //TODO 1.8 nope
//        if (grinder.tank.getFluidAmount() != 0) {
//            IIcon fluidIcon = grinder.tank.getFluid().getFluid().getIcon();
//            if (fluidIcon != null) {
//                this.mc.renderEngine.bindTexture(texture);
//                drawTexturedModalRect(k + 7, l + 15, 176, 31, 20, 55);
//
//                this.mc.renderEngine
//                        .bindTexture(TextureMap.locationBlocksTexture);
//                int liquidHeight = grinder.tank.getFluidAmount() * 47
//                        / grinder.tank.getCapacity();
//                GuiUtil.drawRepeated(fluidIcon, k + 11, l + 19 + 47
//                        - liquidHeight, 12.0D, liquidHeight, this.zLevel);
//
//                this.mc.renderEngine.bindTexture(texture);
//
//                drawTexturedModalRect(k + 11, l + 19, 176, 86, 12, 47);
//            }
//        }
    }

    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
        String name = StatCollector.translateToLocal("tile.techreborn.industrialgrinder.name");
        this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }

}
