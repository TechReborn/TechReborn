/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2025 TechReborn
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

package techreborn.client.render;

// TODO 26.1: MachineCasingModel disabled — depends on BlockStateResolver / ModelLoadingPlugin
// which are not available in the current Fabric API for 26.1.
// The machine casing block states need to be handled via JSON blockstate files instead.
//
// Original implementation used:
//   - net.fabricmc.fabric.api.client.model.loading.v1.BlockStateResolver
//   - net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
//   - BlockStateModel / BlockStateModel.UnbakedRoot
//   - TextureSlots / TextureSlots.Data / TextureSlots.Resolver
//   - SimpleModelWrapper, Material, BlockModelRotation, QuadCollection, ResolvedModel
//
// To restore: implement using the available Fabric API model loading hooks once they are
// available for 26.1, or switch to blockstate JSON files per direction variant.

public class MachineCasingModel {
	// Stub — no usable implementation until model loading API is available
}
