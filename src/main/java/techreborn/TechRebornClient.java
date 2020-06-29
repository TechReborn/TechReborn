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

package techreborn;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.hud.StackInfoHUD;
import reborncore.client.multiblock.MultiblockRenderer;
import reborncore.common.util.ItemUtils;
import reborncore.mixin.client.AccessorModelPredicateProviderRegistry;
import team.reborn.energy.Energy;
import techreborn.client.render.DynamicBucketBakedModel;
import techreborn.client.render.DynamicCellBakedModel;
import techreborn.client.render.entitys.CableCoverRenderer;
import techreborn.client.render.entitys.DrillHeadRenderer;
import techreborn.client.render.entitys.StorageUnitRenderer;
import techreborn.client.render.entitys.TurbineRenderer;
import techreborn.events.StackToolTipHandler;
import techreborn.init.ModFluids;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;
import techreborn.items.BatteryItem;
import techreborn.items.DynamicCellItem;
import techreborn.items.FrequencyTransmitterItem;
import techreborn.items.armor.BatpackItem;
import techreborn.items.tool.ChainsawItem;
import techreborn.items.tool.industrial.NanosaberItem;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

public class TechRebornClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModelLoadingRegistry.INSTANCE.registerAppender((manager, out) -> {
			out.accept(new ModelIdentifier(new Identifier(TechReborn.MOD_ID, "cell_base"), "inventory"));
			out.accept(new ModelIdentifier(new Identifier(TechReborn.MOD_ID, "cell_fluid"), "inventory"));
			out.accept(new ModelIdentifier(new Identifier(TechReborn.MOD_ID, "cell_background"), "inventory"));
			out.accept(new ModelIdentifier(new Identifier(TechReborn.MOD_ID, "cell_glass"), "inventory"));

			out.accept(new ModelIdentifier(new Identifier(TechReborn.MOD_ID, "bucket_base"), "inventory"));
			out.accept(new ModelIdentifier(new Identifier(TechReborn.MOD_ID, "bucket_fluid"), "inventory"));
			out.accept(new ModelIdentifier(new Identifier(TechReborn.MOD_ID, "bucket_background"), "inventory"));
		});

		ModelLoadingRegistry.INSTANCE.registerVariantProvider(resourceManager -> (modelIdentifier, modelProviderContext) -> {
			if (modelIdentifier.getNamespace().equals(TechReborn.MOD_ID)) {
				if (modelIdentifier.getPath().equals("cell")) {
					return new UnbakedModel() {
						@Override
						public Collection<Identifier> getModelDependencies() {
							return Collections.emptyList();
						}

						@Override
						public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
							return Collections.emptyList();
						}

						@Nullable
						@Override
						public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
							return new DynamicCellBakedModel();
						}

					};
				}
				Fluid fluid = Registry.FLUID.get(new Identifier(TechReborn.MOD_ID, modelIdentifier.getPath().split("_bucket")[0]));
				if (modelIdentifier.getPath().endsWith("_bucket") && fluid != Fluids.EMPTY) {
					return new UnbakedModel() {
						@Override
						public Collection<Identifier> getModelDependencies() {
							return Collections.emptyList();
						}

						@Override
						public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
							return Collections.emptyList();
						}

						@Nullable
						@Override
						public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
							return new DynamicBucketBakedModel();
						}

					};
				}
			}
			return null;
		});

		StackToolTipHandler.setup();

		GuiBase.wrenchStack = new ItemStack(TRContent.WRENCH);
		GuiBase.fluidCellProvider = DynamicCellItem::getCellWithFluid;

		StackInfoHUD.registerElement(new FrequencyTransmitterItem.StackInfoFreqTransmitter());

		Arrays.stream(TRContent.Cables.values()).forEach(cable -> BlockRenderLayerMap.INSTANCE.putBlock(cable.block, RenderLayer.getCutout()));

		BlockRenderLayerMap.INSTANCE.putBlock(TRContent.Machine.LAMP_INCANDESCENT.block, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(TRContent.Machine.LAMP_LED.block, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(TRContent.Machine.ALARM.block, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(TRContent.RUBBER_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(TRContent.REINFORCED_GLASS, RenderLayer.getCutout());

		BlockRenderLayerMap.INSTANCE.putBlock(TRContent.RUBBER_LEAVES, RenderLayer.getCutoutMipped());

		for (ModFluids fluid : ModFluids.values()) {
			BlockRenderLayerMap.INSTANCE.putFluid(fluid.getFluid(), RenderLayer.getTranslucent());
			BlockRenderLayerMap.INSTANCE.putFluid(fluid.getFlowingFluid(), RenderLayer.getTranslucent());
		}

		// Multiblock rendering (MUST ADD HERE IF WANTING TO RENDER MULTIBLOCK)
		BlockEntityRendererRegistry.INSTANCE.register(TRBlockEntities.INDUSTRIAL_GRINDER, MultiblockRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(TRBlockEntities.FUSION_CONTROL_COMPUTER, MultiblockRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(TRBlockEntities.INDUSTRIAL_BLAST_FURNACE, MultiblockRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(TRBlockEntities.VACUUM_FREEZER, MultiblockRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(TRBlockEntities.FLUID_REPLICATOR, MultiblockRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(TRBlockEntities.INDUSTRIAL_SAWMILL, MultiblockRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(TRBlockEntities.DISTILLATION_TOWER, MultiblockRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(TRBlockEntities.IMPLOSION_COMPRESSOR, MultiblockRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(TRBlockEntities.MINING_RIG, MultiblockRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(TRBlockEntities.GREENHOUSE_CONTROLLER, MultiblockRenderer::new);


		// Other rendering
		BlockEntityRendererRegistry.INSTANCE.register(TRBlockEntities.STORAGE_UNIT, StorageUnitRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(TRBlockEntities.CABLE, CableCoverRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(TRBlockEntities.WIND_MILL, TurbineRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(TRBlockEntities.DRILL_HEAD, DrillHeadRenderer::new);

		registerPredicateProvider(
				BatpackItem.class,
				new Identifier("techreborn:empty"),
				(item, stack, world, entity) -> {
					if (!stack.isEmpty() && Energy.of(stack).getEnergy() == 0) {
						return 1.0F;
					}
					return 0.0F;
				}
		);

		registerPredicateProvider(
				BatteryItem.class,
				new Identifier("techreborn:empty"),
				(item, stack, world, entity) -> {
					if (!stack.isEmpty() && Energy.of(stack).getEnergy() == 0) {
						return 1.0F;
					}
					return 0.0F;
				}
		);

		registerPredicateProvider(
				FrequencyTransmitterItem.class,
				new Identifier("techreborn:coords"),
				(item, stack, world, entity) -> {
					if (!stack.isEmpty() && stack.hasTag() && stack.getTag() != null && stack.getTag().contains("x")
							&& stack.getTag().contains("y") && stack.getTag().contains("z") && stack.getTag().contains("dim")) {
						return 1.0F;
					}
					return 0.0F;
				}
		);

		registerPredicateProvider(
				ChainsawItem.class,
				new Identifier("techreborn:animated"),
				(item, stack, world, entity) -> {
					if (!stack.isEmpty() && Energy.of(stack).getEnergy() >= item.getCost() && entity != null && entity.getMainHandStack().equals(stack)) {
						return 1.0F;
					}
					return 0.0F;
				}
		);

		registerPredicateProvider(
				NanosaberItem.class,
				new Identifier("techreborn:active"),
				(item, stack, world, entity) -> {
					if (ItemUtils.isActive(stack)) {
						if (Energy.of(stack).getMaxStored() - Energy.of(stack).getEnergy() >= 0.9 * Energy.of(stack).getMaxStored()) {
							return 0.5F;
						}
						return 1.0F;
					}
					return 0.0F;
				}
		);

	}

	private static <T extends Item> void registerPredicateProvider(Class<T> itemClass, Identifier identifier, ItemModelPredicateProvider<T> modelPredicateProvider) {
		Registry.ITEM.stream()
				.filter(item -> item.getClass().isAssignableFrom(itemClass))
				.forEach(item -> AccessorModelPredicateProviderRegistry.callRegister(item, identifier, modelPredicateProvider));
	}

	//Need the item instance in a few places, this makes it easier
	private interface ItemModelPredicateProvider<T extends Item> extends ModelPredicateProvider {

		float call(T item, ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity);

		@Override
		default float call(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity) {
			return call((T) stack.getItem(), stack, world, entity);
		}
	}

}
