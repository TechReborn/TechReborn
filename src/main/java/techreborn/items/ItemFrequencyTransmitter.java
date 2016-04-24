package techreborn.items;

import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import reborncore.RebornCore;
import techreborn.client.TechRebornCreativeTabMisc;

public class ItemFrequencyTransmitter extends ItemTextureBase implements ITexturedItem {

	public ItemFrequencyTransmitter() {
		setUnlocalizedName("techreborn.frequencyTransmitter");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		RebornCore.jsonDestroyer.registerObject(this);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player,
			EnumHand hand) {
				return null;
	}

	@Override
	public int getMaxMeta() {
		return 1;
	}

	@Override
	public String getTextureName(int arg0) {
		return "techreborn:items/misc/frequency_transmitter";
	}
}
