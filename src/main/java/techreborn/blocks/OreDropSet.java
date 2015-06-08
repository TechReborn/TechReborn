package techreborn.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import techreborn.init.ModBlocks;
import techreborn.config.ConfigTechReborn;
import net.minecraft.block.Block;

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