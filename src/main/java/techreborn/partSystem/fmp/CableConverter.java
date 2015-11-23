package techreborn.partSystem.fmp;

import codechicken.lib.packet.PacketCustom;
import codechicken.lib.raytracer.RayTracer;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ic2.api.item.IC2Items;
import ic2.core.block.wiring.BlockCable;
import ic2.core.block.wiring.TileEntityCable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import reborncore.common.packets.AddDiscriminatorEvent;
import reborncore.common.packets.PacketHandler;
import techreborn.partSystem.parts.CablePart;

import java.util.Arrays;

public class CableConverter implements MultiPartRegistry.IPartConverter {
    @Override
    public Iterable<Block> blockTypes() {
        return Arrays.asList(Block.getBlockFromItem(IC2Items.getItem("copperCableBlock").getItem()));
    }

    @Override
    public TMultiPart convert(World world, BlockCoord blockCoord) {
        Block block = world.getBlock(blockCoord.x, blockCoord.y, blockCoord.z);
        if (block instanceof BlockCable) {
            TileEntity tileEntity = world.getTileEntity(blockCoord.x, blockCoord.y, blockCoord.z);
            if (tileEntity instanceof TileEntityCable) {
                TileEntityCable cable = (TileEntityCable) tileEntity;
                int type = cable.cableType;
                CablePart newPart = new CablePart();
                newPart.setType(type);
                return new FMPModPart(newPart);
            }
        }
        return null;
    }

    private final ThreadLocal<Object> placing = new ThreadLocal<Object>();

    @SubscribeEvent(priority = EventPriority.LOW)
    public void playerInteract(PlayerInteractEvent event) {
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && event.entityPlayer.worldObj.isRemote) {
            if (placing.get() != null) return;//for mods that do dumb stuff and call this event like MFR
            placing.set(event);
            if (place(event.entityPlayer, event.entityPlayer.worldObj)) event.setCanceled(true);
            placing.set(null);
        }
    }

    public static boolean place(EntityPlayer player, World world) {
        MovingObjectPosition hit = RayTracer.reTrace(world, player);
        if (hit == null) return false;

        BlockCoord pos = new BlockCoord(hit.blockX, hit.blockY, hit.blockZ);
        ItemStack held = player.getHeldItem();

        FMPModPart part = null;
        if (held == null) return false;

        Item heldItem = held.getItem();
        if (heldItem == IC2Items.getItem("copperCableItem").getItem()) {
            CablePart cablePart = new CablePart();
            cablePart.setType(held.getItemDamage());
            part = new FMPModPart(cablePart);
        }

        if (part == null) return false;

        if (world.isRemote && !player.isSneaking())//attempt to use block activated like normal and tell the server the right stuff
        {
            Vector3 f = new Vector3(hit.hitVec).add(-hit.blockX, -hit.blockY, -hit.blockZ);
            Block block = world.getBlock(hit.blockX, hit.blockY, hit.blockZ);
            if (!ignoreActivate(block) && block.onBlockActivated(world, hit.blockX, hit.blockY, hit.blockZ, player, hit.sideHit, (float) f.x, (float) f.y, (float) f.z)) {
                player.swingItem();
                PacketCustom.sendToServer(new C08PacketPlayerBlockPlacement(hit.blockX, hit.blockY, hit.blockZ, hit.sideHit, player.inventory.getCurrentItem(), (float) f.x, (float) f.y, (float) f.z));
                return true;
            }
        }

        TileMultipart tile = TileMultipart.getOrConvertTile(world, pos);
        if (tile == null || !tile.canAddPart(part)) {
            pos = pos.offset(hit.sideHit);
            tile = TileMultipart.getOrConvertTile(world, pos);
            if (tile == null || !tile.canAddPart(part)) return false;
        }

        if (!world.isRemote) {
            TileMultipart.addPart(world, pos, part);
            world.playSoundEffect(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, Blocks.wool.stepSound.func_150496_b(), (Blocks.wool.stepSound.getVolume() + 1.0F) / 2.0F, Blocks.wool.stepSound.getPitch() * 0.8F);
            if (!player.capabilities.isCreativeMode) {
                held.stackSize--;
                if (held.stackSize == 0) {
                    player.inventory.mainInventory[player.inventory.currentItem] = null;
                    MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(player, held));
                }
            }
        } else {
            player.swingItem();
            PacketHandler.sendPacketToServer(new PacketFMPPlacePart());
        }
        return true;
    }

    /**
     * Because vanilla is weird.
     */
    private static boolean ignoreActivate(Block block) {
        if (block instanceof BlockFence) return true;
        return false;
    }

    @SubscribeEvent
    public void addDiscriminator(AddDiscriminatorEvent event) {
        event.getPacketHandler().addDiscriminator(event.getPacketHandler().nextDiscriminator, PacketFMPPlacePart.class);
    }

}