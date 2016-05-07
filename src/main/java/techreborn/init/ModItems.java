package techreborn.init;

import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.PoweredItem;
import reborncore.common.util.BucketHandler;
import techreborn.Core;
import techreborn.blocks.BlockMachineFrame;
import techreborn.events.OreUnifier;
import techreborn.items.*;
import techreborn.items.armor.ItemLapotronPack;
import techreborn.items.armor.ItemLithiumBatpack;
import techreborn.items.armor.ItemTRArmour;
import techreborn.items.battery.ItemEnergyCrystal;
import techreborn.items.battery.ItemLapotronCrystal;
import techreborn.items.battery.ItemLithiumBattery;
import techreborn.items.battery.ItemReBattery;
import techreborn.items.tools.*;
import techreborn.api.Reference;

public class ModItems
{

	// This are deprected to stop people using them in the recipes.
	@Deprecated
	public static Item gems;
	@Deprecated
	public static Item ingots;
	@Deprecated
	public static Item nuggets;
	@Deprecated
	public static Item dusts;
	@Deprecated
	public static Item smallDusts;
	@Deprecated
	public static Item parts;
	@Deprecated
	public static Item cells;
	public static Item rockCutter;
	public static Item lithiumBatpack;
	public static Item lapotronpack;
	public static Item lithiumBattery;
	public static Item omniTool;
	public static Item lapotronicOrb;
	public static Item manual;
	public static Item uuMatter;
	public static Item plate;
	public static Item crushedOre;
	public static Item purifiedCrushedOre;
	public static Item cloakingDevice;

	public static Item bucketBerylium;
	public static Item bucketcalcium;
	public static Item bucketcalciumcarbonate;
	public static Item bucketChlorite;
	public static Item bucketDeuterium;
	public static Item bucketGlyceryl;
	public static Item bucketHelium;
	public static Item bucketHelium3;
	public static Item bucketHeliumplasma;
	public static Item bucketHydrogen;
	public static Item bucketLithium;
	public static Item bucketMercury;
	public static Item bucketMethane;
	public static Item bucketNitrocoalfuel;
	public static Item bucketNitrofuel;
	public static Item bucketNitrogen;
	public static Item bucketNitrogendioxide;
	public static Item bucketPotassium;
	public static Item bucketSilicon;
	public static Item bucketSodium;
	public static Item bucketSodiumpersulfate;
	public static Item bucketTritium;
	public static Item bucketWolframium;
	public static Item reBattery;
	public static Item treeTap;
	public static Item ironDrill;
	public static Item diamondDrill;
	public static Item advancedDrill;
	public static Item ironChainsaw;
	public static Item diamondChainsaw;
	public static Item advancedChainsaw;
	public static Item ironJackhammer;
	public static Item steelJackhammer;
	public static Item diamondJackhammer;
	public static Item nanosaber;
	public static Item hammer;
	public static Item wrench;
	public static Item lapotronCrystal;
	public static Item energyCrystal;
	public static Item scrapBox;
	public static Item frequencyTransmitter;

	public static Item bronzeSword;
	public static Item bronzePickaxe;
	public static Item bronzeSpade;
	public static Item bronzeAxe;
	public static Item bronzeHoe;

	public static Item bronzeHelmet;
	public static Item bronzeChestplate;
	public static Item bronzeLeggings;
	public static Item bronzeBoots;

	public static Item rubySword;
	public static Item rubyPickaxe;
	public static Item rubySpade;
	public static Item rubyAxe;
	public static Item rubyHoe;

	public static Item rubyHelmet;
	public static Item rubyChestplate;
	public static Item rubyLeggings;
	public static Item rubyBoots;

	public static Item sapphireSword;
	public static Item sapphirePickaxe;
	public static Item sapphireSpade;
	public static Item sapphireAxe;
	public static Item sapphireHoe;

	public static Item sapphireHelmet;
	public static Item sapphireChestplate;
	public static Item sapphireLeggings;
	public static Item sapphireBoots;

	public static Item peridotSword;
	public static Item peridotPickaxe;
	public static Item peridotSpade;
	public static Item peridotAxe;
	public static Item peridotHoe;

	public static Item peridotHelmet;
	public static Item peridotChestplate;
	public static Item peridotLeggings;
	public static Item peridotBoots;

	public static Item upgrades;

	public static Item missingRecipe;
	public static Item debug;

	public static void init() throws InstantiationException, IllegalAccessException
	{
		gems = new ItemGems();
		GameRegistry.registerItem(gems, "gem");
		ingots = new ItemIngots();
		GameRegistry.registerItem(ingots, "ingot");
		dusts = new ItemDusts();
		GameRegistry.registerItem(dusts, "dust");
		smallDusts = new ItemDustsSmall();
		GameRegistry.registerItem(smallDusts, "smallDust");
		plate = new ItemPlates();
		GameRegistry.registerItem(plate, "plates");
		nuggets = new ItemNuggets();
		GameRegistry.registerItem(nuggets, "nuggets");
		// purifiedCrushedOre = new ItemPurifiedCrushedOre();
		// GameRegistry.registerItem(purifiedCrushedOre, "purifiedCrushedOre");
		parts = new ItemParts();
		GameRegistry.registerItem(parts, "part");
		cells = new ItemCells();
		GameRegistry.registerItem(cells, "cell");
		for (int i = 0; i < ItemCells.types.length; i++)
		{
			if (FluidRegistry.getFluid("fluid" + ItemCells.types[i].toLowerCase()) != null)
			{
				FluidContainerRegistry.registerFluidContainer(
						FluidRegistry.getFluid("fluid" + ItemCells.types[i].toLowerCase()),
						ItemCells.getCellByName(ItemCells.types[i]), ItemCells.getCellByName("empty"));
			}else if(FluidRegistry.getFluid(ItemCells.types[i].toLowerCase()) !=  null){

				FluidContainerRegistry.registerFluidContainer(
						FluidRegistry.getFluid(ItemCells.types[i].toLowerCase()),
						ItemCells.getCellByName(ItemCells.types[i]), ItemCells.getCellByName("empty"));
			}
		}
		rockCutter = PoweredItem.createItem(ItemRockCutter.class);
		GameRegistry.registerItem(rockCutter, "rockCutter");
		lithiumBatpack = PoweredItem.createItem(ItemLithiumBatpack.class);
		GameRegistry.registerItem(lithiumBatpack, "lithiumBatpack");
		lapotronpack = PoweredItem.createItem(ItemLapotronPack.class);
		GameRegistry.registerItem(lapotronpack, "lapotronPack");
		lithiumBattery = PoweredItem.createItem(ItemLithiumBattery.class);
		GameRegistry.registerItem(lithiumBattery, "lithiumBattery");
		lapotronicOrb = PoweredItem.createItem(ItemLapotronicOrb.class);
		GameRegistry.registerItem(lapotronicOrb, "lapotronicOrb");
		omniTool = PoweredItem.createItem(ItemOmniTool.class);
		GameRegistry.registerItem(omniTool, "omniTool");
		energyCrystal = PoweredItem.createItem(ItemEnergyCrystal.class);
		GameRegistry.registerItem(energyCrystal, "energycrystal");
		lapotronCrystal = PoweredItem.createItem(ItemLapotronCrystal.class);
		GameRegistry.registerItem(lapotronCrystal, "lapotroncrystal");

		manual = new ItemTechManual();
		GameRegistry.registerItem(manual, "techmanuel");
		uuMatter = new ItemUUmatter();
		GameRegistry.registerItem(uuMatter, "uumatter");
		reBattery = PoweredItem.createItem(ItemReBattery.class);
		GameRegistry.registerItem(reBattery, "rebattery");
		treeTap = new ItemTreeTap();
		GameRegistry.registerItem(treeTap, "treetap");

		ironDrill = PoweredItem.createItem(ItemIronDrill.class);
		GameRegistry.registerItem(ironDrill, "irondrill");
		diamondDrill = PoweredItem.createItem(ItemDiamondDrill.class);
		GameRegistry.registerItem(diamondDrill, "diamonddrill");
		advancedDrill = PoweredItem.createItem(ItemAdvancedDrill.class);
		GameRegistry.registerItem(advancedDrill, "advanceddrill");

		ironChainsaw = PoweredItem.createItem(ItemIronChainsaw.class);
		GameRegistry.registerItem(ironChainsaw, "ironchainsaw");
		diamondChainsaw = PoweredItem.createItem(ItemDiamondChainsaw.class);
		GameRegistry.registerItem(diamondChainsaw, "diamondchainsaw");
		advancedChainsaw = PoweredItem.createItem(ItemAdvancedChainsaw.class);
		GameRegistry.registerItem(advancedChainsaw, "advancedchainsaw");

		ironJackhammer = PoweredItem.createItem(ItemIronJackhammer.class);
		GameRegistry.registerItem(ironJackhammer, "ironjackhammer");
		steelJackhammer = PoweredItem.createItem(ItemSteelJackhammer.class);
		GameRegistry.registerItem(steelJackhammer, "steeljackhammer");
		diamondJackhammer = PoweredItem.createItem(ItemDiamondJackhammer.class);
		GameRegistry.registerItem(diamondJackhammer, "diamondjackhammer");

		bronzeSword = new ItemTRSword(Reference.BRONZE);
		GameRegistry.registerItem(bronzeSword, "bronzeSword");
		bronzePickaxe = new ItemTRPickaxe(Reference.BRONZE);
		GameRegistry.registerItem(bronzePickaxe, "bronzePickaxe");
		bronzeSpade = new ItemTRSpade(Reference.BRONZE);
		GameRegistry.registerItem(bronzeSpade, "bronzeSpade");
		bronzeAxe = new ItemTRAxe(Reference.BRONZE);
		GameRegistry.registerItem(bronzeAxe, "bronzeAxe");
		bronzeHoe = new ItemTRHoe(Reference.BRONZE);
		GameRegistry.registerItem(bronzeHoe, "bronzeHoe");

		bronzeHelmet = new ItemTRArmour(Reference.BRONZE_ARMOUR, EntityEquipmentSlot.HEAD);
		GameRegistry.registerItem(bronzeHelmet, "bronzeHelmet");
		bronzeChestplate = new ItemTRArmour(Reference.BRONZE_ARMOUR, EntityEquipmentSlot.CHEST);
		GameRegistry.registerItem(bronzeChestplate, "bronzeChestplate");
		bronzeLeggings = new ItemTRArmour(Reference.BRONZE_ARMOUR, EntityEquipmentSlot.LEGS);
		GameRegistry.registerItem(bronzeLeggings, "bronzeLeggings");
		bronzeBoots = new ItemTRArmour(Reference.BRONZE_ARMOUR, EntityEquipmentSlot.FEET);
		GameRegistry.registerItem(bronzeBoots, "bronzeBoots");

		rubySword = new ItemTRSword(Reference.RUBY);
		GameRegistry.registerItem(rubySword, "rubySword");
		rubyPickaxe = new ItemTRPickaxe(Reference.RUBY);
		GameRegistry.registerItem(rubyPickaxe, "rubyPickaxe");
		rubySpade = new ItemTRSpade(Reference.RUBY);
		GameRegistry.registerItem(rubySpade, "rubySpade");
		rubyAxe = new ItemTRAxe(Reference.RUBY);
		GameRegistry.registerItem(rubyAxe, "rubyAxe");
		rubyHoe = new ItemTRHoe(Reference.RUBY);
		GameRegistry.registerItem(rubyHoe, "rubyHoe");

		rubyHelmet = new ItemTRArmour(Reference.RUBY_ARMOUR, EntityEquipmentSlot.HEAD);
		GameRegistry.registerItem(rubyHelmet, "rubyHelmet");
		rubyChestplate = new ItemTRArmour(Reference.RUBY_ARMOUR, EntityEquipmentSlot.CHEST);
		GameRegistry.registerItem(rubyChestplate, "rubyChestplate");
		rubyLeggings = new ItemTRArmour(Reference.RUBY_ARMOUR, EntityEquipmentSlot.LEGS);
		GameRegistry.registerItem(rubyLeggings, "rubyLeggings");
		rubyBoots = new ItemTRArmour(Reference.RUBY_ARMOUR, EntityEquipmentSlot.FEET);
		GameRegistry.registerItem(rubyBoots, "rubyBoots");

		sapphireSword = new ItemTRSword(Reference.SAPPHIRE);
		GameRegistry.registerItem(sapphireSword, "sapphireSword");
		sapphirePickaxe = new ItemTRPickaxe(Reference.SAPPHIRE);
		GameRegistry.registerItem(sapphirePickaxe, "sapphirePickaxe");
		sapphireSpade = new ItemTRSpade(Reference.SAPPHIRE);
		GameRegistry.registerItem(sapphireSpade, "sapphireSpade");
		sapphireAxe = new ItemTRAxe(Reference.SAPPHIRE);
		GameRegistry.registerItem(sapphireAxe, "sapphireAxe");
		sapphireHoe = new ItemTRHoe(Reference.SAPPHIRE);
		GameRegistry.registerItem(sapphireHoe, "sapphireHoe");

		sapphireHelmet = new ItemTRArmour(Reference.SAPPHIRE_ARMOUR, EntityEquipmentSlot.HEAD);
		GameRegistry.registerItem(sapphireHelmet, "sapphireHelmet");
		sapphireChestplate = new ItemTRArmour(Reference.SAPPHIRE_ARMOUR, EntityEquipmentSlot.CHEST);
		GameRegistry.registerItem(sapphireChestplate, "sapphireChestplate");
		sapphireLeggings = new ItemTRArmour(Reference.SAPPHIRE_ARMOUR, EntityEquipmentSlot.LEGS);
		GameRegistry.registerItem(sapphireLeggings, "sapphireLeggings");
		sapphireBoots = new ItemTRArmour(Reference.SAPPHIRE_ARMOUR, EntityEquipmentSlot.FEET);
		GameRegistry.registerItem(sapphireBoots, "sapphireBoots");

		peridotSword = new ItemTRSword(Reference.PERIDOT);
		GameRegistry.registerItem(peridotSword, "peridotSword");
		peridotPickaxe = new ItemTRPickaxe(Reference.PERIDOT);
		GameRegistry.registerItem(peridotPickaxe, "peridotPickaxe");
		peridotSpade = new ItemTRSpade(Reference.PERIDOT);
		GameRegistry.registerItem(peridotSpade, "peridotSpade");
		peridotAxe = new ItemTRAxe(Reference.PERIDOT);
		GameRegistry.registerItem(peridotAxe, "peridotAxe");
		peridotHoe = new ItemTRHoe(Reference.PERIDOT);
		GameRegistry.registerItem(peridotHoe, "peridotHoe");

		peridotHelmet = new ItemTRArmour(Reference.PERIDOT_ARMOUR, EntityEquipmentSlot.HEAD);
		GameRegistry.registerItem(peridotHelmet, "peridotHelmet");
		peridotChestplate = new ItemTRArmour(Reference.PERIDOT_ARMOUR, EntityEquipmentSlot.CHEST);
		GameRegistry.registerItem(peridotChestplate, "peridotChestplate");
		peridotLeggings = new ItemTRArmour(Reference.PERIDOT_ARMOUR, EntityEquipmentSlot.LEGS);
		GameRegistry.registerItem(peridotLeggings, "peridotLeggings");
		peridotBoots = new ItemTRArmour(Reference.PERIDOT_ARMOUR, EntityEquipmentSlot.FEET);
		GameRegistry.registerItem(peridotBoots, "peridotBoots");
		
		hammer = new ItemHammer(100);
		GameRegistry.registerItem(hammer, "hammer");

		wrench = new ItemWrench();
		GameRegistry.registerItem(wrench, "wrench");

		nanosaber = new ItemNanosaber();
		GameRegistry.registerItem(nanosaber, "nanosaber");

		scrapBox = new ItemScrapBox();
		GameRegistry.registerItem(scrapBox, "scrapbox");
		
		frequencyTransmitter = new ItemFrequencyTransmitter();
		GameRegistry.registerItem(frequencyTransmitter, "frequencyTransmitter");

		upgrades = new ItemUpgrades();
		GameRegistry.registerItem(upgrades, "upgrades");

		cloakingDevice = PoweredItem.createItem(ItemCloakingDevice.class);
		GameRegistry.registerItem(cloakingDevice, "cloakingdevice");

		// buckets
		bucketBerylium = new ItemFluidbucket(ModFluids.BlockFluidBerylium);
		bucketBerylium.setUnlocalizedName("bucketberylium").setContainerItem(Items.BUCKET);
		GameRegistry.registerItem(bucketBerylium, "bucketberylium");
		FluidContainerRegistry.registerFluidContainer(
				FluidRegistry.getFluidStack("fluidberylium", FluidContainerRegistry.BUCKET_VOLUME),
				new ItemStack(bucketBerylium), new ItemStack(Items.BUCKET));
		BucketHandler.INSTANCE.buckets.put(ModFluids.BlockFluidBerylium.getFluid(), bucketBerylium);

		bucketcalcium = new ItemFluidbucket(ModFluids.BlockFluidCalcium);
		bucketcalcium.setUnlocalizedName("bucketcalcium").setContainerItem(Items.BUCKET);
		GameRegistry.registerItem(bucketcalcium, "bucketcalcium");
		FluidContainerRegistry.registerFluidContainer(
				FluidRegistry.getFluidStack("fluidcalcium", FluidContainerRegistry.BUCKET_VOLUME),
				new ItemStack(bucketcalcium), new ItemStack(Items.BUCKET));
		BucketHandler.INSTANCE.buckets.put(ModFluids.BlockFluidCalcium.getFluid(), bucketcalcium);

		bucketcalciumcarbonate = new ItemFluidbucket(ModFluids.BlockFluidCalciumCarbonate);
		bucketcalciumcarbonate.setUnlocalizedName("bucketcalciumcarbonate").setContainerItem(Items.BUCKET);
		GameRegistry.registerItem(bucketcalciumcarbonate, "bucketcalciumcarbonate");
		FluidContainerRegistry.registerFluidContainer(
				FluidRegistry.getFluidStack("fluidcalciumcarbonate", FluidContainerRegistry.BUCKET_VOLUME),
				new ItemStack(bucketcalciumcarbonate), new ItemStack(Items.BUCKET));
		BucketHandler.INSTANCE.buckets.put(ModFluids.BlockFluidCalciumCarbonate.getFluid(),
				bucketcalciumcarbonate);

		bucketChlorite = new ItemFluidbucket(ModFluids.BlockFluidChlorite);
		bucketChlorite.setUnlocalizedName("bucketchlorite").setContainerItem(Items.BUCKET);
		GameRegistry.registerItem(bucketChlorite, "bucketcalchlorite");
		FluidContainerRegistry.registerFluidContainer(
				FluidRegistry.getFluidStack("fluidchlorite", FluidContainerRegistry.BUCKET_VOLUME),
				new ItemStack(bucketcalciumcarbonate), new ItemStack(Items.BUCKET));
		BucketHandler.INSTANCE.buckets.put(ModFluids.BlockFluidChlorite.getFluid(), bucketChlorite);

		bucketDeuterium = new ItemFluidbucket(ModFluids.BlockFluidDeuterium);
		bucketDeuterium.setUnlocalizedName("bucketdeuterium").setContainerItem(Items.BUCKET);
		GameRegistry.registerItem(bucketDeuterium, "bucketdeuterium");
		FluidContainerRegistry.registerFluidContainer(
				FluidRegistry.getFluidStack("fluiddeuterium", FluidContainerRegistry.BUCKET_VOLUME),
				new ItemStack(bucketDeuterium), new ItemStack(Items.BUCKET));
		BucketHandler.INSTANCE.buckets.put(ModFluids.BlockFluidDeuterium.getFluid(), bucketDeuterium);

		bucketGlyceryl = new ItemFluidbucket(ModFluids.BlockFluidGlyceryl);
		bucketGlyceryl.setUnlocalizedName("bucketglyceryl").setContainerItem(Items.BUCKET);
		GameRegistry.registerItem(bucketGlyceryl, "bucketglyceryl");
		FluidContainerRegistry.registerFluidContainer(
				FluidRegistry.getFluidStack("fluidglyceryl", FluidContainerRegistry.BUCKET_VOLUME),
				new ItemStack(bucketGlyceryl), new ItemStack(Items.BUCKET));
		BucketHandler.INSTANCE.buckets.put(ModFluids.BlockFluidGlyceryl.getFluid(), bucketGlyceryl);

		bucketHelium = new ItemFluidbucket(ModFluids.BlockFluidHelium);
		bucketHelium.setUnlocalizedName("buckethelium").setContainerItem(Items.BUCKET);
		GameRegistry.registerItem(bucketHelium, "buckethelium");
		FluidContainerRegistry.registerFluidContainer(
				FluidRegistry.getFluidStack("fluidhelium", FluidContainerRegistry.BUCKET_VOLUME),
				new ItemStack(bucketHelium), new ItemStack(Items.BUCKET));
		BucketHandler.INSTANCE.buckets.put(ModFluids.BlockFluidHelium.getFluid(), bucketHelium);

		bucketHelium3 = new ItemFluidbucket(ModFluids.BlockFluidHelium3);
		bucketHelium3.setUnlocalizedName("buckethelium3").setContainerItem(Items.BUCKET);
		GameRegistry.registerItem(bucketHelium3, "buckethelium3");
		FluidContainerRegistry.registerFluidContainer(
				FluidRegistry.getFluidStack("fluidhelium3", FluidContainerRegistry.BUCKET_VOLUME),
				new ItemStack(bucketHelium3), new ItemStack(Items.BUCKET));
		BucketHandler.INSTANCE.buckets.put(ModFluids.BlockFluidHelium3.getFluid(), bucketHelium3);

		bucketHeliumplasma = new ItemFluidbucket(ModFluids.BlockFluidHeliumplasma);
		bucketHeliumplasma.setUnlocalizedName("bucketheliumplasma").setContainerItem(Items.BUCKET);
		GameRegistry.registerItem(bucketHeliumplasma, "bucketheliumplasma");
		FluidContainerRegistry.registerFluidContainer(
				FluidRegistry.getFluidStack("fluidheliumplasma", FluidContainerRegistry.BUCKET_VOLUME),
				new ItemStack(bucketHeliumplasma), new ItemStack(Items.BUCKET));
		BucketHandler.INSTANCE.buckets.put(ModFluids.BlockFluidHeliumplasma.getFluid(), bucketHeliumplasma);

		bucketHydrogen = new ItemFluidbucket(ModFluids.BlockFluidHydrogen);
		bucketHydrogen.setUnlocalizedName("buckethydrogen").setContainerItem(Items.BUCKET);
		GameRegistry.registerItem(bucketHydrogen, "buckethydrogen");
		FluidContainerRegistry.registerFluidContainer(
				FluidRegistry.getFluidStack("fluidhydrogen", FluidContainerRegistry.BUCKET_VOLUME),
				new ItemStack(bucketHydrogen), new ItemStack(Items.BUCKET));
		BucketHandler.INSTANCE.buckets.put(ModFluids.BlockFluidHydrogen.getFluid(), bucketHydrogen);

		bucketLithium = new ItemFluidbucket(ModFluids.BlockFluidLithium);
		bucketLithium.setUnlocalizedName("bucketlithium").setContainerItem(Items.BUCKET);
		GameRegistry.registerItem(bucketLithium, "bucketlithium");
		FluidContainerRegistry.registerFluidContainer(
				FluidRegistry.getFluidStack("fluidlithium", FluidContainerRegistry.BUCKET_VOLUME),
				new ItemStack(bucketLithium), new ItemStack(Items.BUCKET));
		BucketHandler.INSTANCE.buckets.put(ModFluids.BlockFluidLithium.getFluid(), bucketLithium);

		bucketMercury = new ItemFluidbucket(ModFluids.BlockFluidMercury);
		bucketMercury.setUnlocalizedName("bucketmercury").setContainerItem(Items.BUCKET);
		GameRegistry.registerItem(bucketMercury, "bucketmercury");
		FluidContainerRegistry.registerFluidContainer(
				FluidRegistry.getFluidStack("fluidmercury", FluidContainerRegistry.BUCKET_VOLUME),
				new ItemStack(bucketMercury), new ItemStack(Items.BUCKET));
		BucketHandler.INSTANCE.buckets.put(ModFluids.BlockFluidMercury.getFluid(), bucketMercury);

		bucketMethane = new ItemFluidbucket(ModFluids.BlockFluidMethane);
		bucketMethane.setUnlocalizedName("bucketmethane").setContainerItem(Items.BUCKET);
		GameRegistry.registerItem(bucketMethane, "bucketmethane");
		FluidContainerRegistry.registerFluidContainer(
				FluidRegistry.getFluidStack("fluidmethane", FluidContainerRegistry.BUCKET_VOLUME),
				new ItemStack(bucketMethane), new ItemStack(Items.BUCKET));
		BucketHandler.INSTANCE.buckets.put(ModFluids.BlockFluidMethane.getFluid(), bucketMethane);

		bucketNitrocoalfuel = new ItemFluidbucket(ModFluids.BlockFluidNitrocoalfuel);
		bucketNitrocoalfuel.setUnlocalizedName("bucketnitrocoalfuel").setContainerItem(Items.BUCKET);
		GameRegistry.registerItem(bucketNitrocoalfuel, "bucketnitrocoalfuel");
		FluidContainerRegistry.registerFluidContainer(
				FluidRegistry.getFluidStack("fluidnitrocoalfuel", FluidContainerRegistry.BUCKET_VOLUME),
				new ItemStack(bucketNitrocoalfuel), new ItemStack(Items.BUCKET));
		BucketHandler.INSTANCE.buckets.put(ModFluids.BlockFluidNitrocoalfuel.getFluid(), bucketNitrocoalfuel);

		bucketNitrofuel = new ItemFluidbucket(ModFluids.BlockFluidNitrofuel);
		bucketNitrofuel.setUnlocalizedName("bucketnitrofuel").setContainerItem(Items.BUCKET);
		GameRegistry.registerItem(bucketNitrofuel, "bucketnitrofuel");
		FluidContainerRegistry.registerFluidContainer(
				FluidRegistry.getFluidStack("fluidnitrofuel", FluidContainerRegistry.BUCKET_VOLUME),
				new ItemStack(bucketNitrofuel), new ItemStack(Items.BUCKET));
		BucketHandler.INSTANCE.buckets.put(ModFluids.BlockFluidNitrofuel.getFluid(), bucketNitrofuel);

		bucketNitrogen = new ItemFluidbucket(ModFluids.BlockFluidNitrogen);
		bucketNitrogen.setUnlocalizedName("bucketnitrogen").setContainerItem(Items.BUCKET);
		GameRegistry.registerItem(bucketNitrogen, "bucketnitrogen");
		FluidContainerRegistry.registerFluidContainer(
				FluidRegistry.getFluidStack("fluidnitrogen", FluidContainerRegistry.BUCKET_VOLUME),
				new ItemStack(bucketNitrogen), new ItemStack(Items.BUCKET));
		BucketHandler.INSTANCE.buckets.put(ModFluids.BlockFluidNitrogen.getFluid(), bucketNitrogen);

		bucketNitrogendioxide = new ItemFluidbucket(ModFluids.BlockFluidNitrogendioxide);
		bucketNitrogendioxide.setUnlocalizedName("bucketnitrogendioxide").setContainerItem(Items.BUCKET);
		GameRegistry.registerItem(bucketNitrogendioxide, "bucketnitrogendioxide");
		FluidContainerRegistry.registerFluidContainer(
				FluidRegistry.getFluidStack("fluidnitrogendioxide", FluidContainerRegistry.BUCKET_VOLUME),
				new ItemStack(bucketNitrogendioxide), new ItemStack(Items.BUCKET));
		BucketHandler.INSTANCE.buckets.put(ModFluids.BlockFluidNitrogendioxide.getFluid(),
				bucketNitrogendioxide);

		bucketPotassium = new ItemFluidbucket(ModFluids.BlockFluidPotassium);
		bucketPotassium.setUnlocalizedName("bucketpotassium").setContainerItem(Items.BUCKET);
		GameRegistry.registerItem(bucketPotassium, "bucketpotassium");
		FluidContainerRegistry.registerFluidContainer(
				FluidRegistry.getFluidStack("fluidpotassium", FluidContainerRegistry.BUCKET_VOLUME),
				new ItemStack(bucketPotassium), new ItemStack(Items.BUCKET));
		BucketHandler.INSTANCE.buckets.put(ModFluids.BlockFluidPotassium.getFluid(), bucketPotassium);

		bucketSilicon = new ItemFluidbucket(ModFluids.BlockFluidSilicon);
		bucketSilicon.setUnlocalizedName("bucketsilicon").setContainerItem(Items.BUCKET);
		GameRegistry.registerItem(bucketSilicon, "bucketsilicon");
		FluidContainerRegistry.registerFluidContainer(
				FluidRegistry.getFluidStack("fluidsilicon", FluidContainerRegistry.BUCKET_VOLUME),
				new ItemStack(bucketSilicon), new ItemStack(Items.BUCKET));
		BucketHandler.INSTANCE.buckets.put(ModFluids.BlockFluidSilicon.getFluid(), bucketSilicon);

		bucketSodium = new ItemFluidbucket(ModFluids.BlockFluidSodium);
		bucketSodium.setUnlocalizedName("bucketsodium").setContainerItem(Items.BUCKET);
		GameRegistry.registerItem(bucketSodium, "bucketsodium");
		FluidContainerRegistry.registerFluidContainer(
				FluidRegistry.getFluidStack("fluidsodium", FluidContainerRegistry.BUCKET_VOLUME),
				new ItemStack(bucketSodium), new ItemStack(Items.BUCKET));
		BucketHandler.INSTANCE.buckets.put(ModFluids.BlockFluidSodium.getFluid(), bucketSodium);

		bucketSodiumpersulfate = new ItemFluidbucket(ModFluids.BlockFluidSodiumpersulfate);
		bucketSodiumpersulfate.setUnlocalizedName("bucketsodiumpersulfate").setContainerItem(Items.BUCKET);
		GameRegistry.registerItem(bucketSodiumpersulfate, "bucketsodiumpersulfate");
		FluidContainerRegistry.registerFluidContainer(
				FluidRegistry.getFluidStack("fluidsodiumpersulfate", FluidContainerRegistry.BUCKET_VOLUME),
				new ItemStack(bucketSodiumpersulfate), new ItemStack(Items.BUCKET));
		BucketHandler.INSTANCE.buckets.put(ModFluids.BlockFluidSodiumpersulfate.getFluid(),
				bucketSodiumpersulfate);

		bucketTritium = new ItemFluidbucket(ModFluids.BlockFluidTritium);
		bucketTritium.setUnlocalizedName("buckettritium").setContainerItem(Items.BUCKET);
		GameRegistry.registerItem(bucketTritium, "buckettritium");
		FluidContainerRegistry.registerFluidContainer(
				FluidRegistry.getFluidStack("fluidtritium", FluidContainerRegistry.BUCKET_VOLUME),
				new ItemStack(bucketTritium), new ItemStack(Items.BUCKET));
		BucketHandler.INSTANCE.buckets.put(ModFluids.BlockFluidTritium.getFluid(), bucketTritium);

		bucketWolframium = new ItemFluidbucket(ModFluids.BlockFluidWolframium);
		bucketWolframium.setUnlocalizedName("bucketwolframium").setContainerItem(Items.BUCKET);
		GameRegistry.registerItem(bucketWolframium, "bucketwolframium");
		FluidContainerRegistry.registerFluidContainer(
				FluidRegistry.getFluidStack("fluidwolframium", FluidContainerRegistry.BUCKET_VOLUME),
				new ItemStack(bucketWolframium), new ItemStack(Items.BUCKET));
		BucketHandler.INSTANCE.buckets.put(ModFluids.BlockFluidWolframium.getFluid(), bucketWolframium);

		missingRecipe = new ItemMissingRecipe().setUnlocalizedName("missingRecipe");
		GameRegistry.registerItem(missingRecipe, "mssingRecipe");

		debug = new ItemDebugTool();
		GameRegistry.registerItem(debug, "debug");

		MinecraftForge.EVENT_BUS.register(BucketHandler.INSTANCE);

		Core.logHelper.info("TechReborns Items Loaded");

		registerOreDict();

		BlockMachineBase.advancedMachineStack = BlockMachineFrame.getFrameByName("advancedMachine", 1);
		BlockMachineBase.machineStack = BlockMachineFrame.getFrameByName("machine", 1);
	}

	public static void registerOreDict()
	{

		OreUnifier.registerOre("gemRuby", ItemGems.getGemByName("ruby"));
		OreUnifier.registerOre("gemSapphire", ItemGems.getGemByName("sapphire"));
		OreUnifier.registerOre("gemPeridot", ItemGems.getGemByName("peridot"));
		OreUnifier.registerOre("gemRedGarnet", ItemGems.getGemByName("redGarnet"));
		OreUnifier.registerOre("gemYellowGarnet", ItemGems.getGemByName("yellowGarnet"));

		// Dusts
		OreUnifier.registerOre("dustAlmandine", ItemDusts.getDustByName("almandine"));
		OreUnifier.registerOre("dustAluminum", ItemDusts.getDustByName("aluminum"));
		OreUnifier.registerOre("dustAluminium", ItemDusts.getDustByName("aluminum"));
		OreUnifier.registerOre("dustAndradite", ItemDusts.getDustByName("andradite"));
		OreUnifier.registerOre("dustAsh", ItemDusts.getDustByName("ashes"));
		OreUnifier.registerOre("dustBasalt", ItemDusts.getDustByName("basalt"));
		OreUnifier.registerOre("dustBauxite", ItemDusts.getDustByName("bauxite"));
		OreUnifier.registerOre("dustBrass", ItemDusts.getDustByName("brass"));
		OreUnifier.registerOre("dustBronze", ItemDusts.getDustByName("bronze"));
		OreUnifier.registerOre("dustCalcite", ItemDusts.getDustByName("calcite"));
		OreUnifier.registerOre("dustCharcoal", ItemDusts.getDustByName("charcoal"));
		OreUnifier.registerOre("dustChrome", ItemDusts.getDustByName("chrome"));
		OreUnifier.registerOre("dustCinnabar", ItemDusts.getDustByName("cinnabar"));
		OreUnifier.registerOre("dustClay", ItemDusts.getDustByName("clay"));
		OreUnifier.registerOre("dustCoal", ItemDusts.getDustByName("coal"));
		OreUnifier.registerOre("dustCopper", ItemDusts.getDustByName("copper"));
		OreUnifier.registerOre("dustDarkAsh", ItemDusts.getDustByName("darkAshes"));
		OreUnifier.registerOre("dustDiamond", ItemDusts.getDustByName("diamond"));
		OreUnifier.registerOre("dustElectrum", ItemDusts.getDustByName("electrum"));
		OreUnifier.registerOre("dustEmerald", ItemDusts.getDustByName("emerald"));
		OreUnifier.registerOre("dustEnderEye", ItemDusts.getDustByName("enderEye"));
		OreUnifier.registerOre("dustEnderPearl", ItemDusts.getDustByName("enderPearl"));
		OreUnifier.registerOre("dustEndstone", ItemDusts.getDustByName("endstone"));
		OreUnifier.registerOre("dustFlint", ItemDusts.getDustByName("flint"));
		OreUnifier.registerOre("dustGalena", ItemDusts.getDustByName("galena"));
		OreUnifier.registerOre("dustGold", ItemDusts.getDustByName("gold"));
		OreUnifier.registerOre("dustGrossular", ItemDusts.getDustByName("grossular"));
		OreUnifier.registerOre("dustInvar", ItemDusts.getDustByName("invar"));
		OreUnifier.registerOre("dustIron", ItemDusts.getDustByName("iron"));
		OreUnifier.registerOre("dustLapis", ItemDusts.getDustByName("lazurite"));
		OreUnifier.registerOre("dustLazurite", ItemDusts.getDustByName("lazurite"));
		OreUnifier.registerOre("dustLead", ItemDusts.getDustByName("lead"));
		OreUnifier.registerOre("dustMagnesium", ItemDusts.getDustByName("magnesium"));
		OreUnifier.registerOre("dustManganese", ItemDusts.getDustByName("manganese"));
		OreUnifier.registerOre("dustMarble", ItemDusts.getDustByName("marble"));
		OreUnifier.registerOre("dustNetherrack", ItemDusts.getDustByName("netherrack"));
		OreUnifier.registerOre("dustNickel", ItemDusts.getDustByName("nickel"));
		OreUnifier.registerOre("dustObsidian", ItemDusts.getDustByName("obsidian"));
		OreUnifier.registerOre("dustPeridot", ItemDusts.getDustByName("peridot"));
		OreUnifier.registerOre("dustPhosphorous", ItemDusts.getDustByName("phosphorous"));
		OreUnifier.registerOre("dustPlatinum", ItemDusts.getDustByName("platinum"));
		OreUnifier.registerOre("dustPyrite", ItemDusts.getDustByName("pyrite"));
		OreUnifier.registerOre("dustPyrope", ItemDusts.getDustByName("pyrope"));
		OreUnifier.registerOre("dustRedGarnet", ItemDusts.getDustByName("redGarnet"));
		OreUnifier.registerOre("dustRedrock", ItemDusts.getDustByName("redrock"));
		OreUnifier.registerOre("dustRuby", ItemDusts.getDustByName("ruby"));
		OreUnifier.registerOre("dustSaltpeter", ItemDusts.getDustByName("saltpeter"));
		OreUnifier.registerOre("dustSapphire", ItemDusts.getDustByName("sapphire"));
		OreUnifier.registerOre("dustSilver", ItemDusts.getDustByName("silver"));
		OreUnifier.registerOre("dustSodalite", ItemDusts.getDustByName("sodalite"));
		OreUnifier.registerOre("dustSpessartine", ItemDusts.getDustByName("spessartine"));
		OreUnifier.registerOre("dustSphalerite", ItemDusts.getDustByName("sphalerite"));
		OreUnifier.registerOre("dustSteel", ItemDusts.getDustByName("steel"));
		OreUnifier.registerOre("dustSulfur", ItemDusts.getDustByName("sulfur"));
		OreUnifier.registerOre("dustCopper", ItemDusts.getDustByName("copper"));
		OreUnifier.registerOre("dustTin", ItemDusts.getDustByName("tin"));
		OreUnifier.registerOre("dustTitanium", ItemDusts.getDustByName("titanium"));
		OreUnifier.registerOre("dustTungsten", ItemDusts.getDustByName("tungsten"));
		OreUnifier.registerOre("dustUvarovite", ItemDusts.getDustByName("uvarovite"));
		OreUnifier.registerOre("dustVoidstone", ItemDusts.getDustByName("voidstone"));
		OreUnifier.registerOre("dustYellowGarnet", ItemDusts.getDustByName("yellowGarnet"));
		OreUnifier.registerOre("dustZinc", ItemDusts.getDustByName("zinc"));
		OreUnifier.registerOre("dustOlivine", ItemDusts.getDustByName("olivine"));
		OreUnifier.registerOre("dustWood", ItemDusts.getDustByName("sawDust"));
		OreUnifier.registerOre("pulpWood", ItemDusts.getDustByName("sawDust"));

		// Small Dusts
		OreUnifier.registerOre("dustAlmandine", ItemDusts.getDustByName("almandine"));
		OreUnifier.registerOre("dustAluminum", ItemDusts.getDustByName("aluminum"));
		OreUnifier.registerOre("dustAluminium", ItemDusts.getDustByName("aluminum"));
		OreUnifier.registerOre("dustAndradite", ItemDusts.getDustByName("andradite"));
		OreUnifier.registerOre("dustAsh", ItemDusts.getDustByName("ashes"));
		OreUnifier.registerOre("dustBasalt", ItemDusts.getDustByName("basalt"));
		OreUnifier.registerOre("dustBauxite", ItemDusts.getDustByName("bauxite"));
		OreUnifier.registerOre("dustBrass", ItemDusts.getDustByName("brass"));
		OreUnifier.registerOre("dustBronze", ItemDusts.getDustByName("bronze"));
		OreUnifier.registerOre("dustCalcite", ItemDusts.getDustByName("calcite"));
		OreUnifier.registerOre("dustCharcoal", ItemDusts.getDustByName("charcoal"));
		OreUnifier.registerOre("dustChrome", ItemDusts.getDustByName("chrome"));
		OreUnifier.registerOre("dustCinnabar", ItemDusts.getDustByName("cinnabar"));
		OreUnifier.registerOre("dustClay", ItemDusts.getDustByName("clay"));
		OreUnifier.registerOre("dustCoal", ItemDusts.getDustByName("coal"));
		OreUnifier.registerOre("dustCopper", ItemDusts.getDustByName("copper"));
		OreUnifier.registerOre("dustDarkAsh", ItemDusts.getDustByName("darkAshes"));
		OreUnifier.registerOre("dustDiamond", ItemDusts.getDustByName("diamond"));
		OreUnifier.registerOre("dustElectrum", ItemDusts.getDustByName("electrum"));
		OreUnifier.registerOre("dustEmerald", ItemDusts.getDustByName("emerald"));
		OreUnifier.registerOre("dustEnderEye", ItemDusts.getDustByName("enderEye"));
		OreUnifier.registerOre("dustEnderPearl", ItemDusts.getDustByName("enderPearl"));
		OreUnifier.registerOre("dustEndstone", ItemDusts.getDustByName("endstone"));
		OreUnifier.registerOre("dustFlint", ItemDusts.getDustByName("flint"));
		OreUnifier.registerOre("dustGalena", ItemDusts.getDustByName("galena"));
		OreUnifier.registerOre("dustGold", ItemDusts.getDustByName("gold"));
		OreUnifier.registerOre("dustGrossular", ItemDusts.getDustByName("grossular"));
		OreUnifier.registerOre("dustInvar", ItemDusts.getDustByName("invar"));
		OreUnifier.registerOre("dustIron", ItemDusts.getDustByName("iron"));
		OreUnifier.registerOre("dustLapis", ItemDusts.getDustByName("lazurite"));
		OreUnifier.registerOre("dustLazurite", ItemDusts.getDustByName("lazurite"));
		OreUnifier.registerOre("dustLead", ItemDusts.getDustByName("lead"));
		OreUnifier.registerOre("dustMagnesium", ItemDusts.getDustByName("magnesium"));
		OreUnifier.registerOre("dustManganese", ItemDusts.getDustByName("manganese"));
		OreUnifier.registerOre("dustMarble", ItemDusts.getDustByName("marble"));
		OreUnifier.registerOre("dustNetherrack", ItemDusts.getDustByName("netherrack"));
		OreUnifier.registerOre("dustNickel", ItemDusts.getDustByName("nickel"));
		OreUnifier.registerOre("dustObsidian", ItemDusts.getDustByName("obsidian"));
		OreUnifier.registerOre("dustPeridot", ItemDusts.getDustByName("peridot"));
		OreUnifier.registerOre("dustPhosphorous", ItemDusts.getDustByName("phosphorous"));
		OreUnifier.registerOre("dustPlatinum", ItemDusts.getDustByName("platinum"));
		OreUnifier.registerOre("dustPyrite", ItemDusts.getDustByName("pyrite"));
		OreUnifier.registerOre("dustPyrope", ItemDusts.getDustByName("pyrope"));
		OreUnifier.registerOre("dustRedGarnet", ItemDusts.getDustByName("redGarnet"));
		OreUnifier.registerOre("dustRedrock", ItemDusts.getDustByName("redrock"));
		OreUnifier.registerOre("dustRuby", ItemDusts.getDustByName("ruby"));
		OreUnifier.registerOre("dustSaltpeter", ItemDusts.getDustByName("saltpeter"));
		OreUnifier.registerOre("dustSapphire", ItemDusts.getDustByName("sapphire"));
		OreUnifier.registerOre("dustSilver", ItemDusts.getDustByName("silver"));
		OreUnifier.registerOre("dustSodalite", ItemDusts.getDustByName("sodalite"));
		OreUnifier.registerOre("dustSpessartine", ItemDusts.getDustByName("spessartine"));
		OreUnifier.registerOre("dustSphalerite", ItemDusts.getDustByName("sphalerite"));
		OreUnifier.registerOre("dustSteel", ItemDusts.getDustByName("steel"));
		OreUnifier.registerOre("dustSulfur", ItemDusts.getDustByName("sulfur"));
		OreUnifier.registerOre("dustCopper", ItemDusts.getDustByName("copper"));
		OreUnifier.registerOre("dustTin", ItemDusts.getDustByName("tin"));
		OreUnifier.registerOre("dustTitanium", ItemDusts.getDustByName("titanium"));
		OreUnifier.registerOre("dustTungsten", ItemDusts.getDustByName("tungsten"));
		OreUnifier.registerOre("dustUvarovite", ItemDusts.getDustByName("uvarovite"));
		OreUnifier.registerOre("dustVoidstone", ItemDusts.getDustByName("voidstone"));
		OreUnifier.registerOre("dustYellowGarnet", ItemDusts.getDustByName("yellowGarnet"));
		OreUnifier.registerOre("dustZinc", ItemDusts.getDustByName("zinc"));
		OreUnifier.registerOre("dustOlivine", ItemDusts.getDustByName("olivine"));
		OreUnifier.registerOre("pulpWood", ItemDusts.getDustByName("sawDust"));

		// Ingots
		OreUnifier.registerOre("ingotAluminum", ItemIngots.getIngotByName("aluminum"));
		OreUnifier.registerOre("ingotAluminium", ItemIngots.getIngotByName("aluminum"));
		OreUnifier.registerOre("ingotBrass", ItemIngots.getIngotByName("brass"));
		OreUnifier.registerOre("ingotBronze", ItemIngots.getIngotByName("bronze"));
		OreUnifier.registerOre("ingotChrome", ItemIngots.getIngotByName("chrome"));
		OreUnifier.registerOre("ingotCopper", ItemIngots.getIngotByName("copper"));
		OreUnifier.registerOre("ingotElectrum", ItemIngots.getIngotByName("electrum"));
		OreUnifier.registerOre("ingotInvar", ItemIngots.getIngotByName("invar"));
		OreUnifier.registerOre("ingotIridium", ItemIngots.getIngotByName("iridium"));
		OreUnifier.registerOre("ingotLead", ItemIngots.getIngotByName("lead"));
		OreUnifier.registerOre("ingotNickel", ItemIngots.getIngotByName("nickel"));
		OreUnifier.registerOre("ingotPlatinum", ItemIngots.getIngotByName("platinum"));
		OreUnifier.registerOre("ingotSilver", ItemIngots.getIngotByName("silver"));
		OreUnifier.registerOre("ingotSteel", ItemIngots.getIngotByName("steel"));
		OreUnifier.registerOre("ingotTin", ItemIngots.getIngotByName("tin"));
		OreUnifier.registerOre("ingotTitanium", ItemIngots.getIngotByName("titanium"));
		OreUnifier.registerOre("ingotTungsten", ItemIngots.getIngotByName("tungsten"));
		OreUnifier.registerOre("ingotTungstensteel", ItemIngots.getIngotByName("tungstensteel"));
		OreUnifier.registerOre("ingotHotTungstenSteel", ItemIngots.getIngotByName("hotTungstensteel"));
		OreUnifier.registerOre("ingotZinc", ItemIngots.getIngotByName("zinc"));
		OreUnifier.registerOre("ingotRefinedIron", ItemIngots.getIngotByName("refinedIron"));
		OreUnifier.registerOre("ingotCopper", ItemIngots.getIngotByName("copper"));


		// Nuggets
		OreUnifier.registerOre("nuggetAluminum", ItemNuggets.getNuggetByName("aluminum"));
		OreUnifier.registerOre("nuggetAluminium", ItemNuggets.getNuggetByName("aluminum"));
		OreUnifier.registerOre("nuggetBrass", ItemNuggets.getNuggetByName("brass"));
		OreUnifier.registerOre("nuggetBronze", ItemNuggets.getNuggetByName("bronze"));
		OreUnifier.registerOre("nuggetChrome", ItemNuggets.getNuggetByName("chrome"));
		OreUnifier.registerOre("nuggetCopper", ItemNuggets.getNuggetByName("copper"));
		OreUnifier.registerOre("nuggetElectrum", ItemNuggets.getNuggetByName("electrum"));
		OreUnifier.registerOre("nuggetInvar", ItemNuggets.getNuggetByName("invar"));
		OreUnifier.registerOre("nuggetIridium", ItemNuggets.getNuggetByName("iridium"));
		OreUnifier.registerOre("nuggetLead", ItemNuggets.getNuggetByName("lead"));
		OreUnifier.registerOre("nuggetNickel", ItemNuggets.getNuggetByName("nickel"));
		OreUnifier.registerOre("nuggetPlatinum", ItemNuggets.getNuggetByName("platinum"));
		OreUnifier.registerOre("nuggetSilver", ItemNuggets.getNuggetByName("silver"));
		OreUnifier.registerOre("nuggetSteel", ItemNuggets.getNuggetByName("steel"));
		OreUnifier.registerOre("nuggetTin", ItemNuggets.getNuggetByName("tin"));
		OreUnifier.registerOre("nuggetTitanium", ItemNuggets.getNuggetByName("titanium"));
		OreUnifier.registerOre("nuggetTungsten", ItemNuggets.getNuggetByName("tungsten"));
		OreUnifier.registerOre("nuggetTungstensteel", ItemNuggets.getNuggetByName("tungstensteel"));
		OreUnifier.registerOre("nuggetHotTungstenSteel", ItemNuggets.getNuggetByName("hotTungstensteel"));
		OreUnifier.registerOre("nuggetZinc", ItemNuggets.getNuggetByName("zinc"));
		OreUnifier.registerOre("nuggetRefinedIron", ItemNuggets.getNuggetByName("refinedIron"));
		OreUnifier.registerOre("nuggetCopper", ItemNuggets.getNuggetByName("copper"));
		OreUnifier.registerOre("nuggetIron", ItemNuggets.getNuggetByName("iron"));

		// Plates
		OreUnifier.registerOre("plateAluminum", ItemPlates.getPlateByName("aluminum"));
		OreUnifier.registerOre("plateAluminium", ItemPlates.getPlateByName("aluminum"));
		OreUnifier.registerOre("plateBrass", ItemPlates.getPlateByName("brass"));
		OreUnifier.registerOre("plateBronze", ItemPlates.getPlateByName("bronze"));
		OreUnifier.registerOre("plateChrome", ItemPlates.getPlateByName("chrome"));
		OreUnifier.registerOre("plateCopper", ItemPlates.getPlateByName("copper"));
		OreUnifier.registerOre("plateDiamond", ItemPlates.getPlateByName("diamond"));
		OreUnifier.registerOre("plateElectrum", ItemPlates.getPlateByName("electrum"));
		OreUnifier.registerOre("plateEmerald", ItemPlates.getPlateByName("emerald"));
		OreUnifier.registerOre("plateGold", ItemPlates.getPlateByName("gold"));
		OreUnifier.registerOre("plateInvar", ItemPlates.getPlateByName("invar"));
		OreUnifier.registerOre("plateIridium", ItemPlates.getPlateByName("iridium"));
		OreUnifier.registerOre("plateIron", ItemPlates.getPlateByName("iron"));
		OreUnifier.registerOre("plateLapis", ItemPlates.getPlateByName("lapis"));
		OreUnifier.registerOre("plateLead", ItemPlates.getPlateByName("lead"));
		OreUnifier.registerOre("plateMagnalium", ItemPlates.getPlateByName("magnalium"));
		OreUnifier.registerOre("plateNickel", ItemPlates.getPlateByName("nickel"));
		OreUnifier.registerOre("plateObsidian", ItemPlates.getPlateByName("obsidian"));
		OreUnifier.registerOre("platePeridot", ItemPlates.getPlateByName("peridot"));
		OreUnifier.registerOre("platePlatinum", ItemPlates.getPlateByName("platinum"));
		OreUnifier.registerOre("plateRedGarnet", ItemPlates.getPlateByName("redGarnet"));
		OreUnifier.registerOre("plateRedstone", ItemPlates.getPlateByName("redstone"));
		OreUnifier.registerOre("plateRuby", ItemPlates.getPlateByName("ruby"));
		OreUnifier.registerOre("plateSapphire", ItemPlates.getPlateByName("sapphire"));
		OreUnifier.registerOre("plateSilicon", ItemPlates.getPlateByName("silicon"));
		OreUnifier.registerOre("plateSilver", ItemPlates.getPlateByName("silver"));
		OreUnifier.registerOre("plateSteel", ItemPlates.getPlateByName("steel"));
		OreUnifier.registerOre("plateTin", ItemPlates.getPlateByName("tin"));
		OreUnifier.registerOre("plateTitanium", ItemPlates.getPlateByName("titanium"));
		OreUnifier.registerOre("plateTungsten", ItemPlates.getPlateByName("tungsten"));
		OreUnifier.registerOre("plateTungstensteel", ItemPlates.getPlateByName("tungstensteel"));
		OreUnifier.registerOre("plateYellowGarnet", ItemPlates.getPlateByName("yellowGarnet"));
		OreUnifier.registerOre("plateZinc", ItemPlates.getPlateByName("zinc"));

		OreUnifier.registerOre("diamondTR", ItemDusts.getDustByName("Diamond"));
		OreUnifier.registerOre("diamondTR", Items.DIAMOND);

		OreUnifier.registerOre("craftingGrinder", ItemParts.getPartByName("diamondGrindingHead"));
		OreUnifier.registerOre("craftingGrinder", ItemParts.getPartByName("wolframiumGrindingHead"));
		OreUnifier.registerOre("circuitElite", ItemParts.getPartByName("dataControlCircuit"));
		OreUnifier.registerOre("circuitData", ItemParts.getPartByName("dataStorageCircuit"));
		OreUnifier.registerOre("craftingSuperconductor", ItemParts.getPartByName("superconductor"));
		OreUnifier.registerOre("batteryUltimate", ItemParts.getPartByName("diamondGrindingHead"));
		OreUnifier.registerOre("blockLapis", ItemParts.getPartByName("lazuriteChunk"));

		OreUnifier.registerOre("containerWater", ItemCells.getCellByName("water"));
		OreUnifier.registerOre("containerWater", Items.WATER_BUCKET);

		OreUnifier.registerOre("materialResin", ItemParts.getPartByName("rubberSap"));
		OreUnifier.registerOre("materialRubber", ItemParts.getPartByName("rubber"));
		OreUnifier.registerOre("circuitBasic", ItemParts.getPartByName("electronicCircuit"));
		OreUnifier.registerOre("circuitAdvanced", ItemParts.getPartByName("advancedCircuit"));

	}

}
