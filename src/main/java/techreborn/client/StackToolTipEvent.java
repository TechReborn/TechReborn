package techreborn.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import techreborn.api.IListInfoProvider;

public class StackToolTipEvent {

    @SubscribeEvent
    public void handleItemTooltipEvent(ItemTooltipEvent event) {
        if(event.itemStack.getItem() instanceof IListInfoProvider){
            ((IListInfoProvider) event.itemStack.getItem()).addInfo(event.toolTip, false);
        } else{
            Block block = Block.getBlockFromItem(event.itemStack.getItem());
            if(block != null && block instanceof BlockContainer && block.getClass().getCanonicalName().startsWith("techreborn.")){
                TileEntity tile = block.createTileEntity(Minecraft.getMinecraft().theWorld, event.itemStack.getItemDamage());
                if(tile instanceof IListInfoProvider){
                    ((IListInfoProvider) tile).addInfo(event.toolTip, false);
                }
            }
        }
    }

}
