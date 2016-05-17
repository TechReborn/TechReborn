package techreborn.items;

import techreborn.client.TechRebornCreativeTab;
import techreborn.lib.ModInfo;

/**
 * Created by modmuss50 on 17/05/2016.
 */
public class EmptyCell extends ItemTextureBase {

    public EmptyCell()
    {
        setUnlocalizedName("techreborn.cell");
        setCreativeTab(TechRebornCreativeTab.instance);
    }
    @Override
    public String getTextureName(int damage) {
        return ModInfo.MOD_ID + ":items/cell_base";
    }

    @Override
    public int getMaxMeta() {
        return 0;
    }
}
