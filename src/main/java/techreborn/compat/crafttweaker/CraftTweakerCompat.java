/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

package techreborn.compat.crafttweaker;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.item.IngredientStack;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import techreborn.compat.ICompatModule;

import static minetweaker.api.minecraft.MineTweakerMC.getItemStack;
import static minetweaker.api.minecraft.MineTweakerMC.getLiquidStack;

public class CraftTweakerCompat implements ICompatModule {
	public static ItemStack toStack(IItemStack iStack) {
		return getItemStack(iStack);
	}

	public static Object toObject(IIngredient iStack) {
		if (iStack == null)
			return null;
		else {
			if (iStack instanceof IOreDictEntry)
				return ((IOreDictEntry) iStack).getName();
			else if (iStack instanceof IItemStack)
				return getItemStack((IItemStack) iStack);
			else if (iStack instanceof IngredientStack) {
				IIngredient ingr = ReflectionHelper.getPrivateValue(IngredientStack.class, (IngredientStack) iStack, "ingredient");
				return toObject(ingr);
			} else
				return null;
		}
	}

	public static FluidStack toFluidStack(ILiquidStack iStack) {
		return getLiquidStack(iStack);
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {

	}

	@Override
	public void init(FMLInitializationEvent event) {

	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		MineTweakerAPI.registerClass(CTAlloySmelter.class);
		MineTweakerAPI.registerClass(CTAssemblingMachine.class);
		MineTweakerAPI.registerClass(CTBlastFurnace.class);
		MineTweakerAPI.registerClass(CTCentrifuge.class);
		MineTweakerAPI.registerClass(CTChemicalReactor.class);
		MineTweakerAPI.registerClass(CTCompressor.class);
		MineTweakerAPI.registerClass(CTIndustrialGrinder.class);
		MineTweakerAPI.registerClass(CTImplosionCompressor.class);
		MineTweakerAPI.registerClass(CTIndustrialElectrolyzer.class);
		MineTweakerAPI.registerClass(CTIndustrialSawmill.class);
		MineTweakerAPI.registerClass(CTFusionReactor.class);
		MineTweakerAPI.registerClass(CTVacuumFreezer.class);
		MineTweakerAPI.registerClass(CTGenerator.class);
		MineTweakerAPI.registerClass(CTRollingMachine.class);
	}

	@Override
	public void serverStarting(FMLServerStartingEvent event) {

	}

}
