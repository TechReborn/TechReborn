package techreborn.items.tools;

import java.util.List;

import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.RebornCore;
import reborncore.common.powerSystem.PoweredItem;
import techreborn.client.TechRebornCreativeTab;

public class ItemTreeTap extends Item implements ITexturedItem {

	public ItemTreeTap() {
		setMaxStackSize(1);
		setMaxDamage(20);
		setUnlocalizedName("techreborn.treetap");
		RebornCore.jsonDestroyer.registerObject(this);
		setCreativeTab(TechRebornCreativeTab.instance);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		return super.onItemRightClick(itemStackIn, worldIn, playerIn);
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		if(stack.getMetadata()!=0){
			return true;
		}
		return false;
	}

    @Override
    public String getTextureName(int damage) {
        return "techreborn:items/tool/treetap";
    }

    @Override
    public int getMaxMeta() {
        return 1;
    }
}
