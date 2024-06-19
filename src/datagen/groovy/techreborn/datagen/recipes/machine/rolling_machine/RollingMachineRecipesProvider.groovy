package techreborn.datagen.recipes.machine.rolling_machine

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.item.Items
import net.minecraft.registry.RegistryWrapper
import techreborn.datagen.TRConventionalTags
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class RollingMachineRecipesProvider extends TechRebornRecipesProvider {
	RollingMachineRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generateRecipes() {
		offerRollingMachineRecipe {
			power 5
			time 250
			pattern (
				Items.IRON_INGOT, Items.STICK, Items.IRON_INGOT,
				Items.IRON_INGOT, Items.REDSTONE_TORCH, Items.IRON_INGOT,
				Items.IRON_INGOT, Items.STICK, Items.IRON_INGOT
			)
			result stack(Items.ACTIVATOR_RAIL, 8)
		}
		offerRollingMachineRecipe {
			power 5
			time 250
			pattern (
				Items.IRON_INGOT, _, Items.IRON_INGOT,
				Items.IRON_INGOT, _, Items.IRON_INGOT,
				_, Items.IRON_INGOT, _
			)
			result stack(Items.BUCKET, 2)
		}
		offerRollingMachineRecipe {
			power 5
			time 250
			pattern (
				TRConventionalTags.NICKEL_INGOTS, Items.COPPER_INGOT, TRConventionalTags.NICKEL_INGOTS,
				Items.COPPER_INGOT, _, Items.COPPER_INGOT,
				TRConventionalTags.NICKEL_INGOTS, Items.COPPER_INGOT, TRConventionalTags.NICKEL_INGOTS
			)
			result stack(TRContent.Parts.CUPRONICKEL_HEATING_COIL, 3)
		}
		offerRollingMachineRecipe {
			power 5
			time 250
			pattern (
				Items.IRON_INGOT, _, Items.IRON_INGOT,
				Items.IRON_INGOT, Items.STONE_PRESSURE_PLATE, Items.IRON_INGOT,
				Items.IRON_INGOT, Items.REDSTONE, Items.IRON_INGOT
			)
			result stack(Items.DETECTOR_RAIL, 8)
		}
		offerRollingMachineRecipe {
			power 5
			time 250
			size(2, 1)
			pattern (
				Items.IRON_INGOT, Items.IRON_INGOT
			)
			result stack(Items.HEAVY_WEIGHTED_PRESSURE_PLATE, 2)
		}
		offerRollingMachineRecipe {
			power 5
			time 250
			size(3, 2)
			pattern (
				Items.IRON_INGOT, Items.IRON_INGOT, Items.IRON_INGOT,
				Items.IRON_INGOT, Items.IRON_INGOT, Items.IRON_INGOT
			)
			result stack(Items.IRON_BARS, 24)
		}
		offerRollingMachineRecipe {
			power 5
			time 250
			pattern (
				Items.IRON_INGOT, Items.IRON_INGOT, _,
				Items.IRON_INGOT, Items.IRON_INGOT, _,
				Items.IRON_INGOT, Items.IRON_INGOT, _
			)
			result stack(Items.IRON_DOOR, 4)
		}
		offerRollingMachineRecipe {
			power 5
			time 250
			size(1, 2)
			pattern (
				Items.GOLD_INGOT, Items.GOLD_INGOT
			)
			result stack(Items.LIGHT_WEIGHTED_PRESSURE_PLATE, 2)
		}
		offerRollingMachineRecipe {
			power 5
			time 250
			pattern (
				TRConventionalTags.ALUMINUM_INGOTS, TRConventionalTags.ALUMINUM_INGOTS, TRConventionalTags.ALUMINUM_INGOTS,
				TRContent.Dusts.MAGNESIUM, TRContent.Dusts.MAGNESIUM, TRContent.Dusts.MAGNESIUM,
				TRConventionalTags.ALUMINUM_INGOTS, TRConventionalTags.ALUMINUM_INGOTS, TRConventionalTags.ALUMINUM_INGOTS
			)
			result stack(TRContent.Plates.MAGNALIUM, 3)
		}
		offerRollingMachineRecipe {
			power 5
			time 250
			size(3, 2)
			pattern (
				Items.IRON_INGOT, _, Items.IRON_INGOT,
				Items.IRON_INGOT, Items.IRON_INGOT, Items.IRON_INGOT
			)
			result stack(Items.MINECART, 1)
		}
		offerRollingMachineRecipe {
			power 5
			time 250
			pattern (
				_, TRConventionalTags.NICKEL_INGOTS, _,
				TRConventionalTags.NICKEL_INGOTS, TRConventionalTags.CHROMIUM_INGOTS, TRConventionalTags.NICKEL_INGOTS,
				_, TRConventionalTags.NICKEL_INGOTS, _
			)
			result stack(TRContent.Parts.NICHROME_HEATING_COIL, 2)
		}
		offerRollingMachineRecipe {
			power 5
			time 250
			pattern (
				Items.GOLD_INGOT, _, Items.GOLD_INGOT,
				Items.GOLD_INGOT, Items.STICK, Items.GOLD_INGOT,
				Items.GOLD_INGOT, Items.REDSTONE, Items.GOLD_INGOT
			)
			result stack(Items.POWERED_RAIL, 8)
		}
		offerRollingMachineRecipe {
			power 5
			time 250
			pattern (
				Items.IRON_INGOT, _, Items.IRON_INGOT,
				Items.IRON_INGOT, Items.STICK, Items.IRON_INGOT,
				Items.IRON_INGOT, _, Items.IRON_INGOT
			)
			result stack(Items.RAIL, 24)
		}
	}
}
