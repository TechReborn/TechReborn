/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.compat.libcd;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.cottonmc.libcd.api.CDSyntaxError;
import io.github.cottonmc.libcd.api.tweaker.util.TweakerUtils;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import reborncore.common.crafting.ingredient.*;
import reborncore.common.fluid.FluidValue;
import reborncore.common.fluid.container.FluidInstance;

import java.util.Collections;
import java.util.Optional;

public class TRRecipeParser {

    public static RebornIngredient processIngredient(Object input) throws CDSyntaxError {
        if (input instanceof RebornIngredient) {
            return (RebornIngredient) input;
        } else if (input instanceof Ingredient) {
            return new WrappedIngredient((Ingredient) input);
        } else if (input instanceof String) {
            String in = (String) input;
            Optional<Integer> amount = Optional.empty();
            int atIndex = in.lastIndexOf('@');
            if (atIndex != -1 && in.lastIndexOf('}') < atIndex) {
                String count = in.substring(atIndex + 1);
                amount = Optional.of(Integer.parseInt(count));
                in = in.substring(0, atIndex);
            }
            if (in.indexOf('~') == 0) {
                //fluids
                Identifier id = new Identifier(in.substring(1));
                Fluid fluid = Registry.FLUID.get(id);
                if (fluid == Fluids.EMPTY) throw new CDSyntaxError("Failed to get fluid for input: " + in);
                return new FluidIngredient(fluid, Optional.empty(), amount);
            } else if (in.indexOf('#') == 0) {
                Identifier id = new Identifier(in.substring(1));
                Tag<Item> itemTag = ItemTags.getContainer().get(id);
                if (itemTag == null) throw new CDSyntaxError("Failed to get item tag for input: " + in);
                return new TagIngredient(id, ItemTags.getContainer().get(id), amount);
            } else {
                ItemStack stack;
                Optional<CompoundTag> tag = Optional.empty();
                boolean requireEmpty = false;
                if (in.contains("->")) {
                    ItemStack readStack = TweakerUtils.INSTANCE.getSpecialStack(in);
                    if (readStack.isEmpty())
                        throw new CDSyntaxError("Failed to get special stack for input: " + in);
                    if (readStack.hasTag()) {
                        tag = Optional.of(readStack.getTag());
                    } else {
                        requireEmpty = true;
                    }
                    stack = readStack;
                } else {
                    int nbtIndex = in.indexOf('{');
                    if (nbtIndex != -1) {
                        try {
                            String nbt = in.substring(nbtIndex);
                            in = in.substring(0, nbtIndex);
                            StringNbtReader reader = new StringNbtReader(new StringReader(nbt));
                            CompoundTag parsedTag = reader.parseCompoundTag();
                            if (parsedTag.isEmpty()) requireEmpty = true;
                            else tag = Optional.of(reader.parseCompoundTag());
                        } catch (CommandSyntaxException e) {
                            throw new CDSyntaxError(e.getMessage());
                        }
                    }
                    Identifier id = new Identifier(in);
                    Item item = Registry.ITEM.get(id);
                    if (item == Items.AIR) throw new CDSyntaxError("Failed to get item for input: " + in);
                    stack = new ItemStack(item);
                }
                return new StackIngredient(Collections.singletonList(stack), amount, tag, requireEmpty);
            }
        }
        else throw new CDSyntaxError("Illegal object passed to TechReborn ingredient parser of type " + input.getClass().getName());
    }

    public static FluidInstance parseFluid(String fluid) {
        int amount = 1000;
        int amtIndex = fluid.indexOf('@');
        if (amtIndex != -1) {
            String amtStr = fluid.substring(amtIndex + 1);
            fluid = fluid.substring(0, amtIndex);
            amount = Integer.parseInt(amtStr);
        }
        Identifier id = new Identifier(fluid);
        return new FluidInstance(Registry.FLUID.get(id), FluidValue.fromRaw(amount));
    }
}
