package techreborn.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import techreborn.client.TechRebornCreativeTabMisc;

public class ItemUUmatter extends Item {

    public ItemUUmatter() {
        setCreativeTab(TechRebornCreativeTabMisc.instance);
        setUnlocalizedName("techreborn.uuMatter");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("techreborn:" + "misc/itemMatter");
    }
}
