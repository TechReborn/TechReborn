package techreborn.api.reactor;


import net.minecraft.item.ItemStack;

public class FusionReactorRecipe {

    /**
     * This is the item stack that is required in the top slot
     *
     * This cannot be null
     */
    ItemStack topInput;

    /**
     * This is the item stack that is required in the bottom slot
     *
     * This can be null
     */
    ItemStack bottomInput;

    /**
     * This is the output stack
     *
     * This cannot be null
     */
    ItemStack output;


    /**
     * This is the required eu that has to be in the rector for the reaction to start
     */
    double startEU;
    /**
     * This is the eu that changes every tick, set as a minus number to use power and a positive number to gen power.
     */
    double euTick;

    /**
     * This is the time in ticks that the reaction takes to complete
     */
    int tickTime;

    public FusionReactorRecipe(ItemStack topInput, ItemStack bottomInput, ItemStack output, double startEU, double euTick, int tickTime) {
        this.topInput = topInput;
        this.bottomInput = bottomInput;
        this.output = output;
        this.startEU = startEU;
        this.euTick = euTick;
        this.tickTime = tickTime;
    }


    public ItemStack getTopInput() {
        return topInput;
    }

    public ItemStack getBottomInput() {
        return bottomInput;
    }

    public ItemStack getOutput() {
        return output;
    }

    public double getStartEU() {
        return startEU;
    }

    public double getEuTick() {
        return euTick;
    }

    public int getTickTime() {
        return tickTime;
    }
}
