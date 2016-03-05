package techreborn.client.render.parts;

import mcmultipart.client.multipart.ISmartMultipartModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.EnumFacing;
import org.lwjgl.util.vector.Vector3f;
import reborncore.common.misc.vecmath.Vecs3dCube;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RenderCablePart implements ISmartMultipartModel {

    private FaceBakery faceBakery = new FaceBakery();

    private TextureAtlasSprite texture;

    public RenderCablePart() {
        texture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("fluxedredstone:blocks/cable_redstone");
    }

    @Override
    public IBakedModel handlePartState(IBlockState state) {
        return new RenderCablePart();
    }

    public void addCubeToList(Vecs3dCube cube, ArrayList<BakedQuad> list, BlockPartFace face, ModelRotation modelRotation, TextureAtlasSprite cubeTexture) {
        list.add(faceBakery.makeBakedQuad(new Vector3f((float) cube.getMinX(), (float) cube.getMinY(), (float) cube.getMinZ()), new Vector3f((float) cube.getMaxX(), (float) cube.getMinY(), (float) cube.getMaxZ()), face, cubeTexture, EnumFacing.DOWN, modelRotation, null, true, true));//down
        list.add(faceBakery.makeBakedQuad(new Vector3f((float) cube.getMinX(), (float) cube.getMaxY(), (float) cube.getMinZ()), new Vector3f((float) cube.getMaxX(), (float) cube.getMaxY(), (float) cube.getMaxZ()), face, cubeTexture, EnumFacing.UP, modelRotation, null, true, true));//up
        list.add(faceBakery.makeBakedQuad(new Vector3f((float) cube.getMinX(), (float) cube.getMinY(), (float) cube.getMinZ()), new Vector3f((float) cube.getMaxX(), (float) cube.getMaxY(), (float) cube.getMaxZ()), face, cubeTexture, EnumFacing.NORTH, modelRotation, null, true, true));//north
        list.add(faceBakery.makeBakedQuad(new Vector3f((float) cube.getMinX(), (float) cube.getMinY(), (float) cube.getMaxZ()), new Vector3f((float) cube.getMaxX(), (float) cube.getMaxY(), (float) cube.getMaxZ()), face, cubeTexture, EnumFacing.SOUTH, modelRotation, null, true, true));//south
        list.add(faceBakery.makeBakedQuad(new Vector3f((float) cube.getMaxX(), (float) cube.getMinY(), (float) cube.getMinZ()), new Vector3f((float) cube.getMaxX(), (float) cube.getMaxY(), (float) cube.getMaxZ()), face, cubeTexture, EnumFacing.EAST, modelRotation, null, true, true));//east
        list.add(faceBakery.makeBakedQuad(new Vector3f((float) cube.getMinX(), (float) cube.getMinY(), (float) cube.getMinZ()), new Vector3f((float) cube.getMinX(), (float) cube.getMaxY(), (float) cube.getMaxZ()), face, cubeTexture, EnumFacing.WEST, modelRotation, null, true, true));//west
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing p_177551_1_) {
        return Collections.emptyList();
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        ArrayList<BakedQuad> list = new ArrayList<BakedQuad>();
        BlockFaceUV uv = new BlockFaceUV(new float[]{0.0F, 0.0F, 16.0F, 16.0F}, 0);
        BlockPartFace face = new BlockPartFace(null, 0, "", uv);
        int thickness = 4;
        int lastThickness = 16 - thickness;
        addCubeToList(new Vecs3dCube(thickness, thickness, thickness, lastThickness, lastThickness, lastThickness), list, face, ModelRotation.X0_Y0, texture);
        return list;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
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
}
