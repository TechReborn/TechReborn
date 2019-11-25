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

package techreborn.blocks.generator;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import reborncore.api.tile.IMachineGuiHandler;
import reborncore.common.blocks.RebornMachineBlock;

import techreborn.client.EGui;
import techreborn.lib.ModInfo;
import techreborn.tiles.generator.fluid.lv.TileThermalGenerator;
import techreborn.utils.TechRebornCreativeTab;

import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;

/**
 * @author estebes
 */
public class BlockThermalGenerator extends RebornMachineBlock {
    public BlockThermalGenerator() {
        super();

        setCreativeTab(TechRebornCreativeTab.instance);
        ShootingStar.registerModel(new ModelCompound(ModInfo.MOD_ID, this, "machines/generators"));
    }

    @Override
    public TileEntity createNewTileEntity(final World world, final int meta) {
        return new TileThermalGenerator();
    }

    @Override
    public IMachineGuiHandler getGui() {
        return EGui.THERMAL_GENERATOR;
    }
}
