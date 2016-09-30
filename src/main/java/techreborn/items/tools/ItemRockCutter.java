package techreborn.items.tools;

import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.RebornCore;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.PoweredItem;
import techreborn.client.TechRebornCreativeTab;
import techreborn.config.ConfigTechReborn;
import techreborn.lib.ModInfo;

import java.util.List;
import java.util.Random;

public class ItemRockCutter extends ItemPickaxe implements IEnergyItemInfo, ITexturedItem {

	public static final int maxCharge = ConfigTechReborn.RockCutterCharge;
	public static final int tier = ConfigTechReborn.RockCutterTier;
	public int cost = 500;

	public ItemRockCutter() {
		super(ToolMaterial.DIAMOND);
		setUnlocalizedName("techreborn.rockcutter");
		setCreativeTab(TechRebornCreativeTab.instance);
		setMaxStackSize(1);
		efficiencyOnProperMaterial = 16F;
		RebornCore.jsonDestroyer.registerObject(this);
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
	public boolean canHarvestBlock(IBlockState state) {
		if (Items.DIAMOND_PICKAXE.canHarvestBlock(state)) {
			return true;
		}
		return false;
	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass) {

		if (!stack.isItemEnchanted()) {
			stack.addEnchantment(Enchantment.getEnchantmentByID(33), 1);
		}

		return super.getHarvestLevel(stack, toolClass);
	}

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state) {
		if (!PoweredItem.canUseEnergy(cost, stack)) {
			return 2F;
		} else {
			return Items.DIAMOND_PICKAXE.getStrVsBlock(stack, state);
		}
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		stack.addEnchantment(Enchantments.SILK_TOUCH, 1);
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
		return 200;
	}

	@Override
	public int getStackTier(ItemStack stack) {
		return 2;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item,
	                        CreativeTabs par2CreativeTabs, List itemList) {
		ItemStack itemStack = new ItemStack(this, 1);
		itemStack.addEnchantment(Enchantments.SILK_TOUCH, 1);
		itemList.add(itemStack);

		ItemStack charged = new ItemStack(this, 1);
		charged.addEnchantment(Enchantments.SILK_TOUCH, 1);
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
		return "techreborn:items/tool/rockcutter";
	}

	@Override
	public int getMaxMeta() {
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player,
	                                      int useRemaining) {
		return new ModelResourceLocation(ModInfo.MOD_ID + ":" + getUnlocalizedName(stack).substring(5), "inventory");
	}

}
