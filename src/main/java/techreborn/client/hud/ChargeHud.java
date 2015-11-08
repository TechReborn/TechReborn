package techreborn.client.hud;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import org.lwjgl.opengl.GL11;
import reborncore.common.util.Color;
import techreborn.api.power.IEnergyInterfaceItem;
import techreborn.client.keybindings.KeyBindings;
import techreborn.config.ConfigTechReborn;

public class ChargeHud {
    public static final ChargeHud instance = new ChargeHud();
    private static Minecraft mc = Minecraft.getMinecraft();
    public static KeyBindings key;
    public static boolean showHud = true;


    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onRenderExperienceBar(RenderGameOverlayEvent event) {
        if (key.config.isPressed()) {
            showHud = !showHud;
        }

        if (event.isCancelable() || event.type != ElementType.ALL)
            return;

        if (mc.inGameHasFocus || (mc.currentScreen != null && mc.gameSettings.showDebugInfo)) {
            if (ConfigTechReborn.ShowChargeHud)
                drawChargeHud(event.resolution);
        }
    }

    public void drawChargeHud(ScaledResolution res) {
        EntityPlayer player = mc.thePlayer;
        ItemStack armorstack = player.getCurrentArmor(2);
        ItemStack stack = mc.thePlayer.inventory.getCurrentItem();

        int y = 5;

        if (armorstack != null && ConfigTechReborn.ShowChargeHud && armorstack.getItem() instanceof IEnergyInterfaceItem) {
            double MaxCharge = ((IEnergyInterfaceItem) armorstack.getItem()).getMaxPower(armorstack);
            double CurrentCharge = ((IEnergyInterfaceItem) armorstack.getItem()).getEnergy(armorstack);
            Color color = Color.GREEN;
            double quarter = MaxCharge / 4;
            double half = MaxCharge / 2;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(32826);
            RenderHelper.enableStandardItemLighting();
            RenderHelper.enableGUIStandardItemLighting();
            //Render the stack
            RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, armorstack, 0, y - 5);
            //Render Overlay
            RenderItem.getInstance().renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, armorstack, 0, y - 5);
            //Get the color depending on current charge
            if (CurrentCharge <= half) {
                color = Color.YELLOW;
            }
            if (CurrentCharge <= quarter) {
                color = Color.DARK_RED;
            }
            mc.fontRenderer.drawString(color + GetEUString(CurrentCharge) + "/" + GetEUString(MaxCharge), 20, y, 0);
            y += 20;
        }

        if (showHud) {
            if (stack != null && stack.getItem() instanceof IEnergyInterfaceItem) {
                double MaxCharge = ((IEnergyInterfaceItem) stack.getItem()).getMaxPower(stack);
                double CurrentCharge = ((IEnergyInterfaceItem) stack.getItem()).getEnergy(stack);
                Color color = Color.GREEN;
                double quarter = MaxCharge / 4;
                double half = MaxCharge / 2;
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(32826);
                RenderHelper.enableStandardItemLighting();
                RenderHelper.enableGUIStandardItemLighting();
                RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, stack, 0, y - 5);
                RenderItem.getInstance().renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, stack, 0, y - 5);
                if (CurrentCharge <= half) {
                    color = Color.YELLOW;
                }
                if (CurrentCharge <= quarter) {
                    color = Color.DARK_RED;
                }
                mc.fontRenderer.drawString(color + GetEUString(CurrentCharge) + "/" + GetEUString(MaxCharge), 20, y, 0);
            }
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private String GetEUString(double euValue) {
        if (euValue > 1000000) {
            double tenX = Math.round(euValue / 100000);
            return Double.toString(tenX / 10.0).concat("M ");
        } else if (euValue > 1000) {
            double tenX = Math.round(euValue / 100);
            return Double.toString(tenX / 10.0).concat("k ");
        } else {
            return Double.toString(Math.floor(euValue)).concat(" EU");
        }
    }
}
