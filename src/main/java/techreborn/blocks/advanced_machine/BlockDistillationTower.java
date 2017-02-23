/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

package techreborn.blocks.advanced_machine;

import net.minecraft.block.material.Material;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IRotationTexture;
import techreborn.client.TechRebornCreativeTab;

public class BlockDistillationTower extends BlockMachineBase implements IRotationTexture {

	private final String prefix = "techreborn:blocks/machine/advanced_machines/";

	public BlockDistillationTower(Material material) {
		super();
		setUnlocalizedName("techreborn.distillationtower");
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public String getFrontOff() {
		return prefix + "distillation_tower_front_off";
	}

	@Override
	public String getFrontOn() {
		return prefix + "distillation_tower_front_off";
	}

	@Override
	public String getSide() {
		return prefix + "advanced_machine_side";
	}

	@Override
	public String getTop() {
		return prefix + "industrial_centrifuge_top_off";
	}

	@Override
	public String getBottom() {
		return prefix + "industrial_centrifuge_bottom";
	}
}
