package techreborn.client.render.parts;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;
import reborncore.client.models.BakedModelUtils;
import reborncore.common.misc.vecmath.Vecs3dCube;
import techreborn.parts.powerCables.CableMultipart;
import techreborn.parts.powerCables.EnumCableType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RenderCablePart implements IBakedModel {

	EnumCableType type;
	private FaceBakery faceBakery = new FaceBakery();
	private TextureAtlasSprite texture;

	public RenderCablePart(EnumCableType type) {
		texture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(type.textureName);
		this.type = type;
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState blockState, EnumFacing side, long rand) {
		type = blockState.getValue(CableMultipart.TYPE);
		texture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(type.textureName);
		ArrayList<BakedQuad> list = new ArrayList<>();
		BlockFaceUV uv = new BlockFaceUV(new float[] { 0.0F, 0.0F, 16.0F, 16.0F }, 0);
		BlockPartFace face = new BlockPartFace(null, 0, "", uv);
		double thickness = type.cableThickness;
		double lastThickness = 16 - thickness;
		IExtendedBlockState state = (IExtendedBlockState) blockState;
		if (side != null) {
			return Collections.emptyList();
		}
		BakedModelUtils.addCubeToList(new Vecs3dCube(thickness, thickness, thickness, lastThickness, lastThickness, lastThickness),
			list, face, ModelRotation.X0_Y0, texture, null, faceBakery);
		if (state != null) {
			if (state.getValue(CableMultipart.UP)) {
				BakedModelUtils.addCubeToList(new Vecs3dCube(thickness, lastThickness, thickness, lastThickness, 16.0, lastThickness),
					list, face, ModelRotation.X0_Y0, texture, EnumFacing.UP, faceBakery);
			}
			if (state.getValue(CableMultipart.DOWN)) {
				BakedModelUtils.addCubeToList(new Vecs3dCube(thickness, 0.0, thickness, lastThickness, thickness, lastThickness), list,
					face, ModelRotation.X0_Y0, texture, EnumFacing.DOWN, faceBakery);
			}
			if (state.getValue(CableMultipart.NORTH)) {
				BakedModelUtils.addCubeToList(new Vecs3dCube(0.0, thickness, thickness, thickness, lastThickness, lastThickness), list,
					face, ModelRotation.X0_Y90, texture, EnumFacing.NORTH, faceBakery);
			}
			if (state.getValue(CableMultipart.SOUTH)) {
				BakedModelUtils.addCubeToList(new Vecs3dCube(0.0, thickness, thickness, thickness, lastThickness, lastThickness),
					list, face, ModelRotation.X0_Y270, texture, EnumFacing.SOUTH, faceBakery);
			}
			if (state.getValue(CableMultipart.EAST)) {
				BakedModelUtils.addCubeToList(new Vecs3dCube(lastThickness, thickness, thickness, 16.0, lastThickness, lastThickness),
					list, face, ModelRotation.X0_Y0, texture, EnumFacing.EAST, faceBakery);
			}
			if (state.getValue(CableMultipart.WEST)) {
				BakedModelUtils.addCubeToList(new Vecs3dCube(0.0, thickness, thickness, thickness, lastThickness, lastThickness), list,
					face, ModelRotation.X0_Y0, texture, EnumFacing.WEST, faceBakery);
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
