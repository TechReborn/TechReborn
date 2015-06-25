package techreborn.api.recipe.machines;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import techreborn.api.recipe.BaseRecipe;
import techreborn.tiles.TileBlastFurnace;

public class BlastFurnaceRecipe extends BaseRecipe {


	public int neededHeat;

    public BlastFurnaceRecipe(ItemStack input1, ItemStack input2, ItemStack output1 , ItemStack output2, int tickTime, int euPerTick, int neededHeat) {
        super("blastFurnaceRecipe", tickTime, euPerTick);
        if (input1 != null)
            inputs.add(input1);
        if (input2 != null)
            inputs.add(input2);
        if (output1 != null)
            inputs.add(output1);
        if (output2 != null)
            addOutput(output2);

		this.neededHeat = neededHeat;
    }

	@Override
	public boolean canCraft(TileEntity tile) {
		if(tile instanceof TileBlastFurnace){
			TileBlastFurnace blastFurnace = (TileBlastFurnace) tile;
			return blastFurnace.getHeat() >- neededHeat;
		}
		return false;
	}

	@Override
	public boolean onCraft(TileEntity tile) {
		return super.onCraft(tile);
	}
}
