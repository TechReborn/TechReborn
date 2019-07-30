package techreborn.init;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.JsonOps;
import net.minecraft.client.MinecraftClient;
import net.minecraft.datafixers.NbtOps;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class RecipeTemplate {

	public static void generate(PlayerEntity playerEntity) throws IOException {

		if(playerEntity.inventory.getInvStack(0).isEmpty()){
			playerEntity.sendMessage(new LiteralText("no machine in first slot"));
			return;
		}

		List<ItemStack> inputs = new ArrayList<>();
		List<ItemStack> outputs = new ArrayList<>();
		Identifier type = Registry.ITEM.getId(playerEntity.inventory.getInvStack(0).getItem());

		for (int i = 1; i < playerEntity.inventory.getInvSize(); i++) {
			ItemStack stack = playerEntity.inventory.getInvStack(i);
			if(!stack.isEmpty()){
				if(i < 9){
					outputs.add(stack);
				} else {
					inputs.add(stack);
				}
			}
		}

		JsonObject object = new JsonObject();
		object.addProperty("type", type.toString());
		object.addProperty("power", 20);
		object.addProperty("time", 400);

		{
			JsonArray ingredients = new JsonArray();

			Function<ItemStack, JsonObject> toIngredient = stack -> {
				JsonObject jsonObject = new JsonObject();
				if(stack.getItem() == TRContent.CELL){
					jsonObject.addProperty("fluid", Registry.FLUID.getId(TRContent.CELL.getFluid(stack)).toString());
					jsonObject.addProperty("holder", "techreborn:cell");
				} else {
					jsonObject.addProperty("item", Registry.ITEM.getId(stack.getItem()).toString());
					if(stack.getCount() > 1){
						jsonObject.addProperty("count", stack.getCount());
					}
				}
				return jsonObject;
			};
			inputs.forEach(stack -> ingredients.add(toIngredient.apply(stack)));

			object.add("ingredients", ingredients);
		}

		{
			JsonArray results = new JsonArray();

			Function<ItemStack, JsonObject> toResult = stack -> {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("item", Registry.ITEM.getId(stack.getItem()).toString());
				if(stack.getCount() > 1){
					jsonObject.addProperty("count", stack.getCount());
				}
				if(stack.hasTag()){
					jsonObject.add("tag", Dynamic.convert(NbtOps.INSTANCE, JsonOps.INSTANCE, stack.getTag()));
				}
				return jsonObject;
			};
			outputs.forEach(stack -> results.add(toResult.apply(stack)));

			object.add("results", results);
		}

		String json = new GsonBuilder().setPrettyPrinting().create().toJson(object);

		File dir = new File("C:\\Users\\mark\\Documents\\Modding\\1.14\\TechReborn\\");
		System.out.println(dir.getAbsolutePath());
		File file = null;

		int i = 0;
		while (file == null || file.exists()){
			String name = Registry.ITEM.getId(outputs.get(0).getItem()).getPath();
			if(outputs.get(0).getItem() == TRContent.CELL){
				name = Registry.FLUID.getId(TRContent.CELL.getFluid(outputs.get(0))).getPath();
			}

			file = new File(dir, "src/main/resources/data/techreborn/recipes/" + type.getPath() + "/" + name + (i == 0 ? "" : "_" + i) + ".json");
			i ++;
		}

		FileUtils.writeStringToFile(file, json, StandardCharsets.UTF_8);

		MinecraftClient.getInstance().keyboard.setClipboard(file.getAbsolutePath());

		System.out.println(MinecraftClient.getInstance().keyboard.getClipboard());

		playerEntity.sendMessage(new LiteralText("done: " + file.getAbsolutePath()));

	}

}
