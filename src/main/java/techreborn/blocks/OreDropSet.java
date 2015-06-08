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
	public OreDropSet(OreDrop primary, OreDrop... secondaries)
	{
		this.primary = primary;
		this.secondaries = secondaries;
	}
	
	public ArrayList<ItemStack> drop(int fortune, Random random)
	{
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();

		for (OreDrop drop : secondaries)
		{
			drops.add(drop.getDrops(fortune, random));
		}

		drops.add(primary.getDrops(fortune, random));
		return drops;
	}
	
	public OreDrop primary;
	public OreDrop[] secondaries;
}