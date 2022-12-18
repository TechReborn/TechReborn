/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import reborncore.api.blockentity.UnloadHandler;
import reborncore.client.BlockOutlineRenderer;
import reborncore.client.ClientBoundPacketHandlers;
import reborncore.client.ItemStackRenderer;
import reborncore.client.RebornFluidRenderManager;
import reborncore.client.StackToolTipHandler;

import java.util.Locale;

public class RebornCoreClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		RebornFluidRenderManager.setupClient();
		ClientBoundPacketHandlers.init();
		HudRenderCallback.EVENT.register(new ItemStackRenderer());
		ItemTooltipCallback.EVENT.register(new StackToolTipHandler());
		WorldRenderEvents.BLOCK_OUTLINE.register(new BlockOutlineRenderer());

		/* register UnloadHandler */
		ClientBlockEntityEvents.BLOCK_ENTITY_UNLOAD.register((blockEntity, world) -> {
			if (blockEntity instanceof UnloadHandler) ((UnloadHandler) blockEntity).onUnload();
		});

		ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
			String strangeMcLang = client.getLanguageManager().getLanguage().getCode();
			RebornCore.locale = Locale.forLanguageTag(strangeMcLang.substring(0, 2));
		});
	}
}
