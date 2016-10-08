package techreborn.events;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.oredict.OreDictionary;
import reborncore.common.util.ItemUtils;
import techreborn.config.ConfigTechReborn;

import java.util.HashMap;

public class OreUnifier {

	public static HashMap<String, ItemStack> oreHash = new HashMap<>();

	public static void registerOre(String name, ItemStack ore) {
		oreHash.put(name, ore);
		OreDictionary.registerOre(name, ore);
	}

	public static void registerOre(String name, Item ore) {
		registerOre(name, new ItemStack(ore));
	}

	public static void registerOre(String name, Block ore) {
		registerOre(name, new ItemStack(ore));
	}

	@SubscribeEvent
	public void itemTick(TickEvent.PlayerTickEvent event) {
		if (ConfigTechReborn.OreUnifer && !event.player.worldObj.isRemote
			&& event.player.worldObj.getTotalWorldTime() % 10 == 0) {
			if (event.player.getHeldItem(EnumHand.MAIN_HAND) != null) {
				int[] oreIds = OreDictionary.getOreIDs(event.player.getHeldItem(EnumHand.MAIN_HAND));
				for (int id : oreIds) {
					String oreName = OreDictionary.getOreName(id);
					if (oreHash.containsKey(oreName)) {
						if (ItemUtils.isItemEqual(event.player.getHeldItem(EnumHand.MAIN_HAND), oreHash.get(oreName),
							true, true, true)
							&& !ItemUtils.isItemEqual(event.player.getHeldItem(EnumHand.MAIN_HAND),
							oreHash.get(oreName), true, true, false)) {
							ItemStack stack = oreHash.get(oreName).copy();
							stack.stackSize = event.player.getHeldItem(EnumHand.MAIN_HAND).stackSize;
							stack.setTagCompound(event.player.getHeldItem(EnumHand.MAIN_HAND).getTagCompound());
							event.player.inventory.setInventorySlotContents(event.player.inventory.currentItem, stack);
						}
					}
				}
			}
		}
	}

}
