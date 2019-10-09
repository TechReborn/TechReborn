package techreborn.compat.libcd;

import com.google.gson.JsonObject;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.JsonOps;
import io.github.cottonmc.libcd.impl.IngredientAccessUtils;
import net.minecraft.datafixers.NbtOps;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.registry.Registry;
import reborncore.common.crafting.ingredient.IngredientManager;
import reborncore.common.crafting.ingredient.RebornIngredient;

import java.util.Arrays;
import java.util.List;

public class WrappedIngredient extends RebornIngredient {
    private Ingredient underlying;

    public WrappedIngredient(Ingredient underlying) {
        //TODO: PR to RebornCore to not require all this?
        super(IngredientManager.STACK_RECIPE_TYPE);
        this.underlying = underlying;
    }

    @Override
    public boolean test(ItemStack stack) {
        return underlying.test(stack);
    }

    @Override
    public Ingredient getPreview() {
        return underlying;
    }

    @Override
    public List<ItemStack> getPreviewStacks() {
        return Arrays.asList(underlying.getStackArray());
    }

    @Override
    protected JsonObject toJson() {
        //TODO: doesn't currently support tags or ingredients with multiple allowed stacks, needs RebornCore changes
        JsonObject jsonObject = new JsonObject();
        ItemStack mainStack = ((IngredientAccessUtils) (Object) underlying).libcd_getStackArray()[0];
        jsonObject.addProperty("item", Registry.ITEM.getId(mainStack.getItem()).toString());
        if (mainStack.hasTag()) {
            jsonObject.add("nbt", Dynamic.convert(NbtOps.INSTANCE, JsonOps.INSTANCE, mainStack.getTag()));
        }
        return jsonObject;
    }

    @Override
    public int getCount() {
        return underlying.getStackArray().length;
    }
}
