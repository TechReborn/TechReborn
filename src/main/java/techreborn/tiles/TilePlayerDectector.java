package techreborn.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.WorldUtils;

import java.util.Iterator;

public class TilePlayerDectector extends TilePowerAcceptor {

	public String owenerUdid = "";
	boolean redstone = false;

	public TilePlayerDectector() {
		super(1);
	}

	@Override
	public double getMaxPower() {
		return 10000;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing direction) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing direction) {
		return false;
	}

	@Override
	public double getMaxOutput() {
		return 0;
	}

	@Override
	public double getMaxInput() {
		return 32;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.LOW;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (!world.isRemote && world.getWorldTime() % 20 == 0) {
			boolean lastRedstone = redstone;
			redstone = false;
			if (canUseEnergy(10)) {
				Iterator tIterator = super.world.playerEntities.iterator();
				while (tIterator.hasNext()) {
					EntityPlayer player = (EntityPlayer) tIterator.next();
					if (player.getDistanceSq((double) super.getPos().getX() + 0.5D,
						(double) super.getPos().getY() + 0.5D, (double) super.getPos().getZ() + 0.5D) <= 256.0D) {
						BlockMachineBase blockMachineBase = (BlockMachineBase) world.getBlockState(pos).getBlock();
						int meta = blockMachineBase.getMetaFromState(world.getBlockState(pos));
						if (meta == 0) {// ALL
							redstone = true;
						} else if (meta == 1) {// Others
							if (!owenerUdid.isEmpty() && !owenerUdid.equals(player.getUniqueID().toString())) {
								redstone = true;
							}
						} else {// You
							if (!owenerUdid.isEmpty() && owenerUdid.equals(player.getUniqueID().toString())) {
								redstone = true;
							}
						}
						redstone = true;
					}
				}
				useEnergy(10);
			}
			if (lastRedstone != redstone) {
				WorldUtils.updateBlock(world, getPos());
				world.notifyNeighborsOfStateChange(getPos(), world.getBlockState(getPos()).getBlock(), true);
			}
		}
	}

	public boolean isProvidingPower() {
		return redstone;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		owenerUdid = tag.getString("ownerID");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setString("ownerID", owenerUdid);
		return tag;
	}
}
