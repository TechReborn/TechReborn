package techreborn.tiles;

import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.lib.Functions;
import techreborn.powerSystem.TilePowerAcceptor;
import techreborn.util.Inventory;

public class TileAesu extends TilePowerAcceptor implements IWrenchable {

    public static final int MAX_OUTPUT = ConfigTechReborn.aesuMaxOutput;
    public static final int MAX_STORAGE = ConfigTechReborn.aesuMaxStorage;
    public Inventory inventory = new Inventory(2, "TileAesu", 64);
    private int OUTPUT = 64; //The current output
    private double euLastTick = 0;
    private double euChange;
    private int ticks;

    public TileAesu() {
        super(5);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (ticks == ConfigTechReborn.aveargeEuOutTickTime) {
            euChange = -1;
            ticks = 0;

        } else {
            ticks++;
            euChange += getEnergy() - euLastTick;
            if (euLastTick == getEnergy()) {
                euChange = 0;
            }
        }

        euLastTick = getEnergy();
    }

    @Override
    public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
        if (!entityPlayer.isSneaking()) {
            return true;
        }
        return false;
    }

    @Override
    public short getFacing() {
        return (short) getRotation();
    }

    @Override
    public void setFacing(short facing) {
        setRotation(facing);
    }

    @Override
    public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
        if (entityPlayer.isSneaking()) {
            return true;
        }
        return false;
    }

    @Override
    public float getWrenchDropRate() {
        return 1.0F;
    }

    @Override
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
        return getDropWithNBT();
    }

    public boolean isComplete() {
        return false;
    }

    public void handleGuiInputFromClient(int id) {
        if (id == 0) {
            OUTPUT += 256;
        }
        if (id == 1) {
            OUTPUT += 64;
        }
        if (id == 2) {
            OUTPUT -= 64;
        }
        if (id == 3) {
            OUTPUT -= 256;
        }
        if (OUTPUT > MAX_OUTPUT) {
            OUTPUT = MAX_OUTPUT;
        }
        if (OUTPUT <= -1) {
            OUTPUT = 0;
        }
    }

    public double getEuChange() {
        if (euChange == -1) {
            return -1;
        }
        return (euChange / ticks);
    }

    public ItemStack getDropWithNBT() {
        NBTTagCompound tileEntity = new NBTTagCompound();
        ItemStack dropStack = new ItemStack(ModBlocks.Aesu, 1);
        writeToNBTWithoutCoords(tileEntity);
        dropStack.setTagCompound(new NBTTagCompound());
        dropStack.stackTagCompound.setTag("tileEntity", tileEntity);
        return dropStack;
    }

    public void writeToNBTWithoutCoords(NBTTagCompound tagCompound) {
        super.writeToNBTWithoutCoords(tagCompound);
        tagCompound.setDouble("euChange", euChange);
        tagCompound.setDouble("euLastTick", euLastTick);
        inventory.writeToNBT(tagCompound);
    }

    public void readFromNBTWithoutCoords(NBTTagCompound nbttagcompound) {
        super.readFromNBTWithoutCoords(nbttagcompound);
        this.euChange = nbttagcompound.getDouble("euChange");
        this.euLastTick = nbttagcompound.getDouble("euLastTick");
        inventory.readFromNBT(nbttagcompound);
    }


    @Override
    public double getMaxPower() {
        return TileAesu.MAX_STORAGE;
    }

    @Override
    public boolean canAcceptEnergy(ForgeDirection direction) {
        return getRotation() != Functions.getIntDirFromDirection(direction);
    }

    @Override
    public boolean canProvideEnergy(ForgeDirection direction) {
        return getRotation() == Functions.getIntDirFromDirection(direction);
    }

    @Override
    public double getMaxOutput() {
        return OUTPUT;
    }

    @Override
    public double getMaxInput() {
        return 4096 * 2;
    }
}
