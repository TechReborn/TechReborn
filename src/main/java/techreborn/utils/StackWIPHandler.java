package techreborn.utils;

import net.minecraft.block.Block;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import techreborn.init.ModBlocks;

import java.util.ArrayList;

/**
 * Created by Mark on 23/03/2016.
 */
public class StackWIPHandler
{

	ArrayList<Block> wipBlocks = new ArrayList<>();

	public StackWIPHandler()
	{
		wipBlocks.add(ModBlocks.LightningRod);
		wipBlocks.add(ModBlocks.MagicalAbsorber);
		wipBlocks.add(ModBlocks.ChunkLoader);
		wipBlocks.add(ModBlocks.ElectricCraftingTable);
		wipBlocks.add(ModBlocks.playerDetector);
	}

	@SubscribeEvent
	public void toolTip(ItemTooltipEvent event)
	{
		Block block = Block.getBlockFromItem(event.getItemStack().getItem());
		if (block != null && wipBlocks.contains(block))
		{
			event.getToolTip().add(TextFormatting.RED + "WIP Coming Soon");
		}
	}
}
