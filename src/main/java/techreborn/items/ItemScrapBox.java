package techreborn.items;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import techreborn.api.ScrapboxList;
import techreborn.client.TechRebornCreativeTabMisc;

public class ItemScrapBox extends ItemTR {
	
	public ItemScrapBox() {
		setUnlocalizedName("techreborn.scrapbox");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if(!world.isRemote) {
			int random = world.rand.nextInt(ScrapboxList.stacks.size());
			ItemStack out = ScrapboxList.stacks.get(random).copy();
			player.addChatComponentMessage(new ChatComponentText("int " + random + " stack " + out.getDisplayName()));
			float xOffset = world.rand.nextFloat() * 0.8F + 0.1F;
			float yOffset = world.rand.nextFloat() * 0.8F + 0.1F;
			float zOffset = world.rand.nextFloat() * 0.8F + 0.1F;
			EntityItem entityitem = new EntityItem(world, player.getPosition().getX() + xOffset, player.getPosition().getY() + yOffset, player.getPosition().getZ() + zOffset, out);

			entityitem.setPickupDelay(20);
			world.spawnEntityInWorld(entityitem);
			
			itemStack.stackSize--;
		}
		return itemStack;
	}
}
