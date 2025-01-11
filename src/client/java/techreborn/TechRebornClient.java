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
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.item.model.ItemModelTypes;
import net.minecraft.client.render.item.property.select.SelectProperties;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import reborncore.client.ClientJumpEvent;
import reborncore.client.gui.GuiBase;
import reborncore.client.multiblock.MultiblockRenderer;
import techreborn.client.ClientGuiType;
import techreborn.client.ClientboundPacketHandlers;
import techreborn.client.events.ClientJumpHandler;
import techreborn.client.events.StackToolTipHandler;
import techreborn.client.keybindings.KeyBindings;
import techreborn.client.render.*;
import techreborn.client.render.entitys.CableCoverRenderer;
import techreborn.client.render.entitys.NukeRenderer;
import techreborn.client.render.entitys.StorageUnitRenderer;
import techreborn.client.render.entitys.TurbineRenderer;
import techreborn.init.ModFluids;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;
import techreborn.items.DynamicCellItem;

import java.util.Arrays;

public class TechRebornClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ItemModelTypes.ID_MAPPER.put(ItemCellModel.ID, ItemCellModel.Unbaked.CODEC);
		ItemModelTypes.ID_MAPPER.put(ItemBucketModel.ID, ItemBucketModel.Unbaked.CODEC);
		SelectProperties.ID_MAPPER.put(ActiveProperty.ID, ActiveProperty.TYPE);

		KeyBindings.registerKeys();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (KeyBindings.suitNightVision.wasPressed()) {
				KeyBindings.handleSuitNVToggle();
			}
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (KeyBindings.quantumSuitSprint.wasPressed()) {
				KeyBindings.handleQuantumSuitSprintToggle();
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

		ClientGuiType.AESU.toString();

		ClientJumpEvent.EVENT.register(new ClientJumpHandler());
	}

	//Need the item instance in a few places, this makes it easier
	private interface ItemModelPredicateProvider<T extends Item> {

		float call(T item, ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed);

		default float unclampedCall(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed) {
			//noinspection unchecked
			return call((T) stack.getItem(), stack, world, entity, seed);
		}

	}
}
