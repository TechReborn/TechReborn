package techreborn.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import techreborn.lib.ModInfo;

import java.util.List;

/**
 * Created by modmuss50 on 17/05/2016.
 */
@Deprecated
public class EmptyCell extends ItemTextureBase {

    public EmptyCell() {
        super();
        setUnlocalizedName("techreborn.cell");
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        tooltip.add("§cDeprecated");
        tooltip.add("§7Place to workbench to get new item");
    }

    @Override
    public String getTextureName(int damage) {
        return ModInfo.MOD_ID + ":items/cell_base";
    }

    @Override
    public int getMaxMeta() {
        return 1;
    }

}
