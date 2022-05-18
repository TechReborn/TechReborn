package reborncore.client.multiblock;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.AbstractRandom;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import reborncore.common.blockentity.MultiblockWriter;

import java.util.Random;
import java.util.function.BiPredicate;

/**
 * Renders a hologram
 */
@Environment(EnvType.CLIENT)
public
record HologramRenderer(BlockRenderView view, MatrixStack matrix, VertexConsumerProvider vertexConsumerProvider,
						float scale) implements MultiblockWriter {
	private static final BlockPos OUT_OF_WORLD_POS = new BlockPos(0, 260, 0); // Bad hack; disables lighting

	@Override
	public MultiblockWriter add(int x, int y, int z, BiPredicate<BlockView, BlockPos> predicate, BlockState state) {
		final BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
		matrix.push();
		matrix.translate(x, y, z);
		matrix.translate(0.5, 0.5, 0.5);
		matrix.scale(scale, scale, scale);


		if (state.getBlock() instanceof FluidBlock) {
			FluidState fluidState = ((FluidBlock) state.getBlock()).getFluidState(state);
			MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(fluidState.getFluid().getBucketItem()), ModelTransformation.Mode.FIXED, 15728880, OverlayTexture.DEFAULT_UV, matrix, vertexConsumerProvider, 0);
		} else {
			matrix.translate(-0.5, -0.5, -0.5);
			VertexConsumer consumer = vertexConsumerProvider.getBuffer(RenderLayers.getBlockLayer(state));
			blockRenderManager.renderBlock(state, OUT_OF_WORLD_POS, view, matrix, consumer, false, AbstractRandom.create());
		}

		matrix.pop();
		return this;
	}
}
