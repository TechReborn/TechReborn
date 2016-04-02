package techreborn.utils;

import ic2.api.item.IC2Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import techreborn.items.ItemCells;


public class RecipeUtils {
    public static ItemStack getEmptyCell(int stackSize) {
        if (Loader.isModLoaded("IC2")) {
            ItemStack cell = IC2Items.getItem("fluid_cell").copy();
            cell.stackSize = stackSize;
            return cell;
        } else {
            return ItemCells.getCellByName("empty", stackSize);
        }
    }
}
