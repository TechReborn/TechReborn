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


import reborncore.common.fluid.*;
import techreborn.TechReborn;
import techreborn.init.TRContent.BlockInfo;

import java.util.Locale;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public enum ModFluids implements BlockInfo {
	BERYLLIUM,
	CALCIUM,
	CALCIUM_CARBONATE,
	CARBON,
	CARBON_FIBER,
	CHLORITE,
	COMPRESSED_AIR,
	DEUTERIUM,
	DIESEL,
	ELECTROLYZED_WATER,
	GLYCERYL,
	HELIUM,
	HELIUM3,
	HELIUMPLASMA,
	HYDROGEN,
	LITHIUM,
	MERCURY,
	METHANE,
	NITRO_CARBON,
	NITRO_DIESEL,
	NITROCOAL_FUEL,
	NITROFUEL,
	NITROGEN,
	NITROGEN_DIOXIDE,
	OIL,
	POTASSIUM,
	SILICON,
	SODIUM,
	SODIUM_SULFIDE,
	SODIUM_PERSULFATE,
	SULFUR,
	SULFURIC_ACID,
	TRITIUM,
	WOLFRAMIUM,
	BIOFUEL,
	FLUORINE,
	NITRIC_ACID,
	URANIUM_HEXAFLUORIDE;

	public final String name;
	private RebornFluid stillFluid;
	private RebornFluid flowingFluid;

	private RebornFluidBlock block;
	private RebornBucketItem bucket;
	private final Identifier identifier;

	ModFluids() {
		name = this.toString().toLowerCase(Locale.ROOT);
		this.identifier = Identifier.fromNamespaceAndPath(TechReborn.MOD_ID, name);

		FluidSettings fluidSettings = FluidSettings.create();

		Identifier texture_still = Identifier.fromNamespaceAndPath(TechReborn.MOD_ID, "block/fluids/" + name + "_still");
		Identifier texture_flowing = Identifier.fromNamespaceAndPath(TechReborn.MOD_ID, "block/fluids/" + name + "_flowing");

		fluidSettings.setStillTexture(texture_still);
		fluidSettings.setFlowingTexture(texture_flowing);

		stillFluid = new RebornFluid(true, fluidSettings, () -> block, () -> bucket, () -> flowingFluid, () -> stillFluid) {
		};
		flowingFluid = new RebornFluid(false, fluidSettings, () -> block, () -> bucket, () -> flowingFluid, () -> stillFluid) {
		};

		block = new RebornFluidBlock(stillFluid, TRBlockSettings.fluid(identifier.getPath()));
		bucket = new RebornBucketItem(stillFluid, TRItemSettings.item(identifier.getPath() + "_bucket").craftRemainder(Items.BUCKET).stacksTo(1));
	}

	public void register() {
		RebornFluidManager.register(stillFluid, identifier);
		RebornFluidManager.register(flowingFluid, Identifier.fromNamespaceAndPath(TechReborn.MOD_ID, identifier.getPath() + "_flowing"));

		Registry.register(BuiltInRegistries.BLOCK, identifier, block);
		Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(TechReborn.MOD_ID, identifier.getPath() + "_bucket"), bucket);
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
