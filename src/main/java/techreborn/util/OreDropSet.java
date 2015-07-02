package techreborn.util;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class OreDropSet
{
	public OreDropSet(OreDrop... oreDrops)
	{
		this.dropSet = oreDrops;
	}
	
	public ArrayList<ItemStack> drop(int fortune, Random random)
	{
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();

		for (OreDrop drop : dropSet)
		{
			drops.add(drop.getDrops(fortune, random));
		}

		return drops;
	}

	public OreDrop[] dropSet;
}