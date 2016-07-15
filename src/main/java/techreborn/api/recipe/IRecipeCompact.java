package techreborn.api.recipe;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;

public interface IRecipeCompact {

    ImmutableList<ItemStack> getItems(String name);
}
