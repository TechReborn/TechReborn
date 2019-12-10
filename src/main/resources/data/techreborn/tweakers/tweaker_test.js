//Warn that the sample script is running
log.warn("WARNING! The TechReborn tweaker sample script is running!");
log.warn("If you're seeing this and aren't in dev mode, please report it!");

var TRTweaker = libcd.require("techreborn.TRTweaker");
var TweakerUtils = libcd.require("libcd.util.TweakerUtils");

//Add an alloy smelter recipe that takes 8 coal and 2 obsidian -> 2 diamonds
TRTweaker.addAlloySmelter(["minecraft:coal@8", "minecraft:obsidian@2"], ["minecraft:diamond@2"], 6, 200);

//Add an assembling machine recipe that takes 3 diamonds and 2 sticks -> 1 diamond pickaxe
TRTweaker.addAssemblingMachine(["minecraft:diamond@3", "minecraft:stick@2"], ["minecraft:diamond_pickaxe"], 20, 200);

//Add a blast furnace recipe that takes 64 of any item in the minecraft:coals tag -> 8 diamonds
TRTweaker.addBlastFurnace(["#minecraft:coals@64"], ["minecraft:diamond@8"], 128, 200, 1300);

//Add a centrifuge recipe that takes any potion -> a water bottle
TRTweaker.addCentrifuge(["minecraft:potion"], [TweakerUtils.getSpecialStack("minecraft:potion->minecraft:water")], 5, 1000);

//Add a chemical reactor recipe that takes a water bottle and a can of methane -> a potion of fire resistance
//DISCLAIMER: do not try this at home
TRTweaker.addChemicalReactor(["minecraft:potion->minecraft:water", TRTweaker.createFluidIngredient("techreborn:methane", ["techreborn:cell"], -1)], [TweakerUtils.getSpecialStack("minecraft:potion->minecraft:fire_resistance")], 30, 800);

//Add a compressor recipe that takes 9 coal blocks -> 3 pieces of obsidian
TRTweaker.addCompressor(["minecraft:coal_ore@9"], ["minecraft:coal_block@3"], 10, 300);

//Add a distillation tower recipe that takes a potion of regneration -> a strong potion of regeneration
TRTweaker.addDistillationTower(["minecraft:potion->minecraft:regeneration"], [TweakerUtils.getSpecialStack("minecraft:potion->minecraft:strong_regeneration")], 20, 400);

//Add an extractor recipe that takes 4 coal -> 1 gunpowder
TRTweaker.addExtractor(["minecraft:coal@4"], ["minecraft:gunpowder"], 10, 300);

//Add a grinder recipe of 1 sugar cane -> 3 sugar
TRTweaker.addGrinder(["minecraft:sugar_cane"], ["minecraft:sugar@3"], 4, 270);

//Add an implosion compressor recipe of 32 coal and 16 flint -> 16 diamonds
TRTweaker.addImplosionCompressor(["minecraft:coal@32", "minecraft:flint@16"], ["minecraft:diamond@16"], 30, 2000);

//Add an industrial electrolyzer recipe of 1 skeleton skull -> 1 wither skeleton skull
TRTweaker.addIndustrialElectrolyzer(["minecraft:skeleton_skull"], ["minecraft:wither_skeleton_skull"], 50, 1400);

//Add an industrial electrolyzer recipe of 1 sea lantern -> 5 prismarine crystals and 4 prismarine shards
TRTweaker.addIndustrialGrinder(["minecraft:sea_lantern"], ["minecraft:prismarine_crystals@5", "minecraft:prismarine_shard@4"], 64, 100);

//Add an industrial electrolyzer recipe of 1 sea lantern and 1 bucket of electrolyzed water -> 9 prismarine crystals
TRTweaker.addIndustrialGrinder(["minecraft:sea_lantern"], ["minecraft:prismarine_crystals@9"], 64, 100, "techreborn:electrolyzed_water@1000");

//Add an industrial sawmill recipe of 3 sugar cane -> 18 paper
TRTweaker.addIndustrialSawmill(["minecraft:sugar_cane@3"], ["minecraft:paper@18"], 40, 200);

//Add an industrial sawmill recipe of 1 heart of the sea and 1/2 bucket of water -> 16 nautilus shells
TRTweaker.addIndustrialSawmill(["minecraft:heart_of_the_sea"], ["minecraft:nautilus_shell@16"], 40, 200, "minecraft:water@500");

//Add a recycler recipe of 1 water bucket -> 1 empty bucket
TRTweaker.addRecycler(["minecraft:water_bucket"], ["minecraft:bucket"], 5, 40);

//Add a scrapbox recipe of 1 scrap box -> 1 shulker box
TRTweaker.addScrapbox("minecraft:shulker_box", 10, 20);

//Add a scrapbox recipe of 1 cell of water -> 1 blue ice
TRTweaker.addVacuumFreezer([TRTweaker.createFluidIngredient("minecraft:water", ["techreborn:cell"], -1)], ["minecraft:blue_ice"], 60, 440);

//Add a fluid replicator recipe of 2 uu matter and 1 bucket of wulframium -> 2 buckets of wolframium
TRTweaker.addFluidReplicator(["techreborn:uu_matter@2"], 40, 100, "techreborn:wolframium@1000");

//Add a fusion reactor recipe of 3 wither skeleton skulls and 4 soul sand -> 1 nether star
TRTweaker.addFusionReactor(["minecraft:wither_skeleton_skull@3", "minecraft:soul_sand@4"], ["minecraft:nether_star"], -2048, 1024, 90000, 1);

//Add a rolling machine recipe for 5 popped chorus fruit in a helmet shape -> a shulker shell
var chorus = "minecraft:popped_chorus_fruit";
TRTweaker.addRollingMachine([[chorus, chorus, chorus], [chorus, "", chorus]], "minecraft:shulker_shell");

//Create a pattern/dictionary set for a shaped recipe
var pattern = [ '/ /',
                '/_/',
                '/ /'];
var dict = {
    "/": "minecraft:stick",
    "_": "#minecraft:wooden_slabs"
};

//Add a rolling machine recipe for sticks on the sides and any wooden slab in the middle -> six ladders
TRTweaker.addDictRollingMachine(pattern, dict, "minecraft:ladder@12");

//Add a solid canning machine recipe for 1 empty cell and 1 blue ice -> a cell full of water
TRTweaker.addSolidCanningMachine(["techreborn:cell{}", "minecraft:blue_ice"], [TweakerUtils.getSpecialStack("techreborn:cell->minecraft:water")], 1, 100);

//Add a wire mill recipe for 1 woll -> 4 string
TRTweaker.addWireMill(["#minecraft:wool"], ["minecraft:string@4"], 2, 200);

//Add a plasma fluid generator recipe for 1 mB of wolframium -> 300 EU
TRTweaker.addFluidGenerator("plasma", "techreborn:wolframium", 300);
