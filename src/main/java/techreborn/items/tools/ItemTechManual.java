package techreborn.items.tools;

import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.items.ItemTR;
import techreborn.items.ItemTextureBase;

public class ItemTechManual extends ItemTR implements ITexturedItem {

    public ItemTechManual() {
        setCreativeTab(TechRebornCreativeTab.instance);
        setUnlocalizedName("techreborn.manual");
        setMaxStackSize(1);
    }


    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        player.openGui(Core.INSTANCE, GuiHandler.manuelID, world,
                (int) player.posX, (int) player.posY, (int) player.posY);
        return itemStack;
    }

    @Override
    public int getMaxMeta() {
        return 1;
    }

    @Override
    public String getTextureName(int damage) {
        return "techreborn:items/tool/manual";
    }

}
