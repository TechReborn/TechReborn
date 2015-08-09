package techreborn.itemblocks;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import techreborn.achievement.ICraftAchievement;
import techreborn.achievement.IPickupAchievement;

public class ItemBlockBase extends ItemMultiTexture implements IPickupAchievement, ICraftAchievement {
    public ItemBlockBase(Block p_i45346_1_, Block p_i45346_2_,
                         String[] p_i45346_3_) {
        super(p_i45346_1_, p_i45346_2_, p_i45346_3_);
    }

    @Override
    public Achievement getAchievementOnCraft(ItemStack stack,
                                             EntityPlayer player, IInventory matrix) {
        return field_150939_a instanceof ICraftAchievement ? ((ICraftAchievement) field_150939_a)
                .getAchievementOnCraft(stack, player, matrix) : null;
    }

    @Override
    public Achievement getAchievementOnPickup(ItemStack stack,
                                              EntityPlayer player, EntityItem item) {
        return field_150939_a instanceof IPickupAchievement ? ((IPickupAchievement) field_150939_a)
                .getAchievementOnPickup(stack, player, item) : null;
    }

}
