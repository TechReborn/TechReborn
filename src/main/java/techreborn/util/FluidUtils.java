package techreborn.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.oredict.OreDictionary;

public class FluidUtils {

    public static boolean drainContainers(IFluidHandler fluidHandler, IInventory inv, int inputSlot, int outputSlot) {
        ItemStack input = inv.getStackInSlot(inputSlot);
        ItemStack output = inv.getStackInSlot(outputSlot);

        if (input != null) {
            FluidStack fluidInContainer = getFluidStackInContainer(input);
            ItemStack emptyItem = input.getItem().getContainerItem(input);
            if (fluidInContainer != null && (emptyItem == null || output == null || (output.stackSize < output.getMaxStackSize() && isItemEqual(output, emptyItem, true, true)))) {
                int used = fluidHandler.fill(ForgeDirection.UNKNOWN, fluidInContainer, false);
                if (used >= fluidInContainer.amount) {
                    fluidHandler.fill(ForgeDirection.UNKNOWN, fluidInContainer, true);
                    if (emptyItem != null)
                        if (output == null)
                            inv.setInventorySlotContents(outputSlot, emptyItem);
                        else
                            output.stackSize++;
                    inv.decrStackSize(inputSlot, 1);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean fillContainers(IFluidHandler fluidHandler, IInventory inv, int inputSlot, int outputSlot, Fluid fluidToFill) {
        ItemStack input = inv.getStackInSlot(inputSlot);
        ItemStack output = inv.getStackInSlot(outputSlot);
        ItemStack filled = getFilledContainer(fluidToFill, input);
        if (filled != null && (output == null || (output.stackSize < output.getMaxStackSize() && isItemEqual(filled, output, true, true)))) {
            FluidStack fluidInContainer = getFluidStackInContainer(filled);
            FluidStack drain = fluidHandler.drain(ForgeDirection.UNKNOWN, fluidInContainer, false);
            if (drain != null && drain.amount == fluidInContainer.amount) {
                fluidHandler.drain(ForgeDirection.UNKNOWN, fluidInContainer, true);
                if (output == null)
                    inv.setInventorySlotContents(outputSlot, filled);
                else
                    output.stackSize++;
                inv.decrStackSize(inputSlot, 1);
                return true;
            }
        }
        return false;
    }

    public static FluidStack getFluidStackInContainer(ItemStack stack) {
        return FluidContainerRegistry.getFluidForFilledItem(stack);
    }

    public static ItemStack getFilledContainer(Fluid fluid, ItemStack empty) {
        if (fluid == null || empty == null) return null;
        return FluidContainerRegistry.fillFluidContainer(new FluidStack(fluid, Integer.MAX_VALUE), empty);
    }

    public static boolean isItemEqual(final ItemStack a, final ItemStack b, final boolean matchDamage, final boolean matchNBT) {
        if (a == null || b == null)
            return false;
        if (a.getItem() != b.getItem())
            return false;
        if (matchNBT && !ItemStack.areItemStackTagsEqual(a, b))
            return false;
        if (matchDamage && a.getHasSubtypes()) {
            if (isWildcard(a) || isWildcard(b))
                return true;
            if (a.getItemDamage() != b.getItemDamage())
                return false;
        }
        return true;
    }

    public static boolean isWildcard(ItemStack stack) {
        return isWildcard(stack.getItemDamage());
    }

    public static boolean isWildcard(int damage) {
        return damage == -1 || damage == OreDictionary.WILDCARD_VALUE;
    }
}
