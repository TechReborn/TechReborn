package techreborn.tiles.multiblock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.IWrenchable;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.tile.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import techreborn.api.Reference;
import techreborn.init.ModBlocks;

import static techreborn.tiles.multiblock.MultiblockChecker.CASING_REINFORCED;
import static techreborn.tiles.multiblock.MultiblockChecker.ZERO_OFFSET;

public class TileVacuumFreezer extends TilePowerAcceptor implements IWrenchable,IInventoryProvider, IRecipeCrafterProvider, ISidedInventory {

	public Inventory inventory = new Inventory(3, "TileVacuumFreezer", 64, this);
    public MultiblockChecker multiblockChecker;
	public RecipeCrafter crafter;

	public TileVacuumFreezer() {
		int[] inputs = new int[] {0};
		int[] outputs = new int[] {1};
		crafter = new RecipeCrafter(Reference.vacuumFreezerRecipe, this, 2, 1, inventory, inputs, outputs);
	}

    @Override
    public void validate() {
        super.validate();
        multiblockChecker = new MultiblockChecker(worldObj, getPos().down());
    }

    @Override
	public void update() {
		super.update();
        if(getMultiBlock())
            crafter.updateEntity();
	}

    public boolean getMultiBlock() {
        return multiblockChecker.checkRectY(1, 1, CASING_REINFORCED, ZERO_OFFSET);
    }

	@Override
	public double getMaxPower() {
		return 64000;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.MEDIUM;
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
		return new ItemStack(ModBlocks.AlloySmelter, 1);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		crafter.readFromNBT(tagCompound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		crafter.writeToNBT(tagCompound);
		return tagCompound;
	}

	public int getProgressScaled(int scale) {
		if (crafter.currentTickTime != 0) {
			return crafter.currentTickTime * scale / crafter.currentNeededTicks;
		}
		return 0;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public RecipeCrafter getRecipeCrafter() {
		return crafter;
	}

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[] {0, 1};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return index == 0;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return index == 1;
    }

}
