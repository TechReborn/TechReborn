package techreborn.partSystem.client;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;
import reborncore.common.misc.Location;
import reborncore.common.misc.vecmath.Vecs3d;
import techreborn.partSystem.IModPart;
import techreborn.partSystem.ModPartItem;

/**
 * This is based of
 * https://github.com/Qmunity/QmunityLib/blob/master/src%2Fmain%
 * 2Fjava%2Fuk%2Fco%2Fqmunity%2Flib%2Fclient%2Frender%2FRenderPartPlacement.java
 * <p/>
 * You should go check them out!
 */
@SideOnly(Side.CLIENT)
public class PartPlacementRenderer {

    private Framebuffer fb = null;
    private int width = 0, height = 0;

    @SubscribeEvent
    public void onRenderTick(RenderWorldLastEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        ItemStack item = player.getCurrentEquippedItem();
        if (item == null)
            return;
        if (!(item.getItem() instanceof ModPartItem))
            return;
        if (Minecraft.getMinecraft().gameSettings.hideGUI
                && Minecraft.getMinecraft().currentScreen == null)
            return;
        MovingObjectPosition mop = player.rayTrace(
                player.capabilities.isCreativeMode ? 5 : 4, 0);
        if (mop == null
                || mop.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK)
            return;
        IModPart part = ((ModPartItem) item.getItem()).getModPart();
        if (part == null)
            return;
        EnumFacing faceHit = EnumFacing.getOrientation(mop.sideHit);
        Location location = new Location(mop.blockX, mop.blockY, mop.blockZ);
        if (fb == null || width != Minecraft.getMinecraft().displayWidth
                || height != Minecraft.getMinecraft().displayHeight) {
            width = Minecraft.getMinecraft().displayWidth;
            height = Minecraft.getMinecraft().displayHeight;
            fb = new Framebuffer(width, height, true);
        }

        GL11.glPushMatrix();
        {
            Minecraft.getMinecraft().getFramebuffer().unbindFramebuffer();
            GL11.glPushMatrix();
            {
                GL11.glLoadIdentity();
                fb.bindFramebuffer(true);
                GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT
                        | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glLoadIdentity();
                GL11.glClearColor(0, 0, 0, 0);
                net.minecraft.client.renderer.RenderHelper
                        .enableStandardItemLighting();
                GL11.glPushMatrix();
                {
                    Vec3 playerPos = player.getPosition(event.partialTicks);
                    double x = location.getX() - playerPos.getPos().getX()
                            + faceHit.offsetX;
                    double y = location.getY() - playerPos.getPos().getY()
                            + faceHit.offsetY;
                    double z = location.getZ() - playerPos.getPos().getZ()
                            + faceHit.offsetZ;
                    GL11.glRotated(player.rotationPitch, 1, 0, 0);
                    GL11.glRotated(player.rotationYaw - 180, 0, 1, 0);
                    GL11.glTranslated(x, y, z);
                    part.renderDynamic(new Vecs3d(0, 0, 0), event.partialTicks);
                }
                GL11.glPopMatrix();
                net.minecraft.client.renderer.RenderHelper
                        .disableStandardItemLighting();
                fb.unbindFramebuffer();
            }
            GL11.glPopMatrix();
            Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);
            GL11.glPushMatrix();
            {
                Minecraft mc = Minecraft.getMinecraft();
                ScaledResolution scaledresolution = new ScaledResolution(mc,
                        mc.displayWidth, mc.displayHeight);
                GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
                GL11.glMatrixMode(GL11.GL_PROJECTION);
                GL11.glLoadIdentity();
                GL11.glOrtho(0, scaledresolution.getScaledWidth_double(),
                        scaledresolution.getScaledHeight_double(), 0, 0.1,
                        10000D);
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glLoadIdentity();
                GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
                fb.bindFramebufferTexture();
                {
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA,
                            GL11.GL_ONE_MINUS_SRC_ALPHA);
                    Tessellator tessellator = Tessellator.instance;
                    int w = scaledresolution.getScaledWidth();
                    int h = scaledresolution.getScaledHeight();

                    tessellator.startDrawingQuads();
                    tessellator.setColorRGBA_F(1, 1, 1, 0.5F);
                    tessellator.addVertexWithUV(w, h, 0.0D, 1.0D, 0.0D);
                    tessellator.addVertexWithUV(w, 0, 0.0D, 1.0D, 1.0D);
                    tessellator.addVertexWithUV(0, 0, 0.0D, 0.0D, 1.0D);
                    tessellator.addVertexWithUV(0, h, 0.0D, 0.0D, 0.0D);
                    tessellator.draw();
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glEnable(GL11.GL_LIGHTING);
                }
                fb.unbindFramebufferTexture();
                GL11.glDisable(GL11.GL_BLEND);
            }
            GL11.glPopMatrix();
            fb.framebufferClear();
            Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);
        }
        GL11.glPopMatrix();
    }
}
