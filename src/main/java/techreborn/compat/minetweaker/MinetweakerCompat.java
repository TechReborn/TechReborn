package techreborn.compat.minetweaker;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.item.IngredientStack;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import techreborn.compat.ICompatModule;

import static minetweaker.api.minecraft.MineTweakerMC.getItemStack;
import static minetweaker.api.minecraft.MineTweakerMC.getLiquidStack;


public class MinetweakerCompat implements ICompatModule {
    @Override
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Override
    public void init(FMLInitializationEvent event) {

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        MineTweakerAPI.registerClass(MTAlloySmelter.class);
        MineTweakerAPI.registerClass(MTAssemblingMachine.class);
        MineTweakerAPI.registerClass(MTBlastFurnace.class);
        MineTweakerAPI.registerClass(MTCentrifuge.class);
        MineTweakerAPI.registerClass(MTChemicalReactor.class);
        MineTweakerAPI.registerClass(MTGrinder.class);
        MineTweakerAPI.registerClass(MTImplosionCompressor.class);
        MineTweakerAPI.registerClass(MTIndustrialElectrolyzer.class);
        MineTweakerAPI.registerClass(MTIndustrialSawmill.class);
        MineTweakerAPI.registerClass(MTPlateCuttingMachine.class);
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {

    }

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

}
