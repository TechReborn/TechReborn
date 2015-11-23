package techreborn.items.tools;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import techreborn.items.ItemTR;

public class ItemHammer extends ItemTR {
    private String iconName;

    public ItemHammer(int MaxDamage) {
        setUnlocalizedName("techreborn.hammer");
        setMaxDamage(MaxDamage);
    }

    @Override
    public Item setUnlocalizedName(String par1Str) {
        iconName = par1Str;
        return super.setUnlocalizedName(par1Str);
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

//    @Override //TODO
//    public boolean doesContainerItemLeaveCraftingGrid(ItemStack itemStack) {
//        return false;
//    }



    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack copiedStack = itemStack.copy();

        copiedStack.setItemDamage(copiedStack.getItemDamage() + 1);
        copiedStack.stackSize = 1;

        return copiedStack;
    }

}
