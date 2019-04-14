package techreborn.api.recipe.recipes;

import com.google.gson.JsonObject;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import reborncore.common.crafting.Recipe;
import reborncore.common.crafting.RecipeType;
import techreborn.tiles.machine.multiblock.TileIndustrialBlastFurnace;

public class BlastFurnaceRecipe extends Recipe {

	int heat;

	public BlastFurnaceRecipe(RecipeType type, ResourceLocation name) {
		super(type, name);
	}

	@Override
	public void deserialize(JsonObject jsonObject) {
		super.deserialize(jsonObject);
		heat = JsonUtils.getInt(jsonObject, "heat");
	}

	@Override
	public void serialize(JsonObject jsonObject) {
		super.serialize(jsonObject);
		jsonObject.addProperty("heat", heat);
	}

	@Override
	public boolean canCraft(final TileEntity tile) {
		if (tile instanceof TileIndustrialBlastFurnace) {
			final TileIndustrialBlastFurnace blastFurnace = (TileIndustrialBlastFurnace) tile;
			return blastFurnace.getHeat() >= heat;
		}
		return false;
	}

	@Override
	public boolean onCraft(final TileEntity tile) {
		return true;
	}
}
