package techreborn.items.tools;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import reborncore.api.IItemTexture;
import reborncore.api.TextureRegistry;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.lib.ModInfo;

public class ItemFluidbucket extends ItemBucket implements IItemTexture {
    private String iconName;

    public ItemFluidbucket(Block block) {
        super(block);
        setContainerItem(Items.bucket);
        setCreativeTab(TechRebornCreativeTabMisc.instance);
        setUnlocalizedName("techreborn.fluidbucket");
        TextureRegistry.registerItem(this);
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

    @Override
    public String getModID() {
        return ModInfo.MOD_ID;
    }

}
