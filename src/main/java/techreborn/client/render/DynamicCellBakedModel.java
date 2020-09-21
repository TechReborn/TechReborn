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

import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import techreborn.TechReborn;

import java.util.Random;
import java.util.function.Supplier;

public class DynamicCellBakedModel extends BaseDynamicFluidBakedModel {

	private static final ModelIdentifier CELL_BASE = new ModelIdentifier(new Identifier(TechReborn.MOD_ID, "cell_base"), "inventory");
	private static final ModelIdentifier CELL_BACKGROUND = new ModelIdentifier(new Identifier(TechReborn.MOD_ID, "cell_background"), "inventory");
	private static final ModelIdentifier CELL_FLUID = new ModelIdentifier(new Identifier(TechReborn.MOD_ID, "cell_fluid"), "inventory");
	private static final ModelIdentifier CELL_GLASS = new ModelIdentifier(new Identifier(TechReborn.MOD_ID, "cell_glass"), "inventory");

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
		super.emitItemQuads(stack, randomSupplier, context);

		BakedModelManager bakedModelManager = MinecraftClient.getInstance().getBakedModelManager();
		context.fallbackConsumer().accept(bakedModelManager.getModel(CELL_GLASS));
	}

	@Override
	public Sprite getSprite() {
		return MinecraftClient.getInstance()
				.getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
				.apply(new Identifier("techreborn:item/cell_base"));
	}

	@Override
	public ModelIdentifier getBaseModel() {
		return CELL_BASE;
	}

	@Override
	public ModelIdentifier getBackgroundModel() {
		return CELL_BACKGROUND;
	}

	@Override
	public ModelIdentifier getFluidModel() {
		return CELL_FLUID;
	}
}
