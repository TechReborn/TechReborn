package techreborn.tiles.fusionReactor;

import ic2.api.item.IC2Items;
import ic2.core.item.ItemFluidCell;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;
import techreborn.api.reactor.FusionReactorRecipe;
import techreborn.api.reactor.FusionReactorRecipeHelper;
import techreborn.init.ModBlocks;
import techreborn.powerSystem.TilePowerAcceptor;


public class TileEntityFusionController extends TilePowerAcceptor implements IInventory {

    public Inventory inventory = new Inventory(3, "TileEntityFusionController", 64);

    //0= no coils, 1 = coils
    public int coilStatus = 0;
    int emptyCellCount = 0 ;
    int topStackSlot = 0;
    int bottomStackSlot = 1;
    int outputStackSlot = 2;

    public int crafingTickTime = 0;
    public int finalTickTime = 0;
    public int neededPower = 0;

    FusionReactorRecipe currentRecipe = null;
    boolean hasStartedCrafting = false;

    public TileEntityFusionController() {
        super(4);
    }

    @Override
    public double getMaxPower() {
        return 100000000;
    }

    @Override
    public boolean canAcceptEnergy(ForgeDirection direction) {
        if(direction == ForgeDirection.DOWN || direction == ForgeDirection.UP){
            return false;
        }
        return true;
    }

    @Override
    public boolean canProvideEnergy(ForgeDirection direction) {
        if(direction == ForgeDirection.DOWN || direction == ForgeDirection.UP){
            return true;
        }
        return false;
    }

    @Override
    public double getMaxOutput() {
        if(!hasStartedCrafting){
            return 0;
        }
        return 1000000;
    }

    @Override
    public double getMaxInput() {
        if(hasStartedCrafting){
            return 0;
        }
        return 8192;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        inventory.readFromNBT(tagCompound);
        crafingTickTime = tagCompound.getInteger("crafingTickTime");
        finalTickTime = tagCompound.getInteger("finalTickTime");
        neededPower = tagCompound.getInteger("neededPower");
        hasStartedCrafting = tagCompound.getBoolean("hasStartedCrafting");
        emptyCellCount = tagCompound.getInteger("emptyCellCount");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        inventory.writeToNBT(tagCompound);

        if(crafingTickTime == -1){
            crafingTickTime = 0;
        }
        if(finalTickTime == -1){
            finalTickTime = 0;
        }
        if(neededPower == -1){
            neededPower = 0;
        }
        tagCompound.setInteger("crafingTickTime", crafingTickTime);
        tagCompound.setInteger("finalTickTime", finalTickTime);
        tagCompound.setInteger("neededPower", neededPower);
        tagCompound.setBoolean("hasStartedCrafting", hasStartedCrafting);
        tagCompound.setInteger("emptyCellCount", emptyCellCount);
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
    public ItemStack getStackInSlotOnClosing(int slot) {
        return inventory.getStackInSlotOnClosing(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory.setInventorySlotContents(slot, stack);
    }

    @Override
    public String getInventoryName() {
        return inventory.getInventoryName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return inventory.hasCustomInventoryName();
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
    public void openInventory() {
        inventory.openInventory();
    }

    @Override
    public void closeInventory() {
        inventory.closeInventory();
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return slot!=2;
        //return inventory.isItemValidForSlot(slot, stack);
    }


    public boolean checkCoils() {
        if ((isCoil(this.xCoord + 3, this.yCoord, this.zCoord + 1)) &&
                (isCoil(this.xCoord + 3, this.yCoord, this.zCoord)) &&
                (isCoil(this.xCoord + 3, this.yCoord, this.zCoord - 1)) &&
                (isCoil(this.xCoord - 3, this.yCoord, this.zCoord + 1)) &&
                (isCoil(this.xCoord - 3, this.yCoord, this.zCoord)) &&
                (isCoil(this.xCoord - 3, this.yCoord, this.zCoord - 1)) &&
                (isCoil(this.xCoord + 2, this.yCoord, this.zCoord + 2)) &&
                (isCoil(this.xCoord + 2, this.yCoord, this.zCoord + 1)) &&
                (isCoil(this.xCoord + 2, this.yCoord, this.zCoord - 1)) &&
                (isCoil(this.xCoord + 2, this.yCoord, this.zCoord - 2)) &&
                (isCoil(this.xCoord - 2, this.yCoord, this.zCoord + 2)) &&
                (isCoil(this.xCoord - 2, this.yCoord, this.zCoord + 1)) &&
                (isCoil(this.xCoord - 2, this.yCoord, this.zCoord - 1)) &&
                (isCoil(this.xCoord - 2, this.yCoord, this.zCoord - 2)) &&
                (isCoil(this.xCoord + 1, this.yCoord, this.zCoord + 3)) &&
                (isCoil(this.xCoord + 1, this.yCoord, this.zCoord + 2)) &&
                (isCoil(this.xCoord + 1, this.yCoord, this.zCoord - 2)) &&
                (isCoil(this.xCoord + 1, this.yCoord, this.zCoord - 3)) &&
                (isCoil(this.xCoord - 1, this.yCoord, this.zCoord + 3)) &&
                (isCoil(this.xCoord - 1, this.yCoord, this.zCoord + 2)) &&
                (isCoil(this.xCoord - 1, this.yCoord, this.zCoord - 2)) &&
                (isCoil(this.xCoord - 1, this.yCoord, this.zCoord - 3)) &&
                (isCoil(this.xCoord, this.yCoord, this.zCoord + 3)) &&
                (isCoil(this.xCoord, this.yCoord, this.zCoord - 3))) {
            coilStatus = 1;
            return true;
        }
        coilStatus = 0;
        return false;
    }

    private boolean isCoil(int x, int y, int z) {
        return worldObj.getBlock(x, y, z) == ModBlocks.FusionCoil;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        //TODO improve this code a lot

        if (worldObj.getTotalWorldTime() % 20 == 0) {
            checkCoils();
        }

        if (!worldObj.isRemote) {
            if(emptyCellCount>0){
                if (getStackInSlot(outputStackSlot) == null) {
                    setInventorySlotContents(outputStackSlot, IC2Items.getItem("cell").splitStack(emptyCellCount));
                    emptyCellCount=0;
                }
            }
            if (coilStatus == 1) {
                if (currentRecipe == null) {
                    if (inventory.hasChanged || crafingTickTime != 0) {
                        for (FusionReactorRecipe reactorRecipe : FusionReactorRecipeHelper.reactorRecipes) {
                            if (ItemUtils.isItemEqual(getStackInSlot(topStackSlot), reactorRecipe.getTopInput(), true, true, true)) {
                                if (reactorRecipe.getBottomInput() != null) {
                                    if (ItemUtils.isItemEqual(getStackInSlot(bottomStackSlot), reactorRecipe.getBottomInput(), true, true, true) == false) {
                                        break;
                                    }
                                }
                                if (canFitStack(reactorRecipe.getOutput(), outputStackSlot, true)) {
                                    currentRecipe = reactorRecipe;
                                    if(crafingTickTime != 0){
                                        finalTickTime = currentRecipe.getTickTime();
                                        neededPower = (int) currentRecipe.getStartEU();
                                    }
                                    hasStartedCrafting = false;
                                    crafingTickTime = 0;
                                    finalTickTime = currentRecipe.getTickTime();
                                    neededPower = (int) currentRecipe.getStartEU();
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    /*
                    if (inventory.hasChanged) {
                        if (!validateRecipe()) {
                            resetCrafter();
                            System.out.println("reset1");
                            return;
                        }
                    }//*/
                    if (!hasStartedCrafting) {
                        if (canUseEnergy(currentRecipe.getStartEU() + 64)) {
                            useEnergy(currentRecipe.getStartEU());
                            hasStartedCrafting = true;
                        }
                    } else {
                        if (crafingTickTime < currentRecipe.getTickTime()) {
                            if(crafingTickTime==0){
                                decrStackSize(topStackSlot, currentRecipe.getTopInput().stackSize);
                                if (currentRecipe.getBottomInput() != null) {
                                    decrStackSize(bottomStackSlot, currentRecipe.getBottomInput().stackSize);
                                }
                            }
                            if (currentRecipe.getEuTick() > 0) { //Power gen
                                addEnergy(currentRecipe.getEuTick()); //Waste power if it has no where to go
                                crafingTickTime++;
                            } else { //Power user
                                if (canUseEnergy(currentRecipe.getEuTick() * -1)) {
                                    setEnergy(getEnergy() - (currentRecipe.getEuTick() * -1));
                                    crafingTickTime++;
                                }
                            }
                        } else {
                            if (canFitStack(currentRecipe.getOutput(), outputStackSlot, true)) {
                                if (getStackInSlot(outputStackSlot) == null) {
                                    setInventorySlotContents(outputStackSlot, currentRecipe.getOutput().copy());
                                } else {
                                    decrStackSize(outputStackSlot, -currentRecipe.getOutput().stackSize);
                                }

                                if(currentRecipe.getTopInput().getItem().getUnlocalizedName().toLowerCase().contains("cell"))emptyCellCount++;
                                if(currentRecipe.getBottomInput().getItem().getUnlocalizedName().toLowerCase().contains("cell"))emptyCellCount++;
                                if(currentRecipe.getOutput().getItem().getUnlocalizedName().toLowerCase().contains("cell"))emptyCellCount--;


                                if(emptyCellCount>64)emptyCellCount=64;
                                System.out.println(String.valueOf(emptyCellCount));
                                /*
                                decrStackSize(topStackSlot, currentRecipe.getTopInput().stackSize);
                                if (currentRecipe.getBottomInput() != null) {
                                    decrStackSize(bottomStackSlot, currentRecipe.getBottomInput().stackSize);
                                }//*/
                                crafingTickTime=0;
                                if (!validateRecipe()) {
                                    resetCrafter();
                                    System.out.println("reset2X");
                                    //return;
                                }
                                //System.out.println("reset2");
                            }
                        }
                    }
                }
            } else {
                if (currentRecipe != null) {
                    resetCrafter();
                    System.out.println("reset3");
                }
            }
        }


        if (inventory.hasChanged) {
            inventory.hasChanged = false;
        }
    }

    private boolean validateRecipe() {
        if (ItemUtils.isItemEqual(getStackInSlot(topStackSlot), currentRecipe.getTopInput(), true, true, true)) {
            if (currentRecipe.getBottomInput() != null) {
                if (ItemUtils.isItemEqual(getStackInSlot(bottomStackSlot), currentRecipe.getBottomInput(), true, true, true) == false) {
                    return false;
                }
            }
            if (canFitStack(currentRecipe.getOutput(), outputStackSlot, true)) {
                return true;
            }
        }
        return false;
    }

    private void resetCrafter() {
        currentRecipe = null;
        crafingTickTime = -1;
        finalTickTime = -1;
        neededPower = -1;
        hasStartedCrafting = false;
    }

    public boolean canFitStack(ItemStack stack, int slot, boolean oreDic) {//Checks to see if it can fit the stack
        if (stack == null) {
            return true;
        }
        if (inventory.getStackInSlot(slot) == null) {
            return true;
        }
        if (ItemUtils.isItemEqual(inventory.getStackInSlot(slot), stack, true, true, oreDic)) {
            if (stack.stackSize + inventory.getStackInSlot(slot).stackSize <= stack.getMaxStackSize()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        checkCoils();
    }
}
