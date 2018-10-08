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

package techreborn.api;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.EnumHelper;

/**
 * This contains some static stuff used in recipes and other things
 */
public class Reference {

	private static final Class<?>[] ARMOR_PARAMETERS = { String.class, int.class, int[].class, int.class, SoundEvent.class, float.class };
	public static ArmorMaterial BRONZE_ARMOR = EnumHelper.addEnum(ArmorMaterial.class, "BRONZE", ARMOR_PARAMETERS, "techreborn:bronze", 17, new int[] { 3, 6, 5,
		2 }, 8, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F);
	public static ArmorMaterial RUBY_ARMOR = EnumHelper.addEnum(ArmorMaterial.class, "RUBY", ARMOR_PARAMETERS, "techreborn:ruby", 16, new int[] { 2, 7, 5,
		2 }, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F);
	public static ArmorMaterial SAPPHIRE_ARMOR = EnumHelper.addEnum(ArmorMaterial.class, "SAPPHIRE", ARMOR_PARAMETERS, "techreborn:sapphire", 19, new int[] { 4, 4, 4,
		4 }, 8, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F);
	public static ArmorMaterial PERIDOT_ARMOR = EnumHelper.addEnum(ArmorMaterial.class, "PERIDOT", ARMOR_PARAMETERS, "techreborn:peridot", 17, new int[] { 3, 8, 3,
		2 }, 16, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F);
	public static ArmorMaterial CLOAKING_ARMOR = EnumHelper.addEnum(ArmorMaterial.class, "CLOAKING", ARMOR_PARAMETERS, "techreborn:cloaking", 5, new int[] { 1, 2, 3,
			1 }, 0, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0F);

	public static ToolMaterial BRONZE = EnumHelper.addToolMaterial("BRONZE", 2, 375, 6.0F, 2.25F, 8);
	public static ToolMaterial RUBY = EnumHelper.addToolMaterial("RUBY", 2, 320, 6.2F, 2.7F, 10);
	public static ToolMaterial SAPPHIRE = EnumHelper.addToolMaterial("SAPPHIRE", 2, 620, 5.0F, 2F, 8);
	public static ToolMaterial PERIDOT = EnumHelper.addToolMaterial("PERIDOT", 2, 400, 7.0F, 2.4F, 16);

	public static String ALLOY_SMELTER_RECIPE = "ALLOY_SMELTER_RECIPE";
	public static String ASSEMBLING_MACHINE_RECIPE = "ASSEMBLING_MACHINE_RECIPE";
	public static String BLAST_FURNACE_RECIPE = "BLAST_FURNACE_RECIPE";
	public static String CENTRIFUGE_RECIPE = "CENTRIFUGE_RECIPE";
	public static String CHEMICAL_REACTOR_RECIPE = "CHEMICAL_REACTOR_RECIPE";
	public static String COMPRESSOR_RECIPE = "COMPRESSOR_RECIPE";
	public static String DISTILLATION_TOWER_RECIPE = "DISTILLATION_TOWER_RECIPE";
	public static String EXTRACTOR_RECIPE = "EXTRACTOR_RECIPE";
	public static String GRINDER_RECIPE = "GRINDER_RECIPE";
	public static String IMPLOSION_COMPRESSOR_RECIPE = "IMPLOSION_COMPRESSOR_RECIPE";
	public static String INDUSTRIAL_ELECTROLYZER_RECIPE = "INDUSTRIAL_ELECTROLYZER_RECIPE";
	public static String INDUSTRIAL_GRINDER_RECIPE = "INDUSTRIAL_GRINDER_RECIPE"; 
	public static String INDUSTRIAL_SAWMILL_RECIPE = "INDUSTRIAL_SAWMILL_RECIPE";
	public static String RECYCLER_RECIPE = "RECYCLER_RECIPE"; 
	public static String SCRAPBOX_RECIPE = "SCRAPBOX_RECIPE";
	public static String VACUUM_FREEZER_RECIPE = "VACUUM_FREEZER_RECIPE";
	public static String FLUID_REPLICATOR_RECIPE = "FLUID_REPLICATOR_RECIPE";
}
