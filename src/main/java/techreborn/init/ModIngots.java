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

import java.util.Arrays;

import com.google.common.base.CaseFormat;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.RebornRegistry;
import techreborn.items.ItemIngots;
import techreborn.lib.ModInfo;

/**
 * @author drcrazy
 *
 */
public enum ModIngots implements IStringSerializable {
	ADVANCED_ALLOY, ALUMINUM, BRASS, BRONZE, CHROME, COPPER, ELECTRUM, HOT_TUNGSTENSTEEL, INVAR, IRIDIUM_ALLOY, IRIDIUM,
	LEAD, MIXED_METAL, NICKEL, PLATINUM, REFINED_IRON, SILVER, STEEL, TIN, TITANIUM, TUNGSTEN, TUNGSTENSTEEL, ZINC;

	public final String name;
	public final Item item;

	private ModIngots() {
		name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "INGOT_" + this.toString());
		item = new ItemIngots();
		item.setRegistryName(new ResourceLocation(ModInfo.MOD_ID, name));
		item.setTranslationKey(ModInfo.MOD_ID + "." + name);
	}

	public ItemStack getStack() {
		return new ItemStack(item);
	}

	public ItemStack getStack(int amount) {
		return new ItemStack(item, amount);
	}

	public static void register() {
		Arrays.stream(ModIngots.values()).forEach(ingot -> RebornRegistry.registerItem(ingot.item));
	}

	@SideOnly(Side.CLIENT)
	public static void registerModel() {
		ResourceLocation blockstateJson = new ResourceLocation(ModInfo.MOD_ID, "items/materials/ingots");
		Arrays.stream(ModIngots.values()).forEach(ingot -> ModelLoader.setCustomModelResourceLocation(ingot.item, 0,
				new ModelResourceLocation(blockstateJson, "type=" + ingot.name)));
	}

	@Override
	public String getName() {
		return name;
	}

}
