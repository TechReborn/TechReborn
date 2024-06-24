package reborncore.common.crafting;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.RecipeSerializer;

public record RebornRecipeSerializer<R extends RebornRecipe>(MapCodec<R> codec, PacketCodec<RegistryByteBuf, R> packetCodec) implements RecipeSerializer<R> {
}
