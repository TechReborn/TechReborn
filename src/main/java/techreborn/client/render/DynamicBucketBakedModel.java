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

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.indigo.renderer.helper.GeometryHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelItemPropertyOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ExtendedBlockView;
import reborncore.common.fluid.container.ItemFluidInfo;
import reborncore.common.util.Color;
import techreborn.TechReborn;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class DynamicBucketBakedModel implements BakedModel, FabricBakedModel {

	private final Fluid fluid;

	private static final ModelIdentifier BUCKET_BASE = new ModelIdentifier(new Identifier(TechReborn.MOD_ID, "bucket_base"), "inventory");
	private static final ModelIdentifier BUCKET_BACKGROUND = new ModelIdentifier(new Identifier(TechReborn.MOD_ID, "bucket_background"), "inventory");
	private static final ModelIdentifier BUCKET_FLUID = new ModelIdentifier(new Identifier(TechReborn.MOD_ID, "bucket_fluid"), "inventory");

	public DynamicBucketBakedModel(Fluid fluid) {
		this.fluid = fluid;
	}

	@Override
	public void emitBlockQuads(ExtendedBlockView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {

	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
		Fluid fluid = Fluids.EMPTY;
		if (stack.getItem() instanceof ItemFluidInfo) {
			ItemFluidInfo fluidInfo = (ItemFluidInfo) stack.getItem();
			fluid = fluidInfo.getFluid(stack);

		}
		BakedModelManager bakedModelManager = MinecraftClient.getInstance().getBakedModelManager();
		context.fallbackConsumer().accept(bakedModelManager.getModel(BUCKET_BASE));
		context.fallbackConsumer().accept(bakedModelManager.getModel(BUCKET_BACKGROUND));

		if (fluid != Fluids.EMPTY) {
			FluidRenderHandler fluidRenderHandler = FluidRenderHandlerRegistry.INSTANCE.get(fluid);
			BakedModel fluidModel = bakedModelManager.getModel(BUCKET_FLUID);
			int fluidColor = fluidRenderHandler.getFluidColor(MinecraftClient.getInstance().world, MinecraftClient.getInstance().player.getBlockPos(), fluid.getDefaultState());
			Sprite fluidSprite = fluidRenderHandler.getFluidSprites(MinecraftClient.getInstance().world, BlockPos.ORIGIN, fluid.getDefaultState())[0];
			int color = new Color((float) (fluidColor >> 16 & 255) / 255.0F, (float) (fluidColor >> 8 & 255) / 255.0F, (float) (fluidColor & 255) / 255.0F).getColor();
			context.pushTransform(quad -> {
				quad.nominalFace(GeometryHelper.lightFace(quad));
				quad.spriteColor(0, color, color, color, color);
				quad.spriteBake(0, fluidSprite, MutableQuadView.BAKE_LOCK_UV);
				return true;
			});
			final QuadEmitter emitter = context.getEmitter();
			fluidModel.getQuads(null, null, randomSupplier.get()).forEach(q -> {
				emitter.fromVanilla(q.getVertexData(), 0, false);
				emitter.emit();
			});
			context.popTransform();
		}
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, Random random) {
		return Collections.emptyList();
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public boolean useAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean hasDepthInGui() {
		return false;
	}

	@Override
	public boolean isBuiltin() {
		return false;
	}

	@Override
	public Sprite getSprite() {
		return MinecraftClient.getInstance().getSpriteAtlas().getSprite(new Identifier("minecraft:item/bucket"));
	}

	@Override
	public ModelTransformation getTransformation() {
		return ModelHelper.DEFAULT_ITEM_TRANSFORMS;
	}

	@Override
	public ModelItemPropertyOverrideList getItemPropertyOverrides() {
		return ModelItemPropertyOverrideList.EMPTY;
	}
}
