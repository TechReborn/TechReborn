package techreborn.client.hud;

import org.lwjgl.opengl.GL11;

import codechicken.lib.colour.ColourARGB;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class ChargeHud 
{
	public static final ChargeHud instance = new ChargeHud();
	private static Minecraft mc = Minecraft.getMinecraft();
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.LOW)
	public void onRenderExperienceBar(RenderGameOverlayEvent event) 
	{
		if (event.isCancelable() || event.type != ElementType.ALL)
			return;
		
		if (mc.inGameHasFocus || (mc.currentScreen != null && mc.gameSettings.showDebugInfo))
			drawChargeHud(event.resolution);
	}
	
	public void drawChargeHud(ScaledResolution res)
	{
		EntityPlayer player = mc.thePlayer;
		ItemStack stack = player.getCurrentArmor(2);
		if(stack != null)
		{
			if((stack.getItem() instanceof IElectricItem) && ConfigTechReborn.ShowChargeHud)
			{
				double MaxCharge = ((IElectricItem) stack.getItem()).getMaxCharge(stack);
				double CurrentCharge = ElectricItem.manager.getCharge(stack);
				Color color = Color.GREEN;
				double quarter = MaxCharge / 4;
				double half = MaxCharge / 2;
				double threeQuarters = MaxCharge / 4 * 3; 
		        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		        GL11.glEnable(32826);
		        RenderHelper.enableStandardItemLighting();
		        RenderHelper.enableGUIStandardItemLighting();
				//Render the stack
		        RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, stack, 0, 0);
				RenderItem.getInstance().renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, stack, 0, 0);
				//Render Overlay
				if(CurrentCharge <= threeQuarters)
				{
					color = Color.YELLOW;
				}
				if(CurrentCharge <= half)
				{
					color = Color.RED;
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
