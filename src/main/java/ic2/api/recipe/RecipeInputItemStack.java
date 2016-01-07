package ic2.api.recipe;

import net.minecraft.item.*;
import java.util.*;

public class RecipeInputItemStack implements IRecipeInput
{
    public final ItemStack input;
    public final int amount;
    
    public RecipeInputItemStack(final ItemStack aInput) {
        this(aInput, aInput.stackSize);
    }
    
    public RecipeInputItemStack(final ItemStack aInput, final int aAmount) {
        if (aInput.getItem() == null) {
            throw new IllegalArgumentException("Invalid item stack specfied");
        }
        this.input = aInput.copy();
        this.amount = aAmount;
    }
    
    @Override
    public boolean matches(final ItemStack subject) {
        return subject.getItem() == this.input.getItem() && (subject.getItemDamage() == this.input.getItemDamage() || this.input.getItemDamage() == 32767);
    }
    
    @Override
    public int getAmount() {
        return this.amount;
    }
    
    @Override
    public List<ItemStack> getInputs() {
        return Arrays.asList(this.input);
    }
    
    @Override
    public String toString() {
        final ItemStack stack = this.input.copy();
        this.input.stackSize = this.amount;
        return "RInputItemStack<" + stack + ">";
    }
}
