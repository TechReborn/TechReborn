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
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import techreborn.TechReborn;

import java.util.function.Supplier;

public class DynamicCellBakedModel extends BaseDynamicFluidBakedModel {
	public static final Identifier CELL_BASE = Identifier.of(TechReborn.MOD_ID, "item/cell_base");
	public static final Identifier CELL_BACKGROUND = Identifier.of(TechReborn.MOD_ID, "item/cell_background");
	public static final Identifier CELL_FLUID = Identifier.of(TechReborn.MOD_ID, "item/cell_fluid");
	public static final Identifier CELL_GLASS = Identifier.of(TechReborn.MOD_ID, "item/cell_glass");

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
		super.emitItemQuads(stack, randomSupplier, context);

		BakedModelManager bakedModelManager = MinecraftClient.getInstance().getBakedModelManager();
		bakedModelManager.getModel(CELL_GLASS).emitItemQuads(stack, randomSupplier, context);
	}

	@Override
	public Sprite getParticleSprite() {
		return MinecraftClient.getInstance()
				.getSpriteAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)
				.apply(Identifier.of("techreborn:item/cell_base"));
	}

	@Override
	public Identifier getBaseModel() {
		return CELL_BASE;
	}

	@Override
	public Identifier getBackgroundModel() {
		return CELL_BACKGROUND;
	}

	@Override
	public Identifier getFluidModel() {
		return CELL_FLUID;
	}
}
