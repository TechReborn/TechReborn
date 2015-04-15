package techreborn.api;

import techreborn.util.ItemUtils;

import java.util.ArrayList;

public final class TechRebornAPI {

    public static ArrayList<CentrifugeRecipie> centrifugeRecipies = new ArrayList<CentrifugeRecipie>();
    public static ArrayList<RollingMachineRecipie> rollingmachineRecipes = new ArrayList<RollingMachineRecipie>();


    public static void registerCentrifugeRecipe(CentrifugeRecipie recipie) {
        boolean shouldAdd = true;
        for (CentrifugeRecipie centrifugeRecipie : centrifugeRecipies) {
            if (ItemUtils.isItemEqual(centrifugeRecipie.getInputItem(), recipie.getInputItem(), false, true)) {
                try {
                    throw new RegisteredItemRecipe("Item " + recipie.getInputItem().getUnlocalizedName() + " is already being used in a recipe for the Centrifuge");
                } catch (RegisteredItemRecipe registeredItemRecipe) {
                    registeredItemRecipe.printStackTrace();
                    shouldAdd = false;
                }
            }
        }
        if (shouldAdd)
            centrifugeRecipies.add(recipie);
    }

    public static void registerRollingMachineRecipe(RollingMachineRecipie recipie) {
        boolean shouldAdd = true;
        for (CentrifugeRecipie centrifugeRecipie : centrifugeRecipies) {
            if (ItemUtils.isItemEqual(centrifugeRecipie.getInputItem(), recipie.getInputItem1(), false, true)) {
                try {
                    throw new RegisteredItemRecipe("Item " + recipie.getInputItem1().getUnlocalizedName() + " is already being used in a recipe for the RollingMachine");
                } catch (RegisteredItemRecipe registeredItemRecipe) {
                    registeredItemRecipe.printStackTrace();
                    shouldAdd = false;
                }
            }
        }
        if (shouldAdd)
            rollingmachineRecipes.add(recipie);
    }
}


class RegisteredItemRecipe extends Exception {
    public RegisteredItemRecipe(String message) {
        super(message);
    }
}
