package techreborn.items.tools;

import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import reborncore.RebornCore;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.lib.ModInfo;

public class ItemFluidbucket extends ItemBucket implements ITexturedItem {
    private String iconName;

    public ItemFluidbucket(Block block) {
        super(block);
        setContainerItem(Items.bucket);
        setCreativeTab(TechRebornCreativeTabMisc.instance);
        setUnlocalizedName("techreborn.fluidbucket");
        RebornCore.jsonDestroyer.registerObject(this);
    }

    @Override
    public Item setUnlocalizedName(String par1Str) {
        iconName = par1Str;
        return super.setUnlocalizedName(par1Str);
    }

    @Override
    public String getTextureName(int damage) {
        return  ModInfo.MOD_ID + ":items/bucket/" + iconName;
    }

    @Override
    public int getMaxMeta() {
        return 1;
    }
}
