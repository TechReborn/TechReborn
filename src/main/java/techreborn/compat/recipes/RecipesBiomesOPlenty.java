//package techreborn.compat.recipes;
//
//
//import biomesoplenty.api.block.BOPBlocks;
//import net.minecraftforge.fml.common.event.FMLInitializationEvent;
//import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
//import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
//import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
//import net.minecraft.init.Items;
//import net.minecraft.item.ItemStack;
//import net.minecraftforge.fluids.FluidRegistry;
//import net.minecraftforge.fluids.FluidStack;
//import net.minecraftforge.oredict.OreDictionary;
//import techreborn.api.recipe.RecipeHandler;
//import techreborn.api.recipe.machines.IndustrialSawmillRecipe;
//import techreborn.compat.ICompatModule;
//import techreborn.config.ConfigTechReborn;
//
//public class RecipesBiomesOPlenty implements ICompatModule {
//    @Override
//    public void preInit(FMLPreInitializationEvent event) {
//
//    }
//
//    @Override
//    public void init(FMLInitializationEvent event) {
//        if (ConfigTechReborn.AllowBOPRecipes) {
//            ItemStack pulpStack = OreDictionary.getOres("pulpWood").get(0);
//            RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPBlocks.log_0, 1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(BOPBlocks.planks_0, 6, 0), pulpStack, null, 200, 30, false));
//            RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPBlocks.log_0, 1, 0), new ItemStack(Items.water_bucket), null, new ItemStack(BOPBlocks.planks_0, 6, 0), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
//
//            RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPBlocks.log_0, 1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(BOPBlocks.planks_0, 6, 1), pulpStack, null, 200, 30, false));
//            RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPBlocks.log_0, 1, 1), new ItemStack(Items.water_bucket), null, new ItemStack(BOPBlocks.planks_0, 6, 1), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
//
//            RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPBlocks.log_0, 1, 2), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(BOPBlocks.planks_0, 6, 2), pulpStack, null, 200, 30, false));
//            RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPBlocks.log_0, 1, 2), new ItemStack(Items.water_bucket), null, new ItemStack(BOPBlocks.planks_0, 6, 2), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
//
//            RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPBlocks.log_0, 1, 3), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(BOPBlocks.planks_0, 6, 3), pulpStack, null, 200, 30, false));
//            RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPBlocks.log_0, 1, 3), new ItemStack(Items.water_bucket), null, new ItemStack(BOPBlocks.planks_0, 6, 3), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
//
//            RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPBlocks.log_1, 1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(BOPBlocks.planks_0, 6, 4), pulpStack, null, 200, 30, false));
//            RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPBlocks.log_1, 1, 0), new ItemStack(Items.water_bucket), null, new ItemStack(BOPBlocks.planks_0, 6, 4), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
//
//            RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPBlocks.log_1, 1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(BOPBlocks.planks_0, 6, 5), pulpStack, null, 200, 30, false));
//            RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPBlocks.log_1, 1, 1), new ItemStack(Items.water_bucket), null, new ItemStack(BOPBlocks.planks_0, 6, 5), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
//
//            RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPBlocks.log_1, 1, 2), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(BOPBlocks.planks_0, 6, 6), pulpStack, null, 200, 30, false));
//            RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPBlocks.log_1, 1, 2), new ItemStack(Items.water_bucket), null, new ItemStack(BOPBlocks.planks_0, 6, 6), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
//
//            RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPBlocks.log_1, 1, 3), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(BOPBlocks.planks_0, 6, 7), pulpStack, null, 200, 30, false));
//            RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPBlocks.log_1, 1, 3), new ItemStack(Items.water_bucket), null, new ItemStack(BOPBlocks.planks_0, 6, 7), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
//
//            RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPBlocks.log_2, 1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(BOPBlocks.planks_0, 6, 8), pulpStack, null, 200, 30, false));
//            RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPBlocks.log_2, 1, 0), new ItemStack(Items.water_bucket), null, new ItemStack(BOPBlocks.planks_0, 6, 8), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
//
//            RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPBlocks.log_2, 1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(BOPBlocks.planks_0, 6, 9), pulpStack, null, 200, 30, false));
//            RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPBlocks.log_2, 1, 1), new ItemStack(Items.water_bucket), null, new ItemStack(BOPBlocks.planks_0, 6, 9), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
//
//            RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPBlocks.log_3, 1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(BOPBlocks.planks_0, 6, 11), pulpStack, null, 200, 30, false));
//            RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPBlocks.log_3, 1, 0), new ItemStack(Items.water_bucket), null, new ItemStack(BOPBlocks.planks_0, 6, 11), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
//
//            RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPBlocks.log_3, 1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(BOPBlocks.planks_0, 6, 12), pulpStack, null, 200, 30, false));
//            RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPBlocks.log_3, 1, 1), new ItemStack(Items.water_bucket), null, new ItemStack(BOPBlocks.planks_0, 6, 12), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
//
//            RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPBlocks.log_3, 1, 2), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(BOPBlocks.planks_0, 6, 13), pulpStack, null, 200, 30, false));
//            RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPBlocks.log_3, 1, 2), new ItemStack(Items.water_bucket), null, new ItemStack(BOPBlocks.planks_0, 6, 13), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
//
//            RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPBlocks.log_3, 1, 3), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(BOPBlocks.planks_0, 6, 14), pulpStack, null, 200, 30, false));
//            RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPBlocks.log_3, 1, 3), new ItemStack(Items.water_bucket), null, new ItemStack(BOPBlocks.planks_0, 6, 14), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
//        }
//    }
//
//    @Override
//    public void postInit(FMLPostInitializationEvent event) {
//
//    }
//
//    @Override
//    public void serverStarting(FMLServerStartingEvent event) {
//
//    }
//}
