package techreborn.client.render;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import techreborn.init.ModItems;
import techreborn.items.DynamicCell;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ModelDynamicCell implements IModel {

	public static final ModelDynamicCell MODEL = new ModelDynamicCell(
		new ResourceLocation("techreborn:items/cell_cover"),
		new ResourceLocation("techreborn:items/cell_empty")
	);

	public static final ModelResourceLocation MODEL_LOCATION = new ModelResourceLocation(new ResourceLocation("techreborn", "dynamic_cell"), "default");

	private static final float NORTH_Z_FLUID = 7.6f / 16f;
	private static final float SOUTH_Z_FLUID = 8.4f / 16f;

	public static void init() {
		ModelLoader.setCustomMeshDefinition(ModItems.CELL, stack -> MODEL_LOCATION);
		ModelBakery.registerItemVariants(ModItems.CELL, MODEL_LOCATION);
		ModelLoaderRegistry.registerLoader(new DynamicCellLoader());
	}

	private final ResourceLocation baseTexture;
	private final ResourceLocation emptyTexture;
	private final Fluid fluid;

	public ModelDynamicCell(ResourceLocation baseTexture, ResourceLocation emptyTexture) {
		this(baseTexture, emptyTexture, null);
	}

	public ModelDynamicCell(ResourceLocation baseTexture, ResourceLocation emptyTexture, Fluid fluid) {
		this.baseTexture = baseTexture;
		this.emptyTexture = emptyTexture;
		this.fluid = fluid;
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return ImmutableList.of();
	}

	@Override
	public Collection<ResourceLocation> getTextures() {
		return ImmutableList.of(baseTexture, emptyTexture);
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {

		ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transformMap = IPerspectiveAwareModel.MapWrapper.getTransforms(state);
		TRSRTransformation transform = state.apply(Optional.<IModelPart>absent()).or(TRSRTransformation.identity());

		ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
		builder.addAll(new ItemLayerModel(ImmutableList.of(baseTexture)).bake(transform, format, bakedTextureGetter).getQuads(null, null, 0L));

		ResourceLocation sprite = fluid != null ? fluid.getStill() : emptyTexture;
		int color = fluid != null ? fluid.getColor() : Color.WHITE.getRGB();
		TextureAtlasSprite fluidSprite = bakedTextureGetter.apply(sprite);
		if (fluid != null) {
			if (fluidSprite != null) {
				builder.add(ItemTextureQuadConverter.genQuad(format, transform, 5, 2, 11, 14, NORTH_Z_FLUID, fluidSprite, EnumFacing.NORTH, color));
				builder.add(ItemTextureQuadConverter.genQuad(format, transform, 5, 2, 11, 14, SOUTH_Z_FLUID, fluidSprite, EnumFacing.SOUTH, color));
			}
		}

		return new BakedDynamicCell(builder.build(), this, bakedTextureGetter.apply(baseTexture), format, transformMap);
	}

	@Override
	public IModelState getDefaultState() {
		return TRSRTransformation.identity();
	}

	public static class DynamicCellLoader implements ICustomModelLoader {

		@Override
		public boolean accepts(ResourceLocation modelLocation) {
			return modelLocation.getResourceDomain().equals("techreborn") && modelLocation.getResourcePath().contains("dynamic_cell");
		}

		@Override
		public IModel loadModel(ResourceLocation modelLocation) throws Exception {
			return MODEL;
		}

		@Override
		public void onResourceManagerReload(IResourceManager resourceManager) {}

	}

	public static class BakedDynamicCell implements IBakedModel {

		private final List<BakedQuad> quads;
		private final ModelDynamicCell parent;
		private final TextureAtlasSprite particle;
		private final VertexFormat format;
		private final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transformMap;

		public BakedDynamicCell(List<BakedQuad> quads,
		                        ModelDynamicCell parent,
		                        TextureAtlasSprite particle,
		                        VertexFormat format,
		                        ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transformMap) {
			this.transformMap = transformMap;
			this.quads = quads;
			this.parent = parent;
			this.particle = particle;
			this.format = format;
		}

		@Override
		public List<BakedQuad> getQuads(
			@Nullable
				IBlockState state,
			@Nullable
				EnumFacing side, long rand) {
			return quads;
		}

		@Override
		public boolean isAmbientOcclusion() {
			return true;
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
			return particle;
		}

		@Override
		public ItemCameraTransforms getItemCameraTransforms() {
			return ModelHelper.DEFAULT_ITEM_TRANSFORMS;
		}

		@Override
		public ItemOverrideList getOverrides() {
			return OVERRIDES;
		}

	}

	public static final OverrideHandler OVERRIDES = new OverrideHandler();

	public static class OverrideHandler extends ItemOverrideList {

		private final HashMap<String, IBakedModel> modelCache = new HashMap<>();

		private final Function<ResourceLocation, TextureAtlasSprite> textureGetter = location ->
			Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());

		private OverrideHandler() {
			super(ImmutableList.of());
		}

		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
			FluidStack fluidStack = DynamicCell.getFluidHandler(stack).getFluid();
			if (fluidStack == null) {
				//return default bucket
				return originalModel;
			}
			String name = fluidStack.getFluid().getName();
			if (!modelCache.containsKey(name)) {
				BakedDynamicCell bakedCell = (BakedDynamicCell) originalModel;
				ModelDynamicCell model = new ModelDynamicCell(bakedCell.parent.baseTexture, bakedCell.parent.emptyTexture, fluidStack.getFluid());
				modelCache.put(name, model.bake(new SimpleModelState(bakedCell.transformMap), bakedCell.format, textureGetter));
			}
			return modelCache.get(name);
		}

	}

}
