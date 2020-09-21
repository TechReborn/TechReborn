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

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import techreborn.TechReborn;

public class DynamicBucketBakedModel extends BaseDynamicFluidBakedModel {

	private static final ModelIdentifier BUCKET_BASE = new ModelIdentifier(new Identifier(TechReborn.MOD_ID, "bucket_base"), "inventory");
	private static final ModelIdentifier BUCKET_BACKGROUND = new ModelIdentifier(new Identifier(TechReborn.MOD_ID, "bucket_background"), "inventory");
	private static final ModelIdentifier BUCKET_FLUID = new ModelIdentifier(new Identifier(TechReborn.MOD_ID, "bucket_fluid"), "inventory");

	@Override
	public Sprite getSprite() {
		return MinecraftClient.getInstance()
				.getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
				.apply(new Identifier("minecraft:item/bucket"));
	}

	@Override
	public ModelIdentifier getBaseModel() {
		return BUCKET_BASE;
	}

	@Override
	public ModelIdentifier getBackgroundModel() {
		return BUCKET_BACKGROUND;
	}

	@Override
	public ModelIdentifier getFluidModel() {
		return BUCKET_FLUID;
	}
}
