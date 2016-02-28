package techreborn.items.tools;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import techreborn.config.ConfigTechReborn;

public class ItemDiamondDrill extends ItemDrill {

    public ItemDiamondDrill() {
    	super(ToolMaterial.EMERALD, "techreborn.diamondDrill", ConfigTechReborn.DiamondDrillCharge, ConfigTechReborn.DiamondDrillTier, 2.5F);
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack stack) {
        return Items.diamond_pickaxe.canHarvestBlock(block, stack) || Items.diamond_shovel.canHarvestBlock(block, stack);
    }

    @Override
    public String getTextureName(int damage) {
        return "techreborn:items/tool/diamondDrill";
    }

    @Override
    public int getMaxMeta() {
        return 1;
    }
}
