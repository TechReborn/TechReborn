package techreborn.client.render.entitys;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import techreborn.blockentity.storage.item.StorageUnitBaseBlockEntity;

/**
 * Created by drcrazy on 07-Jan-20 for TechReborn-1.15.
 */
public class StorageUnitEntityRenderer extends BlockEntityRenderer<StorageUnitBaseBlockEntity> {
	public StorageUnitEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(StorageUnitBaseBlockEntity storage, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if (storage.getWorld() == null) {
			return;
		}
		ItemStack stack = storage.getStoredStack();
		if (stack.isEmpty()) {
			return;
		}
		matrices.push();
		Direction direction = storage.getFacing();
		matrices.translate(0.5, 0.5, 0.5);
		matrices.multiply(Vector3f.NEGATIVE_Y.getDegreesQuaternion((direction.asRotation())));
		matrices.translate(0, 0, 0.501);
		matrices.scale(0.5F, 0.5F, 0.5F);
		matrices.scale(1, 1, 0.001F);
		MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GUI, 0xf000f0 /* (A vanilla constant) */, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
		matrices.pop();
	}
}
