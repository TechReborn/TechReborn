package techreborn.tiles.multiblock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.oredict.OreDictionary;
import reborncore.api.IListInfoProvider;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.IWrenchable;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.FluidUtils;
import reborncore.common.util.Inventory;
import reborncore.common.util.Tank;
import techreborn.init.ModBlocks;
import techreborn.init.ModFluids;
import techreborn.items.ItemDusts;

import java.util.Random;

import static techreborn.tiles.multiblock.MultiblockChecker.CASING_NORMAL;
import static techreborn.tiles.multiblock.MultiblockChecker.CASING_REINFORCED;
import static techreborn.tiles.multiblock.MultiblockChecker.ZERO_OFFSET;

public class TileIndustrialSawmill extends TilePowerAcceptor implements IWrenchable, IInventoryProvider, ISidedInventory, IListInfoProvider {

	public Inventory inventory = new Inventory(5, "Sawmill", 64, this);
	public Tank tank = new Tank("Sawmill", 16000, this);

	public int tickTime;
	public MultiblockChecker multiblockChecker;

	public TileIndustrialSawmill() {
		super(2);
	}

	@Override
	public void update() {
		super.update();
		if (getMutliBlock()) {
			ItemStack wood = inventory.getStackInSlot(0);
			if (tickTime == 0) {
				if (wood != null) {
					for (int id : OreDictionary.getOreIDs(wood)) {
						String name = OreDictionary.getOreName(id);
						if (name.equals("logWood") &&
							canAddOutput(2, 10) &&
							canAddOutput(3, 5) &&
							canAddOutput(4, 3) &&
							canUseEnergy(128.0F) &&
							!tank.isEmpty() &&
							tank.getFluid().amount >= 1000) {
							wood.shrink(1);
							if (wood.getCount() == 0)
								setInventorySlotContents(0, ItemStack.EMPTY);
							tank.drain(1000, true);
							useEnergy(128.0F);
							tickTime = 1;
						}
					}
				}
			} else if (++tickTime > 100) {
				Random rnd = world.rand;
				addOutput(2, new ItemStack(Blocks.PLANKS, 6 + rnd.nextInt(4)));
				if (rnd.nextInt(4) != 0) {
					ItemStack pulp = ItemDusts.getDustByName("sawDust", 2 + rnd.nextInt(3));
					addOutput(3, pulp);
				}
				if (rnd.nextInt(3) == 0) {
					ItemStack paper = new ItemStack(Items.PAPER, 1 + rnd.nextInt(2));
					addOutput(4, paper);
				}
				tickTime = 0;
			}
		}
		FluidUtils.drainContainers(tank, inventory, 1, 4);
	}

	public void addOutput(int slot, ItemStack stack) {
		if (getStackInSlot(slot) == null)
			setInventorySlotContents(slot, stack);
		getStackInSlot(slot).grow(stack.getCount());
	}

	public boolean canAddOutput(int slot, int amount) {
		ItemStack stack = getStackInSlot(slot);
		return stack == null || getInventoryStackLimit() - stack.getCount() >= amount;
	}

	@Override
	public void validate() {
		super.validate();
		multiblockChecker = new MultiblockChecker(world, getPos().down(3));
	}

	public boolean getMutliBlock() {
		boolean down = multiblockChecker.checkRectY(1, 1, CASING_NORMAL, ZERO_OFFSET);
		boolean up = multiblockChecker.checkRectY(1, 1, CASING_NORMAL, new BlockPos(0, 2, 0));
		boolean blade = multiblockChecker.checkRingY(1, 1, CASING_REINFORCED, new BlockPos(0, 1, 0));
		IBlockState centerBlock = multiblockChecker.getBlock(0, 1, 0);
		boolean center = centerBlock.getBlock() == Blocks.WATER;
		return down && center && blade && up;
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
		return new ItemStack(ModBlocks.industrialSawmill, 1);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		tank.readFromNBT(tagCompound);
		tickTime = tagCompound.getInteger("tickTime");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tank.writeToNBT(tagCompound);
		tagCompound.setInteger("tickTime", tickTime);
		return tagCompound;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
			return (T) tank;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[] { 0, 2, 3, 4, 5 };
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return index == 0;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return index >= 2;
	}

	public int getProgressScaled(int scale) {
		if (tickTime != 0) {
			return tickTime * scale / 100;
		}
		return 0;
	}

	@Override
	public double getMaxPower() {
		return 64000;
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
		return 64;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.MEDIUM;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

}
