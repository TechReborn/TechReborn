package techreborn.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import reborncore.RebornCore;

import techreborn.Core;
import techreborn.client.EGui;
import techreborn.client.TechRebornCreativeTabMisc;

import me.modmuss50.jsonDestroyer.api.ITexturedItem;

public class ItemDestructopack extends ItemTextureBase implements ITexturedItem {

	public ItemDestructopack() {
		this.setUnlocalizedName("techreborn.destructopack");
		this.setCreativeTab(TechRebornCreativeTabMisc.instance);
		RebornCore.jsonDestroyer.registerObject(this);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, final World world, final EntityPlayer player,
			final EnumHand hand) {
		player.openGui(Core.INSTANCE, EGui.DESTRUCTOPACK.ordinal(), world, (int) player.posX, (int) player.posY,
				(int) player.posY);
		return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public int getMaxMeta() {
		return 1;
	}

	@Override
	public String getTextureName(final int arg0) {
		return "techreborn:items/misc/destructopack";
	}
}
