package techreborn.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import reborncore.api.power.IEnergyInterfaceItem;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.Color;
import techreborn.client.keybindings.KeyBindings;
import techreborn.config.ConfigTechReborn;

public class ChargeHud
{
	public static final ChargeHud instance = new ChargeHud();
	public static KeyBindings key;
	public static boolean showHud = true;
	private static Minecraft mc = Minecraft.getMinecraft();

	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.LOW)
	public void onRenderExperienceBar(RenderGameOverlayEvent event)
	{
		if (key.config.isPressed())
		{
			showHud = !showHud;
		}

		if (event.isCancelable() || event.getType() != ElementType.ALL)
			return;

		if (mc.inGameHasFocus || (mc.currentScreen != null && mc.gameSettings.showDebugInfo))
		{
			if (ConfigTechReborn.ShowChargeHud)
				drawChargeHud(event.getResolution());
		}
	}

	public void drawChargeHud(ScaledResolution res)
	{
		EntityPlayer player = mc.thePlayer;
		ItemStack armorstack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		ItemStack offHandstack = player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
		ItemStack stack = mc.thePlayer.inventory.getCurrentItem();

		int y = 5;

		if (armorstack != null && ConfigTechReborn.ShowChargeHud
				&& armorstack.getItem() instanceof IEnergyInterfaceItem)
		{
			double MaxCharge = ((IEnergyInterfaceItem) armorstack.getItem()).getMaxPower(armorstack);
			double CurrentCharge = ((IEnergyInterfaceItem) armorstack.getItem()).getEnergy(armorstack);
			Color color = Color.GREEN;
			double quarter = MaxCharge / 4;
			double half = MaxCharge / 2;
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(32826);
			RenderHelper.enableStandardItemLighting();
			RenderHelper.enableGUIStandardItemLighting();
			// Render the stack
			renderItemStack(armorstack, 0, y - 5);
			// Get the color depending on current charge
			if (CurrentCharge <= half)
			{
				color = Color.YELLOW;
			}
			if (CurrentCharge <= quarter)
			{
				color = Color.DARK_RED;
			}
			mc.fontRendererObj.drawString(color + PowerSystem.getLocaliszedPower(CurrentCharge) + "/"
					+ PowerSystem.getLocaliszedPower(MaxCharge), 20, y, 0);
			y += 20;
		}

		if (showHud)
		{
			if (offHandstack != null && offHandstack.getItem() instanceof IEnergyInterfaceItem)
			{
				double MaxCharge = ((IEnergyInterfaceItem) offHandstack.getItem()).getMaxPower(offHandstack);
				double CurrentCharge = ((IEnergyInterfaceItem) offHandstack.getItem()).getEnergy(offHandstack);
				Color color = Color.GREEN;
				double quarter = MaxCharge / 4;
				double half = MaxCharge / 2;
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glEnable(32826);
				RenderHelper.enableStandardItemLighting();
				RenderHelper.enableGUIStandardItemLighting();
				renderItemStack(offHandstack, 0, y - 5);
				if (CurrentCharge <= half)
				{
					color = Color.YELLOW;
				}
				if (CurrentCharge <= quarter)
				{
					color = Color.DARK_RED;
				}
				mc.fontRendererObj.drawString(color + PowerSystem.getLocaliszedPower(CurrentCharge) + "/"
						+ PowerSystem.getLocaliszedPower(MaxCharge), 20, y, 0);
				y += 20;
			}
			if (stack != null && stack.getItem() instanceof IEnergyInterfaceItem)
			{
				double MaxCharge = ((IEnergyInterfaceItem) stack.getItem()).getMaxPower(stack);
				double CurrentCharge = ((IEnergyInterfaceItem) stack.getItem()).getEnergy(stack);
				Color color = Color.GREEN;
				double quarter = MaxCharge / 4;
				double half = MaxCharge / 2;
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glEnable(32826);
				RenderHelper.enableStandardItemLighting();
				RenderHelper.enableGUIStandardItemLighting();
				renderItemStack(stack, 0, y - 5);
				if (CurrentCharge <= half)
				{
					color = Color.YELLOW;
				}
				if (CurrentCharge <= quarter)
				{
					color = Color.DARK_RED;
				}
				mc.fontRendererObj.drawString(color + PowerSystem.getLocaliszedPower(CurrentCharge) + "/" + PowerSystem.getLocaliszedPower(MaxCharge), 20, y, 0);
			}
		}
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	public void renderItemStack(ItemStack stack, int x, int y)
	{
		if (stack != null)
		{
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			RenderHelper.enableGUIStandardItemLighting();

			RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
			itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);

			GL11.glDisable(GL11.GL_LIGHTING);
		}
	}

}
