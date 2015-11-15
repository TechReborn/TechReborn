package techreborn.compat.minetweaker;

import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techreborn.api.recipe.machines.IndustrialElectrolyzerRecipe;
import techreborn.lib.Reference;

@ZenClass("mods.techreborn.industrialElectrolyzer")
public class MTIndustrialElectrolyzer extends MTGeneric {
    @ZenMethod
    public static void addRecipe(IItemStack output1, IItemStack output2, IItemStack output3, IItemStack output4, IIngredient cells, IIngredient input2, int ticktime, int euTick) {
        ItemStack oInput1 = (ItemStack) MinetweakerCompat.toObject(cells);
        ItemStack oInput2 = (ItemStack) MinetweakerCompat.toObject(input2);

        IndustrialElectrolyzerRecipe r = new IndustrialElectrolyzerRecipe(oInput1, oInput2, MinetweakerCompat.toStack(output1), MinetweakerCompat.toStack(output2), MinetweakerCompat.toStack(output3), MinetweakerCompat.toStack(output4), ticktime, euTick);

        addRecipe(r);
    }

    @Override
    public String getMachineName() {
        return Reference.industrialElectrolyzerRecipe;
    }
}
