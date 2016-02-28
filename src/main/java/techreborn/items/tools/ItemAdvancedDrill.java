package techreborn.items.tools;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import techreborn.config.ConfigTechReborn;

public class ItemAdvancedDrill extends ItemDrill {

    public ItemAdvancedDrill() {
    	super(ToolMaterial.EMERALD, "techreborn.advancedDrill", ConfigTechReborn.AdvancedDrillCharge, ConfigTechReborn.AdvancedDrillTier, 4.0F);
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack stack) {
        return Items.diamond_pickaxe.canHarvestBlock(block, stack) || Items.diamond_shovel.canHarvestBlock(block, stack);
    }

    @Override
    public String getTextureName(int damage) {
        return "techreborn:items/tool/advancedDrill";
    }

    @Override
    public int getMaxMeta() {
        return 1;
    }
}
