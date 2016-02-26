package techreborn.api.upgrade;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import reborncore.common.util.Inventory;
import techreborn.api.recipe.RecipeCrafter;

public class UpgradeHandler {

    RecipeCrafter crafter;

    Inventory inventory;

    ArrayList<Integer> slots = new ArrayList<Integer>();

    public UpgradeHandler(RecipeCrafter crafter, Inventory inventory, int... slots) {
        this.crafter = crafter;
        this.inventory = inventory;
        for (int slot : slots) {
            this.slots.add(slot);
        }
    }

    public void tick() {
        if (crafter.parentTile.getWorld().isRemote)
            return;
        crafter.resetPowerMulti();
        crafter.resetSpeedMulti();
        for (int slot : this.slots) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (stack != null && stack.getItem() instanceof IMachineUpgrade) {
                ((IMachineUpgrade) stack.getItem()).processUpgrade(crafter, stack);
            }
        }
        if (crafter.currentRecipe != null)
            crafter.currentNeededTicks = (int) (crafter.currentRecipe.tickTime() * (1.0 - crafter.getSpeedMultiplier()));
    }
}
