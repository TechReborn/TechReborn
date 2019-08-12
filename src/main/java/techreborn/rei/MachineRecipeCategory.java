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

package techreborn.rei;

import com.mojang.blaze3d.platform.GlStateManager;
import me.shedaniel.rei.api.*;
import me.shedaniel.rei.gui.renderables.RecipeRenderer;
import me.shedaniel.rei.gui.widget.RecipeBaseWidget;
import me.shedaniel.rei.gui.widget.SlotWidget;
import me.shedaniel.rei.gui.widget.Widget;
import me.shedaniel.rei.plugin.DefaultPlugin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.util.StringUtils;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class MachineRecipeCategory<R extends RebornRecipe> implements RecipeCategory<MachineRecipeDisplay<R>> {

	private final RebornRecipeType<R> rebornRecipeType;
	private int recipeLines;

	MachineRecipeCategory(RebornRecipeType<R> rebornRecipeType) {
        this(rebornRecipeType, 2);
	}

    MachineRecipeCategory(RebornRecipeType<R> rebornRecipeType, int lines) {
        this.rebornRecipeType = rebornRecipeType;
        this.recipeLines = lines;
    }

	@Override
	public Identifier getIdentifier() {
		return rebornRecipeType.getName();
	}

	@Override
	public String getCategoryName() {
		return StringUtils.t(rebornRecipeType.getName().toString());
	}

	@Override
	public Renderer getIcon() {
		return Renderable.fromItemStack(new ItemStack(ReiPlugin.iconMap.getOrDefault(rebornRecipeType, () -> Items.DIAMOND_SHOVEL).asItem()));
	}

	@Override
	public RecipeRenderer getSimpleRenderer(MachineRecipeDisplay<R> recipe) {
		return Renderable.fromRecipe(() -> Collections.singletonList(recipe.getInput().get(0)), recipe::getOutput);
	}

	@Override
	public List<Widget> setupDisplay(Supplier<MachineRecipeDisplay<R>> recipeDisplaySupplier, Rectangle bounds) {

		MachineRecipeDisplay<R> machineRecipe = recipeDisplaySupplier.get();

		Point startPoint = new Point((int) bounds.getCenterX() - 41, (int) bounds.getCenterY() - recipeLines*12 -1);

        class RecipeBackgroundWidget extends RecipeBaseWidget {
            RecipeBackgroundWidget(Rectangle bounds) {
                super(bounds);
            }

            public void render(int mouseX, int mouseY, float delta) {
                super.render(mouseX, mouseY, delta);
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                GuiLighting.disable();
                MinecraftClient.getInstance().getTextureManager().bindTexture(DefaultPlugin.getDisplayTexture());
                int width = MathHelper.ceil((double)(System.currentTimeMillis() / 50L) % 24.0D / 1.0D);
                this.blit(startPoint.x + 24, startPoint.y + 1, 82, 91, width, 17);
            }
        }
        List<Widget> widgets = new LinkedList(Arrays.asList(new RecipeBackgroundWidget(bounds)));

		int i = 0;
		for (List<ItemStack> inputs : machineRecipe.getInput()){
			widgets.add(new SlotWidget(startPoint.x + 1, startPoint.y + 1 + (i++ * 20), inputs, true, true, true));
		}

		i = 0;
		for (ItemStack outputs : machineRecipe.getOutput()){
			widgets.add(new SlotWidget(startPoint.x + 61, startPoint.y + 1 + (i++ * 20), Collections.singletonList(outputs), true, true, true));
		}

		return widgets;
	}

    @Override
    public DisplaySettings getDisplaySettings() {
		return new DisplaySettings<MachineRecipeDisplay>() {
            public int getDisplayHeight(RecipeCategory category) {

                if (recipeLines == 1) {
                    return 36;
                }
                else if (recipeLines == 3) {
                    return 90;
                }
                else if (recipeLines == 4) {
                    return 110;
                }
                return 66;
            }

            public int getDisplayWidth(RecipeCategory category, MachineRecipeDisplay display) {
                return 150;
            }

            public int getMaximumRecipePerPage(RecipeCategory category) {
                return 99;
            }
        };
    }
}
