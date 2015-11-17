package techreborn.compat.minetweaker;

import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techreborn.api.recipe.machines.VacuumFreezerRecipe;
import techreborn.lib.Reference;

@ZenClass("mods.techreborn.vacuumFreezer")
public class MTVacuumFreezer extends MTGeneric {

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient input, int ticktime, int euTick) {
        ItemStack oInput1 = (ItemStack) MinetweakerCompat.toObject(input);

        VacuumFreezerRecipe r = new VacuumFreezerRecipe(oInput1, MinetweakerCompat.toStack(output), ticktime, euTick);
        addRecipe(r);
    }

    @Override
    public  String getMachineName() {
        return Reference.vacuumFreezerRecipe;
    }
}
