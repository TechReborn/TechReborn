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

package techreborn.events;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import techreborn.blocks.fluid.BlockFluidTechReborn;
import techreborn.init.ModFluids;
import techreborn.lib.ModInfo;

/**
 * @author drcrazy
 *
 */
@SideOnly(Side.CLIENT)
public class FluidBlockModelHandler {

	@SubscribeEvent
	public void registerModels(ModelRegistryEvent event) {
		registerFluidBlockModel(ModFluids.BLOCK_BERYLLIUM);
		registerFluidBlockModel(ModFluids.BLOCK_CALCIUM);
		registerFluidBlockModel(ModFluids.BLOCK_CALCIUM_CARBONATE);
		registerFluidBlockModel(ModFluids.BLOCK_CHLORITE);
		registerFluidBlockModel(ModFluids.BLOCK_DEUTERIUM);
		registerFluidBlockModel(ModFluids.BLOCK_GLYCERYL);
		registerFluidBlockModel(ModFluids.BLOCK_HELIUM);
		registerFluidBlockModel(ModFluids.BLOCK_HELIUM_3);
		registerFluidBlockModel(ModFluids.BLOCK_HELIUMPLASMA);
		registerFluidBlockModel(ModFluids.BLOCK_HYDROGEN);
		registerFluidBlockModel(ModFluids.BLOCK_LITHIUM);
		registerFluidBlockModel(ModFluids.BLOCK_MERCURY);
		registerFluidBlockModel(ModFluids.BLOCK_METHANE);
		registerFluidBlockModel(ModFluids.BLOCK_NITROCOAL_FUEL);
		registerFluidBlockModel(ModFluids.BLOCK_NITROFUEL);
		registerFluidBlockModel(ModFluids.BLOCK_NITROGEN);
		registerFluidBlockModel(ModFluids.BLOCK_NITROGENDIOXIDE);
		registerFluidBlockModel(ModFluids.BLOCK_POTASSIUM);
		registerFluidBlockModel(ModFluids.BLOCK_SILICON);
		registerFluidBlockModel(ModFluids.BLOCK_SODIUM);
		registerFluidBlockModel(ModFluids.BLOCK_SODIUMPERSULFATE);
		registerFluidBlockModel(ModFluids.BLOCK_TRITIUM);
		registerFluidBlockModel(ModFluids.BLOCK_WOLFRAMIUM);
		registerFluidBlockModel(ModFluids.BLOCK_CARBON);
		registerFluidBlockModel(ModFluids.BLOCK_CARBON_FIBER);
		registerFluidBlockModel(ModFluids.BLOCK_NITRO_CARBON);
		registerFluidBlockModel(ModFluids.BLOCK_SULFUR);
		registerFluidBlockModel(ModFluids.BLOCK_SODIUM_SULFIDE);
		registerFluidBlockModel(ModFluids.BLOCK_DIESEL);
		registerFluidBlockModel(ModFluids.BLOCK_NITRO_DIESEL);
		registerFluidBlockModel(ModFluids.BLOCK_OIL);
		registerFluidBlockModel(ModFluids.BLOCK_SULFURIC_ACID);
		registerFluidBlockModel(ModFluids.BLOCK_COMPRESSED_AIR);
		registerFluidBlockModel(ModFluids.BLOCK_ELECTROLYZED_WATER);
		registerFluidBlockModel(ModFluids.BLOCK_BIO_FUEL);
	}

	private static void registerFluidBlockModel(BlockFluidTechReborn block) {
		String name = block.getTranslationKey().substring(5).toLowerCase();
		Item item = Item.getItemFromBlock(block);
		ModelResourceLocation location = new ModelResourceLocation(
				new ResourceLocation(ModInfo.MOD_ID.toLowerCase(), "fluids"), name);
		
		ModelLoader.registerItemVariants(item);
		ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				return location;
			}
		});
		ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return location;
			}
		});
	}

}
