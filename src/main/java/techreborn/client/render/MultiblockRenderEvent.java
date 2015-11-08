/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package techreborn.client.render;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import org.lwjgl.opengl.GL11;
import reborncore.client.multiblock.IMultiblockRenderHook;
import reborncore.client.multiblock.Multiblock;
import reborncore.client.multiblock.MultiblockSet;
import reborncore.client.multiblock.component.MultiblockComponent;

public class MultiblockRenderEvent {
    public static boolean rendering = false;

    private static RenderBlocks blockRender = RenderBlocks.getInstance();
    public MultiblockSet currentMultiblock;
    public static ChunkCoordinates anchor;
    public static int angle;

    public void setMultiblock(MultiblockSet set) {
        currentMultiblock = set;
        anchor = null;
        angle = 0;
    }

    @SubscribeEvent
    public void onWorldRenderLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer != null && mc.objectMouseOver != null && !mc.thePlayer.isSneaking()) {
            mc.thePlayer.getCurrentEquippedItem();
            renderPlayerLook(mc.thePlayer, mc.objectMouseOver);
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (currentMultiblock != null && anchor == null && event.action == Action.RIGHT_CLICK_BLOCK && event.entityPlayer == Minecraft.getMinecraft().thePlayer) {
            anchor = new ChunkCoordinates(event.x, event.y, event.z);
            angle = MathHelper.floor_double(event.entityPlayer.rotationYaw * 4.0 / 360.0 + 0.5) & 3;
            event.setCanceled(true);
        }
    }

    private void renderPlayerLook(EntityPlayer player, MovingObjectPosition src) {
        if (currentMultiblock != null) {
            int anchorX = anchor != null ? anchor.posX : src.blockX;
            int anchorY = anchor != null ? anchor.posY + 1 : src.blockY + 1;
            int anchorZ = anchor != null ? anchor.posZ : src.blockZ;

            rendering = true;
            Multiblock mb = currentMultiblock.getForEntity(player);
            boolean didAny = false;
            for (MultiblockComponent comp : mb.getComponents())
                if (renderComponent(player.worldObj, mb, comp, anchorX, anchorY, anchorZ))
                    didAny = true;
            rendering = false;

            if (!didAny) {
                setMultiblock(null);
                player.addChatComponentMessage(new ChatComponentText("Structure Complete!").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
            }
        }
    }

    private boolean renderComponent(World world, Multiblock mb, MultiblockComponent comp, int anchorX, int anchorY, int anchorZ) {
        ChunkCoordinates pos = comp.getRelativePosition();
        int x = pos.posX + anchorX;
        int y = pos.posY + anchorY;
        int z = pos.posZ + anchorZ;

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glColor4f(1F, 1F, 1F, 0.4F);
        GL11.glTranslated(x + 0.5 - RenderManager.renderPosX, y + 0.5 - RenderManager.renderPosY, z + 0.5 - RenderManager.renderPosZ);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

        blockRender.useInventoryTint = false;
        Block block = comp.getBlock();
        if (IMultiblockRenderHook.renderHooks.containsKey(block))
            IMultiblockRenderHook.renderHooks.get(block).renderBlockForMultiblock(world, mb, block, comp.getMeta(), blockRender);
        else blockRender.renderBlockAsItem(comp.getBlock(), comp.getMeta(), 1F);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPopMatrix();
        return true;
    }
}
