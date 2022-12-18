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

package techreborn.datagen.models

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.registry.RegistryWrapper
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class BlockLootTableProvider extends FabricBlockLootTableProvider{

	BlockLootTableProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output)
	}

	@Override
	public void generate() {
		TRContent.StorageBlocks.values().each {
			addDrop(it.getBlock())
			addDrop(it.getSlabBlock())
			addDrop(it.getStairsBlock())
			addDrop(it.getWallBlock())
		}
		TRContent.Cables.values().each {
			addDrop(it.block)
		}
		TRContent.Machine.values().each {
			addDrop(it.block)
		}
		TRContent.SolarPanels.values().each {
			addDrop(it.block)
		}
		TRContent.StorageUnit.values().each {
			addDrop(it.block)
		}
		TRContent.TankUnit.values().each {
			addDrop(it.block)
		}
		TRContent.MachineBlocks.values().each {
			addDrop(it.getFrame())
			addDrop(it.getCasing())
		}
	}
}
