package techreborn.datagen

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import techreborn.datagen.recipes.smelting.SmeltingRecipesProvider
import techreborn.datagen.tags.WaterExplosionTagProvider

class TechRebornDataGen implements DataGeneratorEntrypoint {
    @Override
    void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        fabricDataGenerator.addProvider(WaterExplosionTagProvider.&new)
        fabricDataGenerator.addProvider(SmeltingRecipesProvider.&new)
    }
}
