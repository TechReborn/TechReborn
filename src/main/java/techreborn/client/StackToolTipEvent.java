package techreborn.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.lwjgl.input.Keyboard;
import reborncore.api.IListInfoProvider;
import reborncore.common.util.Color;
import techreborn.api.power.IEnergyInterfaceItem;

public class StackToolTipEvent {

    @SubscribeEvent
    public void handleItemTooltipEvent(ItemTooltipEvent event) {
        if (event.itemStack.getItem() instanceof IListInfoProvider) {
            ((IListInfoProvider) event.itemStack.getItem()).addInfo(event.toolTip, false);
        } else if (event.itemStack.getItem() instanceof IEnergyInterfaceItem) {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                int percentage = percentage((int) ((IEnergyInterfaceItem) event.itemStack.getItem()).getMaxPower(event.itemStack), (int) ((IEnergyInterfaceItem) event.itemStack.getItem()).getEnergy(event.itemStack));
                ChatFormatting color;
                if (percentage <= 10) {
                    color = ChatFormatting.RED;
                } else if (percentage >= 75) {
                    color = ChatFormatting.GREEN;
                } else {
                    color = ChatFormatting.YELLOW;
                }
                event.toolTip.add(color + "" + (int) ((IEnergyInterfaceItem) event.itemStack.getItem()).getEnergy(event.itemStack) + ChatFormatting.LIGHT_PURPLE + " stored eu");
                event.toolTip.add(Color.GREEN + "" + (int) ((IEnergyInterfaceItem) event.itemStack.getItem()).getMaxPower(event.itemStack) + ChatFormatting.LIGHT_PURPLE + " max eu");
                event.toolTip.add(ChatFormatting.GREEN + "" + percentage + "%" + ChatFormatting.LIGHT_PURPLE + " charged");
                event.toolTip.add(Color.GREEN + "" + (int) ((IEnergyInterfaceItem) event.itemStack.getItem()).getMaxTransfer(event.itemStack) + ChatFormatting.LIGHT_PURPLE + " eu/tick in/out");
            }
        } else {
            Block block = Block.getBlockFromItem(event.itemStack.getItem());
            if (block != null && block instanceof BlockContainer && block.getClass().getCanonicalName().startsWith("techreborn.")) {
                TileEntity tile = block.createTileEntity(Minecraft.getMinecraft().theWorld, event.itemStack.getItemDamage());
                if (tile instanceof IListInfoProvider) {
                    ((IListInfoProvider) tile).addInfo(event.toolTip, false);
                }
            }
        }
    }


    public int percentage(int MaxValue, int CurrentValue) {
        if (CurrentValue == 0)
            return 0;
        return (int) ((CurrentValue * 100.0f) / MaxValue);
    }

}
