package techreborn.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.DimensionManager;
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
import techreborn.items.ItemFrequencyTransmitter;
import techreborn.items.tools.ItemNanosaber;

public class HudStackInfo {
    public static final HudStackInfo instance = new HudStackInfo();
    public static KeyBindings key;
    public static boolean showHud = true;
    private static Minecraft mc = Minecraft.getMinecraft();
    private int x = 2;
    private int y = 7;
    private final int yDef = 7;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onRenderExperienceBar(RenderGameOverlayEvent event) {
        if (key.config.isPressed()) {
            showHud = !showHud;
        }

        if (event.isCancelable() || event.getType() != ElementType.ALL)
            return;

        if (mc.inGameHasFocus || (mc.currentScreen != null && mc.gameSettings.showDebugInfo)) {
            if (ConfigTechReborn.ShowChargeHud)
                drawChargeHud(event.getResolution());
        }
    }

    public void drawChargeHud(ScaledResolution res) {
        EntityPlayer player = mc.thePlayer;
        y = yDef;
        if (showHud) {
            for (ItemStack stack : player.getArmorInventoryList()) {
                addInfo(stack);
            }
            addInfo(player.getHeldItemOffhand());
            addInfo(player.getHeldItemMainhand());
        }
    }

    public void renderItemStack(ItemStack stack, int x, int y) {
        if (stack != null) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderHelper.enableGUIStandardItemLighting();

            RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
            itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);

            GL11.glDisable(GL11.GL_LIGHTING);
        }
    }

    private void renderStackForInfo(ItemStack stack) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(32826);
        RenderHelper.enableStandardItemLighting();
        RenderHelper.enableGUIStandardItemLighting();
        renderItemStack(stack, x, y - 5);
    }

    private void addInfo(ItemStack stack) {
        if (stack != null) {
            boolean didShit = false;
            Color gold = Color.GOLD;
            Color grey = Color.GRAY;
            if (stack.getItem() instanceof IEnergyInterfaceItem) {
                double MaxCharge = ((IEnergyInterfaceItem) stack.getItem()).getMaxPower(stack);
                double CurrentCharge = ((IEnergyInterfaceItem) stack.getItem()).getEnergy(stack);
                Color color = Color.GREEN;
                double quarter = MaxCharge / 4;
                double half = MaxCharge / 2;
                renderStackForInfo(stack);
                if (CurrentCharge <= half) {
                    color = Color.YELLOW;
                }
                if (CurrentCharge <= quarter) {
                    color = Color.DARK_RED;
                }
                if (stack.getItem() instanceof ItemNanosaber) {
                    String state = I18n.translateToLocal("techreborn.message.nanosaberInactive");
                    if (stack.getTagCompound() != null && stack.getTagCompound().getBoolean("isActive")) {
                        state = I18n.translateToLocal("techreborn.message.nanosaberActive");
                    }
                    mc.fontRendererObj.drawString(color + PowerSystem.getLocaliszedPower(CurrentCharge) + "/" + PowerSystem.getLocaliszedPower(MaxCharge) + gold + " (" + state + ")", x + 18, y, 0);
                } else {
                    mc.fontRendererObj.drawString(color + PowerSystem.getLocaliszedPower(CurrentCharge) + "/" + PowerSystem.getLocaliszedPower(MaxCharge), x + 18, y, 0);
                }
                didShit = true;
            } else if (stack.getItem() instanceof ItemFrequencyTransmitter) {
                renderStackForInfo(stack);
                if (stack.getTagCompound() != null) {
                    int coordX = stack.getTagCompound().getInteger("x");
                    int coordY = stack.getTagCompound().getInteger("y");
                    int coordZ = stack.getTagCompound().getInteger("z");
                    int coordDim = stack.getTagCompound().getInteger("dim");
                    mc.fontRendererObj.drawString(grey + "X: " + gold + coordX + grey + " Y: " + gold + coordY + grey + " Z: " + gold + coordZ + grey + " Dim: " + gold + DimensionManager.getProviderType(coordDim).getName() + " (" + coordDim + ")", x + 18, y, 0);
                } else {
                    mc.fontRendererObj.drawString(grey + I18n.translateToLocal("techreborn.message.noCoordsSet"), x + 18, y, 0);
                }
                didShit = true;
            }
            if (didShit) {
                y += 20;
            }
        }
    }

}
