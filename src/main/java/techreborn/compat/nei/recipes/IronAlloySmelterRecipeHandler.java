package techreborn.compat.nei.recipes;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.ItemList;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import org.lwjgl.opengl.GL11;
import reborncore.common.util.ItemUtils;
import techreborn.api.recipe.IBaseRecipeType;
import techreborn.client.gui.GuiAlloyFurnace;
import techreborn.lib.Reference;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by MrBretzel on 01/11/2015.
 */
public class IronAlloySmelterRecipeHandler extends GenericRecipeHander implements INeiBaseRecipe {

    @Override
    public void addPositionedStacks(List<PositionedStack> input, List<PositionedStack> outputs, IBaseRecipeType recipeType) {
        int offset = 4;
        PositionedStack pStack = new PositionedStack(ItemUtils.getStackWithAllOre(recipeType.getInputs().get(0)), 46 - offset, 9 - offset, false);
        input.add(pStack);

        PositionedStack pStack2 = new PositionedStack(ItemUtils.getStackWithAllOre(recipeType.getInputs().get(1)), 64 - offset, 9 - offset, false);
        input.add(pStack2);

        Iterator i = ItemList.items.iterator();
        List<ItemStack> fuels = new ArrayList<ItemStack>();
        while(i.hasNext()) {
            ItemStack item = (ItemStack) i.next();
            if(!fuels.contains(new ItemStack(item.getItem()))) {
                int burnTime = TileEntityFurnace.getItemBurnTime(item);
                if(burnTime > 0) {
                    fuels.add(new ItemStack(item.getItem()));
                }
            }
        }

        PositionedStack pStack3 = new PositionedStack(fuels, 51, 42, false);
        input.add(pStack3);

        PositionedStack pStack4 = new PositionedStack(recipeType.getOutput(0), 115 - offset, 28 - offset, false);
        outputs.add(pStack4);
    }

    @Override
    public String getRecipeName() {
        return Reference.alloySmelteRecipe;
    }

    @Override
    public String getGuiTexture() {
        return "techreborn:textures/gui/alloy_furnace.png";
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiAlloyFurnace.class;
    }

    @Override
    public INeiBaseRecipe getNeiBaseRecipe() {
        return this;
    }

    @Override
    public void loadTransferRects() {
        this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(
                new Rectangle(75, 20, 25, 20), Reference.alloySmelteRecipe, new Object[0]));
    }

    @Override
    public void drawBackground(int recipeIndex) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 5, 11, 166, 65);
    }
}
