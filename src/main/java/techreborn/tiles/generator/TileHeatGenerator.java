package techreborn.tiles.generator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.IWrenchable;
import reborncore.common.tile.TilePowerProducer;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.power.EnergyUtils;

public class TileHeatGenerator extends TilePowerProducer implements IWrenchable {

	public static final int euTick = ConfigTechReborn.HeatGeneratorOutput;

	@Override
	public double emitEnergy(EnumFacing enumFacing, double amount) {
		BlockPos pos = getPos().offset(enumFacing);
		EnergyUtils.PowerNetReceiver receiver = EnergyUtils.getReceiver(
				worldObj, enumFacing.getOpposite(), pos);
		if(receiver != null) {
			addEnergy(amount - receiver.receiveEnergy(amount, false));
		} else addEnergy(amount);
		return 0; //Temporary hack die to my bug RebornCore
	}

	@Override
	public void update() {
		super.update();

		if (!worldObj.isRemote) {
			if (worldObj.getBlockState(new BlockPos(getPos().getX() + 1, getPos().getY(), getPos().getZ()))
					.getBlock() == Blocks.LAVA) {
				addEnergy(euTick);
			} else if (worldObj.getBlockState(new BlockPos(getPos().getX(), getPos().getY(), getPos().getZ() + 1))
					.getBlock() == Blocks.LAVA) {
				addEnergy(euTick);
			} else if (worldObj.getBlockState(new BlockPos(getPos().getX(), getPos().getY(), getPos().getZ() - 1))
					.getBlock() == Blocks.LAVA) {
				addEnergy(euTick);
			} else if (worldObj.getBlockState(new BlockPos(getPos().getX() - 1, getPos().getY(), getPos().getZ()))
					.getBlock() == Blocks.LAVA) {
				addEnergy(euTick);
			} else if (worldObj.getBlockState(new BlockPos(getPos().getX(), getPos().getY() - 1, getPos().getZ()))
					.getBlock() == Blocks.LAVA) {
				addEnergy(euTick);
			}

		}

	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, EnumFacing side) {
		return false;
	}

	@Override
	public EnumFacing getFacing() {
		return getFacingEnum();
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
		return entityPlayer.isSneaking();
	}

	@Override
	public float getWrenchDropRate() {
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.heatGenerator, 1);
	}

	public boolean isComplete() {
		return false;
	}

	@Override
	public double getMaxPower() {
		return 16000;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.LOW;
	}

}
