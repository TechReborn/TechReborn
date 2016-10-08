package techreborn.items.tools;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.PoweredItem;
import techreborn.client.TechRebornCreativeTab;
import techreborn.config.ConfigTechReborn;
import techreborn.items.ItemTextureBase;

import java.util.List;

public class ItemCloakingDevice extends ItemTextureBase implements IEnergyItemInfo {
	public static int Teir = ConfigTechReborn.CloakingDeviceTier;
	public static int MaxCharge = ConfigTechReborn.CloakingDeviceCharge;
	public static int Limit = 100;
	public static boolean isActive;
	private int armorType = 1;

	public ItemCloakingDevice() {
		setUnlocalizedName("techreborn.cloakingdevice");
		setMaxStackSize(1);
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		if (PoweredItem.canUseEnergy(ConfigTechReborn.CloakingDeviceEUTick, itemStack)) {
			PoweredItem.useEnergy(ConfigTechReborn.CloakingDeviceEUTick, itemStack);
			player.setInvisible(true);
		} else {
			if (!player.isPotionActive(MobEffects.INVISIBILITY)) {
				player.setInvisible(false);
			}
		}
	}

	@Override
	public double getMaxPower(ItemStack stack) {
		return MaxCharge;
	}

	@Override
	public boolean canAcceptEnergy(ItemStack stack) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(ItemStack itemStack) {
		return false;
	}

	@Override
	public double getMaxTransfer(ItemStack stack) {
		return Limit;
	}

	@Override
	public int getStackTier(ItemStack stack) {
		return Teir;
	}

	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		ItemStack itemstack1 = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

		if (itemstack1 == null) {
			player.setItemStackToSlot(EntityEquipmentSlot.CHEST, itemStack.copy());
			itemStack.stackSize = 0;
		}

		return itemStack;
	}

	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List itemList) {
		ItemStack itemStack = new ItemStack(this, 1);
		itemList.add(itemStack);

		ItemStack charged = new ItemStack(this, 1);
		PoweredItem.setEnergy(getMaxPower(charged), charged);
		itemList.add(charged);
	}

	public double getDurabilityForDisplay(ItemStack stack) {
		double charge = (PoweredItem.getEnergy(stack) / getMaxPower(stack));
		return 1 - charge;

	}

	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}

	@Override
	public String getTextureName(int damage) {
		return "techreborn:items/tool/cloakingDevice";
	}

	@Override
	public int getMaxMeta() {
		return 1;
	}
}
