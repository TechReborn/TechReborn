package techreborn.api;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlastFurnaceRecipe {
    ItemStack input1, input2;
    ItemStack output1, output2;
    int tickTime;
    int minHeat;

    public BlastFurnaceRecipe(ItemStack input1, ItemStack input2, ItemStack output1, ItemStack output2,int tickTime, int minHeat) {
        this.input1 = input1;
        this.input2 = input2;
        this.output1 = output1;
        this.output2 = output2;
        this.tickTime = tickTime;
        this.minHeat = minHeat;
    }

    public BlastFurnaceRecipe(Item input1, Item input2, int inputAmount, Item output1, net.minecraft.item.Item output2, int tickTime, int minHeat) {
        if(input1 != null)
            this.input1 = new ItemStack(input1, inputAmount);
        if(input2 != null)
            this.input2 = new ItemStack(input1, inputAmount);
        if (output1 != null)
            this.output1 = new ItemStack(output1);
        if (output2 != null)
            this.output2 = new ItemStack(output2);
        this.tickTime = tickTime;
        this.minHeat = minHeat;
    }

    public ItemStack getInput1() {
        return input1;
    }

    public ItemStack getInput2() {
        return input2;
    }

    public ItemStack getOutput1() {
        return output1;
    }

    public ItemStack getOutput2() {
        return output2;
    }

    public int getTickTime() {
        return tickTime;
    }

    public int getMinHeat() {
        return minHeat;
    }
}

