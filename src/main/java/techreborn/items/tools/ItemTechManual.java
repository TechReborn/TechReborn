package techreborn.items.tools;

import me.modmuss50.jsonDestroyer.api.ITexturedItem;
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
import techreborn.items.ItemTextureBase;

import java.util.List;

public class ItemTechManual extends ItemTextureBase implements ITexturedItem {

	public ItemTechManual() {
		setCreativeTab(TechRebornCreativeTab.instance);
		setUnlocalizedName("techreborn.manual");
		setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World world, EntityPlayer player,
	                                                EnumHand hand) {
		player.openGui(Core.INSTANCE, GuiHandler.manuelID, world, (int) player.posX, (int) player.posY,
			(int) player.posY);
		return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
	}

	@Override
	public int getMaxMeta() {
		return 1;
	}

	@Override
	public String getTextureName(int damage) {
		return "techreborn:items/tool/manual";
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add(TextFormatting.RED + I18n.translateToLocal("tooltip.wip"));
	}
}
