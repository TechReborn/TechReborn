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

package techreborn.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import reborncore.api.IToolDrop;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.Inventory;
import techreborn.api.Reference;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileAssemblingMachine extends TilePowerAcceptor
	implements IToolDrop, IInventoryProvider, IRecipeCrafterProvider, IContainerProvider {

	@ConfigRegistry(config = "machines", category = "assembling_machine", key = "AssemblingMachineMaxInput", comment = "Assembling Machine Max Input (Value in EU)")
	public static int maxInput = 128;
	@ConfigRegistry(config = "machines", category = "assembling_machine", key = "AssemblingMachineMaxEnergy", comment = "Assembling Machine Max Energy (Value in EU)")
	public static int maxEnergy = 10000;
	//  @ConfigRegistry(config = "machines", category = "assembling_machine", key = "AssemblingMachineWrenchDropRate", comment = "Assembling Machine Wrench Drop Rate")
	public static float wrenchDropRate = 1.0F;

	public int tickTime;
	public Inventory inventory = new Inventory(8, "TileAssemblingMachine", 64, this);
	public RecipeCrafter crafter;

	public TileAssemblingMachine() {
		super();
		// Input slots
		final int[] inputs = new int[2];
		inputs[0] = 0;
		inputs[1] = 1;
		final int[] outputs = new int[1];
		outputs[0] = 2;
		this.crafter = new RecipeCrafter(Reference.ASSEMBLING_MACHINE_RECIPE, this, 2, 2, this.inventory, inputs, outputs);
	}

	@Override
	public void update() {
		super.update();
		this.charge(3);
	}

	@Override
	public ItemStack getToolDrop(final EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.ASSEMBLY_MACHINE, 1);
	}

	public boolean isComplete() {
		return false;
	}

	//	 @Override
	//	 public void addWailaInfo(List<String> info)
	//	 {
	//	 super.addWailaInfo(info);
	//	 info.add("Power Stored " + energy.getEnergyStored() +" EU");
	//	 if(crafter.currentRecipe !=null){
	//	 info.add("Power Usage " + crafter.currentRecipe.euPerTick() + " EU/t");
	//	 }
	//	 }

	public int getProgressScaled(final int scale) {
		if (this.crafter.currentTickTime != 0) {
			return this.crafter.currentTickTime * scale / this.crafter.currentNeededTicks;
		}
		return 0;
	}

	@Override
	public double getBaseMaxPower() {
		return maxEnergy;
	}

	@Override
	public boolean canAcceptEnergy(final EnumFacing direction) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(final EnumFacing direction) {
		return false;
	}

	@Override
	public double getBaseMaxOutput() {
		return 0;
	}

	@Override
	public double getBaseMaxInput() {
		return maxInput;
	}

	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

	@Override
	public RecipeCrafter getRecipeCrafter() {
		return this.crafter;
	}


	@SuppressWarnings("deprecation")
	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("assemblingmachine").player(player.inventory).inventory(8, 84).hotbar(8, 142)
			.addInventory().tile(this).slot(0, 47, 17).slot(1, 65, 17).outputSlot(2, 116, 35).energySlot(3, 56, 53)
			.upgradeSlot(4, 152, 8).upgradeSlot(5, 152, 26).upgradeSlot(6, 152, 44).upgradeSlot(7, 152, 62)
			.syncEnergyValue().syncCrafterValue().addInventory().create(this);
	}
}
