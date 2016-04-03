package techreborn.api.recipe;

import net.minecraft.item.ItemStack;
import techreborn.api.Reference;
import techreborn.api.TechRebornAPI;

public class ScrapboxRecipe extends BaseRecipe {

    public ScrapboxRecipe(ItemStack output) {
        super(Reference.scrapboxRecipe, 0, 0);
        inputs.add(new ItemStack(TechRebornAPI.getItem("scrapBox")));
        addOutput(output);
    }

    @Override
    public String getUserFreindlyName() {
        return "Scrapbox";
    }
}
