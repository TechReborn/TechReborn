package techreborn.api;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CentrifugeRecipie {
	ItemStack inputItem;
	ItemStack output1, output2, output3, output4;
	int tickTime;

	public CentrifugeRecipie(ItemStack inputItem, ItemStack output1, ItemStack output2, ItemStack output3, ItemStack output4, int tickTime) {
		this.inputItem = inputItem;
		this.output1 = output1;
		this.output2 = output2;
		this.output3 = output3;
		this.output4 = output4;
		this.tickTime = tickTime;
	}

	public CentrifugeRecipie(Item inputItem, int inputAmount, Item output1, Item output2, Item output3, Item output4, int tickTime) {
		this.inputItem = new ItemStack(inputItem, inputAmount);
		this.output1 = new ItemStack(output1);
		this.output2 = new ItemStack(output2);
		this.output3 = new ItemStack(output3);
		this.output4 = new ItemStack(output4);
		this.tickTime = tickTime;
	}

	public ItemStack getInputItem() {
		return inputItem;
	}

	public ItemStack getOutput1() {
		return output1;
	}

	public ItemStack getOutput2() {
		return output2;
	}

	public ItemStack getOutput3() {
		return output3;
	}

	public ItemStack getOutput4() {
		return output4;
	}

	public int getTickTime() {
		return tickTime;
	}
}


