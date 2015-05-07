package techreborn.api;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AlloySmelterRecipe {
    ItemStack input1, input2;
    ItemStack output1;
    int tickTime;
    int minHeat;

    public AlloySmelterRecipe(ItemStack input1, ItemStack input2, ItemStack output1 ,int tickTime) {
        this.input1 = input1;
        this.input2 = input2;
        this.output1 = output1;
        this.tickTime = tickTime;
    }

    public AlloySmelterRecipe(Item input1, Item input2, int inputAmount, Item output1, int tickTime) {
        if(input1 != null)
            this.input1 = new ItemStack(input1, inputAmount);
        if(input2 != null)
            this.input2 = new ItemStack(input1, inputAmount);
        if (output1 != null)
            this.output1 = new ItemStack(output1);
        this.tickTime = tickTime;
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

    public int getTickTime() {
        return tickTime;
    }
}

