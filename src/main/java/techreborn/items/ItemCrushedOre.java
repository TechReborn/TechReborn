package techreborn.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.init.ModItems;
import techreborn.lib.ModInfo;

import java.security.InvalidParameterException;
import java.util.List;

public class ItemCrushedOre extends ItemMetaBase {
    public static ItemStack getCrushedOreByName(String name, int count) {
        for (int i = 0; i < types.length; i++) {
            if (types[i].equalsIgnoreCase(name)) {
                return new ItemStack(ModItems.crushedOre, count, i);
            }
        }
        throw new InvalidParameterException("The gem " + name + " could not be found.");
    }

    public static ItemStack getCrushedOreByName(String name) {
        return getCrushedOreByName(name, 1);
    }

    public static final String[] types = new String[]
            {"Aluminum", "Ardite", "Bauxite", "Cadmium", "Cinnabar", "Cobalt", "DarkIron",
                    "Indium", "Iridium", "Nickel", "Osmium", "Platinum",
                    "Pyrite", "Sphalerite", "Tetrahedrite", "Tungsten", "Galena"};


    public ItemCrushedOre() {
        setCreativeTab(TechRebornCreativeTabMisc.instance);
        setHasSubtypes(true);
        setUnlocalizedName("techreborn.crushedore");
    }


    @Override
    // gets Unlocalized Name depending on meta data
    public String getUnlocalizedName(ItemStack itemStack) {
        int meta = itemStack.getItemDamage();
        if (meta < 0 || meta >= types.length) {
            meta = 0;
        }

        return super.getUnlocalizedName() + "." + types[meta];
    }

    // Adds Dusts SubItems To Creative Tab
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        for (int meta = 0; meta < types.length; ++meta) {
            list.add(new ItemStack(item, 1, meta));
        }
    }

    @Override
    public String getTextureName(int damage) {
        return ModInfo.MOD_ID + ":items/crushedOre/crushed" + types[damage] + "Ore";
    }

    @Override
    public int getMaxMeta() {
        return types.length;
    }

}
