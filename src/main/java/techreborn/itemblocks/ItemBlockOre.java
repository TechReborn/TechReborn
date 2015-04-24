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
import techreborn.blocks.BlockOre;
import techreborn.init.ModBlocks;

public class ItemBlockOre extends ItemMultiTexture implements
		IPickupAchievement, ICraftAchievement {

	public ItemBlockOre(Block block)
	{
		super(ModBlocks.ore, ModBlocks.ore, BlockOre.types);
	}

	@Override
	public Achievement getAchievementOnCraft(ItemStack stack,
			EntityPlayer player, IInventory matrix)
	{
		return field_150939_a instanceof ICraftAchievement ? ((ICraftAchievement) field_150939_a)
				.getAchievementOnCraft(stack, player, matrix) : null;
	}

	@Override
	public Achievement getAchievementOnPickup(ItemStack stack,
			EntityPlayer player, EntityItem item)
	{
		return field_150939_a instanceof IPickupAchievement ? ((IPickupAchievement) field_150939_a)
				.getAchievementOnPickup(stack, player, item) : null;
	}

}
