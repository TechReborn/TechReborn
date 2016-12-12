package techreborn.itemblocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import techreborn.client.TechRebornCreativeTabMisc;

/**
 * Created by modmuss50 on 20/02/2016.
 */
public class ItemBlockRubberSapling extends ItemBlock {

	public ItemBlockRubberSapling(Block block) {
		super(block);
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		setUnlocalizedName("techreborn.rubberSapling");
	}
}
