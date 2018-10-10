/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.tiles.tier1;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import reborncore.common.tile.TileLegacyMachineBase;
import reborncore.common.util.Inventory;
import techreborn.api.armor.ArmorSlot;
import techreborn.api.armor.IArmorUpgrade;
import techreborn.api.armor.IModularArmorManager;
import techreborn.api.armor.ModularArmorUtils;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;
import techreborn.tiles.TileGenericMachine;

public class TileWorkstation extends TileGenericMachine implements IContainerProvider {

	public static int armorSlot = 1;

	public TileWorkstation() {
		super("Grinder", 256, 40_000, ModBlocks.GRINDER, 0);
		this.inventory = new Inventory(6, "TileGrinder", 64, this);
	}

	// IContainerProvider
	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("grinder").player(player.inventory).inventory().hotbar()
			.armor().complete(28, 18).addArmor()
			.addInventory().tile(this)
			.slot(armorSlot, 97, 27)
			.toggleSlot(2, 71, 60, this::isValidForUprgade, this::showSlot)
			.toggleSlot(3, 89, 60, this::isValidForUprgade, this::showSlot)
			.toggleSlot(4, 107, 60, this::isValidForUprgade, this::showSlot)
			.toggleSlot(5, 125, 60, this::isValidForUprgade, this::showSlot)
			.energySlot(0, 8, 72).syncEnergyValue()
			.addInventory().create(this);
	}

	private boolean showSlot(Pair<TileLegacyMachineBase, Integer> pair) {
		//TODO check the size of the item's inv
		return !getStackInSlot(armorSlot).isEmpty();
	}

	public boolean isUpgradeSlot(int id){
		return id >= 2 && id <= 5;
	}

	public boolean isValidForUprgade(ItemStack stack){
		if(!ModularArmorUtils.isUprgade(stack)){
			return false;
		}
		IArmorUpgrade upgrade = ModularArmorUtils.getArmorUprgade(stack);
		if(!upgrade.allowMultiple()){
			for (int i = 2; i < 5; i++) {
				ItemStack other = getStackInSlot(i);
				if(ModularArmorUtils.isUprgade(other)){
					if(ModularArmorUtils.getArmorUprgade(other).getName().equals(upgrade.getName())){
						return false;
					}
				}
			}
		}
		if(!ModularArmorUtils.isModularArmor(getStackInSlot(armorSlot))){
			return false;
		}
		IModularArmorManager armorManager = ModularArmorUtils.getManager(getStackInSlot(armorSlot));
		if(armorManager.getArmorStack().getItem() instanceof ItemArmor){
			EntityEquipmentSlot slot = ((ItemArmor) armorManager.getArmorStack().getItem()).getEquipmentSlot();
			if(!upgrade.getValidSlots().contains(ArmorSlot.fromEntityEquipmentSlot(slot))){
				return false;
			}
		}

		return true;
	}

	public void invalidateArmor(){
		if(!getStackInSlot(armorSlot).isEmpty() && ModularArmorUtils.isModularArmor(getStackInSlot(armorSlot))){
			IModularArmorManager manager = ModularArmorUtils.getManager(getStackInSlot(armorSlot));
			manager.invalidate();
		}
	}

	public boolean hasArmorInvenotry(){
		return !getStackInSlot(armorSlot).isEmpty() && getStackInSlot(armorSlot).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
	}

	public IItemHandlerModifiable getArmorInvenory(){
		Validate.isTrue(hasArmorInvenotry());
		IItemHandler iItemHandler = getStackInSlot(armorSlot).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		Validate.notNull(iItemHandler);
		return (IItemHandlerModifiable) iItemHandler;
	}

	//Thise are overridden to allow some of the slot ids to be held in the item
	@Override
	public ItemStack getStackInSlot(int index) {
		if(isUpgradeSlot(index)){
			if(!hasArmorInvenotry()){
				return ItemStack.EMPTY;
			}
			IItemHandlerModifiable itemHandler = getArmorInvenory();
			return itemHandler.getStackInSlot(index);
		}
		return super.getStackInSlot(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if(isUpgradeSlot(index)) {
			if (!hasArmorInvenotry()) {
				return ItemStack.EMPTY;
			}
			if (getStackInSlot(index).getCount() > count) {
					ItemStack result = getStackInSlot(index).splitStack(count);
					markDirty();
					invalidateArmor();
					return result;
			}
			ItemStack stack = getStackInSlot(index);
			setInventorySlotContents(index, ItemStack.EMPTY);
			return stack;
		}
		return super.decrStackSize(index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		if(isUpgradeSlot(index)){
			if(!hasArmorInvenotry()){
				return ItemStack.EMPTY;
			}
			if (getStackInSlot(index).isEmpty()) {
				return ItemStack.EMPTY;
			}
			ItemStack stackToTake = getStackInSlot(index);
			setInventorySlotContents(index, ItemStack.EMPTY);
			return stackToTake;
		}
		return super.removeStackFromSlot(index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if(isUpgradeSlot(index)){
			if(!hasArmorInvenotry()){
				return;
			}
			IItemHandlerModifiable itemHandler = getArmorInvenory();
			ItemStack oldStack = getStackInSlot(index);
			if(ModularArmorUtils.isUprgade(oldStack)){
				ModularArmorUtils.getArmorUprgade(oldStack).onRemoved(oldStack, this);
			}
			itemHandler.setStackInSlot(index, stack);
			ItemStack newStack = getStackInSlot(index);
			if(ModularArmorUtils.isUprgade(newStack)){
				ModularArmorUtils.getArmorUprgade(newStack).onAdded(newStack, this);
			}
			invalidateArmor();
			return;
		}
		super.setInventorySlotContents(index, stack);
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	@Override
	public boolean hasSlotConfig() {
		return false;
	}
}