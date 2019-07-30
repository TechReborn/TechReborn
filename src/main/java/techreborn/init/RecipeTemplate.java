package techreborn.init;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.JsonOps;
import net.minecraft.client.MinecraftClient;
import net.minecraft.container.Container;
import net.minecraft.datafixers.NbtOps;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RecipeTemplate {

	public static void generateFromInv(PlayerEntity playerEntity) throws IOException {

		if(playerEntity.inventory.getInvStack(0).isEmpty()){
			playerEntity.sendMessage(new LiteralText("no machine in first slot"));
			auto(playerEntity);
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

		File file = generate(type,false, 20, 400, inputs, outputs);

		playerEntity.sendMessage(new LiteralText("done: " + file.getAbsolutePath()));

	}

	public static void auto(PlayerEntity playerEntity) throws IOException {
		List<Item> items = Registry.ITEM.stream().collect(Collectors.toList());


		Map<String, Map<String, Item>> map = new HashMap<>();
		List<String> names = new ArrayList<>();


		for(Item item : items){
			Identifier identifier = Registry.ITEM.getId(item);
			String path = identifier.getPath();

			if(path.contains("_")){
				String name = path.substring(0, path.lastIndexOf('_'));
				String type = path.substring(path.lastIndexOf('_') + 1);

				Map<String, Item> typeMap = map.computeIfAbsent(type, s -> new HashMap<>());

				typeMap.put(name, item);

				if (!names.contains(name)) {
					names.add(name);
				}
			}

		}

		boolean compressor = false;
		boolean grinder = false;
		boolean sawmill = true;

		if(compressor){
			//Compressor
			for(String name : names){
				ItemStack plateStack = ItemStack.EMPTY;
				if(map.get("plate").containsKey(name)){
					Item plate = map.get("plate").get(name);
					plateStack = new ItemStack(plate);
				}

				if(plateStack.isEmpty()){
					continue;
				}

				if(map.get("ingot").containsKey(name)){
					ItemStack ingotStack = new ItemStack(map.get("ingot").get(name));
					generate(new Identifier("techreborn", "compressor"), true, 10, 300,  Collections.singletonList(ingotStack), Collections.singletonList(plateStack));
				}

				if(map.get("dust").containsKey(name)){
					ItemStack dust = new ItemStack(map.get("dust").get(name));
					generate(new Identifier("techreborn", "compressor"), true, 10, 250,  Collections.singletonList(dust), Collections.singletonList(plateStack));
				}

				if(map.get("block").containsKey(name)){
					ItemStack blockStack = new ItemStack(map.get("block").get(name));

					ItemStack morePlates = plateStack.copy();
					morePlates.setCount(9);

					generate(new Identifier("techreborn", "compressor"), true, 10, 300,  Collections.singletonList(blockStack), Collections.singletonList(morePlates));
				}
			}
		}

		if(grinder){
			String[] highTeirNames = new String[]{"tungsten", "titanium", "aluminium", "iridium", "saltpeter", "coal", "diamond", "emerald", "redstone", "quartz"};

			String[] types = new String[]{"ore", "gem", "ingot"};

			//Grinder
			for(String name : names){
				for(String type : types){
					ItemStack inputStack = ItemStack.EMPTY;
					if(map.get(type).containsKey(name)){
						Item ore = map.get(type).get(name);
						inputStack = new ItemStack(ore);
					}

					if(inputStack.isEmpty()){
						continue;
					}

					boolean ore = type.equals("ore");

					if (ore && Arrays.asList(highTeirNames).contains(name)) {
						continue;
					}

					ItemStack dustStack = ItemStack.EMPTY;
					if(map.get("dust").containsKey(name)){
						Item dust = map.get("dust").get(name);
						dustStack = new ItemStack(dust);
					}

					if(dustStack.isEmpty()){
						continue;
					}

					if (ore){
						dustStack.setCount(2);
					}

					generate(new Identifier("techreborn", "grinder"), true, ore ? 270 : 200, ore ? 31 : 22, Collections.singletonList(inputStack), Collections.singletonList(dustStack));
				}

			}
		}

		if(sawmill){

			CraftingInventory inventory = new CraftingInventory(new Container(null, 0) {
				@Override
				public boolean canUse(PlayerEntity var1) {
					return true;
				}
			}, 1, 1);

			for(String name : names){
				ItemStack inputStack = ItemStack.EMPTY;
				if(map.get("log").containsKey(name)){
					Item ore = map.get("log").get(name);
					inputStack = new ItemStack(ore);
				}

				if(inputStack.isEmpty()){
					continue;
				}

				inventory.setInvStack(0, inputStack.copy());

				List<CraftingRecipe> recipes = playerEntity.world.getRecipeManager().getAllMatches(RecipeType.CRAFTING, inventory, playerEntity.world);
				CraftingRecipe recipe = recipes.get(0);

				ItemStack output = recipe.getOutput();

				if(output.isEmpty()){
					continue;
				}


				addRecipe(inputStack, output);

			}
		}

	}

	public static void addRecipe(ItemStack log, ItemStack plank) throws IOException {
		plank.setCount(4);
		register(log, Fluids.WATER, 100, 128, plank, TRContent.Dusts.SAW.getStack(3), new ItemStack(Items.PAPER));
	}

	static void register(ItemStack input1, Fluid fluid, int ticks, int euPerTick, ItemStack... outputs) throws IOException {
		Identifier sawmill = new Identifier("techreborn:industrial_sawmill");
		generate(sawmill, true, euPerTick, ticks, Collections.singletonList(input1), Arrays.asList(outputs));
	}

	public static File generate(Identifier type, boolean auto, int power, int time, List<ItemStack> inputs, List<ItemStack> outputs) throws IOException {
		JsonObject object = new JsonObject();
		object.addProperty("type", type.toString());
		object.addProperty("power", power);
		object.addProperty("time", time);

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

			String extraPath = auto ? "/auto/" : "/";

			file = new File(dir, "src/main/resources/data/techreborn/recipes/" + type.getPath() + extraPath + name + (i == 0 ? "" : "_" + i) + ".json");
			i ++;
		}

		FileUtils.writeStringToFile(file, json, StandardCharsets.UTF_8);

		MinecraftClient.getInstance().keyboard.setClipboard(file.getAbsolutePath());

		return file;

	}

}
