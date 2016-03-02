package techreborn.client.render;

import mcmultipart.client.multipart.ISmartMultipartModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.EnumFacing;
import org.lwjgl.util.vector.Vector3f;
import reborncore.common.misc.vecmath.Vecs3dCube;

import java.util.ArrayList;
import java.util.List;

public class RenderCablePart implements ISmartMultipartModel {

    private FaceBakery faceBakery = new FaceBakery();

    @Override
    public IBakedModel handlePartState(IBlockState state) {
        return null;
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
        return null;
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        return null;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return null;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return null;
    }
}
