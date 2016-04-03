package techreborn.api.recipe;

import net.minecraft.tileentity.TileEntity;

/**
 * Created by Mark on 03/04/2016.
 */
public interface ITileRecipeHandler<T extends BaseRecipe> {
    boolean canCraft(TileEntity tile, T recipe);

    boolean onCraft(TileEntity tile, T recipe);
}
