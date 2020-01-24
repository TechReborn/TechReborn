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

package techreborn.tiles.processing;

import net.minecraft.entity.player.EntityPlayer;

import reborncore.api.praescriptum.Utils.IngredientUtils;
import reborncore.api.scriba.RegisterTile;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;

import techreborn.api.recipe.Recipes;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;

/**
 * @author estebes
 */
@RebornRegistry(modID = ModInfo.MOD_ID)
@RegisterTile(name = "compressor")
public class TileCompressor extends TileMachine {
    // Config >>
    @ConfigRegistry(config = "machines", category = "compressor", key = "CompressorInput", comment = "Compressor Max Input (Value in EU)")
    public static int maxInput = 32;
    @ConfigRegistry(config = "machines", category = "compressor", key = "CompressorMaxEnergy", comment = "Compressor Max Energy (Value in EU)")
    public static int maxEnergy = 10_000;
    // << Config

    public TileCompressor() {
        super("Compressor", maxInput, maxEnergy, 2, 3, Recipes.compressor, ModBlocks.COMPRESSOR);
    }

    // IContainerProvider >>
    @Override
    public BuiltContainer createContainer(final EntityPlayer player) {
        return new ContainerBuilder("compressor")
                .player(player.inventory)
                .inventory()
                .hotbar()
                .addInventory()
                .tile(this)
                .filterSlot(0, 55, 45, IngredientUtils.isPartOfRecipe(recipeHandler))
                .outputSlot(1, 101, 45)
                .energySlot(energySlot, 8, 72)
                .syncEnergyValue()
                .syncIntegerValue(this::getProgress, this::setProgress)
                .syncIntegerValue(this::getOperationLength, this::setOperationLength)
                .addInventory()
                .create(this);
    }
    // << IContainerProvider
}
