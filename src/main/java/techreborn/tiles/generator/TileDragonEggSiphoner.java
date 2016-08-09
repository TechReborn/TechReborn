package techreborn.tiles.generator;

import reborncore.common.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.tile.TilePowerProducer;
import reborncore.common.util.Inventory;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.power.EnergyUtils;

public class TileDragonEggSiphoner extends TilePowerProducer implements IWrenchable,IInventoryProvider {

	public static final int euTick = ConfigTechReborn.DragonEggSiphonerOutput;
	public Inventory inventory = new Inventory(3, "TileAlloySmelter", 64, this);

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
			if (worldObj.getBlockState(new BlockPos(getPos().getX(), getPos().getY() + 1, getPos().getZ()))
					.getBlock() == Blocks.DRAGON_EGG) {
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
		return new ItemStack(ModBlocks.Dragoneggenergysiphoner, 1);
	}

	public boolean isComplete() {
		return false;
	}

	@Override
	public double getMaxPower() {
		return 256000;
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
