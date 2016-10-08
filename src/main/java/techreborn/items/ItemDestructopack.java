package techreborn.items;

import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import reborncore.RebornCore;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTabMisc;

public class ItemDestructopack extends ItemTextureBase implements ITexturedItem {

	public ItemDestructopack() {
		setUnlocalizedName("techreborn.destructopack");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		RebornCore.jsonDestroyer.registerObject(this);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player,
	                                                EnumHand hand) {
		player.openGui(Core.INSTANCE, GuiHandler.destructoPackID, world, (int) player.posX, (int) player.posY,
			(int) player.posY);
		return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
	}

	@Override
	public int getMaxMeta() {
		return 1;
	}

	@Override
	public String getTextureName(int arg0) {
		return "techreborn:items/misc/destructopack";
	}
}
