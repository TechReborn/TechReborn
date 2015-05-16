package techreborn.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mark on 12/04/15.
 */
public class ItemUtils {

	public static boolean isItemEqual(final ItemStack a, final ItemStack b,
			final boolean matchDamage, final boolean matchNBT)
	{
		if (a == null || b == null)
			return false;
		if (a.getItem() != b.getItem())
			return false;
		if (matchNBT && !ItemStack.areItemStackTagsEqual(a, b))
			return false;
		if (matchDamage && a.getHasSubtypes())
		{
			if (isWildcard(a) || isWildcard(b))
				return true;
			if (a.getItemDamage() != b.getItemDamage())
				return false;
		}
		return true;
	}

	public static boolean isItemEqual(ItemStack a, ItemStack b,
									  boolean matchDamage, boolean matchNBT, boolean useOreDic)
	{
		if(isItemEqual(a, b, matchDamage, matchNBT)){
			return true;
		}
		if (a == null || b == null)
			return false;
		if(useOreDic){
			for (int inta : OreDictionary.getOreIDs(a)){
				for (int intb : OreDictionary.getOreIDs(b)){
					if(inta == intb){
						return true;
					}
				}
			}
		}
		return false;
	}


	public static boolean isWildcard(ItemStack stack)
	{
		return isWildcard(stack.getItemDamage());
	}

	public static boolean isWildcard(int damage)
	{
		return damage == -1 || damage == OreDictionary.WILDCARD_VALUE;
	}

	public static void writeInvToNBT(IInventory inv, String tag,
			NBTTagCompound data)
	{
		NBTTagList list = new NBTTagList();
		for (byte slot = 0; slot < inv.getSizeInventory(); slot++)
		{
			ItemStack stack = inv.getStackInSlot(slot);
			if (stack != null)
			{
				NBTTagCompound itemTag = new NBTTagCompound();
				itemTag.setByte("Slot", slot);
				writeItemToNBT(stack, itemTag);
				list.appendTag(itemTag);
			}
		}
		data.setTag(tag, list);
	}

	public static void readInvFromNBT(IInventory inv, String tag,
			NBTTagCompound data)
	{
		NBTTagList list = data.getTagList(tag, 10);
		for (byte entry = 0; entry < list.tagCount(); entry++)
		{
			NBTTagCompound itemTag = list.getCompoundTagAt(entry);
			int slot = itemTag.getByte("Slot");
			if (slot >= 0 && slot < inv.getSizeInventory())
			{
				ItemStack stack = readItemFromNBT(itemTag);
				inv.setInventorySlotContents(slot, stack);
			}
		}
	}

	public static void writeItemToNBT(ItemStack stack, NBTTagCompound data)
	{
		if (stack == null || stack.stackSize <= 0)
			return;
		if (stack.stackSize > 127)
			stack.stackSize = 127;
		stack.writeToNBT(data);
	}

	public static ItemStack readItemFromNBT(NBTTagCompound data)
	{
		return ItemStack.loadItemStackFromNBT(data);
	}

	public static List<ItemStack> getStackWithAllOre(ItemStack stack){
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		for (int oreID : OreDictionary.getOreIDs(stack)){
			for(ItemStack ore : OreDictionary.getOres(OreDictionary.getOreName(oreID))){
				ItemStack newOre = ore;
				newOre.stackSize = stack.stackSize;
				list.add(newOre);
			}
		}
		if(list.isEmpty()){
			list.add(stack);
		}
		return list;
	}
}
