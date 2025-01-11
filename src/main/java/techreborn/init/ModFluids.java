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
	BIOFUEL;

	public final String name;
	private RebornFluid stillFluid;
	private RebornFluid flowingFluid;

	private RebornFluidBlock block;
	private RebornBucketItem bucket;
	private final Identifier identifier;

	ModFluids() {
		name = this.toString().toLowerCase();
		this.identifier = Identifier.of(TechReborn.MOD_ID, name);

		FluidSettings fluidSettings = FluidSettings.create();

		Identifier texture_still = Identifier.of(TechReborn.MOD_ID, "block/fluids/" + name + "_still");
		Identifier texture_flowing = Identifier.of(TechReborn.MOD_ID, "block/fluids/" + name + "_flowing");

		fluidSettings.setStillTexture(texture_still);
		fluidSettings.setFlowingTexture(texture_flowing);

		stillFluid = new RebornFluid(true, fluidSettings, () -> block, () -> bucket, () -> flowingFluid, () -> stillFluid) {
		};
		flowingFluid = new RebornFluid(false, fluidSettings, () -> block, () -> bucket, () -> flowingFluid, () -> stillFluid) {
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
