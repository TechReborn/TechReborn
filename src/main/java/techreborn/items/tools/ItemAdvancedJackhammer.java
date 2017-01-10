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

public class ItemAdvancedJackhammer extends ItemJackhammer {

	public ItemAdvancedJackhammer() {
		super(ToolMaterial.IRON, "techreborn.advancedJackhammer", ConfigTechReborn.AdvancedJackhammerCharge,
			ConfigTechReborn.AdvancedJackhammerTier);
		this.cost = 250;
		this.efficiencyOnProperMaterial = 60F;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item,
	                        CreativeTabs par2CreativeTabs, List itemList) {
		ItemStack stack = new ItemStack(ModItems.ADVANCED_JACKHAMMER);
		ItemStack uncharged = stack.copy();
		ItemStack charged = stack.copy();
		PoweredItem.setEnergy(getMaxPower(charged), charged);

		itemList.add(uncharged);
		itemList.add(charged);
	}
}
