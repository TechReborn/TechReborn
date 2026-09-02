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
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.ItemQuads;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.fabricmc.fabric.impl.client.rendering.fluid.FluidRenderingRegistryImpl;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.jspecify.annotations.Nullable;
import org.joml.Matrix4fc;
import org.joml.Vector3fc;
import reborncore.common.fluid.container.ItemFluidInfo;
import techreborn.TechReborn;

import java.util.*;
import java.util.function.Supplier;

public class ItemCellModel implements ItemModel {
	public static final Identifier ID = Identifier.fromNamespaceAndPath(TechReborn.MOD_ID, "model/cell");
	public static final Identifier CELL = Identifier.fromNamespaceAndPath(TechReborn.MOD_ID, "item/cell");
	public static final Identifier CELL_BASE = CELL.withSuffix("_base");
	public static final Identifier CELL_BACKGROUND = CELL.withSuffix("_background");
	public static final Identifier CELL_GLASS = CELL.withSuffix("_glass");
	private final ModelRenderProperties settings;
	private final Map<Fluid, Triple<List<BakedQuad>, Supplier<Vector3fc[]>, Integer>> bakedFluids;
	private final Triple<List<BakedQuad>, Supplier<Vector3fc[]>, Integer> emptyBaked;

	ItemCellModel(ModelRenderProperties modelSettings, Map<Fluid, Triple<List<BakedQuad>, Supplier<Vector3fc[]>, Integer>> bakedFluids, Triple<List<BakedQuad>, Supplier<Vector3fc[]>, Integer> emptyBaked) {
		settings = modelSettings;
		this.bakedFluids = bakedFluids;
		this.emptyBaked = emptyBaked;
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
		Fluid fluid = stack.getItem() instanceof ItemFluidInfo fluidInfo ? fluidInfo.getFluid(stack) : Fluids.EMPTY;
		state.appendModelIdentityElement(fluid);
		Triple<List<BakedQuad>, Supplier<Vector3fc[]>, Integer> baked = bakedFluids.getOrDefault(fluid, emptyBaked);
		layerRenderState.setQuads(ItemQuads.split(baked.getLeft()));
		layerRenderState.setExtents(baked.getMiddle());
		int tint = baked.getRight();
		if (tint != -1) {
			layerRenderState.tintLayers().add(tint);
		}
		settings.applyToLayer(layerRenderState, displayContext);
	}

	public record Unbaked() implements ItemModel.Unbaked {
		public static final MapCodec<ItemCellModel.Unbaked> CODEC = MapCodec.unit(ItemCellModel.Unbaked::new);

		@Override
		public void resolveDependencies(ResolvableModel.Resolver resolver) {
			resolver.markDependency(CELL_BASE);
			resolver.markDependency(CELL_BACKGROUND);
			resolver.markDependency(CELL_GLASS);
		}

		@Override
		public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
			ModelBaker baker = context.blockModelBaker();
			ResolvedModel baseModel = baker.getModel(CELL_BASE);
			ResolvedModel backgroundModel = baker.getModel(CELL_BACKGROUND);
			ResolvedModel glassModel = baker.getModel(CELL_GLASS);
			List<BakedQuad> backgroundQuads = backgroundModel.bakeTopGeometry(backgroundModel.getTopTextureSlots(), baker, BlockModelRotation.IDENTITY).getAll();
			TextureSlots modelTextures = baseModel.getTopTextureSlots();
			ModelRenderProperties modelSettings = ModelRenderProperties.fromResolvedModel(baker, baseModel, modelTextures);
			List<BakedQuad> baseQuads = baseModel.bakeTopGeometry(modelTextures, baker, BlockModelRotation.IDENTITY).getAll();
			List<BakedQuad> glassQuads = glassModel.bakeTopGeometry(glassModel.getTopTextureSlots(), baker, BlockModelRotation.IDENTITY).getAll();

			// Bake empty fallback eagerly
			List<BakedQuad> emptyList = new ArrayList<>(backgroundQuads);
			emptyList.addAll(baseQuads);
			emptyList.addAll(glassQuads);
			Triple<List<BakedQuad>, Supplier<Vector3fc[]>, Integer> emptyBaked = Triple.of(emptyList, bakeVector(emptyList), -1);

			// Bake all registered fluids eagerly while baker is still valid
			Map<Fluid, Triple<List<BakedQuad>, Supplier<Vector3fc[]>, Integer>> bakedFluids = new IdentityHashMap<>();
			Map<FluidModel.Unbaked, TextureAtlasSprite> spriteCache = new IdentityHashMap<>();

			// Include vanilla fluids that are not in Fabric's registry
			Map<Fluid, FluidModel.Unbaked> allFluidModels = new IdentityHashMap<>(FluidRenderingRegistryImpl.getUnbakedModels());
			// BlockTintSources.water() returns -1 (white) without biome context,
			// which leaves the grayscale water texture untinted. Use the standard
			// overworld water color from OverworldBiomes instead.
			FluidModel.Unbaked waterModel = new FluidModel.Unbaked(
				new Material(Identifier.withDefaultNamespace("block/water_still")),
				new Material(Identifier.withDefaultNamespace("block/water_flow")),
				new Material(Identifier.withDefaultNamespace("block/water_overlay")),
				BlockTintSources.constant(OverworldBiomes.NORMAL_WATER_COLOR)
			);
			FluidModel.Unbaked lavaModel = new FluidModel.Unbaked(
				new Material(Identifier.withDefaultNamespace("block/lava_still")),
				new Material(Identifier.withDefaultNamespace("block/lava_flow")),
				null,
				null
			);
			allFluidModels.putIfAbsent(Fluids.WATER, waterModel);
			allFluidModels.putIfAbsent(Fluids.LAVA, lavaModel);

			for (Map.Entry<Fluid, FluidModel.Unbaked> entry : allFluidModels.entrySet()) {
				Fluid fluid = entry.getKey();
				FluidModel.Unbaked unbaked = entry.getValue();
				TextureAtlasSprite sprite = spriteCache.computeIfAbsent(unbaked,
					u -> baker.materials().get(u.stillMaterial(), () -> "fluid").sprite()
				);
				int tint = unbaked.tintSource() != null
					? unbaked.tintSource().color(fluid.defaultFluidState().createLegacyBlock()) | 0xFF000000
					: 0xFFFFFFFF;
				List<BakedQuad> list = new ArrayList<>();
				list.addAll(makeCutout(bakeFluidQuads(baker, backgroundModel, sprite)));
				list.addAll(replaceTint(baseQuads, -1));
				list.addAll(glassQuads);
				bakedFluids.put(fluid, Triple.of(list, bakeVector(list), tint));
			}

			return new ItemCellModel(modelSettings, bakedFluids, emptyBaked);
		}

		@Nullable
		public static Pair<TextureAtlasSprite, Integer> parseFluid(Fluid fluid, ModelBaker baker) {
			if (fluid == Fluids.EMPTY) {
				return null;
			}
			FluidModel.Unbaked unbaked = FluidRenderingRegistryImpl.getUnbakedModels().get(fluid);
			if (unbaked == null) {
				// Vanilla fluids are not in Fabric's registry
				if (fluid == Fluids.WATER) {
					unbaked = new FluidModel.Unbaked(
						new Material(Identifier.withDefaultNamespace("block/water_still")),
						new Material(Identifier.withDefaultNamespace("block/water_flow")),
						new Material(Identifier.withDefaultNamespace("block/water_overlay")),
						BlockTintSources.constant(OverworldBiomes.NORMAL_WATER_COLOR)
					);
				} else if (fluid == Fluids.LAVA) {
					unbaked = new FluidModel.Unbaked(
						new Material(Identifier.withDefaultNamespace("block/lava_still")),
						new Material(Identifier.withDefaultNamespace("block/lava_flow")),
						null,
						null
					);
				} else {
					return null;
				}
			}
			TextureAtlasSprite sprite = baker.materials().get(unbaked.stillMaterial(), () -> "fluid").sprite();
			int tint = unbaked.tintSource() != null
				? unbaked.tintSource().color(fluid.defaultFluidState().createLegacyBlock()) | 0xFF000000
				: 0xFFFFFFFF;
			return Pair.of(sprite, tint);
		}

		public static List<BakedQuad> bakeFluidQuads(ModelBaker baker, ResolvedModel model, TextureAtlasSprite sprite) {
			return bakeFluidQuads(baker, model, sprite, TextureSlot.TEXTURE);
		}

		public static List<BakedQuad> bakeFluidQuads(ModelBaker baker, ResolvedModel model, TextureAtlasSprite sprite, TextureSlot slot) {
			Material texture = new Material(sprite.contents().name());
			TextureSlots.Data textures = new TextureSlots.Data.Builder()
				.addTexture(slot.getId(), texture).build();
			TextureSlots sprites = new TextureSlots.Resolver().addLast(textures).resolve(null);
			QuadCollection baked = model.getTopGeometry().bake(sprites, baker, BlockModelRotation.IDENTITY, model);
			return replaceTint(baked.getAll(), 0);
		}

		public static Supplier<Vector3fc[]> bakeVector(List<BakedQuad> quads) {
			Set<Vector3fc> set = new HashSet<>();
			for (BakedQuad bakedQuad : quads) {
				set.add(bakedQuad.position0());
				set.add(bakedQuad.position1());
				set.add(bakedQuad.position2());
				set.add(bakedQuad.position3());
			}
			Vector3fc[] vector = set.toArray(Vector3fc[]::new);
			return () -> vector;
		}

		/**
		 * Force quads to use the cutout (opaque) item render type instead of
		 * translucent, so semi-transparent fluid textures render as fully opaque.
		 */
		static List<BakedQuad> makeCutout(List<BakedQuad> quads) {
			RenderType cutoutType = Sheets.cutoutBlockItemSheet();
			List<BakedQuad> result = new ArrayList<>(quads.size());
			for (BakedQuad quad : quads) {
				BakedQuad.MaterialInfo oldInfo = quad.materialInfo();
				if (oldInfo.itemRenderType() == cutoutType) {
					result.add(quad);
				} else {
					result.add(new BakedQuad(
						quad.position0(), quad.position1(), quad.position2(), quad.position3(),
						quad.packedUV0(), quad.packedUV1(), quad.packedUV2(), quad.packedUV3(),
						quad.direction(), new BakedQuad.MaterialInfo(
							oldInfo.sprite(), oldInfo.layer(), cutoutType,
							oldInfo.itemGlintRenderType(), oldInfo.itemGlintSpecialRenderType(),
							oldInfo.tintIndex(), oldInfo.shadeDirectionOverride(), oldInfo.lightEmission()
						)
					));
				}
			}
			return result;
		}

		public static List<BakedQuad> replaceTint(List<BakedQuad> quads, int index) {
			List<BakedQuad> list = new ArrayList<>(quads.size());
			for (BakedQuad quad : quads) {
				BakedQuad.MaterialInfo oldInfo = quad.materialInfo();
				BakedQuad.MaterialInfo newInfo = new BakedQuad.MaterialInfo(
					oldInfo.sprite(), oldInfo.layer(), oldInfo.itemRenderType(), oldInfo.itemGlintRenderType(),
					oldInfo.itemGlintSpecialRenderType(), index, oldInfo.shadeDirectionOverride(), oldInfo.lightEmission()
				);
				list.add(new BakedQuad(quad.position0(), quad.position1(), quad.position2(), quad.position3(),
					quad.packedUV0(), quad.packedUV1(), quad.packedUV2(), quad.packedUV3(),
					quad.direction(), newInfo));
			}
			return list;
		}

		@Override
		public MapCodec<ItemCellModel.Unbaked> type() {
			return CODEC;
		}
	}
}
