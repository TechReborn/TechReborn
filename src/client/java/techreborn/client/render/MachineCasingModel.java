/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2025 TechReborn
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

import net.fabricmc.fabric.api.client.model.loading.v1.BlockStateResolver;
import net.minecraft.block.BlockState;
import net.minecraft.client.data.TextureKey;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import techreborn.blocks.misc.BlockMachineCasing;
import techreborn.utils.DirectionUtils;

public class MachineCasingModel {
	public static final String MODEL_PATH = "block/machines/structure/";
	@SuppressWarnings("deprecation")
	public static void resolveBlockStates(BlockStateResolver.Context context) {
		BlockMachineCasing block = (BlockMachineCasing) context.block();
		Identifier model = Registries.BLOCK.getId(block).withPrefixedPath(MODEL_PATH);
		SpriteIdentifier alone = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, model);
		SpriteIdentifier start = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, model.withSuffixedPath("_start"));
		SpriteIdentifier middle = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, model.withSuffixedPath("_middle"));
		SpriteIdentifier end = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, model.withSuffixedPath("_end"));
		ModelTextures.Textures.Builder builder = new ModelTextures.Textures.Builder();
		builder.addSprite(Direction.DOWN.getName(), alone);
		builder.addSprite(Direction.UP.getName(), alone);
		builder.addSprite(TextureKey.PARTICLE.getName(), alone);
		block.getStateManager().getStates().forEach(state -> {
			for (Direction direction : Direction.Type.HORIZONTAL) {
				switch (DirectionUtils.getHorizontalPart(direction, state.get(DirectionUtils.HORIZONTAL_NEIGHBORS))) {
					case ALONE -> builder.addSprite(direction.getName(), alone);
					case START -> builder.addSprite(direction.getName(), start);
					case MIDDLE -> builder.addSprite(direction.getName(), middle);
					case END -> builder.addSprite(direction.getName(), end);
				}
			}
			ModelTextures textures = new ModelTextures.Builder().addLast(builder.build()).build(null);
			context.setModel(state, new Unbaked(model, textures));
		});
	}

	public record Unbaked(Identifier id, ModelTextures textures) implements GroupableModel {
		@Override
		public void resolve(Resolver resolver) {
			resolver.resolve(id);
		}

		@Override
		public Object getEqualityGroup(BlockState state) {
			return this;
		}

		@Override
		public BakedModel bake(Baker baker) {
			UnbakedModel model = ((ModelBaker.BakerImpl) baker).getModel(id);
			boolean ambientOcclusion = UnbakedModel.getAmbientOcclusion(model);
			boolean isSideLit = UnbakedModel.getGuiLight(model).isSide();
			ModelTransformation transformation = UnbakedModel.getTransformations(model);
			return model.bake(textures, baker, ModelRotation.X0_Y0, ambientOcclusion, isSideLit, transformation);
		}
	}
}
