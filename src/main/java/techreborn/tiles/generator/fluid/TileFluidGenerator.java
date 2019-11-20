package techreborn.tiles.generator.fluid;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import reborncore.api.IToolDrop;
import reborncore.api.praescriptum.fuels.Fuel;
import reborncore.api.praescriptum.fuels.FuelHandler;
import reborncore.api.tile.IInventoryProvider;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.network.NetworkManager;
import reborncore.common.network.packet.CustomDescriptionPacket;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.FluidUtils;
import reborncore.common.util.Inventory;
import reborncore.common.util.Tank;

import java.util.Optional;

public abstract class TileFluidGenerator extends TilePowerAcceptor implements IToolDrop, IInventoryProvider, IContainerProvider {
    // Constructors >>
    public TileFluidGenerator(String name, int maxOutput, int maxEnergy, int tankCapacity, FuelHandler fuelHandler, Block drop) {
        this.name = "Tile" + name;
        this.maxOutput = maxOutput;
        this.maxEnergy = maxEnergy;
        this.tank = new Tank(this.name, tankCapacity, this);
        this.inventory = new Inventory(2, name, 64, this);
        this.inputSlots = new int[]{0};
        this.outputSlots = new int[]{1};
        this.fuelHandler = fuelHandler;
        this.drop = drop;

        checkTeir();
    }
    // << Constructors

    // TileEntity >>
    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        NBTTagCompound data = tag.getCompoundTag("TileFluidGenerator");
        progress = data.hasKey("progress") ? data.getInteger("progress") : 0;
        isActive = data.hasKey("isActive") && data.getBoolean("isActive");

        tank.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        NBTTagCompound data = new NBTTagCompound();
        data.setInteger("progress", this.progress);
        data.setBoolean("isActive", this.isActive);
        tag.setTag("TileFluidGenerator", data);

        tank.writeToNBT(tag);

        return tag;
    }

    @Override
    public void update() {
        super.update();

//        tank.compareAndUpdate();

        if (world.isRemote) return;

        boolean needsInventoryUpdate = false; // To reduce update frequency of inventories, only call onInventory changed if this was set to true

        if (world.getTotalWorldTime() % 10 == 0) {
            if (!inventory.getStackInSlot(0).isEmpty()) {
                boolean containerDrained = FluidUtils.drainContainers(tank, inventory, 0, 1);
                if (containerDrained) {
                    needsInventoryUpdate = true;
                    tank.compareAndUpdate();
                }
            }
        }

//        if (canWork()) {
//            if (tank.getFluid() != null) {
//                fuelHandler.apply2(fuel, tank.getFluid(), false);
//            }
//        }
//        tank.compareAndUpdate();


        if (tank.getFluid() != null) {
            System.out.println(tank.getFluid().amount);
            tank.drain(1, true);
        }

//        if (progress >= operationLength) {
//            progress = 0;
//
//            if (canWork()) {
//                startWork();
//                needsInventoryUpdate = true;
//
//                if (!isActive) {
//                    isActive = true;
//                    setActive(true);
//                }
//            }  else { // operation conditions not satisfied
//                if (isActive) {
//                    isActive = false;
//                    setActive(false);
//                }
//            }
//        } else {
////            if (operationLength - progress < fuel.getEnergyPerTick()) {
////                if (canWork()) {
////                    startWork();
////                    needsInventoryUpdate = true;
////                }
////            } else {
//                addEnergy(fuel.getEnergyPerTick()); // use energy
//                progress += fuel.getEnergyPerTick(); // update progress
////            }
//        }

        if (needsInventoryUpdate) super.markDirty();
    }
    // << TileEntity

    // TilePowerAcceptor >>
    @Override
    public double getBaseMaxPower() {
        return maxEnergy;
    }

    @Override
    public boolean canAcceptEnergy(final EnumFacing direction) {
        return false;
    }

    @Override
    public boolean canProvideEnergy(final EnumFacing direction) {
        return true;
    }

    @Override
    public double getBaseMaxOutput() {
        return maxOutput;
    }

    @Override
    public double getBaseMaxInput() {
        return 0;
    }
    // << TilePowerAcceptor

    // TileGenericFluidGenerator >>
    protected boolean canWork() {
        // if the tank is empty the generator cannot operate
        if (tank.isEmpty() || tank.getFluid() == null) return false;

        // if the energy buffer is full the generator cannot operate
        if (getEnergy() == maxEnergy) return false;

        if (fuel != null) {
            boolean canUse = fuelHandler.apply2(fuel, tank.getFluid(), true);
            if (!canUse) reset(); // we cannot use the current fuel so reset
        }

        Optional<Fuel> maybeFuel = Optional.ofNullable(fuel);

        // current fuel is still usable
        if (maybeFuel.isPresent()) return canAddEnergy(fuel.getEnergyOutput());

        // The current fuel is not usable anymore so we need to find a new one
        maybeFuel = fuelHandler.findFuel2(tank.getFluid()); // try to find a matching fuel

        if (!maybeFuel.isPresent()) return false; // could not find a fuel

        // if a matching fuel exists update parameters
        updateFuel(maybeFuel.get());

        // we need space for the energy
        return canAddEnergy(fuel.getEnergyOutput());
    }

    protected void startWork() {
        // if the tank is empty the operation cannot be completed
        if (tank.isEmpty() || tank.getFluid() == null) return;

        // if the energy buffer is full the generator cannot operate
        if (getEnergy() == maxEnergy) return;

        // adjust input
        fuelHandler.apply2(fuel, tank.getFluid(), false);

        // update tank
        tank.compareAndUpdate();
    }

    protected void updateFuel(Fuel fuel) {
        operationLength = Math.max((int) fuel.getEnergyOutput(), 1); // set operation length
        this.fuel = fuel; // set fuel
    }

    protected void reset() {
        progress = 0; // reset progress
        operationLength = 0; // reset operation length
        fuel = null; // set current fuel to null
    }

    protected void setActive(boolean value) {
        Block block = world.getBlockState(pos).getBlock();

        if (block instanceof BlockMachineBase)
            ((BlockMachineBase) block).setActive(value, world, pos);
    }
    // << TileGenericFluidGenerator

    // IInventoryProvider >>
    @Override
    public Inventory getInventory() {
        return inventory;
    }
    // << IInventoryProvider

    // IToolDrop >>
    @Override
    public ItemStack getToolDrop(EntityPlayer player) {
        return new ItemStack(drop, 1);
    }
    // << IToolDrop

    // Getters & Setters >>
    @Override
    public Tank getTank() {
        return tank;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getOperationLength() {
        return operationLength;
    }

    public void setOperationLength(int operationLength) {
        this.operationLength = operationLength;
    }

    public int getProgressScaled(int scale) {
        return progress == 0 || operationLength == 0 ? 0 : progress * scale / operationLength;
    }
    // << Getters & Setters

    // Fields >>
    public final String name;
    public final int maxOutput;
    public final int maxEnergy;
    public final Tank tank;
    public final Inventory inventory;
    public final FuelHandler fuelHandler;
    public final Block drop;

    // Slots
    protected final int[] inputSlots;
    protected final int[] outputSlots;

    protected int progress = 0;
    protected int operationLength = 0;
    protected Fuel fuel = null;
    protected boolean isActive = false;
    // << Fields
}