/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.client.render;

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
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import techreborn.init.ModItems;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

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

		ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transformMap = PerspectiveMapWrapper.getTransforms(state);
		TRSRTransformation transform = state.apply(Optional.empty()).orElse(TRSRTransformation.identity());

		ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
		builder.addAll(new ItemLayerModel(ImmutableList.of(baseTexture)).bake(transform, format, bakedTextureGetter).getQuads(null, null, 0L));

		ResourceLocation sprite = fluid != null ? fluid.getStill() : emptyTexture;
		int color = fluid != null ? fluid.getColor() : Color.WHITE.getRGB();
		TextureAtlasSprite fluidSprite = bakedTextureGetter.apply(sprite);
		if (fluid != null) {
			if (fluidSprite != null) {
				builder.add(ItemTextureQuadConverter.genQuad(format, transform, 5, 2, 11, 14, NORTH_Z_FLUID, fluidSprite, EnumFacing.NORTH, color, -1));
				builder.add(ItemTextureQuadConverter.genQuad(format, transform, 5, 2, 11, 14, SOUTH_Z_FLUID, fluidSprite, EnumFacing.SOUTH, color, -1));
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
			return modelLocation.getNamespace().equals("techreborn") && modelLocation.getPath().contains("dynamic_cell");
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
			FluidStack fluidStack = FluidUtil.getFluidContained(stack);

			if (fluidStack == null)
				return originalModel; //return default bucket

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
