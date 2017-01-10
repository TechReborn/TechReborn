package techreborn.items.tools;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.PoweredItem;
import techreborn.client.TechRebornCreativeTab;

import java.util.Random;

public class ItemDrill extends ItemPickaxe implements IEnergyItemInfo {

	public static int tier = 1;
	public int maxCharge = 1;
	public int cost = 250;
	public float unpoweredSpeed = 2.0F;
	public double transferLimit = 100;

	public ItemDrill(ToolMaterial material, String unlocalizedName, int energyCapacity, int tier, float unpoweredSpeed, float efficiencyOnProperMaterial) {
		super(material);
		this.efficiencyOnProperMaterial = efficiencyOnProperMaterial;
		setCreativeTab(TechRebornCreativeTab.instance);
		setMaxStackSize(1);
		setUnlocalizedName(unlocalizedName);
		this.maxCharge = energyCapacity;
		this.tier = tier;
		this.unpoweredSpeed = unpoweredSpeed;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos,
	                                EntityLivingBase entityLiving) {
		Random rand = new Random();
		if (rand.nextInt(EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack) + 1) == 0) {
			PoweredItem.useEnergy(cost, stack);
		}
		return true;
	}

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state) {
		if (!PoweredItem.canUseEnergy(cost, stack)) {
			return unpoweredSpeed;
		}
		if (Items.WOODEN_PICKAXE.getStrVsBlock(stack, state) > 1.0F
			|| Items.WOODEN_SHOVEL.getStrVsBlock(stack, state) > 1.0F) {
			return efficiencyOnProperMaterial;
		} else {
			return super.getStrVsBlock(stack, state);
		}
	}

	@Override
	public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving,
	                         EntityLivingBase entityliving1) {
		return true;
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
	public int getStackTier(ItemStack stack) {
		return tier;
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
}
