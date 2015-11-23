package techreborn.items;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import techreborn.client.TechRebornCreativeTab;
import techreborn.init.ModItems;

import java.util.List;

public class ItemCells extends ItemTR {

    public static final PropertyEnum<EnumCells> VARIANT_PROP = PropertyEnum.create("variant", EnumCells.class);

    public static ItemStack getCellByName(String name, int count) {
        return getCellByName(name, count, true);
    }


    public static ItemStack getCellByName(String name, int count, boolean lookForIC2) {
        Fluid fluid = FluidRegistry.getFluid("fluid" + name.toLowerCase());
//        if (lookForIC2 && IC2Items.getItem("FluidCell") != null) {
//            if (fluid != null) {
//                ItemStack stack = IC2Items.getItem("FluidCell").copy();
//                if (stack != null && stack.getItem() instanceof IFluidContainerItem) {
//                    IFluidContainerItem containerItem = (IFluidContainerItem) stack.getItem();
//                    containerItem.fill(stack, new FluidStack(fluid.getID(), 2147483647), true);
//                    stack.stackSize = count;
//                    return stack;
//                }
//            } else {
//                Core.logHelper.debug("Could not find " + "fluid" + name + " in the fluid registry!");
//            }
//        } //TODO ic2
        int index = -1;
        for (int i = 0; i < types.length; i++) {
            if (types[i].equals(name)) {
                index = i;
                break;
            }
        }
        return new ItemStack(ModItems.cells, count, index);
    }

    public static ItemStack getCellByName(String name) {
        return getCellByName(name, 1);
    }

    public static final String[] types = new String[]
            {"Berylium", "biomass", "calciumCarbonate", "calcium", "carbon",
                    "chlorine", "deuterium", "diesel", "ethanol", "glyceryl",
                    "helium3", "helium", "heliumPlasma", "hydrogen", "ice", "lithium",
                    "mercury", "methane", "nitrocarbon", "nitroCoalfuel",
                    "nitroDiesel", "nitrogen", "nitrogenDioxide", "oil", "potassium",
                    "seedOil", "silicon", "sodium", "sodiumPersulfate",
                    "sodiumSulfide", "sulfur", "sulfuricAcid", "tritium", "wolframium", "empty"};


    public enum EnumCells implements IStringSerializable{
        Berylium(0, "Berylium"),
        biomass(1, "biomass"),
        calciumCarbonate(2, "calciumCarbonate"),
        calcium(3, "calcium"),
        carbon(4, "carbon"),
        chlorine(0, "chlorine"),
        deuterium(0, "deuterium"),
        diesel(0, "diesel"),
        ethanol(0, "ethanol"),
        glyceryl(0, "glyceryl"),
        helium3(0, "helium3"),
        helium(0, "helium"),
        heliumPlasma(0, "heliumPlasma"),
        hydrogen(0, "hydrogen"),
        ice(0, "ice"),
        lithium(0, "lithium"),
        mercury(0, "mercury"),
        methane(0, "methane"),
        nitrocarbon(0, "nitrocarbon"),
        nitroCoalfuel(0, "nitroCoalfuel"),
        nitroDiesel(0, "nitroDiesel"),
        nitrogen(0, "nitrogen"),
        nitrogenDioxide(0, "nitrogenDioxide"),
        oil(0, "oil"),
        potassium(0, "potassium"),
        seedOil(0, "seedOil"),
        silicon(0, "silicon"),
        sodium(0, "sodium"),
        sodiumPersulfate(0, "sodiumPersulfate"),
        sodiumSulfide(0, "sodiumSulfide"),
        sulfur(0, "sulfur"),
        tritium(0, "tritium"),
        wolframium(0, "wolframium"),
        empty(0, "empty");

        private static final EnumCells[] META_LOOKUP = new EnumCells[values().length];
        private final int meta;
        private final String name;

        private EnumCells(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }

        public int getMeta() {
            return meta;
        }

        static
        {
            for (EnumCells cells : values())
            {
                META_LOOKUP[cells.getMeta()] = cells;
            }
        }
    }

    public ItemCells() {
        setUnlocalizedName("techreborn.cell");
        setHasSubtypes(true);
        setCreativeTab(TechRebornCreativeTab.instance);
    }


    @Override
    // gets Unlocalized Name depending on meta data
    public String getUnlocalizedName(ItemStack itemStack) {
        int meta = itemStack.getItemDamage();
        if (meta < 0 || meta >= types.length) {
            meta = 0;
        }

        return super.getUnlocalizedName() + "." + types[meta];
    }

    // Adds Dusts SubItems To Creative Tab
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        for (int meta = 0; meta < types.length; ++meta) {
            list.add(new ItemStack(item, 1, meta));
        }
    }

}
