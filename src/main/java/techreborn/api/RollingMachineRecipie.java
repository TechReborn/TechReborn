package techreborn.api;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RollingMachineRecipie {
	ItemStack inputItem1, inputItem2, inputItem3, inputItem4 ,inputItem5, inputItem6,
	inputItem7, inputItem8, inputItem9;
	ItemStack output1;
	int tickTime;
	

	public RollingMachineRecipie(ItemStack inputItem1, ItemStack inputItem2, ItemStack inputItem3, ItemStack inputItem4, 
			ItemStack inputItem5, ItemStack inputItem6, ItemStack inputItem7, ItemStack inputItem8,ItemStack inputItem9,
			ItemStack output1, int tickTime) 
	{
		this.inputItem1 = inputItem1;
		this.inputItem2 = inputItem2;
		this.inputItem3 = inputItem3;
		this.inputItem4 = inputItem4;
		this.inputItem5 = inputItem5;
		this.inputItem6 = inputItem6;
		this.inputItem7 = inputItem7;
		this.inputItem8 = inputItem8;
		this.inputItem9 = inputItem9;
		this.output1 = output1;
		this.tickTime = tickTime;
		
	}

	public RollingMachineRecipie(Item inputItem1,Item inputItem2, Item inputItem3, Item inputItem4, Item inputItem5,
			Item inputItem6, Item inputItem7, Item inputItem8, Item inputItem9, int inputAmount, 
			Item output1, int tickTime) {
		this.inputItem1 = new ItemStack(inputItem1, inputAmount);
		this.inputItem2 = new ItemStack(inputItem2, inputAmount);
		this.inputItem3 = new ItemStack(inputItem3, inputAmount);
		this.inputItem4 = new ItemStack(inputItem4, inputAmount);
		this.inputItem5 = new ItemStack(inputItem5, inputAmount);
		this.inputItem6 = new ItemStack(inputItem6, inputAmount);
		this.inputItem7 = new ItemStack(inputItem7, inputAmount);
		this.inputItem8 = new ItemStack(inputItem8, inputAmount);
		this.inputItem9 = new ItemStack(inputItem9, inputAmount);

		this.output1 = new ItemStack(output1);
		this.tickTime = tickTime;
		;
	}

	public RollingMachineRecipie(RollingMachineRecipie rollingmachineRecipie)
	{
		this.inputItem1 = rollingmachineRecipie.getInputItem1();
		this.inputItem2 = rollingmachineRecipie.getInputItem2();
		this.inputItem3 = rollingmachineRecipie.getInputItem3();
		this.inputItem4 = rollingmachineRecipie.getInputItem4();
		this.inputItem5 = rollingmachineRecipie.getInputItem5();
		this.inputItem6 = rollingmachineRecipie.getInputItem6();
		this.inputItem7 = rollingmachineRecipie.getInputItem7();
		this.inputItem8 = rollingmachineRecipie.getInputItem8();
		this.inputItem9 = rollingmachineRecipie.getInputItem9();

		this.output1 = rollingmachineRecipie.getOutput1();
		this.tickTime = rollingmachineRecipie.getTickTime();
	}

	public ItemStack getInputItem1() 
	{
		return inputItem1;
	}
	public ItemStack getInputItem2() 
	{
		return inputItem2;
	}
	public ItemStack getInputItem3() 
	{
		return inputItem3;
	}
	public ItemStack getInputItem4() 
	{
		return inputItem4;
	}
	public ItemStack getInputItem5() 
	{
		return inputItem5;
	}
	public ItemStack getInputItem6() 
	{
		return inputItem6;
	}
	public ItemStack getInputItem7() 
	{
		return inputItem7;
	}
	public ItemStack getInputItem8() 
	{
		return inputItem8;
	}
	public ItemStack getInputItem9()
	{
		return inputItem9;
	}

	public ItemStack getOutput1() 
	{
		return output1;
	}

	public int getTickTime()
	{
		return tickTime;
	}

	
}


