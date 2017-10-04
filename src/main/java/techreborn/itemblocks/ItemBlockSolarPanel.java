package techreborn.itemblocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import techreborn.blocks.generator.solarpanel.EnumPanelType;

public class ItemBlockSolarPanel extends ItemBlock {

	public ItemBlockSolarPanel(Block block) {
		super(block);
		setHasSubtypes(true);
	}

	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "." + EnumPanelType.values()[stack.getItemDamage()].getName();
	}
}
