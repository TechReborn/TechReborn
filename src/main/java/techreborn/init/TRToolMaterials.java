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

/**
 * We use custom materials to allow LevelZ to tweak the mining level of our tools.
 */
public class TRToolMaterials {
	public static final ToolMaterial BASIC_CHAINSAW = copy(ToolMaterial.IRON, "basic_chainsaw");
	public static final ToolMaterial BASIC_DRILL = copy(ToolMaterial.IRON, "basic_drill");
	public static final ToolMaterial BASIC_JACKHAMMER = copy(ToolMaterial.DIAMOND, "basic_jackhammer");

	public static final ToolMaterial ADVANCED_CHAINSAW = copy(ToolMaterial.DIAMOND, "advanced_chainsaw");
	public static final ToolMaterial ADVANCED_DRILL = copy(ToolMaterial.DIAMOND, "advanced_drill");
	public static final ToolMaterial ADVANCED_JACKHAMMER = copy(ToolMaterial.DIAMOND, "advanced_jackhammer");

	public static final ToolMaterial INDUSTRIAL_CHAINSAW = copy(ToolMaterial.NETHERITE, "industrial_chainsaw");
	public static final ToolMaterial INDUSTRIAL_DRILL = copy(ToolMaterial.NETHERITE, "industrial_drill");
	public static final ToolMaterial INDUSTRIAL_JACKHAMMER = copy(ToolMaterial.NETHERITE, "industrial_jackhammer");

	public static final ToolMaterial ROCK_CUTTER = copy(ToolMaterial.DIAMOND, "rock_cutter");
	public static final ToolMaterial NANOSABER = copy(ToolMaterial.NETHERITE, "nanosaber");
	public static final ToolMaterial OMNI_TOOL = copy(ToolMaterial.NETHERITE, "omni_tool");

	public static ToolMaterial copy(ToolMaterial material, String id) {
		return new ToolMaterial(material.incorrectBlocksForDrops(), material.durability(), material.speed(), material.attackDamageBonus(), material.enchantmentValue(), material.repairItems());
	}
}
