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

package techreborn.init;

import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;

public class TRToolTier {
	public static final ToolMaterial BRONZE = new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, 375, 7.0F, 6, 6, TRContent.ItemTags.BRONZE_TOOL_MATERIALS);
	public static final ToolMaterial RUBY = new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, 750, 6.0F, 5, 10, TRContent.ItemTags.RUBY_TOOL_MATERIALS);
	public static final ToolMaterial SAPPHIRE = new ToolMaterial(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 1000, 7.0F, 5, 12, TRContent.ItemTags.SAPPHIRE_TOOL_MATERIALS);
	public static final ToolMaterial PERIDOT = new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, 750, 7.0F, 5, 12, TRContent.ItemTags.PERIDOT_TOOL_MATERIALS);

	private TRToolTier() {
		// No instantiation
	}
}
