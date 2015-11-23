package techreborn.client.render;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ic2.api.info.IC2Classic;
import ic2.api.item.IC2Items;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import reborncore.common.misc.Functions;
import reborncore.common.misc.vecmath.Vecs3d;
import reborncore.common.misc.vecmath.Vecs3dCube;
import techreborn.client.IconSupplier;
import techreborn.partSystem.parts.CablePart;


public class RenderCablePart {


    public static void renderBox(Vecs3dCube cube, Block block, Tessellator tessellator, RenderBlocks renderblocks, IIcon texture, Double xD, Double yD, double zD, float thickness) {
        block.setBlockBounds((float) cube.getMinX(), (float) cube.getMinY(), (float) cube.getMinZ(), (float) cube.getMaxX() + thickness, (float) cube.getMaxY() + thickness, (float) cube.getMaxZ() + thickness);
        renderblocks.setRenderBoundsFromBlock(block);
        tessellator.setColorOpaque_F(0.5F, 0.5F, 0.5F);
        renderblocks.renderFaceYNeg(block, xD, yD, zD, texture);
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        renderblocks.renderFaceYPos(block, xD, yD, zD, texture);
        tessellator.setColorOpaque_F(0.8F, 0.8F, 0.8F);
        renderblocks.renderFaceZNeg(block, xD, yD, zD, texture);
        renderblocks.renderFaceZPos(block, xD, yD, zD, texture);
        tessellator.setColorOpaque_F(0.6F, 0.6F, 0.6F);
        renderblocks.renderFaceXNeg(block, xD, yD, zD, texture);
        renderblocks.renderFaceXPos(block, xD, yD, zD, texture);
    }


    @SideOnly(Side.CLIENT)
    public static boolean renderStatic(Vecs3d translation, int pass, CablePart part) {
        Tessellator tessellator = Tessellator.instance;
        IIcon texture = getIconFromType(part.type);
        RenderBlocks renderblocks = RenderBlocks.getInstance();
        double xD = part.xCoord;
        double yD = part.yCoord;
        double zD = part.zCoord;
        Block block = part.getBlockType();
        tessellator.setBrightness(block.getMixedBrightnessForBlock(part.getWorld(), part.getX(), part.getY(), part.getZ()));
        renderBox(part.boundingBoxes[6], block, tessellator, renderblocks, texture, xD, yD, zD, 0F);
        for (ForgeDirection direction : ForgeDirection.values()) {
            if (part.connectedSides.get(direction) != null) {
                renderBox(part.boundingBoxes[Functions.getIntDirFromDirection(direction)], block, tessellator, renderblocks, texture, xD, yD, zD, 0f);
            }
        }
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        renderblocks.setRenderBoundsFromBlock(block);
        return true;
    }

    @SideOnly(Side.CLIENT)
    public static IIcon getIconFromType(int cableType) {
        if (IC2Classic.getLoadedIC2Type() == IC2Classic.IC2Type.SpeigersClassic) {
            return Block.getBlockFromItem(IC2Items.getItem("copperCableBlock").getItem()).getIcon(0, cableType * 16);
        }
        IIcon p = null;
        switch (cableType) {
            case 0:
                p = IconSupplier.insulatedCopperCable;
                break;
            case 1:
                p = IconSupplier.copperCable;
                break;
            case 2:
                p = IconSupplier.goldCable;
                break;
            case 3:
                p = IconSupplier.insulatedGoldCable;
                break;
            case 4:
                p = IconSupplier.doubleInsulatedGoldCable;
                break;
            case 5:
                p = IconSupplier.ironCable;
                break;
            case 6:
                p = IconSupplier.insulatedIronCable;
                break;
            case 7:
                p = IconSupplier.doubleInsulatedIronCable;
                break;
            case 8:
                p = IconSupplier.trippleInsulatedIronCable;
                break;
            case 9:
                p = IconSupplier.glassFiberCable;
                break;
            case 10:
                p = IconSupplier.tinCable;
                break;
            case 11:
                p = IconSupplier.detectorCableBlock;//Detector
                break;
            case 12:
                p = IconSupplier.splitterCableBlock;// Splitter
                break;
            case 13:
                p = IconSupplier.insulatedtinCableBlock;
                break;
            case 14:
                p = IconSupplier.copperCable; // unused?
        }

        return p;
    }
}
