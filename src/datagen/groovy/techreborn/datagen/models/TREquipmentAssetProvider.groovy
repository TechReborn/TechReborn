/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2026 TechReborn
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

package techreborn.datagen.models

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.minecraft.client.resources.model.EquipmentClientInfo
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.equipment.ArmorMaterial
import net.minecraft.world.item.equipment.EquipmentAsset
import techreborn.init.TRArmorMaterials

import java.util.concurrent.CompletableFuture

class TREquipmentAssetProvider implements DataProvider {
	private static final List<ArmorMaterial> ARMOR = [
		TRArmorMaterials.BRONZE, TRArmorMaterials.NANO, TRArmorMaterials.PERIDOT, TRArmorMaterials.QUANTUM,
		TRArmorMaterials.RUBY, TRArmorMaterials.SAPPHIRE, TRArmorMaterials.SILVER, TRArmorMaterials.STEEL,
	]
	private static final List<ArmorMaterial> CHEST_EQUIPMENT = [
		TRArmorMaterials.CLOAKING_DEVICE, TRArmorMaterials.LAPOTRONIC_ORBPACK, TRArmorMaterials.LITHIUM_BATPACK,
	]

	private final PackOutput.PathProvider pathProvider

	TREquipmentAssetProvider(FabricPackOutput output) {
		pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, 'equipment')
	}

	@Override
	CompletableFuture<?> run(CachedOutput cache) {
		Map<ResourceKey<EquipmentAsset>, EquipmentClientInfo> assets = [:]
		ARMOR.each { assets.put(it.assetId(), equipment(it.assetId(), true)) }
		CHEST_EQUIPMENT.each { assets.put(it.assetId(), equipment(it.assetId(), false)) }
		return DataProvider.saveAll(cache, EquipmentClientInfo.CODEC, pathProvider.&json, assets)
	}

	private static EquipmentClientInfo equipment(ResourceKey<EquipmentAsset> asset, boolean leggings) {
		Identifier texture = asset.identifier()
		def builder = EquipmentClientInfo.builder()
			.addLayers(EquipmentClientInfo.LayerType.HUMANOID, new EquipmentClientInfo.Layer(texture))
		if (leggings) builder.addLayers(EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS, new EquipmentClientInfo.Layer(texture))
		return builder.build()
	}

	@Override
	String getName() {
		return 'TechReborn Equipment Asset Definitions'
	}
}
