package techreborn.itemblocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import techreborn.blocks.generator.solarpanel.BlockSolarPanel;
import techreborn.init.ModBlocks;

public class ItemBlockSolarPanel extends ItemBlock {

	public ItemBlockSolarPanel(Block block) {
		super(block);
		setHasSubtypes(true);
	}

	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "." + ModBlocks.SOLAR_PANEL.getStateFromMeta(stack.getItemDamage()).getValue(BlockSolarPanel.TYPE).getName().toLowerCase();
	}
}
