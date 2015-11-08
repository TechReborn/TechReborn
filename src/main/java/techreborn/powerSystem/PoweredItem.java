package techreborn.powerSystem;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import reborncore.jtraits.MixinFactory;
import techreborn.api.power.IEnergyInterfaceItem;
import techreborn.powerSystem.traits.BasePowerTrait;
import techreborn.powerSystem.traits.EUItemPowerTrait;
import techreborn.powerSystem.traits.RFItemPowerTrait;


public abstract class PoweredItem {

    public static Item createItem(Class itemClass) throws IllegalAccessException, InstantiationException {
        return (Item) MixinFactory.mixin(itemClass,BasePowerTrait.class, RFItemPowerTrait.class, EUItemPowerTrait.class).newInstance();
    }

    public static boolean canUseEnergy(double energy, ItemStack stack){
        if(stack.getItem() instanceof IEnergyInterfaceItem){
            return ((IEnergyInterfaceItem) stack.getItem()).canUseEnergy(energy, stack);
        } else {
            return false;
        }
    }

    public static double useEnergy(double energy, ItemStack stack){
        if(stack.getItem() instanceof IEnergyInterfaceItem){
            return ((IEnergyInterfaceItem) stack.getItem()).useEnergy(energy, stack);
        } else {
            return 0;
        }
    }

    public static void setEnergy(double energy, ItemStack stack){
        if(stack.getItem() instanceof IEnergyInterfaceItem){
            ((IEnergyInterfaceItem) stack.getItem()).setEnergy(energy, stack);
        }
    }

    public static double getEnergy(ItemStack stack){
        if(stack.getItem() instanceof IEnergyInterfaceItem){
            return ((IEnergyInterfaceItem) stack.getItem()).getEnergy(stack);
        } else {
            return 0;
        }
    }


}
