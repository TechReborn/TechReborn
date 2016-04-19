package techreborn.client.render.parts;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.lwjgl.util.vector.Vector3f;
import reborncore.common.misc.vecmath.Vecs3dCube;
import techreborn.parts.CableMultipart;
import techreborn.parts.EnumCableType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RenderCablePart implements IBakedModel
{

	EnumCableType type;
	private FaceBakery faceBakery = new FaceBakery();
	private TextureAtlasSprite texture;

	public RenderCablePart(EnumCableType type)
	{
		texture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(type.textureName);
		this.type = type;
	}

	public void addCubeToList(Vecs3dCube cube, ArrayList<BakedQuad> list, BlockPartFace face,
			ModelRotation modelRotation, TextureAtlasSprite cubeTexture, EnumFacing dir)
	{
		BlockFaceUV uv = new BlockFaceUV(new float[] { (float) cube.getMinX(), (float) cube.getMinY(),
				(float) cube.getMaxX(), (float) cube.getMaxY() }, 0);
		face = new BlockPartFace(null, 0, "", uv);
		if(dir == EnumFacing.NORTH ||  dir == EnumFacing.SOUTH){
			uv = new BlockFaceUV(new float[] { (float) cube.getMinZ(), (float) cube.getMinY(),
					(float) cube.getMaxZ(), (float) cube.getMaxY() }, 0);
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
	public List<BakedQuad> getQuads(IBlockState blockState, EnumFacing side, long rand)
	{
		ArrayList<BakedQuad> list = new ArrayList<>();
		BlockFaceUV uv = new BlockFaceUV(new float[] { 0.0F, 0.0F, 16.0F, 16.0F }, 0);
		BlockPartFace face = new BlockPartFace(null, 0, "", uv);
		double thickness = type.cableThickness;
		double lastThickness = 16 - thickness;
		IExtendedBlockState state = (IExtendedBlockState) blockState;
		if (side != null)
		{
			return Collections.emptyList();
		}
		addCubeToList(new Vecs3dCube(thickness, thickness, thickness, lastThickness, lastThickness, lastThickness),
				list, face, ModelRotation.X0_Y0, texture, null);
		if (state != null)
		{
			if (state.getValue(CableMultipart.UP))
			{
				addCubeToList(new Vecs3dCube(thickness, lastThickness, thickness, lastThickness, 16.0, lastThickness),
						list, face, ModelRotation.X0_Y0, texture, EnumFacing.UP);
			}
			if (state.getValue(CableMultipart.DOWN))
			{
				addCubeToList(new Vecs3dCube(thickness, 0.0, thickness, lastThickness, thickness, lastThickness), list,
						face, ModelRotation.X0_Y0, texture, EnumFacing.DOWN);
			}
			if (state.getValue(CableMultipart.NORTH))
			{
				addCubeToList(new Vecs3dCube(thickness, thickness, 0.0, lastThickness, lastThickness, thickness), list,
						face, ModelRotation.X0_Y0, texture, EnumFacing.NORTH);
			}
			if (state.getValue(CableMultipart.SOUTH))
			{
				addCubeToList(new Vecs3dCube(thickness, thickness, lastThickness, lastThickness, lastThickness, 16.0),
						list, face, ModelRotation.X0_Y0, texture, EnumFacing.SOUTH);
			}
			if (state.getValue(CableMultipart.EAST))
			{
				addCubeToList(new Vecs3dCube(lastThickness, thickness, thickness, 16.0, lastThickness, lastThickness),
						list, face, ModelRotation.X0_Y0, texture, EnumFacing.EAST);
			}
			if (state.getValue(CableMultipart.WEST))
			{
				addCubeToList(new Vecs3dCube(0.0, thickness, thickness, thickness, lastThickness, lastThickness), list,
						face, ModelRotation.X0_Y0, texture, EnumFacing.WEST);
			}
		}
		return list;
	}

	@Override
	public boolean isAmbientOcclusion()
	{
		return true;
	}

	@Override
	public boolean isGui3d()
	{
		return true;
	}

	@Override
	public boolean isBuiltInRenderer()
	{
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		return texture;
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms()
	{
		return ItemCameraTransforms.DEFAULT;
	}

	@Override
	public ItemOverrideList getOverrides()
	{
		return null;
	}
}
