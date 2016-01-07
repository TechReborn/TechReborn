package ic2.api.recipe;

import net.minecraft.item.*;
import java.util.*;

public interface ICannerBottleRecipeManager
{
    void addRecipe(IRecipeInput p0, IRecipeInput p1, ItemStack p2);
    
    RecipeOutput getOutputFor(ItemStack p0, ItemStack p1, boolean p2, boolean p3);
    
    Map<Input, RecipeOutput> getRecipes();
    
    class Input
    {
        public final IRecipeInput container;
        public final IRecipeInput fill;
        
        public Input(IRecipeInput container1, IRecipeInput fill1) {
            this.container = container1;
            this.fill = fill1;
        }
        
        public boolean matches(ItemStack container1, ItemStack fill1) {
            return this.container.matches(container1) && this.fill.matches(fill1);
        }
    }
}
