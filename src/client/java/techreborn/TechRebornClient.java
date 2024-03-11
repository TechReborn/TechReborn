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

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.minecraft.client.item.ClampedModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import reborncore.client.ClientJumpEvent;
import reborncore.client.gui.GuiBase;
import reborncore.client.multiblock.MultiblockRenderer;
import reborncore.common.powerSystem.RcEnergyItem;
import reborncore.common.util.ItemUtils;
import team.reborn.energy.api.base.SimpleEnergyItem;
import techreborn.client.ClientGuiType;
import techreborn.client.ClientboundPacketHandlers;
import techreborn.client.events.ClientJumpHandler;
import techreborn.client.events.StackToolTipHandler;
import techreborn.client.keybindings.KeyBindings;
import techreborn.client.render.BaseDynamicFluidBakedModel;
import techreborn.client.render.DynamicBucketBakedModel;
import techreborn.client.render.DynamicCellBakedModel;
import techreborn.client.render.entitys.CableCoverRenderer;
import techreborn.client.render.entitys.NukeRenderer;
import techreborn.client.render.entitys.StorageUnitRenderer;
import techreborn.client.render.entitys.TurbineRenderer;
import techreborn.init.ModFluids;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;
import techreborn.items.BatteryItem;
import techreborn.items.DynamicCellItem;
import techreborn.items.FrequencyTransmitterItem;
import techreborn.items.armor.BatpackItem;
import techreborn.items.tool.ChainsawItem;
import techreborn.items.tool.industrial.NanosaberItem;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;
import java.util.function.Supplier;

public class TechRebornClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModelLoadingPlugin.register((pluginContext) -> {
			pluginContext.addModels(
				new ModelIdentifier(new Identifier(TechReborn.MOD_ID, "cell_base"), "inventory"),
				new ModelIdentifier(new Identifier(TechReborn.MOD_ID, "cell_fluid"), "inventory"),
				new ModelIdentifier(new Identifier(TechReborn.MOD_ID, "cell_background"), "inventory"),
				new ModelIdentifier(new Identifier(TechReborn.MOD_ID, "cell_glass"), "inventory"),
				new ModelIdentifier(new Identifier(TechReborn.MOD_ID, "bucket_base"), "inventory"),
				new ModelIdentifier(new Identifier(TechReborn.MOD_ID, "bucket_fluid"), "inventory"),
				new ModelIdentifier(new Identifier(TechReborn.MOD_ID, "bucket_background"), "inventory")
			);

			pluginContext.resolveModel().register((context) -> {
				final Identifier id = context.id();

				if (!id.getNamespace().equals(TechReborn.MOD_ID) || !id.getPath().startsWith("item/")) {
					return null;
				}

				String path = id.getPath().replace("item/", "");

				if (path.equals("cell")) {
					if (!RendererAccess.INSTANCE.hasRenderer()) {
						return JsonUnbakedModel.deserialize("{\"parent\":\"minecraft:item/generated\",\"textures\":{\"layer0\":\"techreborn:item/cell_background\"}}");
					}

					return new UnbakedDynamicModel(DynamicCellBakedModel::new);
				}

				Fluid fluid = Registries.FLUID.get(new Identifier(TechReborn.MOD_ID, path.split("_bucket")[0]));
				if (path.endsWith("_bucket") && fluid != Fluids.EMPTY) {
					if (!RendererAccess.INSTANCE.hasRenderer()) {
						return JsonUnbakedModel.deserialize("{\"parent\":\"minecraft:item/generated\",\"textures\":{\"layer0\":\"minecraft:item/bucket\"}}");
					}

					return new UnbakedDynamicModel(DynamicBucketBakedModel::new);
				}

				return null;
			});
		});

		KeyBindings.registerKeys();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (KeyBindings.suitNightVision.wasPressed()) {
				KeyBindings.handleSuitNVToggle();
			}
		});

		StackToolTipHandler.setup();
		ClientboundPacketHandlers.init();

		GuiBase.wrenchStack = new ItemStack(TRContent.WRENCH);
		GuiBase.fluidCellProvider = DynamicCellItem::getCellWithFluid;

		Arrays.stream(TRContent.Cables.values()).forEach(cable -> BlockRenderLayerMap.INSTANCE.putBlock(cable.block, RenderLayer.getCutout()));

		BlockRenderLayerMap.INSTANCE.putBlock(TRContent.Machine.LAMP_INCANDESCENT.block, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(TRContent.Machine.LAMP_LED.block, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(TRContent.Machine.ALARM.block, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(TRContent.RUBBER_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(TRContent.REINFORCED_GLASS, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(TRContent.Machine.RESIN_BASIN.block, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(TRContent.POTTED_RUBBER_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(TRContent.Machine.FISHING_STATION.block, RenderLayer.getCutout());

		BlockRenderLayerMap.INSTANCE.putBlock(TRContent.RUBBER_LEAVES, RenderLayer.getCutoutMipped());

		for (ModFluids fluid : ModFluids.values()) {
			BlockRenderLayerMap.INSTANCE.putFluid(fluid.getFluid(), RenderLayer.getTranslucent());
			BlockRenderLayerMap.INSTANCE.putFluid(fluid.getFlowingFluid(), RenderLayer.getTranslucent());
		}

		BlockEntityRendererFactories.register(TRBlockEntities.INDUSTRIAL_GRINDER, MultiblockRenderer::new);
		BlockEntityRendererFactories.register(TRBlockEntities.FUSION_CONTROL_COMPUTER, MultiblockRenderer::new);
		BlockEntityRendererFactories.register(TRBlockEntities.INDUSTRIAL_BLAST_FURNACE, MultiblockRenderer::new);
		BlockEntityRendererFactories.register(TRBlockEntities.VACUUM_FREEZER, MultiblockRenderer::new);
		BlockEntityRendererFactories.register(TRBlockEntities.FLUID_REPLICATOR, MultiblockRenderer::new);
		BlockEntityRendererFactories.register(TRBlockEntities.INDUSTRIAL_SAWMILL, MultiblockRenderer::new);
		BlockEntityRendererFactories.register(TRBlockEntities.DISTILLATION_TOWER, MultiblockRenderer::new);
		BlockEntityRendererFactories.register(TRBlockEntities.IMPLOSION_COMPRESSOR, MultiblockRenderer::new);
		BlockEntityRendererFactories.register(TRBlockEntities.GREENHOUSE_CONTROLLER, MultiblockRenderer::new);
		BlockEntityRendererFactories.register(TRBlockEntities.STORAGE_UNIT, StorageUnitRenderer::new);
		BlockEntityRendererFactories.register(TRBlockEntities.CABLE, CableCoverRenderer::new);
		BlockEntityRendererFactories.register(TRBlockEntities.WIND_MILL, TurbineRenderer::new);

		EntityRendererRegistry.register(TRContent.ENTITY_NUKE, NukeRenderer::new);

		registerPredicateProvider(
				BatpackItem.class,
				new Identifier("techreborn:empty"),
				(item, stack, world, entity, seed) -> {
					if (!stack.isEmpty() && SimpleEnergyItem.getStoredEnergyUnchecked(stack) == 0) {
						return 1.0F;
					}
					return 0.0F;
				}
		);

		registerPredicateProvider(
				BatteryItem.class,
				new Identifier("techreborn:empty"),
				(item, stack, world, entity, seed) -> {
					if (!stack.isEmpty() && SimpleEnergyItem.getStoredEnergyUnchecked(stack) == 0) {
						return 1.0F;
					}
					return 0.0F;
				}
		);

		registerPredicateProvider(
				FrequencyTransmitterItem.class,
				new Identifier("techreborn:coords"),
				(item, stack, world, entity, seed) -> {
					if (!stack.isEmpty() && stack.hasNbt() && stack.getOrCreateNbt().contains("x")
							&& stack.getOrCreateNbt().contains("y") && stack.getOrCreateNbt().contains("z")
							&& stack.getOrCreateNbt().contains("dim")) {
						return 1.0F;
					}
					return 0.0F;
				}
		);

		registerPredicateProvider(
				ChainsawItem.class,
				new Identifier("techreborn:animated"),
				(item, stack, world, entity, seed) -> {
					if (!stack.isEmpty() && SimpleEnergyItem.getStoredEnergyUnchecked(stack) >= item.getCost() && entity != null && entity.getMainHandStack().equals(stack)) {
						return 1.0F;
					}
					return 0.0F;
				}
		);

		registerPredicateProvider(
				NanosaberItem.class,
				new Identifier("techreborn:active"),
				(item, stack, world, entity, seed) -> {
					if (ItemUtils.isActive(stack)) {
						RcEnergyItem energyItem = (RcEnergyItem) stack.getItem();
						if (energyItem.getEnergyCapacity(stack) - energyItem.getStoredEnergy(stack) >= 0.9 * item.getEnergyCapacity(stack)) {
							return 0.5F;
						}
						return 1.0F;
					}
					return 0.0F;
				}
		);

		ClientGuiType.validate();

		ClientJumpEvent.EVENT.register(new ClientJumpHandler());
	}

	private static <T extends Item> void registerPredicateProvider(Class<T> itemClass, Identifier identifier, ItemModelPredicateProvider<T> modelPredicateProvider) {
		Registries.ITEM.stream()
				.filter(itemClass::isInstance)
				.forEach(item -> ModelPredicateProviderRegistry.register(item, identifier, modelPredicateProvider));
	}

	//Need the item instance in a few places, this makes it easier
	private interface ItemModelPredicateProvider<T extends Item> extends ClampedModelPredicateProvider {

		float call(T item, ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed);

		@Override
		default float unclampedCall(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed) {
			//noinspection unchecked
			return call((T) stack.getItem(), stack, world, entity, seed);
		}

	}

	private record UnbakedDynamicModel(Supplier<BaseDynamicFluidBakedModel> supplier) implements UnbakedModel {
		@Override
		public Collection<Identifier> getModelDependencies() {
			return Collections.emptyList();
		}

		@Override
		public void setParents(Function<Identifier, UnbakedModel> modelLoader) {

		}

		@Nullable
		@Override
		public BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
			return supplier.get();
		}
	}
}
