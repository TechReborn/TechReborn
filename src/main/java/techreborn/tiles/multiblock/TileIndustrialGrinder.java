package techreborn.tiles.multiblock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.*;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.IWrenchable;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.FluidUtils;
import reborncore.common.util.Inventory;
import reborncore.common.util.Tank;
import techreborn.api.Reference;
import techreborn.api.recipe.ITileRecipeHandler;
import techreborn.api.recipe.machines.IndustrialGrinderRecipe;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.init.ModFluids;

import static techreborn.tiles.multiblock.MultiblockChecker.CASING_NORMAL;
import static techreborn.tiles.multiblock.MultiblockChecker.CASING_REINFORCED;
import static techreborn.tiles.multiblock.MultiblockChecker.ZERO_OFFSET;

public class TileIndustrialGrinder extends TilePowerAcceptor
	implements IWrenchable, IFluidHandler, IInventoryProvider, ISidedInventory, ITileRecipeHandler<IndustrialGrinderRecipe>, IRecipeCrafterProvider {
	public static final int TANK_CAPACITY = 16000;

	public Inventory inventory = new Inventory(6, "TileGrinder", 64, this);
	public Tank tank = new Tank("TileGrinder", TANK_CAPACITY, this);
	public RecipeCrafter crafter;
	public MultiblockChecker multiblockChecker;

	public TileIndustrialGrinder() {
		super(ConfigTechReborn.CentrifugeTier);
		// TODO configs

		int[] inputs = new int[2];
		inputs[0] = 0;
		inputs[1] = 1;
		int[] outputs = new int[4];
		outputs[0] = 2;
		outputs[1] = 3;
		outputs[2] = 4;
		outputs[3] = 5;
		crafter = new RecipeCrafter(Reference.industrialGrinderRecipe, this, 1, 4, inventory, inputs, outputs);
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
		return new ItemStack(ModBlocks.IndustrialGrinder, 1);
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
	public void update() {
		super.update();

		if (multiblockChecker == null) {
			BlockPos pos = getPos().offset(getFacing().getOpposite(), 2).down();
			multiblockChecker = new MultiblockChecker(worldObj, pos);
		}

		if (getMutliBlock()) {
			crafter.updateEntity();
		}
		FluidUtils.drainContainers(this, inventory, 1, 5);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		tank.readFromNBT(tagCompound);
		crafter.readFromNBT(tagCompound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tank.writeToNBT(tagCompound);
		crafter.writeToNBT(tagCompound);
		return tagCompound;
	}

	@Override
	public void invalidate() {
		super.invalidate();
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
	}

	/* IFluidHandler */
	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
		if (resource.getFluid() == FluidRegistry.WATER || resource.getFluid() == ModFluids.fluidMercury
			|| resource.getFluid() == ModFluids.fluidSodiumpersulfate) {
			int filled = tank.fill(resource, doFill);
			tank.compareAndUpdate();
			return filled;
		}
		return 0;
	}

	@Override
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
		if (resource == null || !resource.isFluidEqual(tank.getFluid())) {
			return null;
		}
		FluidStack fluidStack = tank.drain(resource.amount, doDrain);
		tank.compareAndUpdate();
		return fluidStack;
	}

	@Override
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
		FluidStack drained = tank.drain(maxDrain, doDrain);
		tank.compareAndUpdate();
		return drained;
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid) {
		return fluid == FluidRegistry.WATER || fluid == ModFluids.fluidMercury || fluid == ModFluids.fluidSodiumpersulfate;
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid) {
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from) {
		return new FluidTankInfo[] { tank.getInfo() };
	}

	// ISidedInventory
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return side == EnumFacing.DOWN ? new int[] { 0, 1, 2, 3, 4, 5 } : new int[] { 0, 1, 2, 3, 4, 5 };
	}

	@Override
	public boolean canInsertItem(int slotIndex, ItemStack itemStack, EnumFacing side) {
		if (slotIndex >= 2)
			return false;
		return isItemValidForSlot(slotIndex, itemStack);
	}

	@Override
	public boolean canExtractItem(int slotIndex, ItemStack itemStack, EnumFacing side) {
		return slotIndex == 2 || slotIndex == 3 || slotIndex == 4 || slotIndex == 5;
	}

	public int getProgressScaled(int scale) {
		if (crafter.currentTickTime != 0) {
			return crafter.currentTickTime * scale / crafter.currentNeededTicks;
		}
		return 0;
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
	public boolean canCraft(TileEntity tile, IndustrialGrinderRecipe recipe) {
		FluidStack recipeFluid = recipe.fluidStack;
		FluidStack tankFluid = tank.getFluid();
		if (recipe.fluidStack == null) {
			return true;
		}
		if (tankFluid == null) {
			return false;
		}
		if (tankFluid.isFluidEqual(recipeFluid)) {
			if (tankFluid.amount >= recipeFluid.amount) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onCraft(TileEntity tile, IndustrialGrinderRecipe recipe) {
		FluidStack recipeFluid = recipe.fluidStack;
		FluidStack tankFluid = tank.getFluid();
		if (recipe.fluidStack == null) {
			return true;
		}
		if (tankFluid == null) {
			return false;
		}
		if (tankFluid.isFluidEqual(recipeFluid)) {
			if (tankFluid.amount >= recipeFluid.amount) {
				if (tankFluid.amount == recipeFluid.amount)
					tank.setFluid(null);
				else
					tankFluid.amount -= recipeFluid.amount;
				return true;
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
