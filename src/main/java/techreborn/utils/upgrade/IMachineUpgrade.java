package techreborn.utils.upgrade;

import net.minecraft.item.ItemStack;
import reborncore.common.recipes.RecipeCrafter;

public interface IMachineUpgrade {

    public void processUpgrade(RecipeCrafter crafter, ItemStack stack);
}
