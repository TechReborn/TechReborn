package techreborn.items;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import reborncore.RebornCore;
import techreborn.api.ScrapboxList;
import techreborn.client.TechRebornCreativeTabMisc;

public class ItemScrapBox extends ItemTRNoDestroy {

	public ItemScrapBox() {
		setUnlocalizedName("techreborn.scrapbox");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		RebornCore.jsonDestroyer.registerObject(this);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player,
	                                                EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (!world.isRemote) {
			int random = world.rand.nextInt(ScrapboxList.stacks.size());
			ItemStack out = ScrapboxList.stacks.get(random).copy();
			float xOffset = world.rand.nextFloat() * 0.8F + 0.1F;
			float yOffset = world.rand.nextFloat() * 0.8F + 0.1F;
			float zOffset = world.rand.nextFloat() * 0.8F + 0.1F;
			EntityItem entityitem = new EntityItem(world, player.getPosition().getX() + xOffset,
				player.getPosition().getY() + yOffset, player.getPosition().getZ() + zOffset, out);

			entityitem.setPickupDelay(20);
			world.spawnEntity(entityitem);
			stack.shrink(1);
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}
}
