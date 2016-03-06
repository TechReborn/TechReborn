package techreborn.tiles;

import ic2.api.tile.IWrenchable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.tile.TileMachineBase;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;
import techreborn.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.RecipeHandler;
import techreborn.init.ModBlocks;
import techreborn.lib.Reference;

public class TileIronFurnace extends TileMachineBase implements IInventory {

    public int tickTime;
    public Inventory inventory = new Inventory(3, "TileIronFurnace", 64, this);

    int input1 = 0;    
    int output = 1;
    int fuelslot = 2;

    boolean active = false;
    public int fuel;
    public int fuelGague;
    public int progress;
    public int fuelScale = 200;
    byte direction;
    private static final int[] slots_top = new int[] { 0 };
    private static final int[] slots_bottom = new int[] { 2, 1 };
    private static final int[] slots_sides = new int[] { 1 };

    public int gaugeProgressScaled (int scale)
    {
        return (progress * scale) / fuelScale;
    }

    public int gaugeFuelScaled (int scale)
    {
        if (fuelGague == 0)
        {
            fuelGague = fuel;
            if (fuelGague == 0)
            {
                fuelGague = fuelScale;
            }
        }
        return (fuel * scale) / fuelGague;
    }

    @Override
    public void updateEntity ()
    {
        boolean burning = isBurning();
        boolean updateInventory = false;
        if (fuel <= 0 && canSmelt())
        {
            fuel = fuelGague = (int) (getItemBurnTime(getStackInSlot(fuelslot)));
            if (fuel > 0)
            {
                if (getStackInSlot(fuelslot).getItem().hasContainerItem()) // Fuel slot
                {
                    setInventorySlotContents(fuelslot, new ItemStack(getStackInSlot(fuelslot).getItem().getContainerItem()));
                }
                else if(getStackInSlot(fuelslot).stackSize > 1)
                {
                	decrStackSize(fuelslot, 1);
                }
                else if (getStackInSlot(fuelslot).stackSize == 1)
                {
                    setInventorySlotContents(fuelslot, null);
                }
                updateInventory = true;
            }
        }
        if (isBurning() && canSmelt())
        {
        	updateState();

            progress++;
            if (progress >= fuelScale)
            {
                progress = 0;
                cookItems();
                updateInventory = true;
            }
        }
        else
        {
            progress = 0;
            updateState();
        }
        if (fuel > 0)
        {
            fuel--;
        }
        if (burning != isBurning())
        {
        	this.active = true;
            updateInventory = true;
        }
        if (updateInventory)
        {
            markDirty();
        }
    }

    public void cookItems ()
    {
        if (this.canSmelt())
        {
            ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(getStackInSlot(input1));

            if (getStackInSlot(output) == null)
            {
                setInventorySlotContents(output, itemstack.copy());
            }
            else if (getStackInSlot(output).isItemEqual(itemstack))
            {
            	getStackInSlot(output).stackSize += itemstack.stackSize;
            }
            if(getStackInSlot(input1).stackSize > 1)
            {
            	this.decrStackSize(input1, 1);
            }
            else 
            {
            	setInventorySlotContents(input1, null);
            }
        }
    }

    public boolean canSmelt ()
    {
        if (getStackInSlot(input1) == null)
        {
            return false;
        }
        else
        {
            ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(getStackInSlot(input1));
            if (itemstack == null)
                return false;
            if (getStackInSlot(output) == null)
                return true;
            if (!getStackInSlot(output).isItemEqual(itemstack))
                return false;
            int result = getStackInSlot(output).stackSize + itemstack.stackSize;
            return (result <= getInventoryStackLimit() && result <= itemstack.getMaxStackSize());
        }
    }

    public boolean isBurning ()
    {
        return fuel > 0;
    }

    public ItemStack getResultFor (ItemStack stack)
    {
        ItemStack result = FurnaceRecipes.instance().getSmeltingResult(stack);
        if (result != null) 
        {
            return result.copy();
        }
        return null;
    }

    public static int getItemBurnTime (ItemStack stack)
    {
        if (stack == null)
        {
            return 0;
        }
        else
        {
            Item item = stack.getItem();

            if (stack.getItem() instanceof ItemBlock && Block.getBlockFromItem(item) != null)
            {
                Block block = Block.getBlockFromItem(item);

                if (block == Blocks.wooden_slab)
                {
                    return 150;
                }

                if (block == Blocks.log)
                {
                    return 1200;
                }

                if (block.getMaterial() == Material.wood)
                {
                    return 300;
                }

                if (block == Blocks.coal_block)
                {
                    return 16000;
                }
            }

            if (item instanceof ItemTool && ((ItemTool) item).getToolMaterialName().equals("WOOD"))
                return 200;
            if (item instanceof ItemSword && ((ItemSword) item).getToolMaterialName().equals("WOOD"))
                return 200;
            if (item instanceof ItemHoe && ((ItemHoe) item).getMaterialName().equals("WOOD"))
                return 200;
            if (item == Items.stick)
                return 100;
            if (item == Items.coal)
                return 1600;
            if (item == Items.lava_bucket)
                return 20000;
            if (item == new ItemStack(Blocks.sapling).getItem())
                return 100;
            if (item == Items.blaze_rod)
                return 2400;
            return GameRegistry.getFuelValue(stack);
        }
    }
    
    public void updateState(){
        IBlockState blockState = worldObj.getBlockState(pos);
        if(blockState.getBlock() instanceof BlockMachineBase){
            BlockMachineBase blockMachineBase = (BlockMachineBase) blockState.getBlock();
            if(blockState.getValue(BlockMachineBase.ACTIVE) != progress > 0)
                blockMachineBase.setActive(progress > 0, worldObj, pos);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        inventory.readFromNBT(tagCompound);

    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        inventory.writeToNBT(tagCompound);

    }

    @Override
    public int getSizeInventory() {
        return inventory.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        return inventory.decrStackSize(slot, amount);
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        return inventory.removeStackFromSlot(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory.setInventorySlotContents(slot, stack);
    }


    @Override
    public int getInventoryStackLimit() {
        return inventory.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return inventory.isUseableByPlayer(player);
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return inventory.isItemValidForSlot(slot, stack);
    }

    @Override
    public void openInventory(EntityPlayer player) {
        inventory.openInventory(player);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        inventory.closeInventory(player);
    }


    @Override
    public int getField(int id) {
        return inventory.getField(id);
    }

    @Override
    public void setField(int id, int value) {
        inventory.setField(id, value);
    }

    @Override
    public int getFieldCount() {
        return inventory.getFieldCount();
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public String getName() {
        return inventory.getName();
    }

    @Override
    public boolean hasCustomName() {
        return inventory.hasCustomName();
    }

    @Override
    public IChatComponent getDisplayName() {
        return inventory.getDisplayName();
    }
}
