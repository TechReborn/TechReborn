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
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemModels;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
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
import techreborn.client.render.MachineCasingModel;
import techreborn.client.render.entitys.CableCoverRenderer;
import techreborn.client.render.entitys.NukeRenderer;
import techreborn.client.render.entitys.StorageUnitRenderer;
import techreborn.client.render.entitys.TurbineRenderer;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;

public class TechRebornClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		 ModelLoadingPlugin.register((pluginContext) -> {
		 	for (TRContent.MachineBlocks block : TRContent.MachineBlocks.values()) {
		 		pluginContext.registerBlockStateResolver(block.casing, MachineCasingModel::resolveBlockStates);
		 	}
		 });

		ItemModels.ID_MAPPER.put(ItemCellModel.ID, ItemCellModel.Unbaked.CODEC);
		ItemModels.ID_MAPPER.put(ItemBucketModel.ID, ItemBucketModel.Unbaked.CODEC);
		SelectItemModelProperties.ID_MAPPER.put(ActiveProperty.ID, ActiveProperty.TYPE);

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

		BlockEntityRenderers.register(TRBlockEntities.INDUSTRIAL_GRINDER, MultiblockRenderer::new);
		BlockEntityRenderers.register(TRBlockEntities.FUSION_CONTROL_COMPUTER, MultiblockRenderer::new);
		BlockEntityRenderers.register(TRBlockEntities.INDUSTRIAL_BLAST_FURNACE, MultiblockRenderer::new);
		BlockEntityRenderers.register(TRBlockEntities.VACUUM_FREEZER, MultiblockRenderer::new);
		BlockEntityRenderers.register(TRBlockEntities.FLUID_REPLICATOR, MultiblockRenderer::new);
		BlockEntityRenderers.register(TRBlockEntities.INDUSTRIAL_SAWMILL, MultiblockRenderer::new);
		BlockEntityRenderers.register(TRBlockEntities.DISTILLATION_TOWER, MultiblockRenderer::new);
		BlockEntityRenderers.register(TRBlockEntities.IMPLOSION_COMPRESSOR, MultiblockRenderer::new);
		BlockEntityRenderers.register(TRBlockEntities.GREENHOUSE_CONTROLLER, MultiblockRenderer::new);
		BlockEntityRenderers.register(TRBlockEntities.NUCLEAR_REACTOR, MultiblockRenderer::new);
		BlockEntityRenderers.register(TRBlockEntities.STORAGE_UNIT, StorageUnitRenderer::new);
		BlockEntityRenderers.register(TRBlockEntities.CABLE, CableCoverRenderer::new);
		BlockEntityRenderers.register(TRBlockEntities.WIND_MILL, TurbineRenderer::new);

		EntityRendererRegistry.register(TRContent.ENTITY_NUKE, NukeRenderer::new);

		ClientGuiType.AESU.toString();

		ClientJumpEvent.EVENT.register(new ClientJumpHandler());

		// TODO 26.1: MachineFaceElementRenderer.BLACKLIST — check if MachineFaceElementRenderer still exists in RebornCore
		// MachineFaceElementRenderer.BLACKLIST.add(Identifier.fromNamespaceAndPath("techreborn", "block/machines/tier2_machines/fishing_station_net"));
		// MachineFaceElementRenderer.BLACKLIST.add(Identifier.fromNamespaceAndPath("techreborn", "block/machines/tier2_machines/fishing_station_net_side"));
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

