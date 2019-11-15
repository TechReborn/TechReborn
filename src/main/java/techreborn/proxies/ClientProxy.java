/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.proxies;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.util.RecipeBookClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import prospector.shootingstar.ShootingStar;
import reborncore.api.tile.IUpgradeable;
import reborncore.client.hud.StackInfoHUD;
import techreborn.blocks.BlockRubberLeaves;
import techreborn.client.ClientEventHandler;
import techreborn.client.RegisterItemJsons;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.builder.slot.GuiFluidConfiguration;
import reborncore.client.gui.builder.slot.GuiSlotConfiguration;
import techreborn.client.keybindings.KeyBindings;
import techreborn.client.render.ModelDynamicCell;
import techreborn.client.render.entitys.RenderNukePrimed;
import techreborn.entities.EntityNukePrimed;
import techreborn.events.FluidBlockModelHandler;
import techreborn.events.StackToolTipEvent;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.items.ItemDynamicCell;
import techreborn.items.ItemFrequencyTransmitter;
import techreborn.lib.ModInfo;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		ShootingStar.registerModels(ModInfo.MOD_ID);
		StackInfoHUD.registerElement(new ItemFrequencyTransmitter.StackInfoFreqTransmitter());
		RenderingRegistry.registerEntityRenderingHandler(EntityNukePrimed.class, new RenderManagerNuke());

		MinecraftForge.EVENT_BUS.register(EventHandlerClient.INSTANCE);

		MinecraftForge.EVENT_BUS.register(new FluidBlockModelHandler());
//		MinecraftForge.EVENT_BUS.register(ClientEventHandler.class);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		MinecraftForge.EVENT_BUS.register(new StackToolTipEvent());
		MinecraftForge.EVENT_BUS.register(GuiSlotConfiguration.class);
		MinecraftForge.EVENT_BUS.register(GuiFluidConfiguration.class);
		// TODO FIX ME
		ClientRegistry.registerKeyBinding(KeyBindings.config);
		StateMap rubberLeavesStateMap = new StateMap.Builder().ignore(BlockRubberLeaves.CHECK_DECAY, BlockRubberLeaves.DECAYABLE).build();
		ModelLoader.setCustomStateMapper(ModBlocks.RUBBER_LEAVES, rubberLeavesStateMap);
		GuiBase.wrenchStack = new ItemStack(ModItems.WRENCH);
		GuiBase.fluidCellProvider = ItemDynamicCell::getCellWithFluid;
	}

	@Override
	public boolean isCTMAvailable() {
		return isChiselAround;
	}

	@Override
	public String getUpgradeConfigText() {
		if (Minecraft.getMinecraft().currentScreen instanceof GuiBase) {
			GuiBase base = (GuiBase) Minecraft.getMinecraft().currentScreen;
			if (base.tile instanceof IUpgradeable) {
				if (((IUpgradeable) base.tile).canBeUpgraded()) {
					return TextFormatting.LIGHT_PURPLE + "Right click to configure";
				}
			}
		}
		return super.getUpgradeConfigText();
	}

	public class RenderManagerNuke implements IRenderFactory<EntityNukePrimed> {

		@Override
		public Render<? super EntityNukePrimed> createRenderFor(RenderManager manager) {
			return new RenderNukePrimed(manager);
		}
	}

	@Override
	public boolean fancyGraphics() {
		return Minecraft.getMinecraft().gameSettings.fancyGraphics;
	}

	@Override
	public void rebuildRecipeBook() {
		super.rebuildRecipeBook();
		RecipeBookClient.rebuildTable();
	}
}
