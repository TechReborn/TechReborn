/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.datagen.recipes.machine.implosion_compressor

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.item.Items
import net.minecraft.registry.RegistryWrapper
import techreborn.datagen.TRConventionalTags
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class ImplosionCompressorRecipesProvider extends TechRebornRecipesProvider {
	ImplosionCompressorRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generateRecipes() {
		generateGems()
		generateMisc()
	}

	void generateGems(){
		[
			(TRConventionalTags.DIAMOND_DUSTS) : Items.DIAMOND,
			(TRConventionalTags.EMERALD_DUSTS) : Items.EMERALD,
			(TRConventionalTags.PERIDOT_DUSTS) : TRContent.Gems.PERIDOT,
			(TRConventionalTags.RED_GARNET_DUSTS) : TRContent.Gems.RED_GARNET,
			(TRConventionalTags.RUBY_DUSTS) : TRContent.Gems.RUBY,
			(TRConventionalTags.SAPPHIRE_DUSTS) : TRContent.Gems.SAPPHIRE,
			(TRConventionalTags.YELLOW_GARNET_DUSTS) : TRContent.Gems.YELLOW_GARNET
		].each {dust, gem->
			offerImplosionCompressorRecipe {
				power 30
				time 2000
				ingredient {
					tag(dust, 4)
				}
				ingredients stack(Items.TNT, 16)
				outputs stack(gem, 3), stack(TRContent.Dusts.DARK_ASHES, 12)
				criterion getCriterionName(dust), getCriterionConditions(dust)
			}
			offerImplosionCompressorRecipe {
				power 30
				time 2000
				ingredient {
					tag(dust, 4)
				}
				ingredients stack(Items.END_CRYSTAL, 4)
				outputs stack(gem, 3), stack(TRContent.SmallDusts.ENDER_EYE, 4)
				criterion getCriterionName(dust), getCriterionConditions(dust)
			}
		}
	}

	void generateMisc(){
		offerImplosionCompressorRecipe {
			power 30
			time 2000
			ingredient {
				tag(TRConventionalTags.AMETHYST_DUSTS, 8)
			}
			ingredients stack(Items.TNT, 16)
			outputs stack(Items.AMETHYST_SHARD, 16), TRContent.Dusts.QUARTZ
			criterion getCriterionName(TRConventionalTags.AMETHYST_DUSTS), getCriterionConditions(TRConventionalTags.AMETHYST_DUSTS)
		}
		offerImplosionCompressorRecipe {
			power 30
			time 2000
			ingredient {
				tag(TRConventionalTags.AMETHYST_DUSTS, 8)
			}
			ingredients stack(Items.END_CRYSTAL, 4)
			outputs stack(Items.AMETHYST_SHARD, 16), TRContent.Dusts.QUARTZ
			criterion getCriterionName(TRConventionalTags.AMETHYST_DUSTS), getCriterionConditions(TRConventionalTags.AMETHYST_DUSTS)
		}
		offerImplosionCompressorRecipe {
			power 30
			time 2000
			ingredients TRContent.Ingots.IRIDIUM_ALLOY,  stack(Items.TNT, 8)
			outputs TRContent.Plates.IRIDIUM_ALLOY, stack(TRContent.Dusts.DARK_ASHES, 4)
			criterion getCriterionName(TRContent.Ingots.IRIDIUM_ALLOY), getCriterionConditions(TRContent.Ingots.IRIDIUM_ALLOY)
		}
		offerImplosionCompressorRecipe {
			power 30
			time 2000
			ingredients TRContent.Ingots.IRIDIUM_ALLOY,  stack(Items.END_CRYSTAL, 2)
			outputs TRContent.Plates.IRIDIUM_ALLOY, stack(TRContent.SmallDusts.ENDER_EYE, 2)
			criterion getCriterionName(TRContent.Ingots.IRIDIUM_ALLOY), getCriterionConditions(TRContent.Ingots.IRIDIUM_ALLOY)
		}
		offerImplosionCompressorRecipe {
			power 30
			time 2000
			ingredient {
				tag(TRConventionalTags.NETHERITE_NUGGETS, 9)
			}
			ingredients stack(Items.TNT, 16)
			outputs Items.NETHERITE_INGOT, stack(TRContent.Dusts.DARK_ASHES, 12)
			criterion getCriterionName(TRConventionalTags.NETHERITE_NUGGETS), getCriterionConditions(TRConventionalTags.NETHERITE_NUGGETS)
		}
		offerImplosionCompressorRecipe {
			power 30
			time 2000
			ingredient {
				tag(TRConventionalTags.NETHERITE_NUGGETS, 9)
			}
			ingredients stack(Items.END_CRYSTAL, 4)
			outputs Items.NETHERITE_INGOT, stack(TRContent.SmallDusts.ENDER_EYE, 4)
			criterion getCriterionName(TRConventionalTags.NETHERITE_NUGGETS), getCriterionConditions(TRConventionalTags.NETHERITE_NUGGETS)
		}
		offerImplosionCompressorRecipe {
			power 30
			time 2000
			ingredients stack(Items.REDSTONE_BLOCK, 9), stack(Items.TNT, 16)
			outputs stack(TRContent.Dusts.RED_GARNET, 4), stack(TRContent.Dusts.DARK_ASHES, 4)
			criterion getCriterionName(Items.REDSTONE_BLOCK), getCriterionConditions(Items.REDSTONE_BLOCK)
		}
		offerImplosionCompressorRecipe {
			power 30
			time 2000
			ingredients stack(Items.REDSTONE_BLOCK, 9), stack(Items.END_CRYSTAL, 4)
			outputs stack(TRContent.Dusts.RED_GARNET, 4), stack(TRContent.SmallDusts.ENDER_EYE, 4)
			criterion getCriterionName(Items.REDSTONE_BLOCK), getCriterionConditions(Items.REDSTONE_BLOCK)
		}
		offerImplosionCompressorRecipe {
			power 30
			time 2000
			ingredients stack(Items.SCULK, 64), stack(Items.TNT, 8)
			outputs Items.SCULK_CATALYST, stack(TRContent.Dusts.DARK_ASHES, 4)
			criterion getCriterionName(Items.SCULK), getCriterionConditions(Items.SCULK)
		}
		offerImplosionCompressorRecipe {
			power 30
			time 2000
			ingredients stack(Items.SCULK, 64), stack(Items.END_CRYSTAL, 2)
			outputs Items.SCULK_CATALYST, stack(TRContent.SmallDusts.ENDER_EYE, 2)
			criterion getCriterionName(Items.SCULK), getCriterionConditions(Items.SCULK)
		}
	}
}
