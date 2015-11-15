package techreborn.compat.minetweaker;

import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techreborn.api.recipe.machines.PlateCuttingMachineRecipe;
import techreborn.lib.Reference;

@ZenClass("mods.techreborn.plateCuttingMachine")
public class MTPlateCuttingMachine extends MTGeneric {

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient input1, int ticktime, int euTick) {
        ItemStack oInput1 = (ItemStack) MinetweakerCompat.toObject(input1);
        PlateCuttingMachineRecipe r = new PlateCuttingMachineRecipe(oInput1, MinetweakerCompat.toStack(output), ticktime, euTick);

        addRecipe(r);
    }

    @Override
    public String getMachineName() {
        return Reference.plateCuttingMachineRecipe;
    }
}
