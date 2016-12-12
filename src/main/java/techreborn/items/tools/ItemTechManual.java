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
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.items.ItemTRNoDestroy;

import java.util.List;

public class ItemTechManual extends ItemTRNoDestroy {

	public ItemTechManual() {
		setCreativeTab(TechRebornCreativeTab.instance);
		setUnlocalizedName("techreborn.manual");
		setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player,
	                                                EnumHand hand) {
		player.openGui(Core.INSTANCE, GuiHandler.manuelID, world, (int) player.posX, (int) player.posY,
			(int) player.posY);
		return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add(TextFormatting.RED + I18n.translateToLocal("tooltip.wip"));
	}
}
