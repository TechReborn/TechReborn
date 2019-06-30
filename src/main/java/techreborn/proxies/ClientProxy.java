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

import net.minecraft.util.Formatting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import reborncore.api.tile.IUpgradeable;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.hud.StackInfoHUD;
import techreborn.init.TRContent;
import techreborn.items.DynamicCell;
import techreborn.items.ItemFrequencyTransmitter;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit() {
		super.preInit();
		StackInfoHUD.registerElement(new ItemFrequencyTransmitter.StackInfoFreqTransmitter());
//		RenderingRegistry.registerEntityRenderingHandler(EntityNukePrimed.class, new RenderManagerNuke());
//		MinecraftForge.EVENT_BUS.register(new IconSupplier());
//		MinecraftForge.EVENT_BUS.register(ClientEventHandler.class);
	}

	@Override
	public void init() {
		super.init();
//		MinecraftForge.EVENT_BUS.register(new StackToolTipEvent());
//		MinecraftForge.EVENT_BUS.register(GuiSlotConfiguration.class);
//		MinecraftForge.EVENT_BUS.register(GuiFluidConfiguration.class);
		// TODO FIX ME
		//ClientRegistry.registerKeyBinding(KeyBindings.config);
//		StateMap rubberLeavesStateMap = new StateMap.Builder().ignore(BlockRubberLeaves.CHECK_DECAY, BlockRubberLeaves.DECAYABLE).build();
//		ModelLoader.setCustomStateMapper(TRContent.RUBBER_LEAVES, rubberLeavesStateMap);
		GuiBase.wrenchStack = new ItemStack(TRContent.WRENCH);
		GuiBase.fluidCellProvider = DynamicCell::getCellWithFluid;
	}


	@Override
	public String getUpgradeConfigText() {
		if (MinecraftClient.getInstance().currentScreen instanceof GuiBase) {
			GuiBase base = (GuiBase) MinecraftClient.getInstance().currentScreen;
			if (base.tile instanceof IUpgradeable) {
				if (((IUpgradeable) base.tile).canBeUpgraded()) {
					return Formatting.LIGHT_PURPLE + "Right click to configure";
				}
			}
		}
		return super.getUpgradeConfigText();
	}

//	public class RenderManagerNuke implements IRenderFactory<EntityNukePrimed> {
//
//		@Override
//		public EntityRenderer<? super EntityNukePrimed> createRenderFor(EntityRenderDispatcher manager) {
//			return new RenderNukePrimed(manager);
//		}
//	}

	@Override
	public boolean fancyGraphics() {
		return MinecraftClient.getInstance().options.fancyGraphics;
	}

}
