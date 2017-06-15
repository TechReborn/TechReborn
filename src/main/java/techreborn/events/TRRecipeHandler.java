package techreborn.events;


import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import reborncore.common.util.ItemUtils;
import techreborn.init.ModItems;
import techreborn.lib.ModInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TRRecipeHandler {

	private static List<IForgeRegistryEntry> hiddenEntrys = new ArrayList<>();

	public static void hideEntry(IForgeRegistryEntry entry){
		hiddenEntrys.add(entry);
	}

	@SubscribeEvent
	public void pickupEvent(EntityItemPickupEvent entityItemPickupEvent){
		if(entityItemPickupEvent.getEntityPlayer() instanceof EntityPlayerMP){
			if(ItemUtils.isInputEqual("logWood", entityItemPickupEvent.getItem().getItem(), false, false, true)){
				for(IRecipe recipe : CraftingManager.REGISTRY){
					if(recipe.getRecipeOutput().getItem() == ModItems.TREE_TAP){
						entityItemPickupEvent.getEntityPlayer().unlockRecipes(Collections.singletonList(recipe));
					}
				}
			}
		}
	}

	public static void unlockTRRecipes(EntityPlayerMP playerMP){
		List<IRecipe> recipeList = new ArrayList<>();
		for(IRecipe recipe : CraftingManager.REGISTRY){
			if(isRecipeValid(recipe)){
				recipeList.add(recipe);
			}
		}
		playerMP.unlockRecipes(recipeList);
	}

	private static boolean isRecipeValid(IRecipe recipe){
		if(recipe.getRegistryName() == null){
			return false;
		}
		if(!recipe.getRegistryName().getResourceDomain().equals(ModInfo.MOD_ID)){
			return false;
		}
		if(!recipe.getRecipeOutput().getItem().getRegistryName().getResourceDomain().equals(ModInfo.MOD_ID)){
			return false;
		}
		if(hiddenEntrys.contains(recipe.getRecipeOutput().getItem())){
			return false;
		}
		//Hide uu recipes
		for(Ingredient ingredient : recipe.getIngredients()){
			if(ingredient.apply(new ItemStack(ModItems.UU_MATTER))){
				return false;
			}
		}
		return true;
	}

}
