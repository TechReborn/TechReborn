package techreborn.items.tools;

import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import techreborn.items.ItemPlates;
import techreborn.items.ItemTR;

public class ItemHammer extends ItemTR implements ITexturedItem {
    private String iconName;

    public ItemHammer(int MaxDamage) {
        setUnlocalizedName("techreborn.hammer");
        setMaxDamage(MaxDamage);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        world.playSoundAtEntity(player, "minecraft:random.anvil_land", 1F, 1F);
        return true;
    }
    
    @Override
    public Item setUnlocalizedName(String par1Str) {
        iconName = par1Str;
        return super.setUnlocalizedName(par1Str);
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack copiedStack = itemStack.copy();

        copiedStack.setItemDamage(copiedStack.getItemDamage() + 1);
        copiedStack.stackSize = 1;
        
        return copiedStack;
    }

    @Override
    public String getTextureName(int damage) {
        return "techreborn:items/tool/hammer";
    }

    @Override
    public int getMaxMeta() {
        return 1;
    }


}
