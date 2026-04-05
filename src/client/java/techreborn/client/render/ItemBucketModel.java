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
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.jspecify.annotations.Nullable;
import org.joml.Matrix4fc;
import org.joml.Vector3fc;
import techreborn.TechReborn;

import java.util.*;
import java.util.function.Supplier;

public class ItemBucketModel implements ItemModel {
	public static final Identifier ID = Identifier.fromNamespaceAndPath(TechReborn.MOD_ID, "model/bucket");
	public static final Identifier BUCKET = Identifier.fromNamespaceAndPath(TechReborn.MOD_ID, "item/bucket");
	public static final Identifier BUCKET_BASE = BUCKET.withSuffix("_base");
	public static final Identifier BUCKET_BACKGROUND = BUCKET.withSuffix("_background");
	private final ModelRenderProperties settings;
	private final Triple<List<BakedQuad>, Supplier<Vector3fc[]>, Integer> baked;

	public ItemBucketModel(ModelRenderProperties modelSettings, Triple<List<BakedQuad>, Supplier<Vector3fc[]>, Integer> baked) {
		settings = modelSettings;
		this.baked = baked;
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
		layerRenderState.prepareQuadList().addAll(baked.getLeft());
		layerRenderState.setExtents(baked.getMiddle());
		int tint = baked.getRight();
		if (tint != -1) {
			layerRenderState.tintLayers().add(tint);
		}
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
		public ItemModel bake(BakingContext context, Matrix4fc transformation) {
			ModelBaker baker = context.blockModelBaker();
			ResolvedModel backgroundModel = baker.getModel(BUCKET_BACKGROUND);
			List<BakedQuad> backgroundQuads = backgroundModel.bakeTopGeometry(backgroundModel.getTopTextureSlots(), baker, BlockModelRotation.IDENTITY).getAll();
			ResolvedModel baseModel = baker.getModel(BUCKET_BASE);
			TextureSlots modelTextures = baseModel.getTopTextureSlots();
			List<BakedQuad> baseQuads = baseModel.bakeTopGeometry(modelTextures, baker, BlockModelRotation.IDENTITY).getAll();
			ModelRenderProperties modelSettings = ModelRenderProperties.fromResolvedModel(baker, baseModel, modelTextures);

			Pair<TextureAtlasSprite, Integer> pair = ItemCellModel.Unbaked.parseFluid(fluid, baker);
			Triple<List<BakedQuad>, Supplier<Vector3fc[]>, Integer> baked;
			if (pair != null) {
				List<BakedQuad> list = new ArrayList<>();
				list.addAll(ItemCellModel.Unbaked.makeCutout(ItemCellModel.Unbaked.bakeFluidQuads(baker, backgroundModel, pair.getLeft())));
				list.addAll(ItemCellModel.Unbaked.replaceTint(baseQuads, -1));
				baked = Triple.of(list, ItemCellModel.Unbaked.bakeVector(list), pair.getRight());
			} else {
				List<BakedQuad> list = new ArrayList<>(backgroundQuads);
				list.addAll(baseQuads);
				baked = Triple.of(list, ItemCellModel.Unbaked.bakeVector(list), -1);
			}
			return new ItemBucketModel(modelSettings, baked);
		}

		@Override
		public MapCodec<ItemBucketModel.Unbaked> type() {
			return CODEC;
		}
	}
}
