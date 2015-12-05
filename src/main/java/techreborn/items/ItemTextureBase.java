package techreborn.items;


import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import reborncore.api.IItemTexture;
import techreborn.lib.ModInfo;

public abstract class ItemTextureBase extends ItemTR implements IItemTexture {
    @Override
    public String getModID() {
        return ModInfo.MOD_ID;
    }

    @Override
    public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining) {
        return new ModelResourceLocation(ModInfo.MOD_ID + ":" + stack.getItem().getUnlocalizedName(stack).substring(5), "inventory");
    }
}
