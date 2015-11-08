package techreborn.items.tools;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import reborncore.common.util.Color;
import techreborn.api.power.IEnergyItemInfo;
import techreborn.client.TechRebornCreativeTab;
import techreborn.config.ConfigTechReborn;
import techreborn.powerSystem.PoweredItem;

import java.util.List;

public class ItemRockCutter extends ItemPickaxe implements IEnergyItemInfo {

    public static final int maxCharge = ConfigTechReborn.RockCutterCharge;
    public int cost = 500;
    public static final int tier = ConfigTechReborn.RockCutterTier;

    public ItemRockCutter() {
        super(Item.ToolMaterial.EMERALD);
        setUnlocalizedName("techreborn.rockcutter");
        setCreativeTab(TechRebornCreativeTab.instance);
        setMaxStackSize(1);
        efficiencyOnProperMaterial = 16F;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("techreborn:" + "tool/rockcutter");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack ist) {
        return true;
    }

    @Override
    public void addInformation(ItemStack iS, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        if (!iS.isItemEnchanted()) {
            par3List.add(Color.WHITE + "Silk Touch I");
        }
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack stack) {
        if (Items.diamond_pickaxe.canHarvestBlock(block, stack)) {
            if (PoweredItem.canUseEnergy(cost, stack)) {
                PoweredItem.useEnergy(cost, stack);
                return true;
            }
        }
        return false;
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass) {
        if (!stack.isItemEnchanted()) {
            stack.addEnchantment(Enchantment.silkTouch, 1);
        }
        return super.getHarvestLevel(stack, toolClass);
    }

    @Override
    public float func_150893_a(ItemStack stack, Block block) {
        if (!stack.isItemEnchanted()) {
            stack.addEnchantment(Enchantment.silkTouch, 1);
        }
        return super.func_150893_a(stack, block);
    }

    @Override
    public boolean isRepairable() {
        return false;
    }

    public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        par1ItemStack.addEnchantment(Enchantment.silkTouch, 1);
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
    public int getStackTeir(ItemStack stack) {
        return 2;
    }

    @SuppressWarnings(
            {"rawtypes", "unchecked"})
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
}
