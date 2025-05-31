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
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.data.TextureKey;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.model.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import reborncore.common.fluid.container.ItemFluidInfo;
import techreborn.TechReborn;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class ItemCellModel implements ItemModel {
	public static final Identifier ID = Identifier.of(TechReborn.MOD_ID, "model/cell");
	public static final Identifier CELL = Identifier.of(TechReborn.MOD_ID, "item/cell");
	public static final Identifier CELL_BASE = CELL.withSuffixedPath("_base");
	public static final Identifier CELL_BACKGROUND = CELL.withSuffixedPath("_background");
	public static final Identifier CELL_GLASS = CELL.withSuffixedPath("_glass");
	private final RenderLayer layer;
	private final ModelSettings settings;
	private final Function<Fluid, Triple<List<BakedQuad>, Supplier<Vector3f[]>, Integer>> bake;
	private final HashMap<Fluid, Triple<List<BakedQuad>, Supplier<Vector3f[]>, Integer>> CACHE_BAKED = new HashMap<>();
	ItemCellModel(ModelSettings modelSettings, Function<Fluid, Triple<List<BakedQuad>, Supplier<Vector3f[]>, Integer>> bakeModel) {
		layer = TexturedRenderLayers.getItemEntityTranslucentCull();
		settings = modelSettings;
		bake = bakeModel;
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
		Fluid fluid = stack.getItem() instanceof ItemFluidInfo fluidInfo ? fluidInfo.getFluid(stack) : Fluids.EMPTY;
		state.addModelKey(fluid);
		Triple<List<BakedQuad>, Supplier<Vector3f[]>, Integer> baked = CACHE_BAKED.computeIfAbsent(fluid, bake);
		layerRenderState.getQuads().addAll(baked.getLeft());
		layerRenderState.setVector(baked.getMiddle());
		layerRenderState.initTints(1)[0] = baked.getRight();
		settings.addSettings(layerRenderState, displayContext);
	}

	public record Unbaked() implements ItemModel.Unbaked {
		public static final MapCodec<ItemCellModel.Unbaked> CODEC = MapCodec.unit(ItemCellModel.Unbaked::new);

		@Override
		public void resolve(ResolvableModel.Resolver resolver) {
			resolver.markDependency(CELL_BASE);
			resolver.markDependency(CELL_BACKGROUND);
			resolver.markDependency(CELL_GLASS);
		}

		@Override
		public ItemModel bake(ItemModel.BakeContext context) {
			Baker baker = context.blockModelBaker();
			BakedSimpleModel baseModel = baker.getModel(CELL_BASE);
			BakedSimpleModel backgroundModel = baker.getModel(CELL_BACKGROUND);
			BakedSimpleModel glassModel = baker.getModel(CELL_GLASS);
			List<BakedQuad> backgroundQuads = backgroundModel.bakeGeometry(backgroundModel.getTextures(), baker, ModelRotation.X0_Y0).getAllQuads();
			ModelTextures modelTextures = baseModel.getTextures();
			ModelSettings modelSettings = ModelSettings.resolveSettings(baker, baseModel, modelTextures);
			List<BakedQuad> baseQuads = baseModel.bakeGeometry(modelTextures, baker, ModelRotation.X0_Y0).getAllQuads();
			List<BakedQuad> glassQuads = glassModel.bakeGeometry(glassModel.getTextures(), baker, ModelRotation.X0_Y0).getAllQuads();
			return new ItemCellModel(modelSettings, (Fluid fluid) -> {
				List<BakedQuad> list = new ArrayList<>(backgroundQuads);
				Pair<Sprite, Integer> pair = parseFluid(fluid);
				if (pair != null) {
					list.addAll(bakeFluidQuads(baker, backgroundModel, pair.getLeft()));
					list.addAll(replaceTint(baseQuads, -1));
					list.addAll(glassQuads);
					return Triple.of(list, bakeVector(list), pair.getRight());
				} else {

					list.addAll(baseQuads);
					list.addAll(glassQuads);
					return Triple.of(list, bakeVector(list), -1);
				}
			});
		}

		@Nullable
		public static Pair<Sprite, Integer> parseFluid(Fluid fluid) {
			FluidRenderHandler handler = FluidRenderHandlerRegistry.INSTANCE.get(fluid);
			if (handler == null) {
				return null;
			}
			MinecraftClient client = MinecraftClient.getInstance();
			if (client.player == null) {
				return null;
			}
			FluidState state = fluid.getDefaultState();
			int tint = handler.getFluidColor(client.world, client.player.getBlockPos(), state) | 0xFF000000;
			Sprite sprite = handler.getFluidSprites(client.world, BlockPos.ORIGIN, state)[0];
			return Pair.of(sprite, tint);
		}

		public static List<BakedQuad> bakeFluidQuads(Baker baker, BakedSimpleModel model, Sprite sprite) {
			SpriteIdentifier texture = new SpriteIdentifier(sprite.getAtlasId(), sprite.getContents().getId());
			ModelTextures.Textures textures = new ModelTextures.Textures.Builder()
				.addSprite(TextureKey.TEXTURE.getName(), texture).build();
			ModelTextures sprites = new ModelTextures.Builder().addLast(textures).build(null);
			BakedGeometry baked = model.getGeometry().bake(sprites, baker, ModelRotation.X0_Y0, model);
			return replaceTint(baked.getAllQuads(), 0);
		}

		public static Supplier<Vector3f[]> bakeVector(List<BakedQuad> quads) {
			Set<Vector3f> set = new HashSet<>();
			for (BakedQuad bakedQuad : quads) {
				BakedQuadFactory.calculatePosition(bakedQuad.vertexData(), set::add);
			}
			Vector3f[] vector = set.toArray(Vector3f[]::new);
			return () -> vector;
		}

		public static List<BakedQuad> replaceTint(List<BakedQuad> quads, int index) {
			List<BakedQuad> list = new ArrayList<>(quads.size());
			for (BakedQuad quad : quads) {
				list.add(new BakedQuad(quad.vertexData(), index, quad.face(), quad.sprite(), quad.shade(), quad.lightEmission()));
			}
			return list;
		}

		@Override
		public MapCodec<ItemCellModel.Unbaked> getCodec() {
			return CODEC;
		}
	}
}
