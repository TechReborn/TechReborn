/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import reborncore.common.fluid.*;
import techreborn.TechReborn;

public enum ModFluids {
	BERYLIUM,
	CALCIUM,
	CALCIUM_CARBONATE,
	CHLORITE,
	DEUTERIUM,
	GLYCERYL,
	HELIUM,
	HELIUM_3,
	HELIUMPLASMA,
	HYDROGEN,
	LITHIUM,
	MERCURY,
	METHANE,
	NITROCOAL_FUEL,
	NITROFUEL,
	NITROGEN,
	NITROGENDIOXIDE,
	POTASSIUM,
	SILICON,
	SODIUM,
	SODIUMPERSULFATE,
	TRITIUM,
	WOLFRAMIUM,
	CARBON,
	CARBON_FIBER,
	NITRO_CARBON,
	SULFUR,
	SODIUM_SULFIDE,
	DIESEL,
	NITRO_DIESEL,
	OIL,
	SULFURIC_ACID,
	COMPRESSED_AIR,
	ELECTROLYZED_WATER;

	private RebornFluid stillFluid;
	private RebornFluid flowingFluid;

	private RebornFluidBlock block;
	private RebornBucketItem bucket;
	private final Identifier identifier;

	ModFluids() {
		this.identifier = new Identifier("techreborn", this.name().replace("_", "").toLowerCase());

		FluidSettings fluidSettings = FluidSettings.create();

		Identifier texture = new Identifier("techreborn", "block/fluids/" + this.name().replace("_", "").toLowerCase() + "_flowing");

		fluidSettings.setStillTexture(texture);
		fluidSettings.setFlowingTexture(texture);

		stillFluid = new RebornFluid(true, fluidSettings, () -> block, () -> bucket, () -> flowingFluid, () -> stillFluid){};
		flowingFluid = new RebornFluid(false, fluidSettings, () -> block, () -> bucket, () -> flowingFluid, () -> stillFluid){};

		block = new RebornFluidBlock(stillFluid, FabricBlockSettings.of(Material.WATER).noCollision().hardness(100.0F).dropsNothing().build());
		bucket = new RebornBucketItem(stillFluid, new Item.Settings().group(TechReborn.ITEMGROUP));
	}

	public void register() {
		RebornFluidManager.register(stillFluid, identifier);
		RebornFluidManager.register(flowingFluid, new Identifier("techreborn", identifier.getPath() + "_flowing"));

		Registry.register(Registry.BLOCK, identifier, block);
	}

	public RebornFluid getFluid() {
		return stillFluid;
	}

	public RebornFluidBlock getBlock() {
		return block;
	}

	public Identifier getIdentifier() {
		return identifier;
	}
}
