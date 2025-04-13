package techreborn.client.render;

import net.fabricmc.fabric.api.client.model.loading.v1.BlockStateResolver;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import techreborn.blocks.misc.BlockMachineCasing;
import techreborn.utils.DirectionUtils;

import java.util.List;

public record MachineCasingModel(BakedGeometry quads, boolean useAmbientOcclusion, Sprite particleSprite) implements BlockStateModel, BlockModelPart {
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
		builder.addSprite(Direction.DOWN.getId(), alone);
		builder.addSprite(Direction.UP.getId(), alone);
		block.getStateManager().getStates().forEach(state -> {
			for (Direction direction : Direction.Type.HORIZONTAL) {
				switch (DirectionUtils.getHorizontalPart(direction, state.get(DirectionUtils.HORIZONTAL_NEIGHBORS))) {
					case ALONE -> builder.addSprite(direction.getId(), alone);
					case START -> builder.addSprite(direction.getId(), start);
					case MIDDLE -> builder.addSprite(direction.getId(), middle);
					case END -> builder.addSprite(direction.getId(), end);
				}
			}
			ModelTextures textures = new ModelTextures.Builder().addLast(builder.build()).build(null);
			context.setModel(state, new Unbaked(model, textures, alone));
		});
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable Direction side) {
		return quads.getQuads(side);
	}

	@Override
	public void addParts(Random random, List<BlockModelPart> parts) {
		parts.add(this);
	}

	public record Unbaked(Identifier id, ModelTextures textures, SpriteIdentifier particle) implements BlockStateModel.UnbakedGrouped {
		@Override
		public BlockStateModel bake(BlockState state, Baker baker) {
			BakedSimpleModel model = baker.getModel(id);
			BakedGeometry baked = model.getGeometry().bake(textures, baker, ModelRotation.X0_Y0, model);
			return new MachineCasingModel(baked, model.getAmbientOcclusion(), baker.getSpriteGetter().get(particle, model));
		}

		@Override
		public Object getEqualityGroup(BlockState state) {
			return this;
		}

		@Override
		public void resolve(Resolver resolver) {
			resolver.markDependency(id);
		}
	}
}