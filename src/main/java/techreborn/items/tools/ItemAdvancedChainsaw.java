package techreborn.items.tools;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import techreborn.config.ConfigTechReborn;

public class ItemAdvancedChainsaw extends ItemChainsaw {

    public ItemAdvancedChainsaw() {
    	super(ToolMaterial.EMERALD, "techreborn.advancedChainsaw", ConfigTechReborn.AdvancedChainsawCharge, ConfigTechReborn.AdvancedDrillTier, 4.0F);
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack stack) {
        return Items.diamond_axe.canHarvestBlock(block, stack);
    }

    @Override
    public String getTextureName(int damage) {
        return "techreborn:items/tool/advancedChainsaw";
    }

    @Override
    public int getMaxMeta() {
        return 1;
    }
}
