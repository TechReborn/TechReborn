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
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ResolvableModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import reborncore.common.fluid.container.ItemFluidInfo;
import techreborn.TechReborn;

import java.util.HashMap;

public class ItemCellModel implements ItemModel {
	public static final Identifier ID = Identifier.of(TechReborn.MOD_ID, "model/cell");
	private final DynamicCellBakedModel model;
	ItemCellModel(BakedModel baseModel, BakedModel fluidModel, BakedModel backgroundModel, BakedModel glassModel) {
		model = new DynamicCellBakedModel(baseModel, fluidModel, backgroundModel, glassModel);
	}

	@Override
	public void update(ItemRenderState state, ItemStack stack, ItemModelManager resolver, ModelTransformationMode transformationMode, @Nullable ClientWorld world, @Nullable LivingEntity user, int seed) {
		ItemRenderState.LayerRenderState layerRenderState = state.newLayer();

		Fluid fluid = Fluids.EMPTY;
		if (stack.getItem() instanceof ItemFluidInfo fluidInfo) {
			fluid = fluidInfo.getFluid(stack);
		}
		model.fluid = fluid;

		RenderLayer renderLayer = RenderLayers.getItemLayer(stack);
		layerRenderState.setModel(model, renderLayer);
	}

	public record Unbaked() implements ItemModel.Unbaked {
		public static final MapCodec<ItemCellModel.Unbaked> CODEC = MapCodec.unit(ItemCellModel.Unbaked::new);
		private static final HashMap<Identifier, BakedModel> CACHE = new HashMap<>();

		@Override
		public void resolve(ResolvableModel.Resolver resolver) {
			resolver.resolve(DynamicCellBakedModel.CELL_BASE);
			resolver.resolve(DynamicCellBakedModel.CELL_BACKGROUND);
			resolver.resolve(DynamicCellBakedModel.CELL_FLUID);
			resolver.resolve(DynamicCellBakedModel.CELL_GLASS);
		}

		@Override
		public ItemModel bake(ItemModel.BakeContext context) {
			BakedModel baseModel = CACHE.computeIfAbsent(DynamicCellBakedModel.CELL_BASE, context::bake);
			BakedModel fluidModel = CACHE.computeIfAbsent(DynamicCellBakedModel.CELL_FLUID, context::bake);
			BakedModel backgroundModel = CACHE.computeIfAbsent(DynamicCellBakedModel.CELL_BACKGROUND, context::bake);
			BakedModel glassModel = CACHE.computeIfAbsent(DynamicCellBakedModel.CELL_GLASS, context::bake);
			return new ItemCellModel(baseModel, fluidModel, backgroundModel, glassModel);
		}

		@Override
		public MapCodec<ItemCellModel.Unbaked> getCodec() {
			return CODEC;
		}
	}
}
