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
import net.minecraft.client.Minecraft;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import reborncore.common.fluid.container.ItemFluidInfo;
import techreborn.TechReborn;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class ItemCellModel implements ItemModel {
	public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(TechReborn.MOD_ID, "model/cell");
	public static final ResourceLocation CELL = ResourceLocation.fromNamespaceAndPath(TechReborn.MOD_ID, "item/cell");
	public static final ResourceLocation CELL_BASE = CELL.withSuffix("_base");
	public static final ResourceLocation CELL_BACKGROUND = CELL.withSuffix("_background");
	public static final ResourceLocation CELL_GLASS = CELL.withSuffix("_glass");
	private final RenderType layer;
	private final ModelRenderProperties settings;
	private final Function<Fluid, Triple<List<BakedQuad>, Supplier<Vector3f[]>, Integer>> bake;
	private final HashMap<Fluid, Triple<List<BakedQuad>, Supplier<Vector3f[]>, Integer>> CACHE_BAKED = new HashMap<>();
	ItemCellModel(ModelRenderProperties modelSettings, Function<Fluid, Triple<List<BakedQuad>, Supplier<Vector3f[]>, Integer>> bakeModel) {
		layer = Sheets.translucentItemSheet();
		settings = modelSettings;
		bake = bakeModel;
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
		Fluid fluid = stack.getItem() instanceof ItemFluidInfo fluidInfo ? fluidInfo.getFluid(stack) : Fluids.EMPTY;
		state.appendModelIdentityElement(fluid);
		Triple<List<BakedQuad>, Supplier<Vector3f[]>, Integer> baked = CACHE_BAKED.computeIfAbsent(fluid, bake);
		layerRenderState.prepareQuadList().addAll(baked.getLeft());
		layerRenderState.setExtents(baked.getMiddle());
		layerRenderState.prepareTintLayers(1)[0] = baked.getRight();
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
		public @NotNull ItemModel bake(ItemModel.BakingContext context) {
			ModelBaker baker = context.blockModelBaker();
			ResolvedModel baseModel = baker.getModel(CELL_BASE);
			ResolvedModel backgroundModel = baker.getModel(CELL_BACKGROUND);
			ResolvedModel glassModel = baker.getModel(CELL_GLASS);
			List<BakedQuad> backgroundQuads = backgroundModel.bakeTopGeometry(backgroundModel.getTopTextureSlots(), baker, BlockModelRotation.X0_Y0).getAll();
			TextureSlots modelTextures = baseModel.getTopTextureSlots();
			ModelRenderProperties modelSettings = ModelRenderProperties.fromResolvedModel(baker, baseModel, modelTextures);
			List<BakedQuad> baseQuads = baseModel.bakeTopGeometry(modelTextures, baker, BlockModelRotation.X0_Y0).getAll();
			List<BakedQuad> glassQuads = glassModel.bakeTopGeometry(glassModel.getTopTextureSlots(), baker, BlockModelRotation.X0_Y0).getAll();
			return new ItemCellModel(modelSettings, (Fluid fluid) -> {
				List<BakedQuad> list = new ArrayList<>(backgroundQuads);
				Pair<TextureAtlasSprite, Integer> pair = parseFluid(fluid);
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
		public static Pair<TextureAtlasSprite, Integer> parseFluid(Fluid fluid) {
			FluidRenderHandler handler = FluidRenderHandlerRegistry.INSTANCE.get(fluid);
			if (handler == null) {
				return null;
			}
			Minecraft client = Minecraft.getInstance();
			if (client.player == null) {
				return null;
			}
			FluidState state = fluid.defaultFluidState();
			int tint = handler.getFluidColor(client.level, client.player.blockPosition(), state) | 0xFF000000;
			TextureAtlasSprite sprite = handler.getFluidSprites(client.level, BlockPos.ZERO, state)[0];
			return Pair.of(sprite, tint);
		}

		public static List<BakedQuad> bakeFluidQuads(ModelBaker baker, ResolvedModel model, TextureAtlasSprite sprite) {
			Material texture = new Material(sprite.atlasLocation(), sprite.contents().name());
			TextureSlots.Data textures = new TextureSlots.Data.Builder()
				.addTexture(TextureSlot.TEXTURE.getId(), texture).build();
			TextureSlots sprites = new TextureSlots.Resolver().addLast(textures).resolve(null);
			QuadCollection baked = model.getTopGeometry().bake(sprites, baker, BlockModelRotation.X0_Y0, model);
			return replaceTint(baked.getAll(), 0);
		}

		public static Supplier<Vector3f[]> bakeVector(List<BakedQuad> quads) {
			Set<Vector3f> set = new HashSet<>();
			for (BakedQuad bakedQuad : quads) {
				FaceBakery.extractPositions(bakedQuad.vertices(), set::add);
			}
			Vector3f[] vector = set.toArray(Vector3f[]::new);
			return () -> vector;
		}

		public static List<BakedQuad> replaceTint(List<BakedQuad> quads, int index) {
			List<BakedQuad> list = new ArrayList<>(quads.size());
			for (BakedQuad quad : quads) {
				list.add(new BakedQuad(quad.vertices(), index, quad.direction(), quad.sprite(), quad.shade(), quad.lightEmission()));
			}
			return list;
		}

		@Override
		public @NotNull MapCodec<ItemCellModel.Unbaked> type() {
			return CODEC;
		}
	}
}
