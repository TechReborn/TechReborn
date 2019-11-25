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

import reborncore.api.IListInfoProvider;
import reborncore.api.praescriptum.Utils.IngredientUtils;
import reborncore.api.scriba.RegisterTile;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.ItemUtils;

import techreborn.api.recipe.Recipes;
import techreborn.init.ModBlocks;
import techreborn.items.ItemDynamicCell;
import techreborn.lib.ModInfo;

import java.util.List;

/**
 * @author estebes
 */
@RebornRegistry(modID = ModInfo.MOD_ID)
@RegisterTile(name = "industrial_centrifuge")
public class TileIndustrialCentrifuge extends TileMachine implements IListInfoProvider {
    // Configure >>
    @ConfigRegistry(config = "machines", category = "centrifuge", key = "CentrifugeMaxInput", comment = "Centrifuge Max Input (Value in EU)")
    public static int maxInput = 32;
    @ConfigRegistry(config = "machines", category = "centrifuge", key = "CentrifugeMaxEnergy", comment = "Centrifuge Max Energy (Value in EU)")
    public static int maxEnergy = 10_000;
    // << Configure

    public TileIndustrialCentrifuge() {
        super("IndustrialCentrifuge", maxInput, maxEnergy, 6, 7, 64,
                new int[]{0, 1}, new int[]{2, 3, 4, 5}, Recipes.centrifuge, ModBlocks.INDUSTRIAL_CENTRIFUGE);
    }

    // IContainerProvider >>
    @Override
    public BuiltContainer createContainer(final EntityPlayer player) {
        return new ContainerBuilder("industrial_centrifuge").player(player.inventory).inventory().hotbar()
                .addInventory()
                .tile(this)
                .filterSlot(0, 34, 47, IngredientUtils.isPartOfRecipe(recipeHandler))
                .filterSlot(1, 126, 47, stack -> ItemUtils.isItemEqual(stack, ItemDynamicCell.getEmptyCell(1), true, true))
                .outputSlot(2, 82, 44)
                .outputSlot(3, 101, 25)
                .outputSlot(4, 120, 44)
                .outputSlot(5, 101, 63)
                .energySlot(energySlot, 8, 72)
                .syncEnergyValue()
                .syncIntegerValue(this::getProgress, this::setProgress)
                .syncIntegerValue(this::getOperationLength, this::setOperationLength)
                .addInventory()
                .create(this);
    }
    // << IContainerProvider

    // IListInfoProvider >>
    @Override
    public void addInfo(final List<String> info, final boolean isRealTile) {
        super.addInfo(info, isRealTile);
        info.add("Round and round it goes");
    }
    // << IListInfoProvider
}