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

public class ItemAdvancedDrill extends ItemDrill {

	public ItemAdvancedDrill() {
		super(ToolMaterial.DIAMOND, "techreborn.advancedDrill", ConfigTechReborn.AdvancedDrillCharge,
			ConfigTechReborn.AdvancedDrillTier, 4.0F, 20F);
		this.cost = 250;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item,
	                        CreativeTabs par2CreativeTabs, NonNullList itemList) {
		ItemStack stack = new ItemStack(ModItems.advancedDrill);
		ItemStack uncharged = stack.copy();
		ItemStack charged = stack.copy();
		PoweredItem.setEnergy(getMaxPower(charged), charged);

		itemList.add(uncharged);
		itemList.add(charged);
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
		return Items.DIAMOND_PICKAXE.canHarvestBlock(blockIn) || Items.DIAMOND_SHOVEL.canHarvestBlock(blockIn);
	}

	@Override
	public String getTextureName(int damage) {
		return "techreborn:items/tool/advancedDrill";
	}

	@Override
	public int getMaxMeta() {
		return 1;
	}
}
