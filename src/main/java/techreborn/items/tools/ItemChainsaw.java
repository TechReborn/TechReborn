package techreborn.items.tools;

import java.util.List;
import java.util.Random;

import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.RebornCore;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.PoweredItem;
import reborncore.common.util.TorchHelper;
import techreborn.client.TechRebornCreativeTab;
import techreborn.utils.OreDictUtils;

public class ItemChainsaw extends ItemAxe implements IEnergyItemInfo, ITexturedItem {

	public int maxCharge = 1;
	public int cost = 250;
	public float unpoweredSpeed = 2.0F;
	public static int tier = 1;
	public double transferLimit = 100;

	public ItemChainsaw(ToolMaterial material, String unlocalizedName, int energyCapacity, int tier, float unpoweredSpeed) {
		super(material);
		efficiencyOnProperMaterial = 20F;
		setCreativeTab(TechRebornCreativeTab.instance);
		setMaxStackSize(1);
		setMaxDamage(240);
		setUnlocalizedName(unlocalizedName);
		RebornCore.jsonDestroyer.registerObject(this);
		this.maxCharge = energyCapacity;
		this.tier = tier;
		this.unpoweredSpeed = unpoweredSpeed;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn) {
		Random rand = new Random();
		if (rand.nextInt(EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) + 1) == 0) {
			PoweredItem.useEnergy(cost, stack);
		}
		return true;
	}

	@Override
	public float getDigSpeed(ItemStack stack, IBlockState state) {
		if (!PoweredItem.canUseEnergy(cost, stack)) {
			return unpoweredSpeed;
		}

		if (OreDictUtils.isOre(state, "treeLeaves") && PoweredItem.canUseEnergy(cost, stack)) {
			return 40F;
		}

		if (Items.wooden_axe.getDigSpeed(stack, state) > 1.0F) {
			return efficiencyOnProperMaterial;
		} else {
			return super.getDigSpeed(stack, state);
		}
	}

	@Override
	public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase entityliving1) {
		return true;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		return TorchHelper.placeTorch(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public double getMaxPower(ItemStack stack) {
		return maxCharge;
	}

	@Override
	public boolean canAcceptEnergy(ItemStack stack) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(ItemStack stack) {
		return false;
	}

	@Override
	public double getMaxTransfer(ItemStack stack) {
		return transferLimit;
	}

	@Override
	public int getStackTeir(ItemStack stack) {
		return tier;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List itemList) {
		ItemStack itemStack = new ItemStack(this, 1);
		itemList.add(itemStack);

		ItemStack charged = new ItemStack(this, 1);
		PoweredItem.setEnergy(getMaxPower(charged), charged);
		itemList.add(charged);
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		double charge = (PoweredItem.getEnergy(stack) / getMaxPower(stack));
		return 1 - charge;

	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}

	@Override
	public String getTextureName(int damage) {
		return "techreborn:items/tool/nullChainsaw";
	}

	@Override
	public int getMaxMeta() {
		return 1;
	}
}
