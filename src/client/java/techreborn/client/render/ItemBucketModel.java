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
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import techreborn.TechReborn;

import java.util.*;
import java.util.function.Supplier;

public class ItemBucketModel implements ItemModel {
	public static final Identifier ID = Identifier.fromNamespaceAndPath(TechReborn.MOD_ID, "model/bucket");
	public static final Identifier BUCKET = Identifier.fromNamespaceAndPath(TechReborn.MOD_ID, "item/bucket");
	public static final Identifier BUCKET_BASE = BUCKET.withSuffix("_base");
	public static final Identifier BUCKET_BACKGROUND = BUCKET.withSuffix("_background");
	private final RenderType layer;
	private final ModelRenderProperties settings;
	private final Supplier<Triple<List<BakedQuad>, Supplier<Vector3f[]>, Integer>> bake;

	public ItemBucketModel(ModelRenderProperties modelSettings, Supplier<Triple<List<BakedQuad>, Supplier<Vector3f[]>, Integer>> quadsProvider) {
		layer = Sheets.translucentItemSheet();
		settings = modelSettings;
		bake = Suppliers.memoize(quadsProvider::get);
	}

	@Override
	public void update(
		ItemStackRenderState state,
		ItemStack stack,
		ItemModelResolver resolver,
		ItemDisplayContext displayContext,
		@Nullable ClientLevel world,
		@Nullable ItemOwner user,
		int seed
	) {
		state.appendModelIdentityElement(this);
		ItemStackRenderState.LayerRenderState layerRenderState = state.newLayer();
		layerRenderState.setRenderType(layer);
		Triple<List<BakedQuad>, Supplier<Vector3f[]>, Integer> baked = bake.get();
		layerRenderState.prepareQuadList().addAll(baked.getLeft());
		layerRenderState.setExtents(baked.getMiddle());
		layerRenderState.prepareTintLayers(1)[0] = baked.getRight();
		settings.applyToLayer(layerRenderState, displayContext);
	}

	public record Unbaked(Fluid fluid) implements ItemModel.Unbaked {
		public static final MapCodec<ItemBucketModel.Unbaked> CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(
				Identifier.CODEC.xmap(BuiltInRegistries.FLUID::getValue, BuiltInRegistries.FLUID::getKey)
					.fieldOf("fluid").forGetter(techreborn.client.render.ItemBucketModel.Unbaked::fluid)
			)
			.apply(instance, ItemBucketModel.Unbaked::new)
		);

		@Override
		public void resolveDependencies(Resolver resolver) {
			resolver.markDependency(BUCKET_BASE);
			resolver.markDependency(BUCKET_BACKGROUND);
		}

		@Override
		public @NotNull ItemModel bake(BakingContext context) {
			ModelBaker baker = context.blockModelBaker();
			ResolvedModel backgroundModel = baker.getModel(BUCKET_BACKGROUND);
			List<BakedQuad> backgroundQuads = backgroundModel.bakeTopGeometry(backgroundModel.getTopTextureSlots(), baker, BlockModelRotation.X0_Y0).getAll();
			ResolvedModel baseModel = baker.getModel(BUCKET_BASE);
			TextureSlots modelTextures = baseModel.getTopTextureSlots();
			List<BakedQuad> baseQuads = baseModel.bakeTopGeometry(modelTextures, baker, BlockModelRotation.X0_Y0).getAll();
			ModelRenderProperties modelSettings = ModelRenderProperties.fromResolvedModel(baker, baseModel, modelTextures);
			return new ItemBucketModel(modelSettings, () -> {
				List<BakedQuad> list = new ArrayList<>(backgroundQuads);
				Pair<TextureAtlasSprite, Integer> pair = ItemCellModel.Unbaked.parseFluid(fluid);
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
		public @NotNull MapCodec<ItemBucketModel.Unbaked> type() {
			return CODEC;
		}
	}
}
