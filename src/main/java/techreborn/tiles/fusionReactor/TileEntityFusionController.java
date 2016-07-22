package techreborn.tiles.fusionReactor;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.power.tile.IEnergyProducerTile;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.explosion.RebornExplosion;
import reborncore.common.tile.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;
import techreborn.api.reactor.FusionReactorRecipe;
import techreborn.api.reactor.FusionReactorRecipeHelper;
import techreborn.init.ModBlocks;
import techreborn.power.EnergyUtils;

public class TileEntityFusionController extends TilePowerAcceptor implements IEnergyProducerTile, IInventoryProvider {

	public Inventory inventory = new Inventory(3, "TileEntityFusionController", 64, this);

	public boolean hasCoils;

	public int recipeTickTime = 0;

	public FusionReactorRecipe recipe = null;
	public boolean repeatSameRecipe = false;

	@Override
	public double getMaxPower() {
		return 100000000;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing direction) {
		return recipeTickTime == 0;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing direction) {
		return recipeTickTime != 0;
	}

	@Override
	public double getMaxOutput() {
		return 1000000;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.INSANE;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		if(tagCompound.hasKey("recipeTickTime") && checkCoils()) {
			ItemStack topInput = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("TopInput"));
			ItemStack bottomInput = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("BottomInput"));
			FusionReactorRecipe newRecipe = findNewNBTRecipe(topInput, bottomInput);
			if(newRecipe != null) {
				this.recipeTickTime = tagCompound.getInteger("recipeTickTime");
				this.repeatSameRecipe = tagCompound.getBoolean("repeatSameRecipe");
				this.recipe = newRecipe;
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		if(recipe != null) {
			tagCompound.setTag("TopInput", recipe.getTopInput().writeToNBT(new NBTTagCompound()));
			tagCompound.setTag("BottomInput", recipe.getBottomInput().writeToNBT(new NBTTagCompound()));

			tagCompound.setInteger("recipeTickTime", recipeTickTime);
			tagCompound.setBoolean("repeatSameRecipe", repeatSameRecipe);
		}
		return tagCompound;
	}


	public boolean checkCoils() {
		if ((isCoil(this.getPos().getX() + 3, this.getPos().getY(), this.getPos().getZ() + 1))
				&& (isCoil(this.getPos().getX() + 3, this.getPos().getY(), this.getPos().getZ()))
				&& (isCoil(this.getPos().getX() + 3, this.getPos().getY(), this.getPos().getZ() - 1))
				&& (isCoil(this.getPos().getX() - 3, this.getPos().getY(), this.getPos().getZ() + 1))
				&& (isCoil(this.getPos().getX() - 3, this.getPos().getY(), this.getPos().getZ()))
				&& (isCoil(this.getPos().getX() - 3, this.getPos().getY(), this.getPos().getZ() - 1))
				&& (isCoil(this.getPos().getX() + 2, this.getPos().getY(), this.getPos().getZ() + 2))
				&& (isCoil(this.getPos().getX() + 2, this.getPos().getY(), this.getPos().getZ() + 1))
				&& (isCoil(this.getPos().getX() + 2, this.getPos().getY(), this.getPos().getZ() - 1))
				&& (isCoil(this.getPos().getX() + 2, this.getPos().getY(), this.getPos().getZ() - 2))
				&& (isCoil(this.getPos().getX() - 2, this.getPos().getY(), this.getPos().getZ() + 2))
				&& (isCoil(this.getPos().getX() - 2, this.getPos().getY(), this.getPos().getZ() + 1))
				&& (isCoil(this.getPos().getX() - 2, this.getPos().getY(), this.getPos().getZ() - 1))
				&& (isCoil(this.getPos().getX() - 2, this.getPos().getY(), this.getPos().getZ() - 2))
				&& (isCoil(this.getPos().getX() + 1, this.getPos().getY(), this.getPos().getZ() + 3))
				&& (isCoil(this.getPos().getX() + 1, this.getPos().getY(), this.getPos().getZ() + 2))
				&& (isCoil(this.getPos().getX() + 1, this.getPos().getY(), this.getPos().getZ() - 2))
				&& (isCoil(this.getPos().getX() + 1, this.getPos().getY(), this.getPos().getZ() - 3))
				&& (isCoil(this.getPos().getX() - 1, this.getPos().getY(), this.getPos().getZ() + 3))
				&& (isCoil(this.getPos().getX() - 1, this.getPos().getY(), this.getPos().getZ() + 2))
				&& (isCoil(this.getPos().getX() - 1, this.getPos().getY(), this.getPos().getZ() - 2))
				&& (isCoil(this.getPos().getX() - 1, this.getPos().getY(), this.getPos().getZ() - 3))
				&& (isCoil(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ() + 3))
				&& (isCoil(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ() - 3))) {
			hasCoils = true;
			return true;
		}
		hasCoils = false;
		return false;
	}

	private boolean isCoil(int x, int y, int z) {
		return worldObj.getBlockState(new BlockPos(x, y, z)).getBlock() == ModBlocks.FusionCoil;
	}

	@Override
	public void update() {
		super.update();

		if (!worldObj.isRemote) {
			if (worldObj.getTotalWorldTime() % 20 == 0) {
				checkCoils();
			}

			if (hasCoils) {
				if (recipe == null || inventory.hasChanged) {
					FusionReactorRecipe newRecipe = findNewRecipe();
					if (newRecipe != null && newRecipe != recipe) {
						this.recipe = newRecipe;
						this.repeatSameRecipe = false;
						this.recipeTickTime = 0;
					}
				}

				if (recipe != null) {
					if (recipeTickTime == 0) {
						if (canUseEnergy(recipe.getStartEU())) {
							useEnergy(recipe.getStartEU());
							consumeInputStacks();
							++recipeTickTime;
						}
					} else if (++recipeTickTime >= recipe.getTickTime()) {
						addOutputStack(recipe.getOutput());
						FusionReactorRecipe newRecipe = findNewRecipe();
						if (newRecipe == recipe) {
							consumeInputStacks();
							this.repeatSameRecipe = true;
							recipeTickTime = 1;
						} else {
							this.recipe = null;
							this.repeatSameRecipe = false;
							recipeTickTime = 0;
						}
					} else {
						double euTick = recipe.getEuTick();
						if (euTick > 0) addEnergy(euTick);
						else if (useEnergy(-euTick) != euTick) {
							this.recipeTickTime = 0;
							this.repeatSameRecipe = false;
							this.recipe = null;
						}
					}
				}
			}

			if (!worldObj.isRemote && getEnergy() > 0 && recipeTickTime != 0) {
				double maxOutput = getEnergy() > getMaxOutput() ? getMaxOutput() : getEnergy();
				double disposed = 0;
				for(EnumFacing facing : EnumFacing.VALUES) {
					if(canProvideEnergy(facing)) {
						double emitted = emitEnergy(facing, maxOutput);
						maxOutput -= emitted;
						disposed += emitted;
					}
				}
				if (disposed != 0)
					useEnergy(disposed);
			}
		}

	}

	public FusionReactorRecipe findNewRecipe() {
		for(FusionReactorRecipe reactorRecipe : FusionReactorRecipeHelper.reactorRecipes) {
			if(canFitStack(reactorRecipe.getOutput(), 2) &&
					canEmptyStack(reactorRecipe.getTopInput(), 0) &&
					canEmptyStack(reactorRecipe.getBottomInput(), 1)) {
				return reactorRecipe;
			}
		}
		return null;
	}

	public FusionReactorRecipe findNewNBTRecipe(ItemStack top, ItemStack bottom) {
		for(FusionReactorRecipe reactorRecipe : FusionReactorRecipeHelper.reactorRecipes) {
			if(canFitStack(reactorRecipe.getOutput(), 2) &&
					ItemUtils.isItemEqual(top, reactorRecipe.getTopInput(), true, true) &&
					ItemUtils.isItemEqual(bottom, reactorRecipe.getBottomInput(), true, true)) {
					return reactorRecipe;
			}
		}
		return null;
	}


	public double emitEnergy(EnumFacing enumFacing, double amount) {
		BlockPos pos = getPos().offset(enumFacing);
		EnergyUtils.PowerNetReceiver receiver = EnergyUtils.getReceiver(
				worldObj, enumFacing.getOpposite(), pos);
		if (receiver != null) {
			return receiver.receiveEnergy(amount, false);
		}
		return 0;
	}

	public void consumeInputStacks() {
		if(--getStackInSlot(0).stackSize == 0) setInventorySlotContents(0, null);
		if(--getStackInSlot(1).stackSize == 0) setInventorySlotContents(1, null);
	}

	public void addOutputStack(ItemStack output) {
		ItemStack stack = getStackInSlot(2);
		if(stack == null) setInventorySlotContents(2, output);
		else stack.stackSize++;
	}

	public boolean canEmptyStack(ItemStack stack, int slot) {
		return inventory.getStackInSlot(slot) != null &&
				ItemUtils.isItemEqual(inventory.getStackInSlot(slot), stack, true, true, true);
	}

	public boolean canFitStack(ItemStack stack, int slot) {
		if (stack == null || inventory.getStackInSlot(slot) == null) {
			return true;
		}
		if (ItemUtils.isItemEqual(inventory.getStackInSlot(slot), stack, true, true, true)) {
			if (stack.stackSize + inventory.getStackInSlot(slot).stackSize <= stack.getMaxStackSize()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Fusion Reactor";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(getName());
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

}
