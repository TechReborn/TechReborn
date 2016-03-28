package techreborn.utils;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import techreborn.init.ModBlocks;

/**
 * Created by Mark on 23/03/2016.
 */
public class StackWIPHandler
{
	ArrayList<Block> wipBlocks = new ArrayList<>();
	public static ArrayList<ItemStack> devHeads = new ArrayList<>();

	public StackWIPHandler()
	{
		wipBlocks.add(ModBlocks.LightningRod);
		wipBlocks.add(ModBlocks.MagicalAbsorber);
		wipBlocks.add(ModBlocks.ChunkLoader);
		wipBlocks.add(ModBlocks.ElectricCraftingTable);
		wipBlocks.add(ModBlocks.playerDetector);
		wipBlocks.add(ModBlocks.chargeBench);
		wipBlocks.add(ModBlocks.playerDetector);
		wipBlocks.add(ModBlocks.Magicenergeyconverter);

		addHead("modmuss50");
		addHead("Gigabit101");
		addHead("ProfProspector");
		addHead("Rushmead");
	}

	private void addHead(String name){
		ItemStack head = new ItemStack(Items.skull, 1, 3);
		head.setTagCompound(new NBTTagCompound());
		head.getTagCompound().setTag("SkullOwner", new NBTTagString(name));
		devHeads.add(head);
	}
	
	@SubscribeEvent
	public void toolTip(ItemTooltipEvent event)
	{
		Block block = Block.getBlockFromItem(event.getItemStack().getItem());
		if (block != null && wipBlocks.contains(block))
		{
			event.getToolTip().add(TextFormatting.RED + "WIP Coming Soon");
		}
		
		if(devHeads.contains(event.getItemStack()))
		{
			event.getToolTip().add(TextFormatting.GOLD + "TechReborn Developer");
		}
	}
}
