package techreborn.tiles.generator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.IWrenchable;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.TilePowerAcceptor;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;

public class TileHeatGenerator extends TilePowerAcceptor implements IWrenchable {

	public static final int euTick = ConfigTechReborn.HeatGeneratorOutput;

	public TileHeatGenerator() {
		super(1);
	}
	
	private long lastOutput = 0;

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (!world.isRemote) {
			for (final EnumFacing direction : EnumFacing.values()) {
				if(direction.equals(EnumFacing.UP))
					continue;
				if (this.world
						.getBlockState(new BlockPos(this.getPos().getX() + direction.getFrontOffsetX(),
								this.getPos().getY() + direction.getFrontOffsetY(),
								this.getPos().getZ() + direction.getFrontOffsetZ()))
						.getBlock() == Blocks.LAVA) {
					if(tryAddingEnergy(euTick))
						this.lastOutput = this.world.getTotalWorldTime();
				}
			}

			if (this.world.getTotalWorldTime() - this.lastOutput < 30 && !this.isActive())
				this.world.setBlockState(this.getPos(),
						this.world.getBlockState(this.getPos()).withProperty(BlockMachineBase.ACTIVE, true));
			else if (this.world.getTotalWorldTime() - this.lastOutput > 30 && this.isActive())
				this.world.setBlockState(this.getPos(),
						this.world.getBlockState(this.getPos()).withProperty(BlockMachineBase.ACTIVE, false));
		}
	}
	
	private boolean tryAddingEnergy(int amount)
	{
		if(this.getMaxPower() - this.getEnergy() >= amount)
		{
			addEnergy(amount);
			return true;
		}
		else if(this.getMaxPower() - this.getEnergy() > 0)
		{
			addEnergy(this.getMaxPower() - this.getEnergy());
			return true;
		}
		return false;
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
		return 10000;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing direction) {
		return false;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing direction) {
		return true;
	}

	@Override
	public double getMaxOutput() {
		return 64;
	}

	@Override
	public double getMaxInput() {
		return 0;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.LOW;
	}

	// @Override
	// public void addWailaInfo(List<String> info)
	// {
	// super.addWailaInfo(info);
	// info.add("Power Generarating " + euTick +" EU/t");
	//
	// }

}
