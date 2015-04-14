package techreborn.api;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CentrifugeRecipie {
	ItemStack inputItem;
	ItemStack output1, output2, output3, output4;
	int tickTime;
	int cells;

	public CentrifugeRecipie(ItemStack inputItem, ItemStack output1, ItemStack output2, ItemStack output3, ItemStack output4, int tickTime, int cells) {
		this.inputItem = inputItem;
		this.output1 = output1;
		this.output2 = output2;
		this.output3 = output3;
		this.output4 = output4;
		this.tickTime = tickTime;
		this.cells = cells;
	}

	public CentrifugeRecipie(Item inputItem, int inputAmount, Item output1, Item output2, Item output3, Item output4, int tickTime, int cells) {
		this.inputItem = new ItemStack(inputItem, inputAmount);
		if(output1!= null)
			this.output1 = new ItemStack(output1);
		if(output2!= null)
			this.output2 = new ItemStack(output2);
		if(output3!= null)
			this.output3 = new ItemStack(output3);
		if(output4!= null)
			this.output4 = new ItemStack(output4);
		this.tickTime = tickTime;
		this.cells = cells;
	}

	public CentrifugeRecipie(CentrifugeRecipie centrifugeRecipie){
		this.inputItem = centrifugeRecipie.getInputItem();
		this.output1 = centrifugeRecipie.getOutput1();
		this.output2 = centrifugeRecipie.getOutput2();
		this.output3 = centrifugeRecipie.getOutput3();
		this.output4 = centrifugeRecipie.getOutput4();
		this.tickTime = centrifugeRecipie.getTickTime();
		this.cells = centrifugeRecipie.getCells();
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

	public int getCells() {
		return cells;
	}
}


