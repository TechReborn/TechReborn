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

package reborncore.client;

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderingRegistry;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import reborncore.common.fluid.FluidSettings;
import reborncore.common.fluid.RebornFluid;
import reborncore.common.fluid.RebornFluidManager;

public class RebornFluidRenderManager {
	public static void setupClient() {
		RebornFluidManager.getFluidStream().forEach(RebornFluidRenderManager::setupFluidRenderer);
	}

	private static void setupFluidRenderer(RebornFluid fluid) {
		FluidSettings fluidSettings = fluid.getFluidSettings();
		FluidModel.Unbaked model = new FluidModel.Unbaked(
			new Material(fluidSettings.getStillTexture(), false),
			new Material(fluidSettings.getFlowingTexture(), false),
			null,
			null
		);
		FluidRenderingRegistry.register(fluid, model);
	}
}
