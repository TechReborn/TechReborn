package ic2.api.event.recipe;

import net.minecraft.item.*;

public abstract class CannerRecipe extends RecipeEvent
{
    int fuel;
    
    public CannerRecipe(final ItemStack input, final int fuel) {
        super(input, input);
        this.fuel = fuel;
    }
    
    public int getFuelValue() {
        return this.fuel;
    }
    
    public static class FuelValue extends CannerRecipe
    {
        public FuelValue(final ItemStack input, final int fuel) {
            super(input, fuel);
        }
    }
    
    public static class FuelMultiplier extends CannerRecipe
    {
        public FuelMultiplier(final ItemStack input, final int percentage) {
            super(input, percentage);
        }
    }
    
    public static class FoodEffect extends CannerRecipe
    {
        public FoodEffect(final ItemStack input, final int meta) {
            super(input, meta);
        }
    }
}
