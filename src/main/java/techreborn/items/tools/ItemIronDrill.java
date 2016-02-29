package techreborn.items.tools;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import techreborn.config.ConfigTechReborn;

public class ItemIronDrill extends ItemDrill {

    public ItemIronDrill() {
    	super(ToolMaterial.IRON, "techreborn.ironDrill", ConfigTechReborn.IronDrillCharge, ConfigTechReborn.IronDrillTier, 2.0F);
    	this.cost = 50;
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack stack) {
        return Items.iron_pickaxe.canHarvestBlock(block, stack) || Items.iron_shovel.canHarvestBlock(block, stack);
    }

    @Override
    public String getTextureName(int damage) {
        return "techreborn:items/tool/ironDrill";
    }

    @Override
    public int getMaxMeta() {
        return 1;
    }
}
