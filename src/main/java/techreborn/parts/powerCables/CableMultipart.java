package techreborn.parts.powerCables;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import ic2.api.energy.tile.*;
import ic2.api.tile.IEnergyStorage;
import net.darkhax.tesla.api.ITeslaProducer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fml.common.Loader;
import reborncore.api.power.IEnergyInterfaceTile;
import reborncore.api.power.tile.IEnergyProducerTile;
import reborncore.api.power.tile.IEnergyReceiverTile;
import reborncore.common.RebornCoreConfig;
import reborncore.common.misc.Functions;
import reborncore.common.misc.vecmath.Vecs3dCube;
import reborncore.common.util.WorldUtils;
import reborncore.mcmultipart.MCMultiPartMod;
import reborncore.mcmultipart.microblock.IMicroblock;
import reborncore.mcmultipart.multipart.*;
import reborncore.mcmultipart.raytrace.PartMOP;
import techreborn.parts.TechRebornParts;
import techreborn.parts.walia.IPartWaliaProvider;
import techreborn.power.EnergyUtils;
import techreborn.utils.damageSources.ElectrialShockSource;

import java.util.*;

/**
 * Created by modmuss50 on 02/03/2016.
 */
public class CableMultipart extends Multipart
        implements INormallyOccludingPart, ISlottedPart, ITickable, ICableType, IPartWaliaProvider {

    public static final IUnlistedProperty<Boolean> UP = Properties.toUnlisted(PropertyBool.create("up"));
    public static final IUnlistedProperty<Boolean> DOWN = Properties.toUnlisted(PropertyBool.create("down"));
    public static final IUnlistedProperty<Boolean> NORTH = Properties.toUnlisted(PropertyBool.create("north"));
    public static final IUnlistedProperty<Boolean> EAST = Properties.toUnlisted(PropertyBool.create("east"));
    public static final IUnlistedProperty<Boolean> SOUTH = Properties.toUnlisted(PropertyBool.create("south"));
    public static final IUnlistedProperty<Boolean> WEST = Properties.toUnlisted(PropertyBool.create("west"));
    public static final IProperty<EnumCableType> TYPE = PropertyEnum.create("type", EnumCableType.class);
    public Vecs3dCube[] boundingBoxes = new Vecs3dCube[14];
    public float center = 0.6F;
    public float offset = 0.10F;
    public Map<EnumFacing, BlockPos> connectedSides;
    public double lastEnergyPacket;
    public EnumCableType cableType = EnumCableType.ICOPPER;

    public CableMultipart(EnumCableType cableType) {
        this.cableType = cableType;
        connectedSides = new HashMap<>();
        refreshBounding();
    }

    public CableMultipart() {
        connectedSides = new HashMap<>();
        refreshBounding();
    }

    public static CableMultipart getPartFromWorld(World world, BlockPos pos, EnumFacing side) {
        if (world == null || pos == null) {
            return null;
        }
        IMultipartContainer container = MultipartHelper.getPartContainer(world, pos);
        if (side != null && container != null) {
            ISlottedPart slottedPart = container.getPartInSlot(PartSlot.getFaceSlot(side));
            if (slottedPart instanceof IMicroblock.IFaceMicroblock
                    && !((IMicroblock.IFaceMicroblock) slottedPart).isFaceHollow()) {
                return null;
            }
        }

        if (container == null) {
            return null;
        }
        ISlottedPart part = container.getPartInSlot(PartSlot.CENTER);
        if (part instanceof CableMultipart) {
            return (CableMultipart) part;
        }
        return null;
    }

    public void refreshBounding() {
        float centerFirst = center - offset;
        double w = (getCableType().cableThickness / 16) - 0.5;
        boundingBoxes[6] = new Vecs3dCube(centerFirst - w, centerFirst - w, centerFirst - w, centerFirst + w,
                centerFirst + w, centerFirst + w);

        int i = 0;
        for (EnumFacing dir : EnumFacing.VALUES) {
            double xMin1 = (dir.getFrontOffsetX() < 0 ? 0.0
                    : (dir.getFrontOffsetX() == 0 ? centerFirst - w : centerFirst + w));
            double xMax1 = (dir.getFrontOffsetX() > 0 ? 1.0
                    : (dir.getFrontOffsetX() == 0 ? centerFirst + w : centerFirst - w));

            double yMin1 = (dir.getFrontOffsetY() < 0 ? 0.0
                    : (dir.getFrontOffsetY() == 0 ? centerFirst - w : centerFirst + w));
            double yMax1 = (dir.getFrontOffsetY() > 0 ? 1.0
                    : (dir.getFrontOffsetY() == 0 ? centerFirst + w : centerFirst - w));

            double zMin1 = (dir.getFrontOffsetZ() < 0 ? 0.0
                    : (dir.getFrontOffsetZ() == 0 ? centerFirst - w : centerFirst + w));
            double zMax1 = (dir.getFrontOffsetZ() > 0 ? 1.0
                    : (dir.getFrontOffsetZ() == 0 ? centerFirst + w : centerFirst - w));

            boundingBoxes[i] = new Vecs3dCube(xMin1, yMin1, zMin1, xMax1, yMax1, zMax1);
            i++;
        }
    }

    @Override
    public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {

        for (EnumFacing dir : EnumFacing.VALUES) {
            if (connectedSides.containsKey(dir)
                    && mask.intersectsWith(boundingBoxes[Functions.getIntDirFromDirection(dir)].toAABB()))
                list.add(boundingBoxes[Functions.getIntDirFromDirection(dir)].toAABB());
        }
        if (mask.intersectsWith(boundingBoxes[6].toAABB())) {
            list.add(boundingBoxes[6].toAABB());
        }
        super.addCollisionBoxes(mask, list, collidingEntity);
    }

    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> list) {

        for (EnumFacing dir : EnumFacing.VALUES) {
            if (connectedSides.containsKey(dir))
                list.add(boundingBoxes[Functions.getIntDirFromDirection(dir)].toAABB());
        }
        list.add(boundingBoxes[6].toAABB());
        super.addSelectionBoxes(list);
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        for (EnumFacing dir : EnumFacing.VALUES) {
            CableMultipart multipart = getPartFromWorld(getWorld(), getPos().offset(dir), dir);
            if (multipart != null) {
                multipart.nearByChange();
            }
        }
    }

    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> list) {
        for (EnumFacing dir : EnumFacing.VALUES) {
            if (connectedSides.containsKey(dir))
                list.add(boundingBoxes[Functions.getIntDirFromDirection(dir)].toAABB());
        }
        list.add(boundingBoxes[6].toAABB());
    }

    @Override
    public void onNeighborBlockChange(Block block) {
        super.onNeighborBlockChange(block);
        nearByChange();
    }

    public void nearByChange() {
        checkConnectedSides();
        for (EnumFacing direction : EnumFacing.VALUES) {
            BlockPos blockPos = getPos().offset(direction);
            WorldUtils.updateBlock(getWorld(), blockPos);
            CableMultipart part = getPartFromWorld(getWorld(), blockPos, direction);
            if (part != null) {
                part.checkConnectedSides();
            }
        }
    }

    @Override
    public void onAdded() {
        nearByChange();
    }


    public boolean shouldConnectTo(EnumFacing dir) {
        if (dir != null) {
            if (internalShouldConnectTo(dir)) {
                CableMultipart cableMultipart = getPartFromWorld(getWorld(), getPos().offset(dir), dir);
                if (cableMultipart != null && cableMultipart.internalShouldConnectTo(dir.getOpposite())) {
                    return true;
                }
            } else {
                TileEntity tile = getNeighbourTile(dir);
                if(tile != null) {
                    EnumFacing facing = dir.getOpposite();
                    if (tile instanceof IEnergyReceiverTile &&
                            ((IEnergyReceiverTile) tile).canAcceptEnergy(facing)) {
                        return true;
                    } else if(tile instanceof IEnergyProducerTile &&
                            ((IEnergyProducerTile) tile).canProvideEnergy(facing)) {
                        return true;
                    } else if (tile instanceof IEnergyReceiver &&
                            ((IEnergyReceiver) tile).canConnectEnergy(facing) &&
                            RebornCoreConfig.getRebornPower().rf()) {
                        return true;
                    } else if (Loader.isModLoaded("IC2") &&
                            RebornCoreConfig.getRebornPower().eu() &&
                            (tile instanceof IEnergySource ||
                                    tile instanceof IEnergyEmitter ||
                                    tile instanceof IEnergyAcceptor)) {
                        return true;
                    } else if (Loader.isModLoaded("Tesla") &&
                            tile.hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, facing)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean internalShouldConnectTo(EnumFacing dir) {
        ISlottedPart part = getContainer().getPartInSlot(PartSlot.getFaceSlot(dir));
        if (part instanceof IMicroblock.IFaceMicroblock) {
            if (!((IMicroblock.IFaceMicroblock) part).isFaceHollow()) {
                return false;
            }
        }

        if (!OcclusionHelper.occlusionTest(getContainer().getParts(), p -> p == this, boundingBoxes[Functions.getIntDirFromDirection(dir)].toAABB())) {
            return false;
        }

        CableMultipart cableMultipart = getPartFromWorld(getWorld(), getPos().offset(dir), dir.getOpposite());

        return cableMultipart != null && cableMultipart.getCableType() == getCableType();
    }

    public TileEntity getNeighbourTile(EnumFacing side) {
        return side != null ? getWorld().getTileEntity(getPos().offset(side)) : null;
    }

    public void checkConnectedSides() {
        refreshBounding();
        connectedSides = new HashMap<>();
        for (EnumFacing dir : EnumFacing.values()) {
            int d = Functions.getIntDirFromDirection(dir);
            if (getWorld() == null) {
                return;
            }
            TileEntity te = getNeighbourTile(dir);
            if (shouldConnectTo(dir)) {
                connectedSides.put(dir, te.getPos());
            }
        }
    }

    @Override
    public EnumSet<PartSlot> getSlotMask() {
        return EnumSet.of(PartSlot.CENTER);
    }

    @Override
    public void update() {
        if (getWorld() != null) {
            if(lastEnergyPacket > 0)
                --lastEnergyPacket;

            if (getWorld().getTotalWorldTime() % 80 == 0) {
                checkConnectedSides();
            }

            for(EnumFacing connection : connectedSides.keySet()) {
                BlockPos blockPos = connectedSides.get(connection);
                TileEntity tileEntity = getWorld().getTileEntity(blockPos);
                if(tileEntity == null) continue;
                EnumFacing opposite = connection.getOpposite();

                //TODO rewrite for new version
                if(tileEntity instanceof IEnergyInterfaceTile) {
                    IEnergyInterfaceTile interfaceTile = (IEnergyInterfaceTile) tileEntity;
                    if(interfaceTile.canProvideEnergy(opposite)) {
                        double extractedEU = interfaceTile.useEnergy(getCableType().transferRate, true);
                        double dispatched = EnergyUtils.dispatchWiresEnergyPacket(getWorld(), getPos(), extractedEU, blockPos);
                        interfaceTile.useEnergy(dispatched);
                        continue;
                    }
                }

                if(Loader.isModLoaded("IC2") && RebornCoreConfig.getRebornPower().eu()) {
                    if (tileEntity instanceof IEnergySource) {
                        IEnergySource source = (IEnergySource) tileEntity;
                        if(source.emitsEnergyTo((emitter, from) -> true, opposite)) {
                            double extractedEU = source.getOfferedEnergy();
                            if (extractedEU > getCableType().transferRate)
                                extractedEU = getCableType().transferRate;
                            double dispatched = EnergyUtils.dispatchWiresEnergyPacket(getWorld(), getPos(), extractedEU, blockPos);
                            source.drawEnergy(dispatched);
                            continue;
                        }
                    } else if(tileEntity instanceof IEnergyStorage) {
                        IEnergyStorage storage = (IEnergyStorage) tileEntity;
                        double extractedEU = storage.getStored();
                        if(extractedEU > getCableType().transferRate)
                            extractedEU = getCableType().transferRate;
                        double dispatched = EnergyUtils.dispatchWiresEnergyPacket(getWorld(), getPos(), extractedEU, blockPos);
                        storage.setStored((int) (extractedEU - dispatched));
                        continue;
                    }
                }

                if(RebornCoreConfig.getRebornPower().rf()) {
                    if (tileEntity instanceof IEnergyProvider) {
                        IEnergyProvider provider = (IEnergyProvider) tileEntity;
                        if (provider.canConnectEnergy(opposite)) {
                            int extractedRF = provider.extractEnergy(opposite,
                                    getCableType().transferRate * RebornCoreConfig.euPerRF, true);
                            double extractedEU = extractedRF / (RebornCoreConfig.euPerRF * 1f);
                            double dispatched = EnergyUtils.dispatchWiresEnergyPacket(getWorld(), getPos(), extractedEU, blockPos);
                            provider.extractEnergy(opposite, (int) (dispatched * RebornCoreConfig.euPerRF), false);
                            continue;
                        }
                    } else if(tileEntity instanceof cofh.api.energy.IEnergyStorage) {
                        cofh.api.energy.IEnergyStorage energyStorage = (cofh.api.energy.IEnergyStorage) tileEntity;
                        int extractedRF = energyStorage.extractEnergy(
                                getCableType().transferRate * RebornCoreConfig.euPerRF, true);
                        double extractedEU = extractedRF / (RebornCoreConfig.euPerRF * 1f);
                        if (extractedEU > getCableType().transferRate)
                            extractedEU = getCableType().transferRate;
                        double dispatched = EnergyUtils.dispatchWiresEnergyPacket(getWorld(), getPos(), extractedEU, blockPos);
                        energyStorage.extractEnergy((int) (dispatched * RebornCoreConfig.euPerRF), false);
                        continue;
                    }
                }

                if(Loader.isModLoaded("Tesla") && RebornCoreConfig.getRebornPower().tesla()) {
                    if(tileEntity.hasCapability(TeslaCapabilities.CAPABILITY_PRODUCER, opposite)) {
                        ITeslaProducer producer = tileEntity.getCapability(TeslaCapabilities.CAPABILITY_PRODUCER, opposite);
                        double extractedRF = producer.takePower(getCableType().transferRate * RebornCoreConfig.euPerRF, true);
                        double extractedEU = extractedRF / (RebornCoreConfig.euPerRF * 1F);
                        double dispatched = EnergyUtils.dispatchWiresEnergyPacket(getWorld(), getPos(), extractedEU, blockPos);
                        producer.takePower((long) (dispatched * RebornCoreConfig.euPerRF), false);
                        continue;
                    }
                }

            }
        }

    }

    @Override
    public IBlockState getExtendedState(IBlockState state) {
        IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;
        return extendedBlockState.withProperty(DOWN, shouldConnectTo(EnumFacing.DOWN))
                .withProperty(UP, shouldConnectTo(EnumFacing.UP)).withProperty(NORTH, shouldConnectTo(EnumFacing.NORTH))
                .withProperty(SOUTH, shouldConnectTo(EnumFacing.SOUTH))
                .withProperty(WEST, shouldConnectTo(EnumFacing.WEST))
                .withProperty(EAST, shouldConnectTo(EnumFacing.EAST)).withProperty(TYPE, getCableType());
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new ExtendedBlockState(MCMultiPartMod.multipart, new IProperty[]{TYPE},
                new IUnlistedProperty[]{DOWN, UP, NORTH, SOUTH, WEST, EAST});
    }

    @Override
    public float getHardness(PartMOP hit) {
        return 0.5F;
    }

    public Material getMaterial() {
        return getCableType().material;
    }

    @Override
    public List<ItemStack> getDrops() {
        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(TechRebornParts.cables, 1, getCableType().ordinal()));
        return list;
    }

    @Override
    public void onEntityCollided(Entity entity) {
        if (getCableType().canKill && entity instanceof EntityLivingBase) {
            entity.attackEntityFrom(new ElectrialShockSource(), (float) (lastEnergyPacket / 16F));
        }

    }

    @Override
    public ItemStack getPickBlock(EntityPlayer player, PartMOP hit) {
        return new ItemStack(TechRebornParts.cables, 1, getCableType().ordinal());
    }

    @Override
    public void addInfo(List<String> info) {
        info.add(TextFormatting.GREEN + "EU Transfer: " +
                TextFormatting.LIGHT_PURPLE + getCableType().transferRate);
        if (getCableType().canKill) {
            info.add(TextFormatting.RED + "Damages entity's!");
        }
    }

    @Override
    public ResourceLocation getModelPath() {
        return new ResourceLocation("techreborn:cable");
    }

    @Override
    public boolean canRenderInLayer(BlockRenderLayer layer) {
        return layer == BlockRenderLayer.CUTOUT;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setString("CableType", cableType.name());
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        if(tag.hasKey("CableType")) {
            cableType = EnumCableType.valueOf(tag.getString("CableType"));
        }
    }

    @Override
    public void writeUpdatePacket(PacketBuffer buf) {
        super.writeUpdatePacket(buf);
        buf.writeInt(cableType.ordinal());
    }

    @Override
    public void readUpdatePacket(PacketBuffer buf) {
        super.readUpdatePacket(buf);
        cableType = EnumCableType.values()[buf.readInt()];
    }

    @Override
    public EnumCableType getCableType() {
        return cableType;
    }

}
