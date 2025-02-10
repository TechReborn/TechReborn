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

package techreborn.init;


import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import reborncore.common.fluid.*;
import techreborn.TechReborn;
import techreborn.init.TRContent.BlockInfo;

import java.util.Locale;

public enum ModFluids implements BlockInfo {
	BERYLLIUM(0x364a34),
	CALCIUM(0x9e6857),
	CALCIUM_CARBONATE(0x8f401b),
	CARBON(0x2d302b),
	CARBON_FIBER(0x3d3838),
	CHLORITE(0x4ba3a3),
	COMPRESSED_AIR(0xb3b3b3),
	DEUTERIUM(0xe5eb38),
	DIESEL(0xd99938),
	ELECTROLYZED_WATER(0x1414c9),
	GLYCERYL(0x1c8a7b),
	HELIUM(0xffff70),
	HELIUM3(0xf2f274),
	HELIUMPLASMA(0xfffa96),
	HYDROGEN(0x334491),
	LITHIUM(0x679ecf),
	MERCURY(0xededed),
	METHANE(0xc43b89),
	NITRO_CARBON(0x85302d),
	NITRO_DIESEL(0xdba842),
	NITROCOAL_FUEL(0x163b34),
	NITROFUEL(0xd68c4b),
	NITROGEN(0x39d4c4),
	NITROGEN_DIOXIDE(0x7ee0d7),
	OIL(0x262626),
	POTASSIUM(0x95b8ba),
	SILICON(0x56435e),
	SODIUM(0x374fa6),
	SODIUM_SULFIDE(0xcf9a1f),
	SODIUM_PERSULFATE(0x378080),
	SULFUR(0xbd5a57),
	SULFURIC_ACID(0xd1d1d1),
	TRITIUM(0xdb3d3d),
	WOLFRAMIUM(0x42374f),
	BIOFUEL(0x1d6920);

	public final String name;
	private RebornFluid stillFluid;
	private RebornFluid flowingFluid;

	private RebornFluidBlock block;
	private RebornBucketItem bucket;
	private final Identifier identifier;

	ModFluids(int color) {
		name = this.toString().toLowerCase(Locale.ROOT);
		this.identifier = Identifier.of(TechReborn.MOD_ID, name);

		FluidSettings fluidSettings = FluidSettings.create();

		stillFluid = new RebornFluid(true, fluidSettings, () -> block, () -> bucket, () -> flowingFluid, () -> stillFluid, color) {
		};
		flowingFluid = new RebornFluid(false, fluidSettings, () -> block, () -> bucket, () -> flowingFluid, () -> stillFluid, color) {
		};

		block = new RebornFluidBlock(stillFluid, TRBlockSettings.fluid(identifier.getPath()));
		bucket = new RebornBucketItem(stillFluid, TRItemSettings.item(identifier.getPath() + "_bucket").recipeRemainder(Items.BUCKET).maxCount(1));
	}

	public void register() {
		RebornFluidManager.register(stillFluid, identifier);
		RebornFluidManager.register(flowingFluid, Identifier.of(TechReborn.MOD_ID, identifier.getPath() + "_flowing"));

		Registry.register(Registries.BLOCK, identifier, block);
		Registry.register(Registries.ITEM, Identifier.of(TechReborn.MOD_ID, identifier.getPath() + "_bucket"), bucket);
	}

	@Override
	public String getName() {
		return name;
	}

	public RebornFluid getFluid() {
		return stillFluid;
	}

	public RebornFluid getFlowingFluid() {
		return flowingFluid;
	}

	@Override
	public RebornFluidBlock getBlock() {
		return block;
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public RebornBucketItem getBucket() {
		return bucket;
	}

	@Override
	public Item asItem() {
		return getBucket();
	}
}
