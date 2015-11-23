package techreborn.items.tools;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import techreborn.client.TechRebornCreativeTabMisc;

public class ItemFluidbucket extends ItemBucket {
    private String iconName;

    public ItemFluidbucket(Block block) {
        super(block);
        setContainerItem(Items.bucket);
        setCreativeTab(TechRebornCreativeTabMisc.instance);
        setUnlocalizedName("techreborn.fluidbucket");
    }

    @Override
    public Item setUnlocalizedName(String par1Str) {
        iconName = par1Str;
        return super.setUnlocalizedName(par1Str);
    }


}
