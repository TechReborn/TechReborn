package techreborn;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import techreborn.client.render.DynamicCellBakedModel;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

public class TechRebornClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEX)
			.register((spriteAtlasTexture, registry) -> registry.register(new Identifier("techreborn:item/cell_base")));

		ModelLoadingRegistry.INSTANCE.registerVariantProvider(resourceManager -> (modelIdentifier, modelProviderContext) -> {
			if(!modelIdentifier.getNamespace().equals("techreborn")){
				return null;
			}
			if(!modelIdentifier.getPath().equals("cell")){
				return null;
			}
			return new UnbakedModel() {
				@Override
				public Collection<Identifier> getModelDependencies() {
					return Collections.emptyList();
				}

				@Override
				public Collection<Identifier> getTextureDependencies(Function<Identifier, UnbakedModel> function, Set<String> set) {
					return Collections.emptyList();
				}

				@Nullable
				@Override
				public BakedModel bake(ModelLoader modelLoader, Function<Identifier, Sprite> function, ModelBakeSettings modelBakeSettings) {
					return new DynamicCellBakedModel();
				}
			};
		});
	}



}
