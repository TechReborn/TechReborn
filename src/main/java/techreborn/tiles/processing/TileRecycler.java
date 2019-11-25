/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.tiles.processing.lv;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import reborncore.api.IToolDrop;
import reborncore.api.scriba.RegisterTile;
import reborncore.api.tile.IInventoryProvider;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;

import techreborn.init.IC2Duplicates;
import techreborn.init.ModBlocks;
import techreborn.items.ingredients.ItemParts;
import techreborn.lib.ModInfo;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @author estebes
 */
@RebornRegistry(modID = ModInfo.MOD_ID)
@RegisterTile(name = "recycler")
public class TileRecycler extends TilePowerAcceptor implements IToolDrop, IInventoryProvider, IContainerProvider {
    // Config >>
    @ConfigRegistry(config = "machines", category = "recycler", key = "RecyclerInput", comment = "Recycler Max Input (Value in EU)")
    public static int maxInput = 32;
    @ConfigRegistry(config = "machines", category = "recycler", key = "RecyclerMaxEnergy", comment = "Recycler Max Energy (Value in EU)")
    public static int maxEnergy = 1000;
    // << Config
    @ConfigRegistry(config = "machines", category = "recycler", key = "produceIC2Scrap", comment = "When enabled and when ic2 is installed the recycler will make ic2 scrap")
    public static boolean produceIC2Scrap = false;

    private final int cost = 2;
    private final int time = 15;
    private final int chance = 6;

    // Constructors >>
//    public TileRecycler(String name, int maxInput, int maxEnergy, int energySlot, int slots, Block drop) {
//        this(name, maxInput, maxEnergy, energySlot, slots, 64, drop);
//    }
//
//    public TileRecycler(String name, int maxInput, int maxEnergy, int energySlot, int slots, int slotSize, Block drop) {
//        this(name, maxInput, maxEnergy, energySlot, slots, slotSize, new int[]{0}, new int[]{1}, drop);
//    }
//
//    public TileRecycler(String name, int maxInput, int maxEnergy, int energySlot, int slots, int slotSize, int[] inputSlots, int[] outputSlots, Block drop) {
    public TileRecycler() {
        this.name = "TileRecycler";
        this.inventory = new Inventory(3, name, 64, this);
//        this.maxInput = maxInput;
//        this.maxEnergy = maxEnergy;
//        this.energySlot = energySlot;
//        this.inventory = new Inventory(slots, name, slotSize, this);
//        this.inputSlots = inputSlots;
//        this.outputSlots = outputSlots;
        this.drop = ModBlocks.RECYCLER;

        checkTeir();
    }
    // << Constructors

    // TileEntity >>
    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        NBTTagCompound data = tag.getCompoundTag("TileRecycler");
        progress = data.hasKey("progress") ? data.getInteger("progress") : 0;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        NBTTagCompound data = new NBTTagCompound();
        data.setInteger("progress", this.progress);
        tag.setTag("TileRecycler", data);

        return tag;
    }

    @Override
    public void update() {
        super.update();

        if (world.isRemote) return;

        charge(energySlot);

        boolean needsInventoryUpdate = false; // To reduce update frequency of inventories, only call onInventory changed if this was set to true

        // operation
        if (canWork()) { // operation conditions satisfied
            if (!active) setActive(true);

            // process start
            if (progress == 0) needsInventoryUpdate = true;

            int progressNeeded = Math.max((int) ((double) operationLength * (1.0D - this.getSpeedMultiplier())), 1);
            if (progress < progressNeeded) {
                useEnergy(getEuPerTick(1.0D)); // use energy
                progress += 1; // update progress
            }

            if (progress >= progressNeeded) { // process end
                finishWork();

                needsInventoryUpdate = true;
            }
        } else { // operation conditions not satisfied
            if (active) setActive(false);
        }

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
        return true;
    }

    @Override
    public boolean canProvideEnergy(final EnumFacing direction) {
        return false;
    }

    @Override
    public double getBaseMaxOutput() {
        return 0;
    }

    @Override
    public double getBaseMaxInput() {
        return maxInput;
    }
    // << TilePowerAcceptor

    // TileMachine >>
    protected boolean canWork() {
        // if there are no inputs the machine cannot operate
        Queue<ItemStack> inputs = new ArrayDeque<>();
        for (int index : inputSlots) {
            ItemStack input = inventory.getStackInSlot(index);
            if (!ItemUtils.isEmpty(input)) inputs.add(input);
        }

        if (inputs.isEmpty()) {
            reset();
            return false;
        }

        // we need to have enough energy
        if (!canUseEnergy(getEuPerTick(1.0D))) return false;

        // we need space for the outputs
        ItemStack scrap = ItemParts.getPartByName("scrap");
        if (produceIC2Scrap && IC2Duplicates.SCRAP.hasIC2Stack())
            scrap = IC2Duplicates.SCRAP.getIc2Stack();

        return addToOutputs(scrap, true) == scrap.getCount();
    }

    protected void finishWork() {
        // if the input is empty the operation cannot be completed
        // if there are no inputs the machine cannot operate
        Queue<ItemStack> inputs = new ArrayDeque<>();
        for (int index : inputSlots) {
            ItemStack input = inventory.getStackInSlot(index);
            if (!ItemUtils.isEmpty(input)) inputs.add(input);
        }

        if (inputs.isEmpty()) return;

        // adjust input
        for (int index : inputSlots)
            inventory.decrStackSize(index, 1);

        // adjust output
        ItemStack scrap = ItemParts.getPartByName("scrap");
        if (produceIC2Scrap && IC2Duplicates.SCRAP.hasIC2Stack())
            scrap = IC2Duplicates.SCRAP.getIc2Stack();

        final int randomchance = this.world.rand.nextInt(chance);
        if (randomchance == 1)
            addToOutputs(scrap, false);

        progress = 0;
    }

    protected void reset() {
        progress = 0; // reset progress
        operationLength = 0; // reset operation length
    }
    // << TileMachine

    // IToolDrop >>
    @Override
    public ItemStack getToolDrop(EntityPlayer player) {
        return new ItemStack(drop, 1);
    }
    // << IToolDrop

    // IInventoryProvider >>
    @Override
    public Inventory getInventory() {
        return inventory;
    }
    // << IInventoryProvider

    // IContainerProvider >>
    @Override
    public BuiltContainer createContainer(final EntityPlayer player) {
        return new ContainerBuilder("recycler")
                .player(player.inventory)
                .inventory()
                .hotbar()
                .addInventory()
                .tile(this)
                .filterSlot(0, 55, 45, stack -> true)
                .outputSlot(1, 101, 45)
                .energySlot(energySlot, 8, 72)
                .syncEnergyValue()
                .syncIntegerValue(this::getProgress, this::setProgress)
                .syncIntegerValue(this::getOperationLength, this::setOperationLength)
                .addInventory()
                .create(this);
    }
    // << IContainerProvider

    // Getters & Setters >>
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

    // Helpers >>
    // Distribute the provided stack across the available output slots
    public int addToOutputs(ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) return 0;

        int remaining = stack.getCount();

        for (int index : outputSlots) {
            if (remaining <= 0) break;

            ItemStack contents = inventory.getStackInSlot(index);

            int transferred = Math.min(remaining, Math.min(inventory.getInventoryStackLimit(), stack.getMaxStackSize()) - contents.getCount());

            if (!contents.isEmpty() && ItemUtils.isItemEqual(contents, stack, true, true)) {
                if (!simulate) inventory.setInventorySlotContents(index, ItemUtils.increaseSize(contents, transferred));

                remaining -= transferred;
            } else if (contents.isEmpty()) {
                if (!simulate) {
                    ItemStack temp = ItemUtils.setSize(stack.copy(), transferred);
                    inventory.setInventorySlotContents(index, temp);
                }

                remaining -= transferred;
            }
        }

        return stack.getCount() - remaining;
    }
    // << Helpers

    // Fields >>
    public final String name;
    //    public final int maxInput;
//    public final int maxEnergy;
    public final Inventory inventory;
    public final Block drop;

    // Slots
    protected final int energySlot = 2;
    protected final int[] inputSlots = new int[]{0};
    protected final int[] outputSlots = new int[]{1};

    protected int progress = 0;
    protected int operationLength = 45;
    // << Fields
}
