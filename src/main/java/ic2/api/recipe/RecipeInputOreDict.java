package ic2.api.recipe;

import net.minecraft.item.*;
import java.util.*;
import net.minecraftforge.oredict.*;

public class RecipeInputOreDict implements IRecipeInput
{
    public final String input;
    public final int amount;
    public final Integer meta;
    private List<ItemStack> ores;
    
    public RecipeInputOreDict(final String input1) {
        this(input1, 1);
    }
    
    public RecipeInputOreDict(final String input1, final int amount1) {
        this(input1, amount1, null);
    }
    
    public RecipeInputOreDict(final String input1, final int amount1, final Integer meta) {
        this.input = input1;
        this.amount = amount1;
        this.meta = meta;
    }
    
    @Override
    public boolean matches(final ItemStack subject) {
        final List<ItemStack> inputs = this.getOres();
        final boolean useOreStackMeta = this.meta == null;
        final Item subjectItem = subject.getItem();
        final int subjectMeta = subject.getItemDamage();
        for (final ItemStack oreStack : inputs) {
            final Item oreItem = oreStack.getItem();
            if (oreItem == null) {
                continue;
            }
            final int metaRequired = useOreStackMeta ? oreStack.getItemDamage() : this.meta;
            if (subjectItem == oreItem && (subjectMeta == metaRequired || metaRequired == 32767)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int getAmount() {
        return this.amount;
    }
    
    @Override
    public List<ItemStack> getInputs() {
        final List<ItemStack> ores = this.getOres();
        boolean hasInvalidEntries = false;
        for (final ItemStack stack : ores) {
            if (stack.getItem() == null) {
                hasInvalidEntries = true;
                break;
            }
        }
        if (!hasInvalidEntries) {
            return ores;
        }
        final List<ItemStack> ret = new ArrayList<ItemStack>(ores.size());
        for (final ItemStack stack2 : ores) {
            if (stack2.getItem() != null) {
                ret.add(stack2);
            }
        }
        return Collections.unmodifiableList((List<? extends ItemStack>)ret);
    }
    
    @Override
    public String toString() {
        if (this.meta == null) {
            return "RInputOreDict<" + this.amount + "x" + this.input + ">";
        }
        return "RInputOreDict<" + this.amount + "x" + this.input + "@" + this.meta + ">";
    }
    
    private List<ItemStack> getOres() {
        if (this.ores != null) {
            return this.ores;
        }
        final List<ItemStack> ret = (List<ItemStack>)OreDictionary.getOres(this.input);
        if (ret != OreDictionary.EMPTY_LIST) {
            this.ores = ret;
        }
        return ret;
    }
}
