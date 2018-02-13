package techreborn.compat.opencomputers;

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
import reborncore.api.tile.IUpgradeable;
import reborncore.common.powerSystem.TilePowerAcceptor;

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

		@Callback(value = "canBeUprgaded", getter = true, doc = "Returns true if the machine can be upgraded")
		public Object[] canBeUprgaded(Context context, Arguments arguments) throws Exception {
			return getObjects(machine instanceof IUpgradeable && machine.canBeUpgraded());
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
			return Integer.MAX_VALUE;
		}
	}
}
