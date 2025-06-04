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

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.model.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import techreborn.TechReborn;

import java.util.*;
import java.util.function.Supplier;

public class ItemBucketModel implements ItemModel {
	public static final Identifier ID = Identifier.of(TechReborn.MOD_ID, "model/bucket");
	public static final Identifier BUCKET = Identifier.of(TechReborn.MOD_ID, "item/bucket");
	public static final Identifier BUCKET_BASE = BUCKET.withSuffixedPath("_base");
	public static final Identifier BUCKET_BACKGROUND = BUCKET.withSuffixedPath("_background");
	private final RenderLayer layer;
	private final ModelSettings settings;
	private final Supplier<Triple<List<BakedQuad>, Supplier<Vector3f[]>, Integer>> bake;

	public ItemBucketModel(ModelSettings modelSettings, Supplier<Triple<List<BakedQuad>, Supplier<Vector3f[]>, Integer>> quadsProvider) {
		layer = TexturedRenderLayers.getItemEntityTranslucentCull();
		settings = modelSettings;
		bake = Suppliers.memoize(quadsProvider::get);
	}

	@Override
	public void update(
		ItemRenderState state,
		ItemStack stack,
		ItemModelManager resolver,
		ItemDisplayContext displayContext,
		@Nullable ClientWorld world,
		@Nullable LivingEntity user,
		int seed
	) {
		state.addModelKey(this);
		ItemRenderState.LayerRenderState layerRenderState = state.newLayer();
		layerRenderState.setRenderLayer(layer);
		Triple<List<BakedQuad>, Supplier<Vector3f[]>, Integer> baked = bake.get();
		layerRenderState.getQuads().addAll(baked.getLeft());
		layerRenderState.setVertices(baked.getMiddle());
		layerRenderState.initTints(1)[0] = baked.getRight();
		settings.addSettings(layerRenderState, displayContext);
	}

	public record Unbaked(Fluid fluid) implements ItemModel.Unbaked {
		public static final MapCodec<ItemBucketModel.Unbaked> CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(
				Identifier.CODEC.xmap(Registries.FLUID::get, Registries.FLUID::getId)
					.fieldOf("fluid").forGetter(Unbaked::fluid)
			)
			.apply(instance, ItemBucketModel.Unbaked::new)
		);

		@Override
		public void resolve(Resolver resolver) {
			resolver.markDependency(BUCKET_BASE);
			resolver.markDependency(BUCKET_BACKGROUND);
		}

		@Override
		public ItemModel bake(BakeContext context) {
			Baker baker = context.blockModelBaker();
			BakedSimpleModel backgroundModel = baker.getModel(BUCKET_BACKGROUND);
			List<BakedQuad> backgroundQuads = backgroundModel.bakeGeometry(backgroundModel.getTextures(), baker, ModelRotation.X0_Y0).getAllQuads();
			BakedSimpleModel baseModel = baker.getModel(BUCKET_BASE);
			ModelTextures modelTextures = baseModel.getTextures();
			List<BakedQuad> baseQuads = baseModel.bakeGeometry(modelTextures, baker, ModelRotation.X0_Y0).getAllQuads();
			ModelSettings modelSettings = ModelSettings.resolveSettings(baker, baseModel, modelTextures);
			return new ItemBucketModel(modelSettings, () -> {
				List<BakedQuad> list = new ArrayList<>(backgroundQuads);
				Pair<Sprite, Integer> pair = ItemCellModel.Unbaked.parseFluid(fluid);
				if (pair != null) {
					list.addAll(ItemCellModel.Unbaked.bakeFluidQuads(baker, backgroundModel, pair.getLeft()));
					list.addAll(ItemCellModel.Unbaked.replaceTint(baseQuads, -1));
					return Triple.of(list, ItemCellModel.Unbaked.bakeVector(list), pair.getRight());
				} else {
					list.addAll(baseQuads);
					return Triple.of(list, ItemCellModel.Unbaked.bakeVector(list), -1);
				}
			});
		}

		@Override
		public MapCodec<ItemBucketModel.Unbaked> getCodec() {
			return CODEC;
		}
	}
}
