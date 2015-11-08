package techreborn.powerSystem.traits;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import reborncore.jtraits.JTrait;
import techreborn.api.power.IEnergyInterfaceItem;

public abstract class BasePowerTrait extends JTrait<Item> implements IEnergyInterfaceItem {

    @Override
    public double getEnergy(ItemStack stack) {
        NBTTagCompound tagCompound = getOrCreateNbtData(stack);
        if (tagCompound.hasKey("charge")) {
            return tagCompound.getDouble("charge");
        }
        return 0;
    }

    @Override
    public void setEnergy(double energy, ItemStack stack) {
        NBTTagCompound tagCompound = getOrCreateNbtData(stack);
        tagCompound.setDouble("charge", energy);

        if (this.getEnergy(stack) > getMaxPower(stack)) {
            this.setEnergy(getMaxPower(stack), stack);
        } else if (this.getEnergy(stack) < 0) {
            this.setEnergy(0, stack);
        }
    }

    @Override
    public double addEnergy(double energy, ItemStack stack) {
        return addEnergy(energy, false, stack);
    }

    @Override
    public double addEnergy(double energy, boolean simulate, ItemStack stack) {
        double energyReceived = Math.min(getMaxPower(stack) - energy, Math.min(this.getMaxPower(stack), energy));

        if (!simulate) {
            setEnergy(energy + energyReceived, stack);
        }
        return energyReceived;
    }

    @Override
    public boolean canUseEnergy(double input, ItemStack stack) {
        return input <= getEnergy(stack);
    }

    @Override
    public double useEnergy(double energy, ItemStack stack) {
        return useEnergy(energy, false, stack);
    }

    @Override
    public double useEnergy(double extract, boolean simulate, ItemStack stack) {
        double energyExtracted = Math.min(extract, Math.min(this.getMaxTransfer(stack), extract));

        if (!simulate) {
            setEnergy(getEnergy(stack) - energyExtracted, stack);
        }
        return energyExtracted;
    }

    @Override
    public boolean canAddEnergy(double energy, ItemStack stack) {
        return this.getEnergy(stack) + energy <= getMaxPower(stack);
    }


    public NBTTagCompound getOrCreateNbtData(ItemStack itemStack) {
        NBTTagCompound tagCompound = itemStack.getTagCompound();
        if (tagCompound == null) {
            tagCompound = new NBTTagCompound();
            itemStack.setTagCompound(tagCompound);
        }

        return tagCompound;
    }


}
