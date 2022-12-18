/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2022 TechReborn
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

package techreborn.init;

import net.fabricmc.fabric.api.event.registry.DynamicRegistrySetupCallback;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerProfessionBuilder;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvents;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;
import reborncore.common.util.TradeUtils;
import techreborn.TechReborn;
import techreborn.config.TechRebornConfig;

import java.util.LinkedList;
import java.util.List;

public class TRVillager {

	public static final Identifier METALLURGIST_ID = new Identifier(TechReborn.MOD_ID, "metallurgist");
	public static final Identifier ELECTRICIAN_ID = new Identifier(TechReborn.MOD_ID, "electrician");

	public static final PointOfInterestType METALLURGIST_POI = PointOfInterestHelper.register(
		METALLURGIST_ID, 1, 1, TRContent.Machine.IRON_ALLOY_FURNACE.block
	);
	public static final PointOfInterestType ELECTRICIAN_POI = PointOfInterestHelper.register(
		ELECTRICIAN_ID, 1, 1, TRContent.Machine.SOLID_FUEL_GENERATOR.block
	);

	public static final VillagerProfession METALLURGIST_PROFESSION = Registry.register(Registries.VILLAGER_PROFESSION, METALLURGIST_ID,
		VillagerProfessionBuilder.create()
			.id(METALLURGIST_ID)
			.workstation(RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, METALLURGIST_ID))
			.workSound(SoundEvents.ENTITY_VILLAGER_WORK_TOOLSMITH)
			.build()
	);

	public static final VillagerProfession ELECTRICIAN_PROFESSION = Registry.register(Registries.VILLAGER_PROFESSION, ELECTRICIAN_ID,
		VillagerProfessionBuilder.create()
			.id(ELECTRICIAN_ID)
			.workstation(RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, ELECTRICIAN_ID))
			.workSound(ModSounds.CABLE_SHOCK)
			.build()
	);

	private TRVillager() {/* No instantiation. */}

	public static void registerVillagerTrades() {
		// metallurgist
		TradeUtils.registerTradesForLevel(METALLURGIST_PROFESSION, TradeUtils.Level.NOVICE, false,
			TradeUtils.createBuy(TRContent.RawMetals.TIN, 1, 6, 12, 2),
			TradeUtils.createBuy(TRContent.RawMetals.LEAD, 1, 4, 12, 2),
			TradeUtils.createSell(TRContent.Parts.RUBBER, 2, 3, 16, 2)
		);
		TradeUtils.registerTradesForLevel(METALLURGIST_PROFESSION, TradeUtils.Level.APPRENTICE, false,
			TradeUtils.createSell(TRContent.Ingots.BRONZE, 2, 1, 12, 10),
			TradeUtils.createSell(TRContent.Ingots.BRASS, 5, 1, 12, 10),
			TradeUtils.createSell(TRContent.Parts.ELECTRONIC_CIRCUIT, 3, 2, 12, 10)
		);
		TradeUtils.registerTradesForLevel(METALLURGIST_PROFESSION, TradeUtils.Level.JOURNEYMAN, false,
			TradeUtils.createSell(TRContent.Ingots.ELECTRUM, 7, 3, 12, 20),
			TradeUtils.createBuy(TRContent.Parts.CARBON_FIBER, 1, 3, 12, 20)
		);
		TradeUtils.registerTradesForLevel(METALLURGIST_PROFESSION, TradeUtils.Level.EXPERT, false,
			TradeUtils.createSell(TRContent.Ingots.ADVANCED_ALLOY, 7, 4, 12, 20),
			TradeUtils.createBuy(TRContent.Ingots.NICKEL, 1, 1, 12, 30)
		);
		TradeUtils.registerTradesForLevel(METALLURGIST_PROFESSION, TradeUtils.Level.MASTER, false,
			TradeUtils.createSell(TRContent.Parts.ADVANCED_CIRCUIT, 7, 3, 12, 30)
		);
		// electrician
		TradeUtils.registerTradesForLevel(ELECTRICIAN_PROFESSION, TradeUtils.Level.NOVICE, false,
			TradeUtils.createBuy(TRContent.Parts.RUBBER, 1, 6, 12, 2),
			TradeUtils.createBuy(Items.COPPER_INGOT, 1, 3, 12, 2),
			TradeUtils.createSell(TRContent.Cables.INSULATED_COPPER, 1, 3, 12, 2)
		);
		TradeUtils.registerTradesForLevel(ELECTRICIAN_PROFESSION, TradeUtils.Level.APPRENTICE, false,
			TradeUtils.createBuy(Items.GOLD_INGOT, 1, 4, 12, 10),
			TradeUtils.createSell(TRContent.Cables.INSULATED_GOLD, 5, 3, 12, 10),
			TradeUtils.createSell(TRContent.Parts.ELECTRONIC_CIRCUIT, 3, 2, 12, 10)
		);
		TradeUtils.registerTradesForLevel(ELECTRICIAN_PROFESSION, TradeUtils.Level.JOURNEYMAN, false,
			TradeUtils.createBuy(TRContent.RED_CELL_BATTERY, 1, 1, 12, 20),
			TradeUtils.createSell(TRContent.Machine.LOW_VOLTAGE_SU, 8, 1, 12, 20),
			TradeUtils.createSell(TRContent.Machine.SOLID_FUEL_GENERATOR, 8, 1, 12, 20)
		);
		TradeUtils.registerTradesForLevel(ELECTRICIAN_PROFESSION, TradeUtils.Level.EXPERT, false,
			TradeUtils.createSell(TRContent.Parts.ADVANCED_CIRCUIT, 7, 3, 12, 20),
			TradeUtils.createBuy(TRContent.Gems.RUBY, 1, 6, 12, 20),
			TradeUtils.createSell(TRContent.Cables.GLASSFIBER, 4, 1, 8, 30)
		);
		TradeUtils.registerTradesForLevel(ELECTRICIAN_PROFESSION, TradeUtils.Level.MASTER, false,
			TradeUtils.createSell(TRContent.Machine.LAMP_LED, 8, 1, 12, 20),
			TradeUtils.createSell(TRContent.LITHIUM_ION_BATTERY, 30, 1, 8, 30)
		);
	}

	public static void registerWanderingTraderTrades() {
		List<TradeOffer> extraCommonTrades = new LinkedList<>();
		List<TradeOffer> extraRareTrades = new LinkedList<>();
		// specify extra trades below here
		extraCommonTrades.add(TradeUtils.createSell(TRContent.RUBBER_SAPLING, 5, 1, 8, 1));
		// registration of the trades, no changes necessary for new trades
		TradeOfferHelper.registerWanderingTraderOffers(1, allTradesList -> allTradesList.addAll(
			extraCommonTrades.stream().map(TradeUtils::asFactory).toList()
		));
		TradeOfferHelper.registerWanderingTraderOffers(2, allTradesList -> allTradesList.addAll(
			extraRareTrades.stream().map(TradeUtils::asFactory).toList()
		));
	}

	public static void registerVillagerHouses() {
		final String[] types = new String[] {"desert", "plains", "savanna", "snowy", "taiga"};
		for (String type : types) {
			DynamicRegistrySetupCallback.EVENT.register(registryManager ->
				registryManager.registerEntryAdded(RegistryKeys.TEMPLATE_POOL, ((rawId, id, pool) -> {
					if (id.equals(new Identifier("minecraft", "village/"+type+"/houses"))) {
						if (TechRebornConfig.enableMetallurgistGeneration) {
							pool.elements.add(StructurePoolElement.ofSingle(TechReborn.MOD_ID + ":village/" + type + "/houses/" + type + "_metallurgist").apply(StructurePool.Projection.RIGID));
						}
						if (TechRebornConfig.enableElectricianGeneration) {
							pool.elements.add(StructurePoolElement.ofSingle(TechReborn.MOD_ID + ":village/" + type + "/houses/" + type + "_electrician").apply(StructurePool.Projection.RIGID));
						}
					}
				}))
			);
		}
	}
}
