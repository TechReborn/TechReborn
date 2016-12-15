package techreborn.tiles.generator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.IWrenchable;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;

public class TileDragonEggSiphoner extends TilePowerAcceptor implements IWrenchable, IInventoryProvider {

	public static final int euTick = ConfigTechReborn.DragonEggSiphonerOutput;
	public Inventory inventory = new Inventory(3, "TileAlloySmelter", 64, this);

	public TileDragonEggSiphoner() {
		super(2);
	}

	private long lastOutput = 0;

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (!world.isRemote) {
			if (world.getBlockState(new BlockPos(getPos().getX(), getPos().getY() + 1, getPos().getZ()))
					.getBlock() == Blocks.DRAGON_EGG) {
				if(tryAddingEnergy(euTick))
					this.lastOutput = this.world.getTotalWorldTime();
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
		return new ItemStack(ModBlocks.dragonEggEnergySiphoner, 1);
	}

	public boolean isComplete() {
		return false;
	}

	@Override
	public double getMaxPower() {
		return 1000;
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
		return euTick;
	}

	@Override
	public double getMaxInput() {
		return 0;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.HIGH;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}
}
