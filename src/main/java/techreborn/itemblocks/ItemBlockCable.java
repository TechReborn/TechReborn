package techreborn.itemblocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import techreborn.blocks.cable.EnumCableType;

/**
 * Created by modmuss50 on 12/06/2017.
 */
public class ItemBlockCable extends ItemBlock {

	public ItemBlockCable(Block block) {
		super(block);
	}

	public int getMetadata(int damage)
	{
		return damage;
	}


	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName() + "." + EnumCableType.values()[stack.getItemDamage()].getName();
	}
}
