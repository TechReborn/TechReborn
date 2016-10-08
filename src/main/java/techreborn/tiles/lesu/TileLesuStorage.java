package techreborn.tiles.lesu;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.common.tile.TileMachineBase;

public class TileLesuStorage extends TileMachineBase {

	public LesuNetwork network;

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (network == null) {
			findAndJoinNetwork(worldObj, getPos().getX(), getPos().getY(), getPos().getZ());
		} else {
			if (network.master != null
				&& network.master.getWorld().getTileEntity(new BlockPos(network.master.getPos().getX(),
				network.master.getPos().getY(), network.master.getPos().getZ())) != network.master) {
				network.master = null;
			}
		}
	}

	public final void findAndJoinNetwork(World world, int x, int y, int z) {
		network = new LesuNetwork();
		network.addElement(this);
		for (EnumFacing direction : EnumFacing.values()) {
			if (world.getTileEntity(new BlockPos(x + direction.getFrontOffsetX(), y + direction.getFrontOffsetY(),
				z + direction.getFrontOffsetZ())) instanceof TileLesuStorage) {
				TileLesuStorage lesu = (TileLesuStorage) world
					.getTileEntity(new BlockPos(x + direction.getFrontOffsetX(), y + direction.getFrontOffsetY(),
						z + direction.getFrontOffsetZ()));
				if (lesu.network != null) {
					lesu.network.merge(network);
				}
			}
		}
	}

	public final void setNetwork(LesuNetwork n) {
		if (n == null) {
		} else {
			network = n;
			network.addElement(this);
		}
	}

	public final void resetNetwork() {
		network = null;
	}

	public final void removeFromNetwork() {
		if (network == null) {
		} else
			network.removeElement(this);
	}

	public final void rebuildNetwork() {
		this.removeFromNetwork();
		this.resetNetwork();
		this.findAndJoinNetwork(worldObj, getPos().getX(), getPos().getY(), getPos().getZ());
	}
}
