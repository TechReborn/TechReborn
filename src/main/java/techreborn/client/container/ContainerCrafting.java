package techreborn.client.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.inventory.ICrafting;
import techreborn.api.recipe.RecipeCrafter;

public abstract class ContainerCrafting extends TechRebornContainer {

    RecipeCrafter crafter;

    int currentTickTime = 0;
    int currentNeededTicks = 0;
    int energy;

    public ContainerCrafting(RecipeCrafter crafter) {
        this.crafter = crafter;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.crafters.size(); i++) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);
            if (this.currentTickTime != crafter.currentTickTime) {
                icrafting.sendProgressBarUpdate(this, 0, crafter.currentTickTime);
            }
            if (this.currentNeededTicks != crafter.currentNeededTicks) {
                icrafting.sendProgressBarUpdate(this, 1, crafter.currentNeededTicks);
            }
            if (this.energy != (int) crafter.energy.getEnergy()) {
                icrafting.sendProgressBarUpdate(this, 2, (int) crafter.energy.getEnergy());
            }
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting crafting) {
        super.addCraftingToCrafters(crafting);
        crafting.sendProgressBarUpdate(this, 0, crafter.currentTickTime);
        crafting.sendProgressBarUpdate(this, 1, crafter.currentNeededTicks);
        crafting.sendProgressBarUpdate(this, 2, (int) crafter.energy.getEnergy());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int id, int value) {
        if (id == 0) {
            this.currentTickTime = value;
        } else if (id == 1) {
            this.currentNeededTicks = value;
        } else if (id == 2) {
            this.energy = value;
        }
        this.crafter.currentTickTime = currentTickTime;
        this.crafter.currentNeededTicks = currentNeededTicks;
        this.crafter.energy.setEnergy(energy);
    }
}
