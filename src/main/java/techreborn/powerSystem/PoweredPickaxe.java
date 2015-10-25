package techreborn.powerSystem;

import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.IElectricItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import techreborn.api.power.IEnergyInterfaceItem;
import techreborn.asm.Strippable;
import techreborn.config.ConfigTechReborn;

import java.util.List;


public abstract class PoweredPickaxe extends ItemPickaxe implements IEnergyInterfaceItem, IElectricItem, IEnergyContainerItem {

    protected PoweredPickaxe(ToolMaterial toolMaterial) {
        super(toolMaterial);
    }

    @SuppressWarnings(
            {"rawtypes", "unchecked"})
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List itemList) {
        ItemStack itemStack = new ItemStack(this, 1);
        itemList.add(itemStack);

        ItemStack charged = new ItemStack(this, 1);
        setEnergy(getMaxPower(charged), charged);
        itemList.add(charged);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        double charge = (getEnergy(stack) / getMaxPower(stack));
        return 1 - charge;

    }

    //TechReborn
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


    public static NBTTagCompound getOrCreateNbtData(ItemStack itemStack) {
        NBTTagCompound tagCompound = itemStack.getTagCompound();
        if (tagCompound == null) {
            tagCompound = new NBTTagCompound();
            itemStack.setTagCompound(tagCompound);
        }

        return tagCompound;
    }


    //End Techreborn


    //IC2
    @Strippable("mod:IC2")
    @Override
    public Item getChargedItem(ItemStack itemStack) {
        return this;
    }

    @Strippable("mod:IC2")
    @Override
    public Item getEmptyItem(ItemStack itemStack) {
        return this;
    }

    @Strippable("mod:IC2")
    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return getMaxPower(itemStack);
    }

    @Strippable("mod:IC2")
    @Override
    public int getTier(ItemStack itemStack) {
        return getStackTeir(itemStack);
    }

    @Strippable("mod:IC2")
    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return getMaxTransfer(itemStack);
    }
    //IC2

    //COFH
    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        if (!PowerSystem.RFPOWENET)
            return 0;
        if (!canAcceptEnergy(container)) {
            return 0;
        }
        maxReceive *= ConfigTechReborn.euPerRF;
        int energyReceived = Math.min(getMaxEnergyStored(container) - getEnergyStored(container), Math.min((int) this.getMaxTransfer(container) * ConfigTechReborn.euPerRF, maxReceive));

        if (!simulate) {
            setEnergy(getEnergy(container) + energyReceived, container);
        }
        return energyReceived / ConfigTechReborn.euPerRF;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        if (!PowerSystem.RFPOWENET)
            return 0;
        if (!canAcceptEnergy(container)) {
            return 0;
        }
        maxExtract *= ConfigTechReborn.euPerRF;
        int energyExtracted = Math.min(getEnergyStored(container), Math.min(maxExtract, maxExtract));

        if (!simulate) {
            setEnergy(getEnergy(container) - energyExtracted, container);
        }
        return energyExtracted / ConfigTechReborn.euPerRF;
    }

    @Override
    public int getEnergyStored(ItemStack container) {
        if (!PowerSystem.RFPOWENET)
            return 0;
        return ((int) getEnergy(container) / ConfigTechReborn.euPerRF);
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {
        if (!PowerSystem.RFPOWENET)
            return 0;
        return ((int) getMaxPower(container) / ConfigTechReborn.euPerRF);
    }
    //Cofh
}
