package techreborn.power;

import cofh.api.energy.IEnergyContainerItem;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Loader;
import reborncore.api.power.IEnergyInterfaceItem;
import reborncore.api.power.IEnergyInterfaceTile;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.RebornCoreConfig;

public class EnergyItem {

    public static void discharge(ItemStack itemStack, IEnergyInterfaceTile tile) {
        Item item = itemStack.getItem();

        if(item instanceof IEnergyItemInfo) {
            IEnergyItemInfo energyInterfaceItem = (IEnergyItemInfo) item;
            if(energyInterfaceItem.canProvideEnergy(itemStack) && getEnergy(itemStack) > 0) {
                double extractEnergy = tile.getMaxPower() - tile.getEnergy();
                double input = extractEnergy > tile.getMaxInput() ? tile.getMaxInput() : extractEnergy;
                double transferLimit = energyInterfaceItem.getMaxTransfer(itemStack);
                double remove = input > transferLimit ? transferLimit : input;
                System.out.println(remove);
                double extracted = useEnergy(remove, itemStack);
                if(extracted != 0) {
                    tile.addEnergy(extracted);
                }
            }
        }

        if(item instanceof IEnergyInterfaceItem) {
            IEnergyInterfaceItem energyInterfaceItem = (IEnergyInterfaceItem) item;
            if(energyInterfaceItem.canProvideEnergy(itemStack) && energyInterfaceItem.getEnergy(itemStack) > 0) {
                double extractEnergy = tile.getMaxPower() - tile.getEnergy();
                double input = extractEnergy > tile.getMaxInput() ? tile.getMaxInput() : extractEnergy;
                double transferLimit = energyInterfaceItem.getMaxTransfer(itemStack);
                double remove = input > transferLimit ? transferLimit : input;
                double extracted = energyInterfaceItem.useEnergy(remove, itemStack);
                if(extracted != 0) {
                    tile.addEnergy(extracted);
                }
            }
        }

        if(RebornCoreConfig.getRebornPower().rf() &&
                item instanceof IEnergyContainerItem) {
            double extractEnergy = tile.getMaxPower() - tile.getEnergy();
            double input = extractEnergy > tile.getMaxInput() ? tile.getMaxInput() : extractEnergy;
            int extractRF = (int) (input * RebornCoreConfig.euPerRF);
            IEnergyContainerItem energyContainer = (IEnergyContainerItem) item;
            if(energyContainer.getEnergyStored(itemStack) > 0) {
                int extractedRF = energyContainer.extractEnergy(itemStack, extractRF, false);
                if (extractedRF != 0) {
                    float extractedEU = extractedRF / (RebornCoreConfig.euPerRF * 1F);
                    tile.addEnergy(extractedEU);
                }
            }
        }

        if(Loader.isModLoaded("IC2") &&
                RebornCoreConfig.getRebornPower().eu() &&
                item instanceof IElectricItem) {
            IElectricItem electricItem = (IElectricItem) item;
            if(electricItem.canProvideEnergy(itemStack) && ElectricItem.manager.getCharge(itemStack) > 0) {
                double extractEnergy = tile.getMaxPower() - tile.getEnergy();
                double input = extractEnergy > tile.getMaxInput() ? tile.getMaxInput() : extractEnergy;
                double extracted = ElectricItem.manager.discharge(itemStack, input, electricItem.getTier(itemStack), false, true, false);
                if(extracted != 0) {
                    tile.addEnergy(extracted);
                }
            }
        }

        if(Loader.isModLoaded("Tesla") &&
                RebornCoreConfig.getRebornPower().tesla() &&
                itemStack.hasCapability(TeslaCapabilities.CAPABILITY_PRODUCER, null)) {
            ITeslaProducer producer = itemStack.getCapability(TeslaCapabilities.CAPABILITY_PRODUCER, null);
            double extractEnergy = tile.getMaxPower() - tile.getEnergy();
            double input = extractEnergy > tile.getMaxInput() ? tile.getMaxInput() : extractEnergy;
            long extractRF = (long) (input * RebornCoreConfig.euPerRF);
            long extractedRF = producer.takePower(extractRF, false);
            if(extractedRF != 0) {
                float extractedEU = extractedRF / (RebornCoreConfig.euPerRF * 1F);
                tile.addEnergy(extractedEU);
            }
        }

    }

    public static void charge(ItemStack itemStack, IEnergyInterfaceTile tile) {
        Item item = itemStack.getItem();


        if(item instanceof IEnergyItemInfo) {
            IEnergyItemInfo energyItemInfo = (IEnergyItemInfo) item;
            if(energyItemInfo.canAcceptEnergy(itemStack) &&
                    getEnergy(itemStack) < energyItemInfo.getMaxPower(itemStack)) {
                double storedEnergy = tile.getEnergy();
                double output = storedEnergy > tile.getMaxOutput() ? tile.getMaxOutput() : storedEnergy;
                double transferLimit = energyItemInfo.getMaxTransfer(itemStack);
                double add = output > transferLimit ? transferLimit : output;
                double addedEnergy = addEnergy(add, itemStack);
                if(addedEnergy != 0) {
                    tile.useEnergy(addedEnergy);
                }
            }
        }

        if(item instanceof IEnergyInterfaceItem) {
            IEnergyInterfaceItem energyItem = (IEnergyInterfaceItem) item;
            if(energyItem.canAcceptEnergy(itemStack) &&
                    energyItem.getEnergy(itemStack) < energyItem.getMaxPower(itemStack)) {
                double storedEnergy = tile.getEnergy();
                double output = storedEnergy > tile.getMaxOutput() ? tile.getMaxOutput() : storedEnergy;
                double transferLimit = energyItem.getMaxTransfer(itemStack);
                double add = output > transferLimit ? transferLimit : output;
                double addedEnergy = energyItem.addEnergy(add, itemStack);
                if(addedEnergy != 0) {
                    tile.useEnergy(addedEnergy);
                }
            }
        }

        if(RebornCoreConfig.getRebornPower().rf() &&
                item instanceof IEnergyContainerItem) {
            double storedEnergy = tile.getEnergy();
            double output = storedEnergy > tile.getMaxOutput() ? tile.getMaxOutput() : storedEnergy;
            int insertRF = (int) (output * RebornCoreConfig.euPerRF);
            IEnergyContainerItem energyContainer = (IEnergyContainerItem) item;
            if(energyContainer.getEnergyStored(itemStack) < energyContainer.getMaxEnergyStored(itemStack)) {
                int addedRF = energyContainer.receiveEnergy(itemStack, insertRF, false);
                if (addedRF != 0) {
                    float addedEU = addedRF / (RebornCoreConfig.euPerRF * 1F);
                    tile.useEnergy(addedEU);
                }
            }
        }

        if(Loader.isModLoaded("IC2") &&
                RebornCoreConfig.getRebornPower().eu() &&
                item instanceof IElectricItem) {
            IElectricItem electricItem = (IElectricItem) item;
            if(ElectricItem.manager.getCharge(itemStack) < electricItem.getMaxCharge(itemStack)) {
                double storedEnergy = tile.getEnergy();
                double output = storedEnergy > tile.getMaxOutput() ? tile.getMaxOutput() : storedEnergy;
                double added = ElectricItem.manager.charge(itemStack, output, electricItem.getTier(itemStack), false, false);
                if(added != 0) {
                    tile.useEnergy(added);
                }
            }
        }

        if(Loader.isModLoaded("Tesla") &&
                RebornCoreConfig.getRebornPower().tesla() &&
                itemStack.hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, null)) {
            ITeslaConsumer consumer = itemStack.getCapability(TeslaCapabilities.CAPABILITY_CONSUMER, null);
            double storedEnergy = tile.getEnergy();
            double output = storedEnergy > tile.getMaxOutput() ? tile.getMaxOutput() : storedEnergy;
            long extractRF = (long) (output * RebornCoreConfig.euPerRF);
            long addedRF = consumer.givePower(extractRF, false);
            if(addedRF != 0) {
                float addedEU = addedRF / (RebornCoreConfig.euPerRF * 1F);
                tile.useEnergy(addedEU);
            }
        }
    }

    public static void setEnergy(double energy, ItemStack stack) {
        NBTTagCompound energyItem = new NBTTagCompound();
        energyItem.setDouble("energy", energy);
        if(stack.hasTagCompound()) {
            stack.getTagCompound().setTag("EnergyItem", energyItem);
        } else {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setTag("EnergyItem", energyItem);
            stack.setTagCompound(tag);
        }
    }

    public static double getEnergy(ItemStack stack) {
        if(stack.getItem() instanceof IEnergyInterfaceItem) {
            return ((IEnergyInterfaceItem) stack.getItem()).getEnergy(stack);
        }

        if(stack.hasTagCompound()) {
            NBTTagCompound energyItem = stack.getTagCompound().getCompoundTag("EnergyItem");
            return energyItem.getDouble("energy");
        }
        return 0.0f;
    }

    public static double getMaxPower(ItemStack itemStack) {
        if(itemStack.getItem() instanceof IEnergyItemInfo)
            return ((IEnergyItemInfo) itemStack.getItem()).getMaxPower(itemStack);
        return 0F;
    }

    public static boolean canAddEnergy(double energy, ItemStack stack) {
        return getMaxPower(stack) - getEnergy(stack) >= energy;
    }

    /**
     * Will try add add the full amount of energy.
     *
     * @param energy
     *            amount to add
     * @param stack
     *            The {@link ItemStack} that contains the power
     * @return The amount of energy that was added.
     */
    public static double addEnergy(double energy, ItemStack stack) {
        return addEnergy(energy, false, stack);
    }

    /**
     * Will try add add the full amount of energy, if simulate is true it wont
     * add the energy
     *
     * @param energy
     *            amount to add
     * @param stack
     *            The {@link ItemStack} that contains the power
     * @return The amount of energy that was added.
     */
    public static double addEnergy(double energy, boolean simulate, ItemStack stack) {
        double canStore = getMaxPower(stack) - getEnergy(stack);
        if(canStore >= energy) {
            if(!simulate)
                setEnergy(getEnergy(stack) + energy, stack);
            return energy;
        }
        if(!simulate)
            setEnergy(getEnergy(stack) + canStore, stack);
        return canStore;
    }

    /**
     * Returns true if it can use the full amount of energy
     *
     * @param energy
     *            amount of energy to use from the tile.
     * @param stack
     *            The {@link ItemStack} that contains the power
     * @return if all the energy can be used.
     */
    public static boolean canUseEnergy(double energy, ItemStack stack) {
        return getEnergy(stack) >= energy;
    }

    /**
     * Will try and use the full amount of energy
     *
     * @param energy
     *            energy to use
     * @param stack
     *            The {@link ItemStack} that contains the power
     * @return the amount of energy used
     */
    public static double useEnergy(double energy, ItemStack stack) {
        return useEnergy(energy, false, stack);
    }

    /**
     * Will try and use the full amount of energy, if simulate is true it wont
     * add the energy
     *
     * @param energy
     *            energy to use
     * @param simulate
     *            if true it will not use the item, it will only simulate it.
     * @param stack
     *            The {@link ItemStack} that contains the power
     * @return the amount of energy used
     */
    public static double useEnergy(double energy, boolean simulate, ItemStack stack) {
        double energyStored = getEnergy(stack);
        if(energyStored >= energy) {
            if(!simulate)
                setEnergy(energyStored - energy, stack);
            return energy;
        }
        if(!simulate)
            setEnergy(0, stack);
        return energyStored;
    }

}
