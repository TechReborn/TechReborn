package techreborn.tiles;

import reborncore.common.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.*;
import reborncore.api.IListInfoProvider;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.misc.Location;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.FluidUtils;
import reborncore.common.util.Inventory;
import reborncore.common.util.Tank;
import techreborn.api.Reference;
import techreborn.api.recipe.ITileRecipeHandler;
import techreborn.api.recipe.machines.IndustrialSawmillRecipe;
import techreborn.blocks.BlockMachineCasing;
import techreborn.init.ModBlocks;
import techreborn.init.ModFluids;

public class TileIndustrialSawmill extends TilePowerAcceptor
		implements IWrenchable, IFluidHandler,IInventoryProvider, ISidedInventory, IListInfoProvider, ITileRecipeHandler<IndustrialSawmillRecipe>, IRecipeCrafterProvider
{
	public static final int TANK_CAPACITY = 16000;

	public int tickTime;
	public Inventory inventory = new Inventory(5, "TileIndustrialSawmill", 64, this);
	public Tank tank = new Tank("TileSawmill", TANK_CAPACITY, this);
	public RecipeCrafter crafter;

	public TileIndustrialSawmill()
	{
		super(2);
		// TODO configs
		// Input slots
		int[] inputs = new int[2];
		inputs[0] = 0;
		inputs[1] = 1;
		int[] outputs = new int[3];
		outputs[0] = 2;
		outputs[1] = 3;
		outputs[2] = 4;
		crafter = new RecipeCrafter(Reference.industrialSawmillRecipe, this, 2, 3, inventory, inputs, outputs);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if (getMutliBlock())
		{
			crafter.updateEntity();
		}
		FluidUtils.drainContainers(this, inventory, 0, 4);
		FluidUtils.drainContainers(this, inventory, 1, 4);
	}

	public boolean getMutliBlock()
	{
		for (EnumFacing direction : EnumFacing.values())
		{
			TileEntity tileEntity = worldObj.getTileEntity(new BlockPos(getPos().getX() + direction.getFrontOffsetX(),
					getPos().getY() + direction.getFrontOffsetY(), getPos().getZ() + direction.getFrontOffsetZ()));
			if (tileEntity instanceof TileMachineCasing)
			{
				if ((tileEntity.getBlockType() instanceof BlockMachineCasing))
				{
					int heat;
					BlockMachineCasing blockMachineCasing = (BlockMachineCasing) tileEntity.getBlockType();
					heat = blockMachineCasing
							.getHeatFromState(tileEntity.getWorld().getBlockState(tileEntity.getPos()));
					Location location = new Location(getPos().getX(), getPos().getY(), getPos().getZ(), direction);
					location.modifyPositionFromSide(direction, 1);
					if (worldObj.getBlockState(location.getBlockPos()).getBlock().getUnlocalizedName()
							.equals("tile.lava"))
					{
						heat += 500;
					}
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, EnumFacing side)
	{
		return false;
	}

	@Override
	public EnumFacing getFacing()
	{
		return getFacingEnum();
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer)
	{
		return entityPlayer.isSneaking();
	}

	@Override
	public float getWrenchDropRate()
	{
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
	{
		return new ItemStack(ModBlocks.industrialSawmill, 1);
	}

	public boolean isComplete()
	{
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		tank.readFromNBT(tagCompound);
		crafter.readFromNBT(tagCompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tank.writeToNBT(tagCompound);
		crafter.writeToNBT(tagCompound);
	}

	/* IFluidHandler */
	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill)
	{
		if (resource.getFluid() == FluidRegistry.WATER || resource.getFluid() == ModFluids.fluidMercury
				|| resource.getFluid() == ModFluids.fluidSodiumpersulfate)
		{
			int filled = tank.fill(resource, doFill);
			tank.compareAndUpdate();
			return filled;
		}
		return 0;
	}

	@Override
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
	{
		if (resource == null || !resource.isFluidEqual(tank.getFluid()))
		{
			return null;
		}
		FluidStack fluidStack = tank.drain(resource.amount, doDrain);
		tank.compareAndUpdate();
		return fluidStack;
	}

	@Override
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
	{
		FluidStack drained = tank.drain(maxDrain, doDrain);
		tank.compareAndUpdate();
		return drained;
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid)
	{
		return fluid == FluidRegistry.WATER;
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid)
	{
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from)
	{
		return new FluidTankInfo[] { tank.getInfo() };
	}

	// ISidedInventory
	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return side == EnumFacing.DOWN ? new int[] { 0, 1, 2, 3, 4 } : new int[] { 0, 1, 2, 3, 4 };
	}

	@Override
	public boolean canInsertItem(int slotIndex, ItemStack itemStack, EnumFacing side)
	{
		if (slotIndex >= 2)
			return false;
		return isItemValidForSlot(slotIndex, itemStack);
	}

	@Override
	public boolean canExtractItem(int slotIndex, ItemStack itemStack, EnumFacing side)
	{
		return slotIndex == 2 || slotIndex == 3 || slotIndex == 4;
	}

	public int getProgressScaled(int scale)
	{
		if (crafter.currentTickTime != 0)
		{
			return crafter.currentTickTime * scale / crafter.currentNeededTicks;
		}
		return 0;
	}

	@Override
	public double getMaxPower()
	{
		return 10000;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing direction)
	{
		return true;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing direction)
	{
		return false;
	}

	@Override
	public double getMaxOutput()
	{
		return 0;
	}

	@Override
	public double getMaxInput()
	{
		return 64;
	}

	@Override
	public EnumPowerTier getTier()
	{
		return EnumPowerTier.LOW;
	}

	@Override
	public boolean canCraft(TileEntity tile, IndustrialSawmillRecipe recipe) {
		if (recipe.fluidStack == null) {
			return true;
		}
		if (tile instanceof TileIndustrialSawmill) {
			TileIndustrialSawmill sawmill = (TileIndustrialSawmill) tile;
			if (sawmill.tank.getFluid() == null) {
				return false;
			}
			if (sawmill.tank.getFluid() == recipe.fluidStack) {
				if (sawmill.tank.getFluidAmount() >= recipe.fluidStack.amount) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean onCraft(TileEntity tile, IndustrialSawmillRecipe recipe) {
		if (recipe.fluidStack == null) {
			return true;
		}
		if (tile instanceof TileIndustrialSawmill) {
			TileIndustrialSawmill sawmill = (TileIndustrialSawmill) tile;
			if (sawmill.tank.getFluid() == null) {
				return false;
			}
			if (sawmill.tank.getFluid() == recipe.fluidStack) {
				if (sawmill.tank.getFluidAmount() >= recipe.fluidStack.amount) {
					if (sawmill.tank.getFluidAmount() > 0) {
						sawmill.tank.setFluid(new FluidStack(recipe.fluidStack.getFluid(),
								sawmill.tank.getFluidAmount() - recipe.fluidStack.amount));
					} else {
						sawmill.tank.setFluid(null);
					}
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public RecipeCrafter getRecipeCrafter() {
		return crafter;
	}
}
