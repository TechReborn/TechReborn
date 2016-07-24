package techreborn.tiles.multiblock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import reborncore.common.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.misc.Location;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.tile.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import techreborn.api.Reference;
import techreborn.blocks.BlockMachineCasing;
import techreborn.init.ModBlocks;
import techreborn.tiles.TileMachineCasing;

import static techreborn.tiles.multiblock.MultiblockChecker.CASING_NORMAL;
import static techreborn.tiles.multiblock.MultiblockChecker.CASING_REINFORCED;
import static techreborn.tiles.multiblock.MultiblockChecker.ZERO_OFFSET;

public class TileImplosionCompressor extends TilePowerAcceptor implements IWrenchable,IInventoryProvider, ISidedInventory, IRecipeCrafterProvider {

    public Inventory inventory = new Inventory(4, "TileImplosionCompressor", 64, this);
    public MultiblockChecker multiblockChecker;
    public RecipeCrafter crafter;

    public TileImplosionCompressor() {
        int[] inputs = new int[]{0, 1};
        int[] outputs = new int[]{2, 3};
        crafter = new RecipeCrafter(Reference.implosionCompressorRecipe, this, 2, 2, inventory, inputs, outputs);
    }

    @Override
    public void validate() {
        super.validate();
        multiblockChecker = new MultiblockChecker(worldObj, getPos().down(3));
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
        return new ItemStack(ModBlocks.ImplosionCompressor, 1);
    }

    public boolean getMutliBlock() {
        boolean down = multiblockChecker.checkRectY(1, 1, CASING_REINFORCED, ZERO_OFFSET);
        boolean up = multiblockChecker.checkRectY(1, 1, CASING_REINFORCED, new BlockPos(0, 2, 0));
        boolean chamber = multiblockChecker.checkRingYHollow(1, 1, CASING_REINFORCED, new BlockPos(0, 1, 0));
        return down && chamber && up;
    }

    @Override
    public void update() {
        super.update();
        if (getMutliBlock()) {
            crafter.updateEntity();
        }
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

    // ISidedInventory
    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return side == EnumFacing.DOWN ? new int[]{0, 1, 2, 3} : new int[]{0, 1, 2, 3};
    }

    @Override
    public boolean canInsertItem(int slotIndex, ItemStack itemStack, EnumFacing side) {
        return slotIndex < 2 && isItemValidForSlot(slotIndex, itemStack);
    }

    @Override
    public boolean canExtractItem(int slotIndex, ItemStack itemStack, EnumFacing side) {
        return slotIndex == 2 || slotIndex == 3;
    }

    public int getProgressScaled(int scale) {
        if (crafter.currentTickTime != 0) {
            return crafter.currentTickTime * scale / crafter.currentNeededTicks;
        }
        return 0;
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
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public RecipeCrafter getRecipeCrafter() {
        return crafter;
    }

}
