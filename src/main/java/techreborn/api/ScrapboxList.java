package techreborn.api;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Use this to add items to the scrap box and to check if an item is present
 * there
 */
public class ScrapboxList {

    /**
     * This full list of items that is registered with this api
     */
    public static List<ItemStack> stacks = new ArrayList<>();

    /**
     * Use this to add an item stack to the list
     *
     * @param stack the itemstack you want to add
     */
    public static void addItemStackToList(ItemStack stack) {
        if (!hasItems(stack)) {
            stacks.add(stack);
        }
    }

    /**
     * @param stack the itemstack you want to test
     * @return if the scrapbox can output this this item
     */
    private static boolean hasItems(ItemStack stack) {
        for (ItemStack s : stacks) {
            // TODO why do this!!!!!!!!!
            if (stack.getDisplayName().equals(s.getDisplayName()))
                return true;
        }
        return false;
    }
}
