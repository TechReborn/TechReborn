package techreborn.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import techreborn.client.TechRebornCreativeTab;
import techreborn.lib.ModInfo;

public class ItemTR extends Item {

    public ItemTR() {
        setNoRepair();
        setCreativeTab(TechRebornCreativeTab.instance);
    }

    @Override
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(ModInfo.MOD_ID + ":" + getUnlocalizedName().toLowerCase().substring(5));
    }

}
