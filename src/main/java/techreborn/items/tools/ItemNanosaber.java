package techreborn.items.tools;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.PoweredItem;
import reborncore.common.util.ChatUtils;
import techreborn.client.TechRebornCreativeTab;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModItems;
import techreborn.lib.MessageIDs;

import javax.annotation.Nullable;
import java.util.List;

public class ItemNanosaber extends ItemSword implements IEnergyItemInfo
{
	public int cost = 250;

	public ItemNanosaber()
	{
		super(ToolMaterial.DIAMOND);
		setNoRepair();
		setCreativeTab(TechRebornCreativeTab.instance);
		setMaxStackSize(1);
		setMaxDamage(1);
		setUnlocalizedName("techreborn.nanosaber");
		this.addPropertyOverride(new ResourceLocation("techreborn:active"), new IItemPropertyGetter()
		{
			@SideOnly(Side.CLIENT) public float apply(ItemStack stack, @Nullable World worldIn,
					@Nullable EntityLivingBase entityIn)
			{
				if (stack != null && stack.getTagCompound().getBoolean("isActive"))
				{
					return 1.0F;
				}
				return 0.0F;
			}
		});
	}

	@Override public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot,
			ItemStack stack)
	{
		Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();
		int modifier = 0;
		if (stack.getTagCompound().getBoolean("isActive"))
			modifier = 9;

		if (slot == EntityEquipmentSlot.MAINHAND)
		{
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(),
					new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double) modifier, 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(),
					new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
		}
		return multimap;
	}

	@Override public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving,
			EntityLivingBase entityliving1)
	{
		if (PoweredItem.canUseEnergy(cost, itemstack))
		{
			PoweredItem.useEnergy(cost, itemstack);
			return true;
		} else
		{
			return false;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" }) @SideOnly(Side.CLIENT) public void getSubItems(Item item,
			CreativeTabs par2CreativeTabs, List itemList)
	{
		ItemStack inactiveUncharged = new ItemStack(ModItems.nanosaber);
		inactiveUncharged.setTagCompound(new NBTTagCompound());
		inactiveUncharged.getTagCompound().setBoolean("isActive", false);

		ItemStack inactiveCharged = new ItemStack(ModItems.nanosaber);
		inactiveCharged.setTagCompound(new NBTTagCompound());
		inactiveCharged.getTagCompound().setBoolean("isActive", false);
		PoweredItem.setEnergy(getMaxPower(inactiveCharged), inactiveCharged);

		ItemStack activeUncharged = new ItemStack(ModItems.nanosaber);
		activeUncharged.setTagCompound(new NBTTagCompound());
		activeUncharged.getTagCompound().setBoolean("isActive", true);

		ItemStack activeCharged = new ItemStack(ModItems.nanosaber);
		activeCharged.setTagCompound(new NBTTagCompound());
		activeCharged.getTagCompound().setBoolean("isActive", true);
		PoweredItem.setEnergy(getMaxPower(activeCharged), activeCharged);

		itemList.add(inactiveUncharged);
		itemList.add(inactiveCharged);
		itemList.add(activeUncharged);
		itemList.add(activeCharged);
	}

	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
	{
		if (stack.getTagCompound() == null || !stack.getTagCompound().getBoolean("isActive"))
		{
			list.add(TextFormatting.GRAY + I18n.translateToLocal("techreborn.message.nanosaberInactive"));
		} else
		{
			list.add(TextFormatting.GRAY + I18n.translateToLocal("techreborn.message.nanosaberActive"));
		}
	}

	@Override public boolean showDurabilityBar(ItemStack stack)
	{
		return true;
	}

	@Override public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player,
			EnumHand hand)
	{
		if (player.isSneaking())
		{
			if (stack.getTagCompound() == null || !stack.getTagCompound().getBoolean("isActive"))
			{
				stack.setTagCompound(new NBTTagCompound());
				stack.getTagCompound().setBoolean("isActive", true);
				if (!world.isRemote && ConfigTechReborn.NanosaberChat)
				{
					ChatUtils.sendNoSpamMessages(MessageIDs.nanosaberID, new TextComponentString(
							TextFormatting.GRAY + I18n.translateToLocal("techreborn.message.setTo") + " "
									+ TextFormatting.GOLD + I18n
									.translateToLocal("techreborn.message.nanosaberActive")));
				}
			} else
			{
				stack.getTagCompound().setBoolean("isActive", false);
				if (!world.isRemote && ConfigTechReborn.NanosaberChat)
				{
					ChatUtils.sendNoSpamMessages(MessageIDs.nanosaberID, new TextComponentString(
							TextFormatting.GRAY + I18n.translateToLocal("techreborn.message.setTo") + " "
									+ TextFormatting.GOLD + I18n
									.translateToLocal("techreborn.message.nanosaberInactive")));
				}
			}
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}

	@Override public double getDurabilityForDisplay(ItemStack stack)
	{
		if (PoweredItem.getEnergy(stack) > getMaxPower(stack))
		{
			return 0;
		}
		double charge = (PoweredItem.getEnergy(stack) / getMaxPower(stack));
		return 1 - charge;


	}

	@Override public boolean isRepairable()
	{
		return false;
	}

	@Override public double getMaxPower(ItemStack stack)
	{
		return 100000;
	}

	@Override public boolean canAcceptEnergy(ItemStack stack)
	{
		return true;
	}

	@Override public boolean canProvideEnergy(ItemStack stack)
	{
		return false;
	}

	@Override public double getMaxTransfer(ItemStack stack)
	{
		return 512;
	}

	@Override public int getStackTier(ItemStack stack)
	{
		return 2;
	}
}
