package techreborn.items.tools;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import techreborn.config.ConfigTechReborn;

public class ItemAdvancedDrill extends ItemDrill
{

	public ItemAdvancedDrill()
	{
		super(ToolMaterial.DIAMOND, "techreborn.advancedDrill", ConfigTechReborn.AdvancedDrillCharge,
				ConfigTechReborn.AdvancedDrillTier, 4.0F, 20F);
		this.cost = 250;
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn)
	{
		return Items.diamond_pickaxe.canHarvestBlock(blockIn) || Items.diamond_shovel.canHarvestBlock(blockIn);
	}

	@Override
	public String getTextureName(int damage)
	{
		return "techreborn:items/tool/advancedDrill";
	}

	@Override
	public int getMaxMeta()
	{
		return 1;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) 
	{
		tooltip.add(TextFormatting.RED + "WIP Coming Soon");
	}
}
