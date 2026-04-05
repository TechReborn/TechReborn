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
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.SimpleModelWrapper;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import techreborn.blocks.misc.BlockMachineCasing;
import techreborn.utils.DirectionUtils;

import java.util.List;

public record MachineCasingModel(BlockStateModelPart part) implements BlockStateModel {
	public static final String MODEL_PATH = "block/machines/structure/";

	public static void resolveBlockStates(BlockStateResolver.Context context) {
		BlockMachineCasing block = (BlockMachineCasing) context.block();
		Identifier model = BuiltInRegistries.BLOCK.getKey(block).withPrefix(MODEL_PATH);
		Material alone = new Material(model);
		Material start = new Material(model.withSuffix("_start"));
		Material middle = new Material(model.withSuffix("_middle"));
		Material end = new Material(model.withSuffix("_end"));
		TextureSlots.Data.Builder builder = new TextureSlots.Data.Builder();
		builder.addTexture(Direction.DOWN.getName(), alone);
		builder.addTexture(Direction.UP.getName(), alone);
		block.getStateDefinition().getPossibleStates().forEach(state -> {
			for (Direction direction : Direction.Plane.HORIZONTAL) {
				switch (DirectionUtils.getHorizontalPart(direction, state.getValue(DirectionUtils.HORIZONTAL_NEIGHBORS))) {
					case ALONE -> builder.addTexture(direction.getName(), alone);
					case START -> builder.addTexture(direction.getName(), start);
					case MIDDLE -> builder.addTexture(direction.getName(), middle);
					case END -> builder.addTexture(direction.getName(), end);
				}
			}
			TextureSlots textures = new TextureSlots.Resolver().addLast(builder.build()).resolve(null);
			context.setModel(state, new Unbaked(model, textures, alone));
		});
	}

	@Override
	public void collectParts(RandomSource random, List<BlockStateModelPart> output) {
		output.add(part);
	}

	@Override
	public Material.Baked particleMaterial() {
		return part.particleMaterial();
	}

	@Override
	public @BakedQuad.MaterialFlags int materialFlags() {
		return 0;
	}

	public record Unbaked(Identifier id, TextureSlots textures, Material particle) implements BlockStateModel.UnbakedRoot {
		@Override
		public BlockStateModel bake(BlockState state, ModelBaker baker) {
			ResolvedModel model = baker.getModel(id);
			QuadCollection baked = model.getTopGeometry().bake(textures, baker, BlockModelRotation.IDENTITY, model);
			return new MachineCasingModel(new SimpleModelWrapper(baked, model.getTopAmbientOcclusion(), baker.materials().get(particle, model)));
		}

		@Override
		public Object visualEqualityGroup(BlockState state) {
			return this;
		}

		@Override
		public void resolveDependencies(Resolver resolver) {
			resolver.markDependency(id);
		}
	}
}
