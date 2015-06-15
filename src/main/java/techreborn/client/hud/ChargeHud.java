package techreborn.client.hud;

import org.lwjgl.opengl.GL11;

import codechicken.lib.colour.ColourARGB;
import techreborn.client.keybindings.KeyBindings;
import techreborn.config.ConfigTechReborn;
import techreborn.util.Color;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import ic2.core.item.ElectricItemManager;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class ChargeHud 
{
	public static final ChargeHud instance = new ChargeHud();
	private static Minecraft mc = Minecraft.getMinecraft();
	public static KeyBindings key;
	public static boolean showHud = true;

	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.LOW)
	public void onRenderExperienceBar(RenderGameOverlayEvent event) 
	{
		if (key.config.isPressed())
		{
			showHud = !showHud;
		}

		if (event.isCancelable() || event.type != ElementType.ALL)
			return;
		
		if (mc.inGameHasFocus || (mc.currentScreen != null && mc.gameSettings.showDebugInfo))
			drawChargeHud(event.resolution);
	}
	
	public void drawChargeHud(ScaledResolution res)
	{
		EntityPlayer player = mc.thePlayer;
		ItemStack stack = player.getCurrentArmor(2);
		ItemStack stack2 = mc.thePlayer.inventory.getCurrentItem();
		if (showHud)
		{
			if(stack2 != null)
			{
				if ((stack2.getItem() instanceof IElectricItem))
				{
					double MaxCharge = ((IElectricItem) stack2.getItem()).getMaxCharge(stack2);
					double CurrentCharge = ElectricItem.manager.getCharge(stack2);
					Color color = Color.GREEN;
					double quarter = MaxCharge / 4;
					double half = MaxCharge / 2;
			        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			        GL11.glEnable(32826);
			        RenderHelper.enableStandardItemLighting();
			        RenderHelper.enableGUIStandardItemLighting();
			        RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, stack2, 0, 20);
					RenderItem.getInstance().renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, stack2, 0, 20);
					if(CurrentCharge <= half)
					{
						color = Color.YELLOW;
					}
					if(CurrentCharge <= quarter)
					{
						color = Color.DARK_RED;
					}
					mc.fontRenderer.drawString(color + Integer.toString((int)CurrentCharge) + "/" + Integer.toString((int)MaxCharge), 20, 25, 0);
	
				}
			}
			
			if(stack != null)
			{
				if((stack.getItem() instanceof IElectricItem) && ConfigTechReborn.ShowChargeHud)
				{
					double MaxCharge = ((IElectricItem) stack.getItem()).getMaxCharge(stack);
					double CurrentCharge = ElectricItem.manager.getCharge(stack);
					Color color = Color.GREEN;
					double quarter = MaxCharge / 4;
					double half = MaxCharge / 2;
			        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			        GL11.glEnable(32826);
			        RenderHelper.enableStandardItemLighting();
			        RenderHelper.enableGUIStandardItemLighting();
					//Render the stack
			        RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, stack, 0, 0);
					//Render Overlay
					RenderItem.getInstance().renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, stack, 0, 0);
					//Get the color depending on current charge
					if(CurrentCharge <= half)
					{
						color = Color.YELLOW;
					}
					if(CurrentCharge <= quarter)
					{
						color = Color.DARK_RED;
					}
					mc.fontRenderer.drawString(color + Double.toString(CurrentCharge) + "/" + Double.toString(MaxCharge), 20, 5, 0);
				}
			}
		}
	}
}
