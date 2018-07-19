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

package techreborn.compat.compat.opencomputers;

import li.cil.oc.api.Network;
import li.cil.oc.api.driver.DriverBlock;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.api.tile.IUpgradeable;
import reborncore.common.powerSystem.TilePowerAcceptor;

import java.util.Arrays;
import java.util.function.IntFunction;

public class DriverMachine implements DriverBlock {
	@Override
	public boolean worksWith(World world, BlockPos blockPos, EnumFacing enumFacing) {
		return world.getTileEntity(blockPos) instanceof TilePowerAcceptor;
	}

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos blockPos, EnumFacing enumFacing) {
		return new MachineEnvironment((TilePowerAcceptor) world.getTileEntity(blockPos));
	}

	public static class MachineEnvironment extends AbstractManagedEnvironment implements NamedBlock {
		TilePowerAcceptor machine;

		public MachineEnvironment(TilePowerAcceptor machine) {
			this.machine = machine;
			setNode(Network.newNode(this, Visibility.Network).withComponent("tr_machine", Visibility.Network).create());
		}

		@Callback(value = "getTeir", getter = true, doc = "Gets the current teir of the machine, this changes with upgrades")
		public Object[] getTeir(Context context, Arguments arguments) throws Exception {
			return getObjects(machine.getTier().name());
		}

		@Callback(value = "getEnergy", getter = true, doc = "Gets the currentally stored energy of the machine. in EU")
		public Object[] getEnergy(Context context, Arguments arguments) throws Exception {
			return getObjects(machine.getEnergy());
		}


		@Callback(value = "getMaxPower", getter = true, doc = "Gets the maximum energy that can be stored in the machine. in EU")
		public Object[] getMaxPower(Context context, Arguments arguments) throws Exception {
			return getObjects(machine.getMaxPower());
		}

		@Callback(value = "getMaxInput", getter = true, doc = "Gets the maximum energy that can be inputted in to the machine. in EU")
		public Object[] getMaxInput(Context context, Arguments arguments) throws Exception {
			return getObjects(machine.getMaxInput());
		}

		@Callback(value = "getMaxOutput", getter = true, doc = "Gets the maximum energy that can be extracted from the machine. in EU")
		public Object[] getMaxOutput(Context context, Arguments arguments) throws Exception {
			return getObjects(machine.getMaxOutput());
		}

		@Callback(value = "canBeUprgraded", getter = true, doc = "Returns true if the machine can be upgraded")
		public Object[] canBeUprgraded(Context context, Arguments arguments) throws Exception {
			return getObjects(machine instanceof IUpgradeable && machine.canBeUpgraded());
		}

		@Callback(value = "hasRecipeCrafter", getter = true, doc = "Returns true if the machine has a recipe crafter")
		public Object[] hasRecipeCrafter(Context context, Arguments arguments) throws Exception{
			return getObjects(machine instanceof IRecipeCrafterProvider);
		}

		@Callback(value = "getRecipeName", getter = true, doc = "Gets the name of the recipe that the machine crafts")
		public Object[] getRecipeName(Context context, Arguments arguments) throws Exception{
			if(!(machine instanceof IRecipeCrafterProvider)){
				return null;
			}
			return getObjects(((IRecipeCrafterProvider) machine).getRecipeCrafter().recipeName);
		}

		@Callback(value = "getInputSlots", getter = true, doc = "Gets the slot ids that the crafter uses to look for inputs")
		public Object[] getInputSlots(Context context, Arguments arguments) throws Exception{
			if(!(machine instanceof IRecipeCrafterProvider)){
				return null;
			}
			return Arrays.stream(((IRecipeCrafterProvider) machine).getRecipeCrafter().inputSlots).mapToObj((IntFunction<Object>) value -> value).toArray();
		}

		@Callback(value = "getOutputSlots", getter = true, doc = "Gets the slot ids that the crafter uses for outputs")
		public Object[] getOutputSlots(Context context, Arguments arguments) throws Exception{
			if(!(machine instanceof IRecipeCrafterProvider)){
				return null;
			}
			return Arrays.stream(((IRecipeCrafterProvider) machine).getRecipeCrafter().outputSlots).mapToObj((IntFunction<Object>) value -> value).toArray();
		}

		@Callback(value = "isActive", getter = true, doc = "Returns a boolean if the machine is actice")
		public Object[] isActive(Context context, Arguments arguments) throws Exception{
			return getObjects(machine.isActive());
		}

		@Callback(value = "getFacing", getter = true, doc = "Gets the facing for the machine")
		public Object[] getFacing(Context context, Arguments arguments) throws Exception{
			EnumFacing facing = machine.getFacing();
			if(facing == null){
				return null;
			}
			return getObjects(facing.getName());
		}

		private Object[] getObjects(Object... objects){
			return objects;
		}

		@Override
		public String preferredName() {
			return "tr_machine";
		}

		@Override
		public int priority() {
			return 10;
		}
	}
}
