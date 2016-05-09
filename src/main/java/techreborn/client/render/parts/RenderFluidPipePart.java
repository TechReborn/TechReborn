package techreborn.client.render.parts;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.lwjgl.util.vector.Vector3f;
import reborncore.common.misc.vecmath.Vecs3dCube;
import techreborn.parts.fluidPipes.MultipartFluidPipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by modmuss50 on 09/05/2016.
 */
public class RenderFluidPipePart implements IBakedModel {

    private FaceBakery faceBakery = new FaceBakery();
    private TextureAtlasSprite texture;

    public RenderFluidPipePart() {
        texture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("techreborn:blocks/fluidPipes/fluidpipe");
    }

    public void addCubeToList(Vecs3dCube cube, ArrayList<BakedQuad> list, BlockPartFace face,
                              ModelRotation modelRotation, TextureAtlasSprite cubeTexture, EnumFacing dir) {
        BlockFaceUV uv = new BlockFaceUV(new float[]{(float) cube.getMinX(), (float) cube.getMinY(),
                (float) cube.getMaxX(), (float) cube.getMaxY()}, 0);
        face = new BlockPartFace(null, 0, "", uv);
        if (dir == EnumFacing.NORTH || dir == EnumFacing.SOUTH) {
            uv = new BlockFaceUV(new float[]{(float) cube.getMinZ(), (float) cube.getMinY(),
                    (float) cube.getMaxZ(), (float) cube.getMaxY()}, 0);
            face = new BlockPartFace(null, 0, "", uv);
        }
        list.add(faceBakery.makeBakedQuad(
                new Vector3f((float) cube.getMinX(), (float) cube.getMinY(), (float) cube.getMinZ()),
                new Vector3f((float) cube.getMaxX(), (float) cube.getMinY(), (float) cube.getMaxZ()), face, cubeTexture,
                EnumFacing.DOWN, modelRotation, null, true, true));// down
        list.add(faceBakery.makeBakedQuad(
                new Vector3f((float) cube.getMinX(), (float) cube.getMaxY(), (float) cube.getMinZ()),
                new Vector3f((float) cube.getMaxX(), (float) cube.getMaxY(), (float) cube.getMaxZ()), face, cubeTexture,
                EnumFacing.UP, modelRotation, null, true, true));// up
        list.add(faceBakery.makeBakedQuad(
                new Vector3f((float) cube.getMinX(), (float) cube.getMinY(), (float) cube.getMinZ()),
                new Vector3f((float) cube.getMaxX(), (float) cube.getMaxY(), (float) cube.getMaxZ()), face, cubeTexture,
                EnumFacing.NORTH, modelRotation, null, true, true));// north
        list.add(faceBakery.makeBakedQuad(
                new Vector3f((float) cube.getMinX(), (float) cube.getMinY(), (float) cube.getMaxZ()),
                new Vector3f((float) cube.getMaxX(), (float) cube.getMaxY(), (float) cube.getMaxZ()), face, cubeTexture,
                EnumFacing.SOUTH, modelRotation, null, true, true));// south
        list.add(faceBakery.makeBakedQuad(
                new Vector3f((float) cube.getMaxX(), (float) cube.getMinY(), (float) cube.getMinZ()),
                new Vector3f((float) cube.getMaxX(), (float) cube.getMaxY(), (float) cube.getMaxZ()), face, cubeTexture,
                EnumFacing.EAST, modelRotation, null, true, true));// east
        list.add(faceBakery.makeBakedQuad(
                new Vector3f((float) cube.getMinX(), (float) cube.getMinY(), (float) cube.getMinZ()),
                new Vector3f((float) cube.getMinX(), (float) cube.getMaxY(), (float) cube.getMaxZ()), face, cubeTexture,
                EnumFacing.WEST, modelRotation, null, true, true));// west
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState blockState, EnumFacing side, long rand) {
        ArrayList<BakedQuad> list = new ArrayList<>();
        BlockFaceUV uv = new BlockFaceUV(new float[]{0.0F, 0.0F, 16.0F, 16.0F}, 0);
        BlockPartFace face = new BlockPartFace(null, 0, "", uv);
        double thickness = MultipartFluidPipe.thickness;
        double lastThickness = 16 - thickness;
        IExtendedBlockState state = (IExtendedBlockState) blockState;
        if (side != null) {
            return Collections.emptyList();
        }
        addCubeToList(new Vecs3dCube(thickness, thickness, thickness, lastThickness, lastThickness, lastThickness),
                list, face, ModelRotation.X0_Y0, texture, null);
        if (state != null) {
            if (state.getValue(MultipartFluidPipe.UP)) {
                addCubeToList(new Vecs3dCube(thickness, lastThickness, thickness, lastThickness, 16.0, lastThickness),
                        list, face, ModelRotation.X0_Y0, texture, EnumFacing.UP);
            }
            if (state.getValue(MultipartFluidPipe.DOWN)) {
                addCubeToList(new Vecs3dCube(thickness, 0.0, thickness, lastThickness, thickness, lastThickness), list,
                        face, ModelRotation.X0_Y0, texture, EnumFacing.DOWN);
            }
            if (state.getValue(MultipartFluidPipe.NORTH)) {
                addCubeToList(new Vecs3dCube(thickness, thickness, 0.0, lastThickness, lastThickness, thickness), list,
                        face, ModelRotation.X0_Y0, texture, EnumFacing.NORTH);
            }
            if (state.getValue(MultipartFluidPipe.SOUTH)) {
                addCubeToList(new Vecs3dCube(thickness, thickness, lastThickness, lastThickness, lastThickness, 16.0),
                        list, face, ModelRotation.X0_Y0, texture, EnumFacing.SOUTH);
            }
            if (state.getValue(MultipartFluidPipe.EAST)) {
                addCubeToList(new Vecs3dCube(lastThickness, thickness, thickness, 16.0, lastThickness, lastThickness),
                        list, face, ModelRotation.X0_Y0, texture, EnumFacing.EAST);
            }
            if (state.getValue(MultipartFluidPipe.WEST)) {
                addCubeToList(new Vecs3dCube(0.0, thickness, thickness, thickness, lastThickness, lastThickness), list,
                        face, ModelRotation.X0_Y0, texture, EnumFacing.WEST);
            }
        }
        return list;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return texture;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return null;
    }
}