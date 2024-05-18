###### LibCD support

TechReborn has built in support for
[libcd](https://www.curseforge.com/minecraft/mc-mods/libcd "wikilink")
alloing the adding of new recipes. Thanks B0undarybreaker for
contributing the support.

------------------------------------------------------------------------

##### Alloy Smelter

Add an alloy smelter recipe that takes 8 coal and 2 obsidian -\> 2
diamonds

`TRTweaker.addAlloySmelter(["minecraft:coal@8", "minecraft:obsidian@2"], [TweakerUtils.createItemStack("minecraft:diamond", 2)], 6, 200);`

------------------------------------------------------------------------

##### Assembling Machine

Add an assembling machine recipe that takes 3 diamonds and 2 sticks -\>
1 diamond pickaxe

`TRTweaker.addAssemblingMachine(["minecraft:diamond@3", "minecraft:stick@2"], [TweakerUtils.createItemStack("minecraft:diamond_pickaxe", 1)], 20, 200);`

------------------------------------------------------------------------

##### Blast Furnace

Add a blast furnace recipe that takes 64 of any item in the
minecraft:coals tag -\> 8 diamonds

`TRTweaker.addBlastFurnace(["#minecraft:coals@64"], [TweakerUtils.createItemStack("minecraft:diamond", 8)], 128, 200, 1300);`

------------------------------------------------------------------------

##### Centrifuge

Add a centrifuge recipe that takes any potion -\> a water bottle

`TRTweaker.addCentrifuge(["minecraft:potion"], [TweakerUtils.getSpecialStack("minecraft:potion->minecraft:water")], 5, 1000);`

------------------------------------------------------------------------

##### Chemical Reactor

Add a chemical reactor recipe that takes a water bottle and a can of
methane -\> a potion of fire resistance

`mods.techreborn.chemicalReactorRecipe.addRecipe(IItemStack output1, IIngredient input1, IIngredient input2, int ticktime, int euTick)`

------------------------------------------------------------------------

##### Compressor

Add a compressor recipe that takes 9 coal blocks -\> 3 pieces of
obsidian

`TRTweaker.addCompressor(["minecraft:coal_ore@9"], [TweakerUtils.createItemStack("minecraft:coal_block", 3)], 10, 300);`

------------------------------------------------------------------------

##### Distillation Tower

Add a distillation tower recipe that takes a potion of regneration -\> a
strong potion of regeneration

`TRTweaker.addDistillationTower(["minecraft:potion->minecraft:regeneration"], [TweakerUtils.getSpecialStack("minecraft:potion->minecraft:strong_regeneration")], 20, 400);`

------------------------------------------------------------------------

##### Extractor

Add an extractor recipe that takes 4 coal -\> 1 gunpowder

`TRTweaker.addExtractor(["minecraft:coal@4"], [TweakerUtils.createItemStack("minecraft:gunpowder", 1)], 10, 300);`

------------------------------------------------------------------------

##### Grinder

Add a grinder recipe of 1 sugar cane -\> 3 sugar

`TRTweaker.addGrinder(["minecraft:sugar_cane"], [TweakerUtils.createItemStack("minecraft:sugar", 3)], 4, 270);`

------------------------------------------------------------------------

##### Fusion Reactor

Add a fluid replicator recipe of 2 uu matter and 1 bucket of wulframium
-\> 2 buckets of wolframium

`TRTweaker.addFluidReplicator(["techreborn:uu_matter@2"], 40, 100, "techreborn:wolframium@1000");`

Add a fusion reactor recipe of 3 wither skeleton skulls and 4 soul sand
-\> 1 nether star

`TRTweaker.addFusionReactor(["minecraft:wither_skeleton_skull@3", "minecraft:soul_sand@4"], [TweakerUtils.createItemStack("minecraft:nether_star", 1)], -2048, 1024, 90000, 1);`

------------------------------------------------------------------------

##### Implosion Compressor

Add an implosion compressor recipe of 32 coal and 16 flint -\> 16
diamonds

`TRTweaker.addImplosionCompressor(["minecraft:coal@32", "minecraft:flint@16"], [TweakerUtils.createItemStack("minecraft:diamond", 16)], 30, 2000);`

------------------------------------------------------------------------

##### Industrial Electrolyzer

Add an industrial electrolyzer recipe of 1 skeleton skull -\> 1 wither
skeleton skull

`TRTweaker.addIndustrialElectrolyzer(["minecraft:skeleton_skull"], [TweakerUtils.createItemStack("minecraft:wither_skeleton_skull", 1)], 50, 1400);`

------------------------------------------------------------------------

##### Industrial Grinder

Add an industrial electrolyzer recipe of 1 sea lantern -\> 5 prismarine
crystals and 4 prismarine shards

`TRTweaker.addIndustrialGrinder(["minecraft:sea_lantern"], [TweakerUtils.createItemStack("minecraft:prismarine_crystals", 5), TweakerUtils.createItemStack("minecraft:prismarine_shard", 4)], 64, 100);`

Add an industrial electrolyzer recipe of 1 sea lantern and 1 bucket of
electrolyzed water -\> 9 prismarine crystals

`TRTweaker.addIndustrialGrinder(["minecraft:sea_lantern"], [TweakerUtils.createItemStack("minecraft:prismarine_crystals", 9)], 64, 100, "techreborn:electrolyzed_water@1000");`

------------------------------------------------------------------------

##### Industrial Sawmill

Add an industrial sawmill recipe of 3 sugar cane -\> 18 paper

`TRTweaker.addIndustrialSawmill(["minecraft:sugar_cane@3"], [TweakerUtils.createItemStack("minecraft:paper", 18)], 40, 200);`

Add an industrial sawmill recipe of 1 heart of the sea and 1/2 bucket of
water -\> 16 nautilus shells

`TRTweaker.addIndustrialSawmill(["minecraft:heart_of_the_sea"], [TweakerUtils.createItemStack("minecraft:nautilus_shell", 16)], 40, 200, "minecraft:water@500");`

------------------------------------------------------------------------

##### Rolling Machine

`TRTweaker.addRollingMachine([["minecraft:stick", "minecraft:oak_planks"], ["minecraft:stone", "minecraft:oak_planks"]], TweakerUtils.createItemStack("minecraft:crafting_table", 1));`

Add a rolling machine recipe for sticks on the sides and any wooden slab
in the middle -\> six ladders `<code>`{=html} //Create a
pattern/dictionary set for a shaped recipe var pattern = \[ \'/ /\',

`               '/_/',`\
`               '/ /'];`

var dict = {

`   "/": "minecraft:stick",`\
`   "_": "#minecraft:wooden_slabs"`

};

TRTweaker.addRollingMachine(pattern, dict,
TweakerUtils.createItemStack(\"minecraft:ladder\", 12));
`</code>`{=html}

------------------------------------------------------------------------

##### Scrapbox

Add a scrapbox recipe of 1 scrap box -\> 1 shulker box
`TRTweaker.addScrapbox(TweakerUtils.createItemStack("minecraft:shulker_box", 1), 10, 20);`

------------------------------------------------------------------------

##### Vacuum Freezer

Add a scrapbox recipe of 1 cell of water -\> 1 blue ice
`TRTweaker.addVacuumFreezer([TRTweaker.createFluidIngredient("minecraft:water", ["techreborn:cell"], -1)], [TweakerUtils.createItemStack("minecraft:blue_ice", 1)], 60, 440);`

------------------------------------------------------------------------

##### Fluid Replicator

Add a fluid replicator recipe of 2 uu matter and 1 bucket of wulframium
-\> 2 buckets of wolframium
`TRTweaker.addFluidReplicator(["techreborn:uu_matter@2"], 40, 100, "techreborn:wolframium@1000");`

------------------------------------------------------------------------

##### Fluid Generators, aka Thermal, Gas, Plasma, Semi and Diesel

Add a plasma fluid generator recipe for 1 mB of wolframium -\> 300 EU

`TRTweaker.addFluidGenerator("plasma", "techreborn:wolframium", 300);`
