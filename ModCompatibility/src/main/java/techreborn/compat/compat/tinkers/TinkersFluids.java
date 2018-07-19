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

//package techreborn.compat.compat.tinkers;
//
//import net.minecraft.item.EnumRarity;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.nbt.NBTTagList;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.fluids.Fluid;
//import net.minecraftforge.fluids.FluidRegistry;
//import net.minecraftforge.fml.common.event.FMLInterModComms;
//import slimeknights.tconstruct.library.fluid.FluidMolten;
//
///**
// * @author Prospector on 08/05/16
// */
//public class TinkersFluids {
//	private static ResourceLocation moltenMetal = new ResourceLocation("tconstruct:blocks/fluids/molten_metal");
//	private static ResourceLocation moltenMetalFlowing = new ResourceLocation(
//		"tconstruct:blocks/fluids/molten_metal_flow");
//
//	public static FluidMolten moltenChrome = new FluidMolten("chrome", 0x90C9C9, moltenMetal, moltenMetalFlowing);
//	public static FluidMolten moltenInvar = new FluidMolten("invar", 0x7F907F, moltenMetal, moltenMetalFlowing);
//	public static FluidMolten moltenIridium = new FluidMolten("iridium", 0xFFFFFF, moltenMetal, moltenMetalFlowing);
//	public static FluidMolten moltenPlatinum = new FluidMolten("platinum", 0x34BFBF, moltenMetal, moltenMetalFlowing);
//	public static FluidMolten moltenTitanium = new FluidMolten("titanium", 0x3C372F, moltenMetal, moltenMetalFlowing);
//	public static FluidMolten moltenTungsten = new FluidMolten("tungsten", 0x3A464F, moltenMetal, moltenMetalFlowing);
//
//	public static void init() {
//		addFluidStuff(moltenChrome, "Chrome");
//		moltenChrome.setTemperature(800);
//		addFluidStuff(moltenInvar, "Invar");
//		moltenInvar.setTemperature(580);
//		addFluidStuff(moltenIridium, "Iridium");
//		moltenIridium.setTemperature(4000);
//		moltenIridium.setRarity(EnumRarity.EPIC);
//		addFluidStuff(moltenPlatinum, "Platinum");
//		moltenPlatinum.setTemperature(900);
//		addFluidStuff(moltenTitanium, "Titanium");
//		moltenTitanium.setTemperature(1000);
//		addFluidStuff(moltenTungsten, "Tungsten");
//		moltenTungsten.setTemperature(1200);
//
//		//Invar Alloying
//		NBTTagList tagList = new NBTTagList();
//		NBTTagCompound fluid = new NBTTagCompound();
//		fluid.setString("FluidName", "invar");
//		fluid.setInteger("Amount", 144);
//		tagList.appendTag(fluid);
//		fluid = new NBTTagCompound();
//		fluid.setString("FluidName", "iron");
//		fluid.setInteger("Amount", 96);
//		tagList.appendTag(fluid);
//		fluid = new NBTTagCompound();
//		fluid.setString("FluidName", "nickel");
//		fluid.setInteger("Amount", 48);
//		tagList.appendTag(fluid);
//
//		NBTTagCompound message = new NBTTagCompound();
//		message.setTag("alloy", tagList);
//		FMLInterModComms.sendMessage("tconstruct", "alloy", message);
//	}
//
//	public static void addFluidStuff(Fluid fluid, String oreSuffix) {
//		FluidRegistry.registerFluid(fluid);
//		FluidRegistry.addBucketForFluid(fluid);
//		NBTTagCompound tag = new NBTTagCompound();
//		tag.setString("fluid", fluid.getName());
//		tag.setString("ore", oreSuffix);
//		tag.setBoolean("toolforge", true);
//		FMLInterModComms.sendMessage("tconstruct", "integrateSmeltery", tag);
//	}
//}
