package techreborn.items.tools;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.powerSystem.PoweredItem;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModItems;

public class ItemDiamondDrill extends ItemDrill {

	public ItemDiamondDrill() {
		super(ToolMaterial.DIAMOND, "techreborn.diamondDrill", ConfigTechReborn.DiamondDrillCharge,
			ConfigTechReborn.DiamondDrillTier, 0.5F, 15F);
		this.cost = 250;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item,
	                        CreativeTabs par2CreativeTabs, NonNullList itemList) {
		ItemStack stack = new ItemStack(ModItems.diamondDrill);
		ItemStack uncharged = stack.copy();
		ItemStack charged = stack.copy();
		PoweredItem.setEnergy(getMaxPower(charged), charged);

		itemList.add(uncharged);
		itemList.add(charged);
	}

	@Override
	public boolean canHarvestBlock(IBlockState state) {
		return Items.DIAMOND_PICKAXE.canHarvestBlock(state) || Items.DIAMOND_SHOVEL.canHarvestBlock(state);
	}

	@Override
	public String getTextureName(int damage) {
		return "techreborn:items/tool/diamondDrill";
	}

	@Override
	public int getMaxMeta() {
		return 1;
	}
}
