package techreborn.items.tools;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import techreborn.config.ConfigTechReborn;

public class ItemDiamondChainsaw extends ItemChainsaw {

    public ItemDiamondChainsaw() {
    	super(ToolMaterial.EMERALD, "techreborn.diamondChainsaw", ConfigTechReborn.DiamondChainsawCharge, ConfigTechReborn.DiamondChainsawTier, 2.5F);
    	this.cost = 250;
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack stack) {
        return Items.diamond_axe.canHarvestBlock(block, stack);
    }

    @Override
    public String getTextureName(int damage) {
        return "techreborn:items/tool/diamondChainsaw";
    }

    @Override
    public int getMaxMeta() {
        return 1;
    }
}
