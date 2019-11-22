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

package techreborn.tiles.generator.fluid.lv;

import net.minecraft.entity.player.EntityPlayer;

import reborncore.api.scriba.RegisterTile;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;

import techreborn.api.recipe.Fuels;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;
import techreborn.tiles.generator.fluid.TileFluidGenerator;

/**
 * @author estebes
 */
@RebornRegistry(modID = ModInfo.MOD_ID)
@RegisterTile(name = "gas_turbine")
public class TileGasTurbine extends TileFluidGenerator {
    // Config >>
    @ConfigRegistry(config = "generators", category = "gas_turbine", key = "GasTurbineMaxOutput", comment = "Gas Turbine Max Output (Value in EU)")
    public static int maxOutput = 32;
    @ConfigRegistry(config = "generators", category = "gas_turbine", key = "GasTurbineMaxEnergy", comment = "Gas Turbine Max Energy (Value in EU)")
    public static int maxEnergy = 10_000;
    @ConfigRegistry(config = "generators", category = "gas_turbine", key = "GasTurbineTankCapacity", comment = "Gas Turbine Tank Capacity")
    public static int tankCapacity = 10_000;
    // << Config

    public TileGasTurbine() {
        super("GasTurbine", maxOutput, maxEnergy, tankCapacity, Fuels.gasTurbine, ModBlocks.GAS_TURBINE);
    }

    // IContainerProvider >>
    @Override
    public BuiltContainer createContainer(final EntityPlayer player) {
        return new ContainerBuilder("gas_turbine")
                .player(player.inventory)
                .inventory()
                .hotbar()
                .addInventory()
                .tile(this)
                .fluidSlot(0, 25, 35)
                .outputSlot(1, 25, 55)
                .syncEnergyValue()
                .syncIntegerValue(this::getRemainingEnergy, this::setRemainingEnergy)
                .syncIntegerValue(this::getTotalEnergy, this::setTotalEnergy)
                .syncTank(this.tank::getFluid, this.tank::setFluid)
                .addInventory()
                .create(this);
    }
    // << IContainerProvider
}
