/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import techreborn.TechReborn;

import java.util.HashMap;

public class ItemBucketModel implements ItemModel {
	public static final Identifier ID = Identifier.of(TechReborn.MOD_ID, "model/bucket");
	private final DynamicBucketBakedModel model;
	ItemBucketModel(Fluid fluid, BakedModel baseModel, BakedModel fluidModel, BakedModel backgroundModel) {
		model = new DynamicBucketBakedModel(fluid, baseModel, fluidModel, backgroundModel);
	}

	@Override
	public void update(ItemRenderState state, ItemStack stack, ItemModelManager resolver, ModelTransformationMode transformationMode, @Nullable ClientWorld world, @Nullable LivingEntity user, int seed) {
		ItemRenderState.LayerRenderState layerRenderState = state.newLayer();
		RenderLayer renderLayer = RenderLayers.getItemLayer(stack);
		layerRenderState.setModel(model, renderLayer);
	}

	public record Unbaked(Fluid fluid) implements ItemModel.Unbaked {
		public static final MapCodec<ItemBucketModel.Unbaked> CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(
				Identifier.CODEC.xmap(Registries.FLUID::get, Registries.FLUID::getId)
					.fieldOf("fluid").forGetter(Unbaked::fluid)
			)
			.apply(instance, ItemBucketModel.Unbaked::new)
		);
		private static final HashMap<Identifier, BakedModel> CACHE = new HashMap<>();

		@Override
		public void resolve(Resolver resolver) {
			resolver.resolve(DynamicBucketBakedModel.BUCKET_BASE);
			resolver.resolve(DynamicBucketBakedModel.BUCKET_FLUID);
			resolver.resolve(DynamicBucketBakedModel.BUCKET_BACKGROUND);
		}

		@Override
		public ItemModel bake(BakeContext context) {
			BakedModel baseModel = CACHE.computeIfAbsent(DynamicBucketBakedModel.BUCKET_BASE, context::bake);
			BakedModel fluidModel = CACHE.computeIfAbsent(DynamicBucketBakedModel.BUCKET_FLUID, context::bake);
			BakedModel backgroundModel = CACHE.computeIfAbsent(DynamicBucketBakedModel.BUCKET_BACKGROUND, context::bake);
			return new ItemBucketModel(fluid, baseModel, fluidModel, backgroundModel);
		}

		@Override
		public MapCodec<ItemBucketModel.Unbaked> getCodec() {
			return CODEC;
		}
	}
}
