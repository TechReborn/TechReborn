package ic2.api.event.recipe;

import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.item.*;

@Cancelable
public class RecipeEvent extends Event
{
    final ItemStack input;
    final ItemStack output;
    
    public RecipeEvent(final ItemStack input, final ItemStack output) {
        this.input = input;
        this.output = output;
    }
    
    public final ItemStack getInput() {
        return this.input.copy();
    }
    
    public final ItemStack getOutput() {
        return this.output.copy();
    }
}
