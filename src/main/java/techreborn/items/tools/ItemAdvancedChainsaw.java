package techreborn.items.tools;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import techreborn.config.ConfigTechReborn;

public class ItemAdvancedChainsaw extends ItemChainsaw {

    public ItemAdvancedChainsaw() {
    	super(ToolMaterial.DIAMOND, "techreborn.advancedChainsaw", ConfigTechReborn.AdvancedChainsawCharge, ConfigTechReborn.AdvancedDrillTier, 4.0F);
    	this.cost = 250;
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {
        return Items.diamond_axe.canHarvestBlock(blockIn);
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
