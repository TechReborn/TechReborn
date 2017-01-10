package techreborn.items.tools;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import techreborn.Core;
import techreborn.client.EGui;
import techreborn.client.TechRebornCreativeTab;
import techreborn.items.ItemTRNoDestroy;

import java.util.List;

public class ItemTechManual extends ItemTRNoDestroy {

	public ItemTechManual() {
		this.setCreativeTab(TechRebornCreativeTab.instance);
		this.setUnlocalizedName("techreborn.manual");
		this.setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, final World world, final EntityPlayer player,
			final EnumHand hand) {
		player.openGui(Core.INSTANCE, EGui.MANUAL.ordinal(), world, (int) player.posX, (int) player.posY,
				(int) player.posY);
		return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer playerIn, final List<String> tooltip, final boolean advanced) {
		tooltip.add(TextFormatting.RED + I18n.translateToLocal("tooltip.wip"));
	}
}
