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

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.RecipeManager;
import reborncore.common.registration.RebornRegister;
import net.minecraft.fluid.Fluid;
import io.github.prospector.silk.fluid.FluidInstance;
import reborncore.common.fluid.FluidUtil;
import techreborn.TechReborn;
import techreborn.api.recipe.recipes.BlastFurnaceRecipe;
import techreborn.api.recipe.recipes.IndustrialGrinderRecipe;
import techreborn.api.recipe.recipes.IndustrialSawmillRecipe;

@RebornRegister(TechReborn.MOD_ID)
public class ModRecipes {


	public static final RebornRecipeType<RebornRecipe> ALLOY_SMELTER = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:alloy_smelter"));
	public static final RebornRecipeType<RebornRecipe> ASSEMBLING_MACHINE = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:assembling_machine"));
	public static final RebornRecipeType<BlastFurnaceRecipe> BLAST_FURNACE = RecipeManager.newRecipeType(BlastFurnaceRecipe.class, new Identifier("techreborn:blast_furnace"));
	public static final RebornRecipeType<RebornRecipe> CENTRIFUGE = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:centrifuge"));
	public static final RebornRecipeType<RebornRecipe> CHEMICAL_REACTOR = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:chemical_reactor"));
	public static final RebornRecipeType<RebornRecipe> COMPRESSOR = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:compressor"));
	public static final RebornRecipeType<RebornRecipe> DISTILLATION_TOWER = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:distillation_tower"));
	public static final RebornRecipeType<RebornRecipe> EXTRACTOR = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:extractor"));
	public static final RebornRecipeType<RebornRecipe> GRINDER = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:grinder"));
	public static final RebornRecipeType<RebornRecipe> IMPLOSION_COMPRESSOR = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:implosion_compressor"));
	public static final RebornRecipeType<RebornRecipe> INDUSTRIAL_ELECTROLYZER = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:industrial_electrolyzer"));
	public static final RebornRecipeType<IndustrialGrinderRecipe> INDUSTRIAL_GRINDER = RecipeManager.newRecipeType(IndustrialGrinderRecipe.class, new Identifier("techreborn:industrial_grinder"));
	public static final RebornRecipeType<IndustrialSawmillRecipe> INDUSTRIAL_SAWMILL = RecipeManager.newRecipeType(IndustrialSawmillRecipe.class, new Identifier("techreborn:industrial_sawmill"));
	public static final RebornRecipeType<RebornRecipe> RECYCLER = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:recycler"));
	public static final RebornRecipeType<RebornRecipe> SCRAPBOX = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:scrapbox"));
	public static final RebornRecipeType<RebornRecipe> VACUUM_FREEZER = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:vacuum_freezer"));
	public static final RebornRecipeType<RebornRecipe> FLUID_REPLICATOR = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:fluid_replicator"));



	public static void init() {
		//Gonna rescan to make sure we have an uptodate list
		//OreUtil.scanForOres();

		/*

		ExtractorRecipes.init();
		RollingMachineRecipes.init();
		FluidGeneratorRecipes.init();
		IndustrialGrinderRecipes.init();
		IndustrialCentrifugeRecipes.init();
		IndustrialElectrolyzerRecipes.init();
		ImplosionCompressorRecipes.init();
		ScrapboxRecipes.init();
		ChemicalReactorRecipes.init();
		FusionReactorRecipes.init();
		DistillationTowerRecipes.init();

		FluidReplicatorRecipes.init();

		addBlastFurnaceRecipes();
		addVacuumFreezerRecipes();
		addGrinderRecipes();
		addCompressorRecipes();

		*/
	}

	public static void postInit() {
		//IndustrialSawmillRecipes.init();
	}

	private static void addCompressorRecipes() {
//		RecipeHandler.addRecipe(Reference.COMPRESSOR_RECIPE, new CompressorRecipe(ItemIngots.getIngotByName("advanced_alloy"),
//				ItemPlates.getPlateByName("advanced_alloy"), 400, 20));
//		RecipeHandler.addRecipe(Reference.COMPRESSOR_RECIPE, new CompressorRecipe(ItemParts.getPartByName("carbon_mesh"),
//				ItemPlates.getPlateByName("carbon"), 400, 2));
//		RecipeHandler.addRecipe(Reference.COMPRESSOR_RECIPE, new CompressorRecipe(OreUtil.getStackFromName("plankWood", 1),
//				OreUtil.getStackFromName("plateWood", 1), 300, 4));
//		RecipeHandler.addRecipe(Reference.COMPRESSOR_RECIPE, new CompressorRecipe(OreUtil.getStackFromName("dustLazurite", 1),
//				ItemPlates.getPlateByName("lazurite", 1), 300, 4));
//		RecipeHandler.addRecipe(Reference.COMPRESSOR_RECIPE, new CompressorRecipe(OreUtil.getStackFromName("obsidian", 1),
//				ItemPlates.getPlateByName("obsidian", 9), 300, 4));
//		RecipeHandler.addRecipe(Reference.COMPRESSOR_RECIPE, new CompressorRecipe(OreUtil.getStackFromName("dustObsidian", 1),
//				ItemPlates.getPlateByName("obsidian", 1), 300, 4));
//		RecipeHandler.addRecipe(Reference.COMPRESSOR_RECIPE, new CompressorRecipe(OreUtil.getStackFromName("dustYellowGarnet", 1),
//				ItemPlates.getPlateByName("YellowGarnet"), 300, 4));
//		RecipeHandler.addRecipe(Reference.COMPRESSOR_RECIPE, new CompressorRecipe(OreUtil.getStackFromName("blockYellowGarnet", 1),
//				ItemPlates.getPlateByName("YellowGarnet", 9), 300, 4));
//		RecipeHandler.addRecipe(Reference.COMPRESSOR_RECIPE, new CompressorRecipe(OreUtil.getStackFromName("dustRedGarnet", 1),
//				ItemPlates.getPlateByName("RedGarnet"), 300, 4));
//		RecipeHandler.addRecipe(Reference.COMPRESSOR_RECIPE, new CompressorRecipe(OreUtil.getStackFromName("blockRedGarnet", 1),
//				ItemPlates.getPlateByName("RedGarnet", 9), 300, 4));
//		RecipeHandler.addRecipe(Reference.COMPRESSOR_RECIPE, new CompressorRecipe(OreUtil.getStackFromName("ingotRefinedIron", 1),
//				ItemPlates.getPlateByName("RefinedIron"), 300, 4));
//
//		ItemStack plate;
//		for (String ore : OreUtil.oreNames) {
//			if (ore.equals("iridium")) {
//				continue;
//			}
//			if (OreUtil.hasPlate(ore)) {
//				try {
//					plate = ItemPlates.getPlateByName(ore, 1);
//				} catch (InvalidParameterException e) {
//					plate = OreUtil.getStackFromName("plate" + OreUtil.capitalizeFirstLetter(ore), 1);
//				}
//				if (plate.isEmpty()) {
//					continue;
//				}
//				if (OreUtil.hasIngot(ore)) {
//					RecipeHandler.addRecipe(Reference.COMPRESSOR_RECIPE, new CompressorRecipe(
//							OreUtil.getStackFromName("ingot" + OreUtil.capitalizeFirstLetter(ore), 1), plate, 300, 4));
//				}
//				if (OreUtil.hasGem(ore) && OreUtil.hasDust(ore)) {
//					RecipeHandler.addRecipe(Reference.COMPRESSOR_RECIPE, new CompressorRecipe(
//							OreUtil.getStackFromName("dust" + OreUtil.capitalizeFirstLetter(ore), 1), plate, 300, 4));
//				}
//				if (OreUtil.hasBlock(ore)) {
//					ItemStack morePlates = plate.copy();
//					morePlates.setCount(9);
//					RecipeHandler.addRecipe(Reference.COMPRESSOR_RECIPE, new CompressorRecipe(
//							OreUtil.getStackFromName("block" + OreUtil.capitalizeFirstLetter(ore), 1), morePlates, 300, 4));
//				}
//			}
//		}
	}

	static void addGrinderRecipes() {

		// Vanilla
		// int eutick = 2;
		// int ticktime = 300;

//		RecipeHandler.addRecipe(Reference.GRINDER_RECIPE, new GrinderRecipe(
//			new ItemStack(Items.BONE),
//			new ItemStack(Items.DYE, 6, 15),
//			170, 19));

//		RecipeHandler.addRecipe(Reference.GRINDER_RECIPE, new GrinderRecipe(
//			new ItemStack(Blocks.COBBLESTONE),
//			new ItemStack(Blocks.SAND),
//			230, 23));
//
//		RecipeHandler.addRecipe(Reference.GRINDER_RECIPE, new GrinderRecipe(
//			new ItemStack(Blocks.GRAVEL),
//			new ItemStack(Items.FLINT),
//			200, 20));

//		RecipeHandler.addRecipe(Reference.GRINDER_RECIPE, new GrinderRecipe(
//				new ItemStack(Items.COAL),
//				ItemDusts.getDustByName("coal"),
//				230, 27));
		
//		RecipeHandler.addRecipe(Reference.GRINDER_RECIPE, new GrinderRecipe(
//				new ItemStack(Items.COAL, 1, 1),
//				ItemDusts.getDustByName("charcoal"),
//				230, 27));
//
//		RecipeHandler.addRecipe(Reference.GRINDER_RECIPE, new GrinderRecipe(
//				new ItemStack(net.minecraft.init.Items.CLAY_BALL),
//				ItemDusts.getDustByName("clay"),
//				200, 18));
//
//		RecipeHandler.addRecipe(Reference.GRINDER_RECIPE, new GrinderRecipe(
//			new ItemStack(Blocks.GLOWSTONE),
//			ItemDusts.getDustByName("glowstone", 4), 220, 21));
//
//		RecipeHandler.addRecipe(Reference.GRINDER_RECIPE, new GrinderRecipe(
//			new ItemStack(Blocks.NETHERRACK),
//			ItemDusts.getDustByName("netherrack"),
//			300, 27));
//
//		RecipeHandler.addRecipe(Reference.GRINDER_RECIPE, new GrinderRecipe(
//				new ItemStack(Blocks.END_STONE),
//				ItemDusts.getDustByName("endstone"),
//				300, 16));
		
//		RecipeHandler.addRecipe(Reference.GRINDER_RECIPE, new GrinderRecipe(
//				new ItemStack(Items.ENDER_EYE),
//				ItemDusts.getDustByName("ender_eye", 2),
//				200, 22));
		
//		RecipeHandler.addRecipe(Reference.GRINDER_RECIPE, new GrinderRecipe(
//				new ItemStack(Items.ENDER_PEARL),
//				ItemDusts.getDustByName("ender_pearl", 2),
//				200, 22));
		
//		RecipeHandler.addRecipe(Reference.GRINDER_RECIPE, new GrinderRecipe(
//				new ItemStack(Blocks.LAPIS_ORE),
//				new ItemStack(Items.DYE, 10, 4),
//				170, 19));
		
//		RecipeHandler.addRecipe(Reference.GRINDER_RECIPE, new GrinderRecipe(
//				new ItemStack(Blocks.OBSIDIAN),
//				ItemDusts.getDustByName("obsidian", 4),
//				170, 19));
//
//		RecipeHandler.addRecipe(Reference.GRINDER_RECIPE, new GrinderRecipe(
//				new ItemStack(Items.BLAZE_ROD),
//				new ItemStack(Items.BLAZE_POWDER, 4),
//				170, 19));

//		if (OreUtil.doesOreExistAndValid("stoneMarble")) {
//			ItemStack marbleStack = getOre("stoneMarble");
//			marbleStack.setCount(1);
//			RecipeHandler.addRecipe(Reference.GRINDER_RECIPE, new GrinderRecipe(
//					marbleStack, ItemDusts.getDustByName("marble"),
//					120, 10));
//		}
//		if (OreUtil.doesOreExistAndValid("stoneBasalt")) {
//			ItemStack marbleStack = getOre("stoneBasalt");
//			marbleStack.setCount(1);
//			RecipeHandler.addRecipe(Reference.GRINDER_RECIPE, new GrinderRecipe(
//					marbleStack, ItemDusts.getDustByName("basalt"),
//					120, 10));
//		}

		//See comments bellow, this allows the ore to go to the product when it sometimes goes straight to dust.
//		RecipeHandler.addRecipe(Reference.GRINDER_RECIPE, new GrinderRecipe(
//			"oreCoal", new ItemStack(Items.COAL, 2),
//			270, 31));
//
//		RecipeHandler.addRecipe(Reference.GRINDER_RECIPE, new GrinderRecipe(
//			"oreDiamond", new ItemStack(Items.DIAMOND, 1),
//			270, 31));
//
//		RecipeHandler.addRecipe(Reference.GRINDER_RECIPE, new GrinderRecipe(
//			"oreEmerald", new ItemStack(Items.EMERALD, 1),
//			270, 31));
//
//		RecipeHandler.addRecipe(Reference.GRINDER_RECIPE, new GrinderRecipe(
//			"oreRedstone", new ItemStack(Items.REDSTONE, 8),
//			270, 31));
//
//		RecipeHandler.addRecipe(Reference.GRINDER_RECIPE, new GrinderRecipe(
//			"oreQuartz", new ItemStack(Items.QUARTZ, 2),
//			270, 31));


//		for (String oreDictionaryName : OreDictionary.getOreNames()) {
//			if (isDictPrefixed(oreDictionaryName, "ore", "gem", "ingot")) {
//				ItemStack oreStack = getDictOreOrEmpty(oreDictionaryName, 1);
//				String[] data = getDictData(oreDictionaryName);
//
//				//High-level ores shouldn't grind here
//				if (data[0].equals("ore") && (
//					data[1].equals("tungsten") ||
//						data[1].equals("titanium") ||
//						data[1].equals("aluminium") ||
//						data[1].equals("iridium") ||
//						data[1].equals("saltpeter")||
//						data[1].equals("coal") || //Done here to skip going to dust so it can go to the output
//						data[1].equals("diamond") || //For example diamond ore should go to diamonds not the diamond dust
//						data[1].equals("emerald") || //TODO possibly remove this and make it a bit more dyamic? (Check for furnace recipes? and then the block drop?)
//						data[1].equals("redstone") ||
//						data[1].equals("quartz")
//						) ||
//					oreStack.isEmpty())
//					continue;
//
//				boolean ore = data[0].equals("ore");
//				TechReborn.LOGGER.debug("Ore: " + data[1]);
//				ItemStack dust = getDictOreOrEmpty(joinDictName("dust", data[1]), ore ? 2 : 1);
//				if (dust.isEmpty() || dust.getItem() == null) {
//					continue;
//				}
//				dust = dust.copy();
//				if (ore) {
//					dust.setCount(2);
//				}
//				boolean useOreDict = true;
//				//Disables the ore dict for lapis, this is becuase it is oredict with dye. This may cause some other lapis ores to not be grindable, but we can fix that when that arrises.
//				if(data[1].equalsIgnoreCase("lapis")){
//					useOreDict = false;
//				}
//				RecipeHandler.addRecipe(Reference.GRINDER_RECIPE, new GrinderRecipe(oreStack, dust, ore ? 270 : 200, ore ? 31 : 22, useOreDict));
//			}
//		}
	}

	static void addVacuumFreezerRecipes() {
//		RecipeHandler.addRecipe(Reference.VACUUM_FREEZER_RECIPE,
//				new VacuumFreezerRecipe(new ItemStack(Blocks.ICE, 2), new ItemStack(Blocks.PACKED_ICE), 60, 64));
//		TODO: Fix recipe
//		RecipeHandler.addRecipe(Reference.VACUUM_FREEZER_RECIPE, new VacuumFreezerRecipe(
//				ItemIngots.getIngotByName("hot_tungstensteel"), ItemIngots.getIngotByName("tungstensteel"), 440, 64));

//		RecipeHandler.addRecipe(Reference.VACUUM_FREEZER_RECIPE, new VacuumFreezerRecipe(
//				ItemCells.getCellByName("heliumplasma"), ItemCells.getCellByName("helium"), 440, 64));
//
//		RecipeHandler.addRecipe(Reference.VACUUM_FREEZER_RECIPE,
//				new VacuumFreezerRecipe(ItemCells.getCellByName("water"), ItemCells.getCellByName("cell"), 60, 64));

	}

	static void addBlastFurnaceRecipes() {

//		RecipeHandler.addRecipe(Reference.BLAST_FURNACE_RECIPE,
//				new BlastFurnaceRecipe(ItemDustsSmall.getSmallDustByName("titanium", 4), null,
//						ItemIngots.getIngotByName("titanium"), null, 3600, 120, 1500));
//		RecipeHandler.addRecipe(Reference.BLAST_FURNACE_RECIPE,
//				new BlastFurnaceRecipe(ItemDusts.getDustByName("aluminum"), null, ItemIngots.getIngotByName("aluminum"),
//						null, 2200, 120, 1700));
//		RecipeHandler.addRecipe(Reference.BLAST_FURNACE_RECIPE,
//				new BlastFurnaceRecipe(ItemDustsSmall.getSmallDustByName("aluminum", 4), null,
//						ItemIngots.getIngotByName("aluminum"), null, 2200, 120, 1700));
//		RecipeHandler.addRecipe(Reference.BLAST_FURNACE_RECIPE,
//				new BlastFurnaceRecipe(ItemDusts.getDustByName("tungsten"), null, ItemIngots.getIngotByName("tungsten"),
//						null, 1800, 120, 2500));
//		RecipeHandler.addRecipe(Reference.BLAST_FURNACE_RECIPE,
//				new BlastFurnaceRecipe(ItemDustsSmall.getSmallDustByName("tungsten", 4), null,
//						ItemIngots.getIngotByName("tungsten"), null, 1800, 120, 2500));
//		RecipeHandler.addRecipe(Reference.BLAST_FURNACE_RECIPE, new BlastFurnaceRecipe(
//				ItemDusts.getDustByName("chrome"), null, ItemIngots.getIngotByName("chrome"), null, 4420, 120, 1700));
//		RecipeHandler.addRecipe(Reference.BLAST_FURNACE_RECIPE,
//				new BlastFurnaceRecipe(ItemDustsSmall.getSmallDustByName("chrome", 4), null,
//						ItemIngots.getIngotByName("chrome"), null, 4420, 120, 1700));
//		RecipeHandler.addRecipe(Reference.BLAST_FURNACE_RECIPE, new BlastFurnaceRecipe(ItemDusts.getDustByName("steel"),
//				null, ItemIngots.getIngotByName("steel"), null, 2800, 120, 1000));
//		RecipeHandler.addRecipe(Reference.BLAST_FURNACE_RECIPE,
//				new BlastFurnaceRecipe(ItemDustsSmall.getSmallDustByName("steel", 4), null,
//						ItemIngots.getIngotByName("steel"), null, 2800, 120, 1000));
//		RecipeHandler.addRecipe(Reference.BLAST_FURNACE_RECIPE,
//				new BlastFurnaceRecipe(ItemDusts.getDustByName("galena", 2), null, ItemIngots.getIngotByName("silver"),
//						ItemIngots.getIngotByName("lead"), 80, 120, 1500));
//		RecipeHandler.addRecipe(Reference.BLAST_FURNACE_RECIPE,
//				new BlastFurnaceRecipe(new ItemStack(Items.IRON_INGOT), ItemDusts.getDustByName("coal", 2),
//						ItemIngots.getIngotByName("steel"), ItemDusts.getDustByName("dark_ashes", 2), 500, 120, 1000));
//		RecipeHandler.addRecipe(Reference.BLAST_FURNACE_RECIPE,
//				new BlastFurnaceRecipe(ItemIngots.getIngotByName("tungsten"), ItemIngots.getIngotByName("steel"),
//						ItemIngots.getIngotByName("hot_tungstensteel"), ItemDusts.getDustByName("dark_ashes", 4), 500,
//						128, 3000));
//		RecipeHandler.addRecipe(Reference.BLAST_FURNACE_RECIPE,
//				new BlastFurnaceRecipe(new ItemStack(Blocks.IRON_ORE), ItemDusts.getDustByName("calcite"),
//						new ItemStack(Items.IRON_INGOT, 3), ItemDusts.getDustByName("dark_ashes"), 140, 120, 1000));
//		RecipeHandler.addRecipe(Reference.BLAST_FURNACE_RECIPE,
//				new BlastFurnaceRecipe(BlockOre.getOreByName("Pyrite"), ItemDusts.getDustByName("calcite"),
//						new ItemStack(Items.IRON_INGOT, 2), ItemDusts.getDustByName("dark_ashes"), 140, 120, 1000));
	}



	public static ItemStack getBucketWithFluid(Fluid fluid) {
		return FluidUtil.getFilledBucket(new FluidInstance(fluid, 1000));
	}

}
