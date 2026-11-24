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

package techreborn.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.core.registries.BuiltInClientRegistries;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jspecify.annotations.Nullable;
import reborncore.client.ClientJumpEvent;
import reborncore.client.gui.GuiBase;
import reborncore.client.multiblock.MultiblockRenderer;
import techreborn.client.events.ClientJumpHandler;
import techreborn.client.events.StackToolTipHandler;
import techreborn.client.keybindings.KeyBindings;
import techreborn.client.render.ActiveProperty;
import techreborn.client.render.ItemBucketModel;
import techreborn.client.render.ItemCellModel;
import techreborn.client.render.entitys.CableCoverRenderer;
import techreborn.client.render.entitys.NukeRenderer;
import techreborn.client.render.entitys.StorageUnitRenderer;
import techreborn.client.render.entitys.TurbineRenderer;
import techreborn.entities.EntityNukePrimed;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class TechRebornClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		Registry.register(BuiltInClientRegistries.ITEM_MODEL_TYPE, ItemCellModel.ID, ItemCellModel.Unbaked.CODEC);
		Registry.register(BuiltInClientRegistries.ITEM_MODEL_TYPE, ItemBucketModel.ID, ItemBucketModel.Unbaked.CODEC);
		Registry.register(BuiltInClientRegistries.SELECT_ITEM_MODEL_PROPERTY_TYPE, ActiveProperty.ID, ActiveProperty.TYPE);

		KeyBindings.registerKeys();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (KeyBindings.suitNightVision.consumeClick()) {
				KeyBindings.handleSuitNVToggle();
			}
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (KeyBindings.quantumSuitSprint.consumeClick()) {
				KeyBindings.handleQuantumSuitSprintToggle();
			}
		});

		StackToolTipHandler.setup();
		ClientboundPacketHandlers.init();

		GuiBase.wrenchStack = new ItemStackTemplate(TRContent.WRENCH);
		GuiBase.fluidCellProvider = fluid -> TRContent.Cells.getCellByFluid(fluid).getStack();

		// TODO 26.1: ChunkSectionLayerMap / BlockRenderLayerMap removed — render layers now defined in model JSON files
		// Arrays.stream(TRContent.Cables.values()).forEach(cable -> ChunkSectionLayerMap.putBlock(cable.block, ChunkSectionLayer.CUTOUT));
		// ChunkSectionLayerMap.putBlock(TRContent.Machine.LAMP_INCANDESCENT.block, ChunkSectionLayer.CUTOUT);
		// ChunkSectionLayerMap.putBlock(TRContent.Machine.LAMP_LED.block, ChunkSectionLayer.CUTOUT);
		// ChunkSectionLayerMap.putBlock(TRContent.Machine.ALARM.block, ChunkSectionLayer.CUTOUT);
		// ChunkSectionLayerMap.putBlock(TRContent.RUBBER_SAPLING, ChunkSectionLayer.CUTOUT);
		// ChunkSectionLayerMap.putBlock(TRContent.REINFORCED_GLASS, ChunkSectionLayer.CUTOUT);
		// ChunkSectionLayerMap.putBlock(TRContent.Machine.RESIN_BASIN.block, ChunkSectionLayer.CUTOUT);
		// ChunkSectionLayerMap.putBlock(TRContent.POTTED_RUBBER_SAPLING, ChunkSectionLayer.CUTOUT);
		// ChunkSectionLayerMap.putBlock(TRContent.Machine.FISHING_STATION.block, ChunkSectionLayer.CUTOUT);
		// ChunkSectionLayerMap.putBlock(TRContent.RUBBER_LEAVES, ChunkSectionLayer.CUTOUT);
		// for (ModFluids fluid : ModFluids.values()) {
		// 	ChunkSectionLayerMap.putFluid(fluid.getFluid(), ChunkSectionLayer.TRANSLUCENT);
		// 	ChunkSectionLayerMap.putFluid(fluid.getFlowingFluid(), ChunkSectionLayer.TRANSLUCENT);
		// }

		registerBlockEntityRenderer(TRBlockEntities.INDUSTRIAL_GRINDER, MultiblockRenderer::new);
		registerBlockEntityRenderer(TRBlockEntities.FUSION_CONTROL_COMPUTER, MultiblockRenderer::new);
		registerBlockEntityRenderer(TRBlockEntities.INDUSTRIAL_BLAST_FURNACE, MultiblockRenderer::new);
		registerBlockEntityRenderer(TRBlockEntities.VACUUM_FREEZER, MultiblockRenderer::new);
		registerBlockEntityRenderer(TRBlockEntities.FLUID_REPLICATOR, MultiblockRenderer::new);
		registerBlockEntityRenderer(TRBlockEntities.INDUSTRIAL_SAWMILL, MultiblockRenderer::new);
		registerBlockEntityRenderer(TRBlockEntities.DISTILLATION_TOWER, MultiblockRenderer::new);
		registerBlockEntityRenderer(TRBlockEntities.IMPLOSION_COMPRESSOR, MultiblockRenderer::new);
		registerBlockEntityRenderer(TRBlockEntities.GREENHOUSE_CONTROLLER, MultiblockRenderer::new);
		registerBlockEntityRenderer(TRBlockEntities.NUCLEAR_REACTOR, MultiblockRenderer::new);
		registerBlockEntityRenderer(TRBlockEntities.STORAGE_UNIT, StorageUnitRenderer::new);
		registerBlockEntityRenderer(TRBlockEntities.CABLE, CableCoverRenderer::new);
		registerBlockEntityRenderer(TRBlockEntities.WIND_MILL, TurbineRenderer::new);

		EntityRendererProvider<EntityNukePrimed> nukeRenderer = NukeRenderer::new;
		Registry.register(BuiltInClientRegistries.ENTITY_RENDERER, BuiltInRegistries.ENTITY_TYPE.getKey(TRContent.ENTITY_NUKE), nukeRenderer);

		ClientGuiType.AESU.toString();

		ClientJumpEvent.EVENT.register(new ClientJumpHandler());

		// TODO 26.1: MachineFaceElementRenderer.BLACKLIST — check if MachineFaceElementRenderer still exists in RebornCore
		// MachineFaceElementRenderer.BLACKLIST.add(Identifier.fromNamespaceAndPath("techreborn", "block/machines/tier2_machines/fishing_station_net"));
		// MachineFaceElementRenderer.BLACKLIST.add(Identifier.fromNamespaceAndPath("techreborn", "block/machines/tier2_machines/fishing_station_net_side"));
	}

	private static <T extends BlockEntity, S extends BlockEntityRenderState> void registerBlockEntityRenderer(
		BlockEntityType<T> type, BlockEntityRendererProvider<T, S> renderer
	) {
		Registry.register(BuiltInClientRegistries.BLOCK_ENTITY_RENDERER, BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(type), renderer);
	}

	//Need the item instance in a few places, this makes it easier
	private interface ItemModelPredicateProvider<T extends Item> {

		float call(T item, ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity entity, int seed);

		default float unclampedCall(ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity entity, int seed) {
			//noinspection unchecked
			return call((T) stack.getItem(), stack, world, entity, seed);
		}

	}
}
