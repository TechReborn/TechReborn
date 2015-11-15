package techreborn.compat.minetweaker;

import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techreborn.api.recipe.machines.ChemicalReactorRecipe;
import techreborn.lib.Reference;

@ZenClass("mods.techreborn.chemicalReactorRecipe")
public class MTChemicalReactor extends MTGeneric{
    @ZenMethod
    public static void addRecipe(IItemStack output1, IIngredient input1, IIngredient input2, int ticktime, int euTick) {
        ItemStack oInput1 = (ItemStack) MinetweakerCompat.toObject(input1);
        ItemStack oInput2 = (ItemStack) MinetweakerCompat.toObject(input2);

        ChemicalReactorRecipe r = new ChemicalReactorRecipe(oInput1, oInput2, MinetweakerCompat.toStack(output1), ticktime, euTick);

        addRecipe(r);
    }

    @Override
    public String getMachineName() {
        return Reference.chemicalReactorRecipe;
    }
}
