package techreborn.datagen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import reborncore.common.misc.RebornCoreTags
import techreborn.init.ModFluids

class WaterExplosionTagProvider extends FabricTagProvider.ItemTagProvider {
    WaterExplosionTagProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator)
    }

    @Override
    protected void generateTags() {
        getOrCreateTagBuilder(RebornCoreTags.WATER_EXPLOSION_ITEM)
            .add(ModFluids.SODIUM.getBucket())
    }
}
