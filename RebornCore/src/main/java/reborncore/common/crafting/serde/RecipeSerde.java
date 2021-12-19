package reborncore.common.crafting.serde;

import com.google.gson.JsonObject;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;

public interface RecipeSerde<R extends RebornRecipe> {
	R fromJson(JsonObject jsonObject, RebornRecipeType<R> type, Identifier name);

	void toJson(R recipe, JsonObject jsonObject);

	void writeToPacket(PacketByteBuf buffer, R recipe);

	R fromPacket(PacketByteBuf buffer, RebornRecipeType<R> type);

	R createDummy(RebornRecipeType<R> type, Identifier name);
}
