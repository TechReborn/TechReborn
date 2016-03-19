package techreborn.client.render.parts;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.lwjgl.util.vector.Vector3f;
import reborncore.common.misc.vecmath.Vecs3dCube;
import techreborn.parts.CableMultipart;
import techreborn.parts.EnumCableType;

import java.util.ArrayList;
import java.util.List;

public class RenderCablePart implements IBakedModel {

    private FaceBakery faceBakery = new FaceBakery();

    private TextureAtlasSprite texture;

    EnumCableType type;

    public RenderCablePart(EnumCableType type) {
        texture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(type.textureName);
        this.type = type;
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
    public List<BakedQuad> getQuads(IBlockState blockState, EnumFacing side, long rand) {
        ArrayList<BakedQuad> list = new ArrayList<BakedQuad>();
        BlockFaceUV uv = new BlockFaceUV(new float[]{0.0F, 0.0F, 16.0F, 16.0F}, 0);
        BlockPartFace face = new BlockPartFace(null, 0, "", uv);
        double thickness =  type.cableThickness;
        double lastThickness = 16 - thickness;
        IExtendedBlockState state = (IExtendedBlockState) blockState;
        addCubeToList(new Vecs3dCube(thickness, thickness, thickness, lastThickness, lastThickness, lastThickness), list, face, ModelRotation.X0_Y0, texture);
        if (state != null) {
            if (state.getValue(CableMultipart.UP)) {
                addCubeToList(new Vecs3dCube(thickness, lastThickness, thickness, lastThickness, 16.0, lastThickness), list, face, ModelRotation.X0_Y0, texture);
            }
            if (state.getValue(CableMultipart.DOWN)) {
                addCubeToList(new Vecs3dCube(thickness, 0.0, thickness, lastThickness, thickness, lastThickness), list, face, ModelRotation.X0_Y0, texture);
            }
            if (state.getValue(CableMultipart.NORTH)) {
                addCubeToList(new Vecs3dCube(thickness, thickness, 0.0, lastThickness, lastThickness, lastThickness), list, face, ModelRotation.X0_Y0, texture);
            }
            if (state.getValue(CableMultipart.SOUTH)) {
                addCubeToList(new Vecs3dCube(thickness, thickness, thickness, lastThickness, lastThickness, 16.0), list, face, ModelRotation.X0_Y0, texture);
            }
            if (state.getValue(CableMultipart.EAST)) {
                addCubeToList(new Vecs3dCube(thickness, thickness, thickness, 16.0, lastThickness, lastThickness), list, face, ModelRotation.X0_Y0, texture);
            }
            if (state.getValue(CableMultipart.WEST)) {
                addCubeToList(new Vecs3dCube(0.0, thickness, thickness, lastThickness, lastThickness, lastThickness), list, face, ModelRotation.X0_Y0, texture);
            }
        }
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

    @Override
    public ItemOverrideList getOverrides() {
        return null;
    }
}
