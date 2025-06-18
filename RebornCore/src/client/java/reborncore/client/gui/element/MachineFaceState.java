/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2025 TeamReborn
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

package reborncore.client.gui.element;

import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.special.SpecialGuiElementRenderState;
import net.minecraft.client.render.model.BlockStateModel;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

public record MachineFaceState(
	Matrix3x2f pose, BlockStateModel model, int x1, int y1, int x2, int y2, float scale, @Nullable ScreenRect scissorArea, ScreenRect bounds
) implements SpecialGuiElementRenderState {
	public static final int TEXTURE_HEIGHT = 16;
	public static final int TEXTURE_WIDTH = 6 * TEXTURE_HEIGHT;
	public static final int TEXTURE_SIZE = 62;

	public MachineFaceState(Matrix3x2f pose, BlockStateModel model, int x, int y) {
		this(
			pose,
			model,
			x,
			y,
			x + TEXTURE_WIDTH,
			y + TEXTURE_HEIGHT,
			1,
			null,
			new ScreenRect(x, y, TEXTURE_SIZE, TEXTURE_SIZE).transformEachVertex(pose)
		);
	}
}
