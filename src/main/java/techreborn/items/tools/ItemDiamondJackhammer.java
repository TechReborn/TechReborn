package techreborn.items.tools;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.powerSystem.PoweredItem;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModItems;

public class ItemDiamondJackhammer extends ItemJackhammer {

	public ItemDiamondJackhammer() {
		super(ToolMaterial.DIAMOND, "techreborn.diamondJackhammer", ConfigTechReborn.DiamondJackhammerCharge,
			ConfigTechReborn.DiamondJackhammerTier);
		this.cost = 100;
		this.efficiencyOnProperMaterial = 16F;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item,
	                        CreativeTabs par2CreativeTabs, List itemList) {
		ItemStack stack = new ItemStack(ModItems.DIAMOND_JACKHAMMER);
		ItemStack uncharged = stack.copy();
		ItemStack charged = stack.copy();
		PoweredItem.setEnergy(getMaxPower(charged), charged);

		itemList.add(uncharged);
		itemList.add(charged);
	}
}
