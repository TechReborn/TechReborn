package techreborn.tiles.generator;

import net.minecraft.util.math.BlockPos;
import reborncore.common.IWrenchable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.tile.TilePowerAcceptor;
import reborncore.common.tile.TilePowerProducer;
import reborncore.common.util.Inventory;
import techreborn.init.ModBlocks;
import techreborn.power.EnergyUtils;

public class TileGenerator extends TilePowerProducer implements IWrenchable,IInventoryProvider {
	public static int outputAmount = 10; // This is in line with BC engines rf,
	public Inventory inventory = new Inventory(2, "TileGenerator", 64, this);
	public int fuelSlot = 0;
	public int burnTime;
	public int totalBurnTime = 0;
	// sould properly use the conversion
	// ratio here.
	public boolean isBurning;
	public boolean lastTickBurning;
	ItemStack burnItem;

	public static int getItemBurnTime(ItemStack stack) {
		return TileEntityFurnace.getItemBurnTime(stack) / 4;
	}

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
		if (worldObj.isRemote) {
			return;
		}
		if (getEnergy() < getMaxPower()) {
			if (burnTime > 0) {
				burnTime--;
				addEnergy(outputAmount);
				isBurning = true;
			}
		} else {
			isBurning = false;
		}

		if (burnTime == 0) {
			updateState();
			burnTime = totalBurnTime = getItemBurnTime(getStackInSlot(fuelSlot));
			if (burnTime > 0) {
				updateState();
				burnItem = getStackInSlot(fuelSlot);
				if (getStackInSlot(fuelSlot).stackSize == 1) {
					setInventorySlotContents(fuelSlot, null);
				} else {
					decrStackSize(fuelSlot, 1);
				}
			}
		}

		lastTickBurning = isBurning;

	}

	public void updateState() {
		IBlockState BlockStateContainer = worldObj.getBlockState(pos);
		if (BlockStateContainer.getBlock() instanceof BlockMachineBase) {
			BlockMachineBase blockMachineBase = (BlockMachineBase) BlockStateContainer.getBlock();
			if (BlockStateContainer.getValue(BlockMachineBase.ACTIVE) != burnTime > 0)
				blockMachineBase.setActive(burnTime > 0, worldObj, pos);
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
	public double getMaxPower() {
		return 8000;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.LOW;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer p0) {
		return new ItemStack(ModBlocks.Generator);
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}
}
