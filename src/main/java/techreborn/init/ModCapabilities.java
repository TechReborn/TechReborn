package techreborn.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import reborncore.api.power.IEnergyItemInfo;
import techreborn.api.armor.ArmorUpgradeCapabilityProvider;
import techreborn.api.armor.IArmorUpgrade;
import techreborn.api.armor.IModularArmorManager;
import techreborn.items.armor.modular.upgrades.PowerStorageUprgade;
import techreborn.items.tools.ItemElectricTreetap;
import techreborn.lib.ModInfo;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ModCapabilities {

	public static Map<Item, IArmorUpgrade> upgradeMap = new HashMap<>();
	public static PowerStorageUprgade powerStorageUprgade = new PowerStorageUprgade();

	public static void init(){
		CapabilityManager.INSTANCE.register(IArmorUpgrade.class, new Capability.IStorage<IArmorUpgrade>() {
			@Nullable
			@Override
			public NBTBase writeNBT(Capability<IArmorUpgrade> capability, IArmorUpgrade instance, EnumFacing side) {
				return null;
			}

			@Override
			public void readNBT(Capability<IArmorUpgrade> capability, IArmorUpgrade instance, EnumFacing side, NBTBase nbt) {

			}
		}, () -> {
			//TODO can I even do this, or do I NEED to have a defualt?
			throw new RuntimeException("No defualt implimentation advalible");
		});

		CapabilityManager.INSTANCE.register(IModularArmorManager.class, new Capability.IStorage<IModularArmorManager>() {
			@Nullable
			@Override
			public NBTBase writeNBT(Capability<IModularArmorManager> capability, IModularArmorManager instance, EnumFacing side) {
				return null;
			}

			@Override
			public void readNBT(Capability<IModularArmorManager> capability, IModularArmorManager instance, EnumFacing side, NBTBase nbt) {

			}
		}, () -> {
			//TODO can I even do this, or do I NEED to have a defualt?
			throw new RuntimeException("No defualt implimentation advalible");
		});

		MinecraftForge.EVENT_BUS.register(ModCapabilities.class);
	}

	@SubscribeEvent
	public static void attachCapabilities(AttachCapabilitiesEvent<ItemStack> event){
		if(upgradeMap.containsKey(event.getObject().getItem())){
			event.addCapability(new ResourceLocation(ModInfo.MOD_ID, "armor_uprgade_attach"), ArmorUpgradeCapabilityProvider.getUpgradeProvider(event.getObject(), upgradeMap.get(event.getObject().getItem())));
		}
		if(event.getObject().getItem() instanceof IEnergyItemInfo && !(event.getObject().getItem() instanceof ItemTool) && !(event.getObject().getItem() instanceof ItemSword) && !(event.getObject().getItem() instanceof ItemElectricTreetap)){
			event.addCapability(new ResourceLocation(ModInfo.MOD_ID, "armor_uprgade_power_attach"), ArmorUpgradeCapabilityProvider.getUpgradeProvider(event.getObject(), powerStorageUprgade));
		}
	}

}
