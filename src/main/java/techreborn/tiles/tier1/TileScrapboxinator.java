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

package techreborn.tiles.tier1;

import net.minecraft.entity.player.EntityPlayer;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.Inventory;
import techreborn.api.recipe.ScrapboxRecipeCrafter;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.lib.ModInfo;
import techreborn.tiles.TileGenericMachine;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileScrapboxinator extends TileGenericMachine implements IContainerProvider {

	@ConfigRegistry(config = "machines", category = "scrapboxinator", key = "ScrapboxinatorMaxInput", comment = "Scrapboxinator Max Input (Value in EU)")
	public static int maxInput = 32;
	@ConfigRegistry(config = "machines", category = "scrapboxinator", key = "ScrapboxinatorMaxEnergy", comment = "Scrapboxinator Max Energy (Value in EU)")
	public static int maxEnergy = 1_000;

	public TileScrapboxinator() {
		super("Scrapboxinator", maxInput, maxEnergy, ModBlocks.SCRAPBOXINATOR, 2);
		final int[] inputs = new int[] { 0 };
		final int[] outputs = new int[] { 1 };
		this.inventory = new Inventory(3, "TileScrapboxinator", 64, this);
		this.crafter = new ScrapboxRecipeCrafter(this, this.inventory, inputs, outputs);
	}
	
	// RebornMachineTile
	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	// IContainerProvider
	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("scrapboxinator").player(player.inventory).inventory().hotbar().addInventory()
				.tile(this).filterSlot(0, 55, 45, stack -> stack.getItem() == ModItems.SCRAP_BOX).outputSlot(1, 101, 45)
				.energySlot(2, 8, 72).syncEnergyValue().syncCrafterValue().addInventory().create(this);
	}
}