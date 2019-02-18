package techreborn.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import techreborn.TechReborn;

public class RecipeDump {

	// You can include this in your mod/a pack/whatever you want, as long as that work follows the Mojang EULA.
	// The original source is viewable at https://gist.github.com/williewillus/a1a899ce5b0f0ba099078d46ae3dae6e

	// This is a janky JSON generator, for porting from below 1.12 to 1.12.
	// Simply replace calls to GameRegistry.addShapeless/ShapedRecipe with these methods, which will dump it to a json in RECIPE_DIR
	// Also works with OD, replace GameRegistry.addRecipe(new ShapedOreRecipe/ShapelessOreRecipe with the same calls
	// After you are done, call generateConstants()
	// Note that in many cases, you can combine multiple old recipes into one, since you can now specify multiple possibilities for an ingredient without using the OD. See vanilla for examples.

		private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
		private static File RECIPE_DIR =  new File (TechReborn.configDir, "recipes");;
		private static final Set<String> USED_OD_NAMES = new TreeSet<>();

		private static void setupDir() {
			if (RECIPE_DIR == null) {
				//RECIPE_DIR = ConfigHandler.config.getConfigFile().toPath().resolve("../recipes/").toFile();
			}

			if (!RECIPE_DIR.exists()) {
				RECIPE_DIR.mkdir();
			}
		}

		
		// EXPERIMENTAL: JSONs generated will definitely not work in 1.12.2 and below, and may not even work when 1.13 comes out
		// When Forge 1.13 is fully released, I will fix this to be correct
		// Usage: Replace calls to GameRegistry.addSmelting with this
		public static void addSmelting(ItemStack in, ItemStack result, float xp) { addSmelting(in, result, xp, 200); }
		public static void addSmelting(ItemStack in, ItemStack result, float xp, int cookTime) {
			setupDir();
			
			// GameRegistry.addSmelting(in, out, xp);
			Map<String, Object> json = new HashMap<>();
			json.put("type", "minecraft:smelting");
			json.put("ingredient", serializeItem(in));
			json.put("result", serializeItem(result)); // vanilla jsons just have a string?
			json.put("experience", xp);
			json.put("cookingtime", cookTime);
			
			// names the json the same name as the output's registry name
			// repeatedly adds _alt if a file already exists
			// janky I know but it works
			String suffix = result.getItem().getHasSubtypes() ? "_" + result.getItemDamage() : "";
			File f = new File(RECIPE_DIR, result.getItem().getRegistryName().getPath() + suffix + ".json");

			while (f.exists()) {
				suffix += "_alt";
				f = new File(RECIPE_DIR, result.getItem().getRegistryName().getPath() + suffix + ".json");
			}

			try (FileWriter w = new FileWriter(f)) {
				GSON.toJson(json, w);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public static void addShapedRecipe(ItemStack result, Object... components) {
			setupDir();

			// GameRegistry.addShapedRecipe(result, components);

			Map<String, Object> json = new HashMap<>();

			List<String> pattern = new ArrayList<>();
			int i = 0;
			while (i < components.length && components[i] instanceof String) {
				pattern.add((String) components[i]);
				i++;
			}
			json.put("pattern", pattern);

			boolean isOreDict = false;
			Map<String, Map<String, Object>> key = new HashMap<>();
			Character curKey = null;
			for (; i < components.length; i++) {
				Object o = components[i];
				if (o instanceof Character) {
					if (curKey != null)
						throw new IllegalArgumentException("Provided two char keys in a row");
					curKey = (Character) o;
				} else {
					if (curKey == null)
						throw new IllegalArgumentException("Providing object without a char key");
					if (o instanceof String)
						isOreDict = true;
					key.put(Character.toString(curKey), serializeItem(o));
					curKey = null;
				}
			}
			json.put("key", key);
			json.put("type", isOreDict ? "forge:ore_shaped" : "minecraft:crafting_shaped");
			json.put("result", serializeItem(result));

			// names the json the same name as the output's registry name
			// repeatedly adds _alt if a file already exists
			// janky I know but it works
			String suffix = result.getItem().getHasSubtypes() ? "_" + result.getItemDamage() : "";
			File f = new File(RECIPE_DIR, result.getItem().getRegistryName().getPath() + suffix + ".json");

			while (f.exists()) {
				suffix += "_alt";
				f = new File(RECIPE_DIR, result.getItem().getRegistryName().getPath() + suffix + ".json");
			}

			try (FileWriter w = new FileWriter(f)) {
				GSON.toJson(json, w);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public static void addShapelessRecipe(ItemStack result, Object... components)
		{
			setupDir();

			// addShapelessRecipe(result, components);

			Map<String, Object> json = new HashMap<>();

			boolean isOreDict = false;
			List<Map<String, Object>> ingredients = new ArrayList<>();
			for (Object o : components) {
				if (o instanceof String)
					isOreDict = true;
				ingredients.add(serializeItem(o));
			}
			json.put("ingredients", ingredients);
			json.put("type", isOreDict ? "forge:ore_shapeless" : "minecraft:crafting_shapeless");
			json.put("result", serializeItem(result));

			// names the json the same name as the output's registry name
			// repeatedly adds _alt if a file already exists
			// janky I know but it works
			String suffix = result.getItem().getHasSubtypes() ? "_" + result.getItemDamage() : "";
			File f = new File(RECIPE_DIR, result.getItem().getRegistryName().getPath() + suffix + ".json");

			while (f.exists()) {
				suffix += "_alt";
				f = new File(RECIPE_DIR, result.getItem().getRegistryName().getPath() + suffix + ".json");
			}


			try (FileWriter w = new FileWriter(f)) {
				GSON.toJson(json, w);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private static Map<String, Object> serializeItem(Object thing) {
			if (thing instanceof Item) {
				return serializeItem(new ItemStack((Item) thing));
			}
			if (thing instanceof Block) {
				return serializeItem(new ItemStack((Block) thing));
			}
			if (thing instanceof ItemStack) {
				ItemStack stack = (ItemStack) thing;
				Map<String, Object> ret = new HashMap<>();
				ret.put("item", stack.getItem().getRegistryName().toString());
				if (stack.getItem().getHasSubtypes() || stack.getItemDamage() != 0) {
					ret.put("data", stack.getItemDamage());
				}
				if (stack.getCount() > 1) {
					ret.put("count", stack.getCount());
				}
				
				if (stack.hasTagCompound()) {
					ret.put("type", "minecraft:item_nbt");
					ret.put("nbt", stack.getTagCompound().toString());
				}

				return ret;
			}
			if (thing instanceof String) {
				Map<String, Object> ret = new HashMap<>();
				USED_OD_NAMES.add((String) thing);
				ret.put("item", "#" + ((String) thing).toUpperCase(Locale.ROOT));
				return ret;
			}

			throw new IllegalArgumentException("Not a block, item, stack, or od name");
		}

		// Call this after you are done generating
		public static void generateConstants() {
			List<Map<String, Object>> json = new ArrayList<>();
			for (String s : USED_OD_NAMES) {
				Map<String, Object> entry = new HashMap<>();
				entry.put("name", s.toUpperCase(Locale.ROOT));
				entry.put("ingredient", ImmutableMap.of("type", "forge:ore_dict", "ore", s));
				json.add(entry);
			}

			try (FileWriter w = new FileWriter(new File(RECIPE_DIR, "_constants.json"))) {
				GSON.toJson(json, w);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
}
