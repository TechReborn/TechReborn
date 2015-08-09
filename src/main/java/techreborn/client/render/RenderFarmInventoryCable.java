package techreborn.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import techreborn.lib.Functions;
import techreborn.lib.vecmath.Vecs3d;
import techreborn.lib.vecmath.Vecs3dCube;
import techreborn.partSystem.parts.FarmInventoryCable;

import java.util.ArrayList;


public class RenderFarmInventoryCable {


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
    public static boolean renderStatic(Vecs3d translation, int pass, FarmInventoryCable part) {
        Tessellator tessellator = Tessellator.instance;
        IIcon stone = Blocks.stone.getIcon(0, 0);
        IIcon redstone = Blocks.redstone_block.getIcon(0, 0);
        IIcon quartz = Blocks.quartz_block.getIcon(0, 0);
        RenderBlocks renderblocks = RenderBlocks.getInstance();
        double xD = part.xCoord;
        double yD = part.yCoord;
        double zD = part.zCoord;
        Block block = part.getBlockType();
        tessellator.setBrightness(block.getMixedBrightnessForBlock(part.getWorld(), part.getX(), part.getY(), part.getZ()));
        Vecs3dCube cube = new Vecs3dCube(part.boundingBoxes[6].getMinX() + 0.1, part.boundingBoxes[6].getMinY() + 0.1, part.boundingBoxes[6].getMinZ() + 0.1, part.boundingBoxes[6].getMaxX() - 0.1, part.boundingBoxes[6].getMaxY() - 0.1, part.boundingBoxes[6].getMaxZ() - 0.1);
        renderBox(cube, block, tessellator, renderblocks, stone, xD, yD, zD, 0F);

        ArrayList<Vecs3dCube> cubes = new ArrayList<Vecs3dCube>();

        cube = new Vecs3dCube(0.15, 0.15, 0.15, 0.3, 0.3, 0.85);
        cubes.add(cube);

        cube = new Vecs3dCube(0.15, 0.15, 0.15, 0.85, 0.3, 0.3);
        cubes.add(cube);

        cube = new Vecs3dCube(0.15, 0.15, 0.15, 0.3, 0.85, 0.3);
        renderBox(cube, block, tessellator, renderblocks, redstone, xD, yD, zD, 0F);

        cube = new Vecs3dCube(0.85, 0.85, 0.85, 0.7, 0.15, 0.7);
        cubes.add(cube);

        cube = new Vecs3dCube(0.85, 0.85, 0.85, 0.7, 0.7, 0.15);
        cubes.add(cube);

        cube = new Vecs3dCube(0.85, 0.85, 0.85, 0.15, 0.7, 0.7);
        cubes.add(cube);

        cube = new Vecs3dCube(0.15, 0.85, 0.85, 0.3, 0.15, 0.7);
        cubes.add(cube);

        cube = new Vecs3dCube(0.85, 0.85, 0.15, 0.7, 0.15, 0.3);
        cubes.add(cube);

        cube = new Vecs3dCube(0.85, 0.15, 0.85, 0.7, 0.3, 0.15);
        cubes.add(cube);

        cube = new Vecs3dCube(0.85, 0.15, 0.85, 0.15, 0.3, 0.7);
        cubes.add(cube);

        cube = new Vecs3dCube(0.15, 0.85, 0.15, 0.85, 0.7, 0.3);
        cubes.add(cube);

        cube = new Vecs3dCube(0.15, 0.85, 0.15, 0.3, 0.7, 0.85);
        cubes.add(cube);

        for (Vecs3dCube vecs3dCube : cubes) {
            renderBox(vecs3dCube, block, tessellator, renderblocks, redstone, xD, yD, zD, 0f);
        }

        for (ForgeDirection direction : ForgeDirection.values()) {
            if (part.connectedSides.get(direction) != null) {
                renderBox(part.boundingBoxes[Functions.getIntDirFromDirection(direction)], block, tessellator, renderblocks, quartz, xD, yD, zD, 0f);
            }
        }
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        renderblocks.setRenderBoundsFromBlock(block);
        return true;
    }

}
