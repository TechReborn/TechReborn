package techreborn.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;

public class ItemTechManuel extends Item{
	
	public ItemTechManuel()
	{
		setCreativeTab(TechRebornCreativeTab.instance);
		setUnlocalizedName("techreborn.manuel");
		setMaxStackSize(1);
	}
	
    @Override
    public void registerIcons(IIconRegister iconRegister)
    {
        itemIcon = iconRegister.registerIcon("techreborn:" + "tool/manuel");
    }
    
    @Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) 
	{
		player.openGui(Core.INSTANCE, GuiHandler.manuelID, world, (int)player.posX, (int)player.posY, (int)player.posY);
		return itemStack;
	}

}
