package techreborn.itemblocks;

import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import reborncore.RebornCore;
import techreborn.client.TechRebornCreativeTabMisc;

/**
 * Created by modmuss50 on 20/02/2016.
 */
public class ItemBlockRubberSapling extends ItemBlock implements ITexturedItem {

    public ItemBlockRubberSapling(Block block) {
        super(block);
        setCreativeTab(TechRebornCreativeTabMisc.instance);
        setUnlocalizedName("techreborn.rubberSapling");
        RebornCore.jsonDestroyer.registerObject(this);
    }

    @Override
    public String getTextureName(int damage) {
        return "techreborn:items/rubber_sapling";
    }

    @Override
    public int getMaxMeta() {
        return 1;
    }
}
