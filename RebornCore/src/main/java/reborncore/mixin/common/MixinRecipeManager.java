package reborncore.mixin.common;

import com.google.gson.JsonObject;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;

import java.util.Objects;

@Mixin(RecipeManager.class)
public class MixinRecipeManager {
	// I dont have the brain power to deal with codecs
	// If you dont like it make a PR or move on.
	@Inject(method = "deserialize", at = @At("HEAD"), cancellable = true)
	private static void deserialize(Identifier id, JsonObject json, CallbackInfoReturnable<RecipeEntry<?>> cir) {
		if (!json.has("type")) {
			return;
		}

		final String type = JsonHelper.getString(json, "type");
		final Identifier typeId = new Identifier(type);
		final RebornRecipeType<?> recipeType = reborncore.common.crafting.RecipeManager.getRecipeType(typeId);

		if (recipeType == null) {
			return;
		}

		RebornRecipe recipe = recipeType.read(json);
		Objects.requireNonNull(recipe);
		cir.setReturnValue(new RecipeEntry<>(id, recipe));
	}
}
