package techreborn.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import reborncore.api.IListInfoProvider;
import reborncore.api.power.IEnergyInterfaceItem;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.Color;
import techreborn.Core;

public class StackToolTipEvent {

	@SubscribeEvent
	public void handleItemTooltipEvent(ItemTooltipEvent event) {
		if (event.getItemStack().getItem() instanceof IListInfoProvider) {
			((IListInfoProvider) event.getItemStack().getItem()).addInfo(event.getToolTip(), false);
		} else if (event.getItemStack().getItem() instanceof IEnergyInterfaceItem) {
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
				int percentage = percentage(
					(int) ((IEnergyInterfaceItem) event.getItemStack().getItem()).getMaxPower(event.getItemStack()),
					(int) ((IEnergyInterfaceItem) event.getItemStack().getItem()).getEnergy(event.getItemStack()));
				TextFormatting color;
				if (percentage <= 10) {
					color = TextFormatting.RED;
				} else if (percentage >= 75) {
					color = TextFormatting.GREEN;
				} else {
					color = TextFormatting.YELLOW;
				}
				event.getToolTip().add(color + ""
					+ PowerSystem.getLocaliszedPower(
					(int) ((IEnergyInterfaceItem) event.getItemStack().getItem()).getEnergy(event.getItemStack()))
					+ TextFormatting.LIGHT_PURPLE + " stored");
				event.getToolTip().add(Color.GREEN + ""
					+ PowerSystem.getLocaliszedPower(
					(int) ((IEnergyInterfaceItem) event.getItemStack().getItem()).getMaxPower(event.getItemStack()))
					+ TextFormatting.LIGHT_PURPLE + " max");
				event.getToolTip()
					.add(TextFormatting.GREEN + "" + percentage + "%" + TextFormatting.LIGHT_PURPLE + " charged");
				event.getToolTip().add(Color.GREEN + "" + PowerSystem.getLocaliszedPower(
					(int) ((IEnergyInterfaceItem) event.getItemStack().getItem()).getMaxTransfer(event.getItemStack()))
					+ TextFormatting.LIGHT_PURPLE + " /tick in/out");
			}
		} else {
			try {
				Block block = Block.getBlockFromItem(event.getItemStack().getItem());
				if (block != null && (block instanceof BlockContainer || block instanceof ITileEntityProvider)
					&& block.getRegistryName().getResourceDomain().contains("techreborn")) {
					TileEntity tile = block.createTileEntity(Minecraft.getMinecraft().theWorld,
						block.getDefaultState());
					if (tile instanceof IListInfoProvider) {
						((IListInfoProvider) tile).addInfo(event.getToolTip(), false);
					}
				}
			} catch (NullPointerException e) {
				Core.logHelper.debug("Failed to load info for " + event.getItemStack().getDisplayName());
			}
		}
	}

	public int percentage(int MaxValue, int CurrentValue) {
		if (CurrentValue == 0)
			return 0;
		return (int) ((CurrentValue * 100.0f) / MaxValue);
	}

}
