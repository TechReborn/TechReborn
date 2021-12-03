package techreborn.datagen.tags

import net.fabricmc.fabric.api.tag.TagFactory
import net.minecraft.item.Item
import net.minecraft.tag.Tag
import net.minecraft.util.Identifier

trait CommonTagsTrait {
    static Tag.Identified<Item> cItem(String path) {
        return TagFactory.ITEM.create(new Identifier("c", path))
    }
}
