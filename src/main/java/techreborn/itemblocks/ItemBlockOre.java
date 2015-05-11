package techreborn.itemblocks;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import techreborn.achievement.ICraftAchievement;
import techreborn.achievement.IPickupAchievement;
import techreborn.achievement.TRAchievements;
import techreborn.blocks.BlockOre;
import techreborn.init.ModBlocks;

public class ItemBlockOre extends ItemBlockBase {

	public ItemBlockOre(Block block)
	{
		super(ModBlocks.ore, ModBlocks.ore, BlockOre.types);
	}
	
}
