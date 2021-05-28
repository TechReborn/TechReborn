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

package reborncore.common.fluid;

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.fluid.Fluid;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import reborncore.client.RenderUtil;
import reborncore.common.util.TemporaryLazy;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class RebornFluidRenderManager implements ClientSpriteRegistryCallback, SimpleSynchronousResourceReloadListener {

	private static final Map<Fluid, TemporaryLazy<Sprite[]>> spriteMap = new HashMap<>();

	public static void setupClient() {
		RebornFluidRenderManager rebornFluidRenderManager = new RebornFluidRenderManager();
		ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register(rebornFluidRenderManager);
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(rebornFluidRenderManager);
		RebornFluidManager.getFluidStream().forEach(RebornFluidRenderManager::setupFluidRenderer);
	}

	private static void setupFluidRenderer(RebornFluid fluid) {
		//Done lazy as we want to ensure we get the sprite at the correct time, but also dont want to be making these calls every time its required.
		TemporaryLazy<Sprite[]> sprites = new TemporaryLazy<>(() -> {
			FluidSettings fluidSettings = fluid.getFluidSettings();
			return new Sprite[]{RenderUtil.getSprite(fluidSettings.getStillTexture()), RenderUtil.getSprite(fluidSettings.getFlowingTexture())};
		});

		spriteMap.put(fluid, sprites);
		FluidRenderHandlerRegistry.INSTANCE.register(fluid, (extendedBlockView, blockPos, fluidState) -> sprites.get());
	}

	@Override
	public void registerSprites(SpriteAtlasTexture spriteAtlasTexture, Registry registry) {
		Stream.concat(
				RebornFluidManager.getFluidStream().map(rebornFluid -> rebornFluid.getFluidSettings().getFlowingTexture()),
				RebornFluidManager.getFluidStream().map(rebornFluid -> rebornFluid.getFluidSettings().getStillTexture())
		).forEach(registry::register);
	}

	@Override
	public Identifier getFabricId() {
		return new Identifier("reborncore", "fluid_render_manager");
	}

	@Override
	public void apply(ResourceManager manager) {
		//Reset the cached fluid sprites
		spriteMap.forEach((key, value) -> value.reset());
	}

	@Override
	public Collection<Identifier> getFabricDependencies() {
		return Collections.singletonList(ResourceReloadListenerKeys.TEXTURES);
	}
}
