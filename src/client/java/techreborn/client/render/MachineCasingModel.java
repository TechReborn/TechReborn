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

import com.mojang.datafixers.util.Either;
import net.fabricmc.fabric.api.client.model.loading.v1.BlockStateResolver;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import techreborn.blocks.misc.BlockMachineCasing;
import techreborn.utils.DirectionUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MachineCasingModel {
	public static final String MODEL_PATH = "block/machines/structure/";
	@SuppressWarnings("deprecation")
	public static void resolveBlockStates(BlockStateResolver.Context context) {
		BlockMachineCasing block = (BlockMachineCasing) context.block();
		Identifier model = Registries.BLOCK.getId(block).withPrefixedPath(MODEL_PATH);
		Either<SpriteIdentifier, String> alone = Either.left(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, model));
		Either<SpriteIdentifier, String> start = Either.left(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, model.withSuffixedPath("_start")));
		Either<SpriteIdentifier, String> middle = Either.left(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, model.withSuffixedPath("_middle")));
		Either<SpriteIdentifier, String> end = Either.left(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, model.withSuffixedPath("_end")));

		block.getStateManager().getStates().forEach(state -> {
			Map<String, Either<SpriteIdentifier, String>> textures = new HashMap<>();
			textures.put("down", alone);
			textures.put("up", alone);
			for (Direction direction : Direction.Type.HORIZONTAL) {
				switch (DirectionUtils.getHorizontalPart(direction, state.get(DirectionUtils.HORIZONTAL_NEIGHBORS))) {
					case ALONE -> textures.put(direction.getName(), alone);
					case START -> textures.put(direction.getName(), start);
					case MIDDLE -> textures.put(direction.getName(), middle);
					case END -> textures.put(direction.getName(), end);
				}
			}
			context.setModel(state, new Unbaked(model, textures));
		});
	}

	public record Unbaked(Identifier id, Map<String, Either<SpriteIdentifier, String>> textures) implements UnbakedModel {
		@Override
		public Collection<Identifier> getModelDependencies() {
			return List.of(id);
		}

		@Override
		public void setParents(Function<Identifier, UnbakedModel> modelLoader) {
		}

		@Override
		public @Nullable BakedModel bake(
			Baker baker,
			Function<SpriteIdentifier, Sprite> textureGetter,
			ModelBakeSettings rotationContainer
		) {
			JsonUnbakedModel model = (JsonUnbakedModel) baker.getOrLoadModel(id);
			model.textureMap.putAll(textures);
			return model.bake(baker, textureGetter, rotationContainer);
		}
	}
}
