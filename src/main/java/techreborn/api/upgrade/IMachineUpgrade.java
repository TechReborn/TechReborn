package techreborn.api.upgrade;

import net.minecraft.item.ItemStack;
import techreborn.utils.RecipeCrafter;

public interface IMachineUpgrade {

    public void processUpgrade(RecipeCrafter crafter, ItemStack stack);
}
