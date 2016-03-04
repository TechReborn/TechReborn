package techreborn.parts;

import mcmultipart.MCMultiPartMod;
import mcmultipart.microblock.IMicroblock;
import mcmultipart.multipart.*;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import reborncore.api.power.IEnergyInterfaceTile;
import reborncore.common.misc.Functions;
import reborncore.common.misc.vecmath.Vecs3dCube;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mark on 02/03/2016.
 */
public class CableMultipart extends Multipart implements IOccludingPart, ISlottedPart, ITickable {

    public Vecs3dCube[] boundingBoxes = new Vecs3dCube[14];
    public float center = 0.6F;
    public float offset = 0.10F;
    public Map<EnumFacing, BlockPos> connectedSides;
    public int ticks = 0;
    public ItemStack stack;
    public int type = 0;

    public static final IUnlistedProperty UP = Properties.toUnlisted(PropertyBool.create("up"));
    public static final IUnlistedProperty DOWN = Properties.toUnlisted(PropertyBool.create("down"));
    public static final IUnlistedProperty NORTH = Properties.toUnlisted(PropertyBool.create("north"));
    public static final IUnlistedProperty EAST = Properties.toUnlisted(PropertyBool.create("east"));
    public static final IUnlistedProperty SOUTH = Properties.toUnlisted(PropertyBool.create("south"));
    public static final IUnlistedProperty WEST = Properties.toUnlisted(PropertyBool.create("west"));

    public CableMultipart() {
        connectedSides = new HashMap<>();
        refreshBounding();
    }

    public static int getMaxCapacity(int type) {
        switch (type) {
            case 0:
                return 128;
            case 1:
                return 128;
            case 2:
                return 512;
            case 3:
                return 512;
            case 4:
                return 512;
            case 5:
                return 2048;
            case 6:
                return 2048;
            case 7:
                return 2048;
            case 8:
                return 2048;
            case 9:
                return 8192;
            case 10:
                return 32;
            case 11:
                return 8192;
            case 12:
                return 8192;
            case 13:
                return 32;
            case 14:
                return 32;
            default:
                return 0;
        }
    }

    public static float getCableThickness(int cableType) {
        float p = 1.0F;
        switch (cableType) {
            case 0:
                p = 6.0F;
                break;
            case 1:
                p = 4.0F;
                break;
            case 2:
                p = 3.0F;
                break;
            case 3:
                p = 6.0F;
                break;
            case 4:
                p = 6.0F;
                break;
            case 5:
                p = 6.0F;
                break;
            case 6:
                p = 10.0F;
                break;
            case 7:
                p = 10.0F;
                break;
            case 8:
                p = 12.0F;
                break;
            case 9:
                p = 4.0F;
                break;
            case 10:
                p = 4.0F;
                break;
            case 11:
                p = 8.0F;
                break;
            case 12:
                p = 8.0F;
                break;
            case 13:
                p = 16.0F;
                break;
            case 14:
                p = 6.0F;
        }

        return p / 16.0F;
    }

    public static String getNameFromType(int cableType) {
        String p = null;
        switch (cableType) {
            case 0:
                p = "insulatedCopperCable";
                break;
            case 1:
                p = "copperCable";
                break;
            case 2:
                p = "goldCable";
                break;
            case 3:
                p = "insulatedGoldCable";
                break;
            case 4:
                p = "doubleInsulatedGoldCable";
                break;
            case 5:
                p = "ironCable";
                break;
            case 6:
                p = "insulatedIronCable";
                break;
            case 7:
                p = "doubleInsulatedIronCable";
                break;
            case 8:
                p = "trippleInsulatedIronCable";
                break;
            case 9:
                p = "glassFiberCable";
                break;
            case 10:
                p = "tinCable";
                break;
            case 11:
                p = "detectorCableBlock";//Detector
                break;
            case 12:
                p = "splitterCableBlock";// Splitter
                break;
            case 13:
                p = "insulatedtinCable";
                break;
            case 14:
                p = "unused"; // unused?
        }

        return p;
    }

    public static String getTextureNameFromType(int cableType) {
        String p = null;
        switch (cableType) {
            case 0:
                p = "insulatedCopperCableItem";
                break;
            case 1:
                p = "copperCableItem";
                break;
            case 2:
                p = "goldCableItem";
                break;
            case 3:
                p = "insulatedGoldCableItem";
                break;
            case 4:
                p = "doubleInsulatedGoldCableItem";
                break;
            case 5:
                p = "ironCableItem";
                break;
            case 6:
                p = "insulatedIronCableItem";
                break;
            case 7:
                p = "doubleInsulatedIronCableItem";
                break;
            case 8:
                p = "trippleInsulatedIronCableItem";
                break;
            case 9:
                p = "glassFiberCableItem";
                break;
            case 10:
                p = "tinCableItem";
                break;
            case 11:
                p = "detectorCableItem";//Detector
                break;
            case 12:
                p = "splitterCableItem";// Splitter
                break;
            case 13:
                p = "insulatedTinCableItem";
                break;
            case 14:
                p = "unused"; // unused?
        }

        return p;
    }

    public void setType(int newType) {
        this.type = newType;
        refreshBounding();
    }

    public void refreshBounding() {
        float centerFirst = center - offset;
        double w = getCableThickness(type) / 2;
        boundingBoxes[6] = new Vecs3dCube(centerFirst - w - 0.03, centerFirst
                - w - 0.08, centerFirst - w - 0.03, centerFirst + w + 0.08,
                centerFirst + w + 0.04, centerFirst + w + 0.08);

        boundingBoxes[6] = new Vecs3dCube(centerFirst - w, centerFirst - w,
                centerFirst - w, centerFirst + w, centerFirst + w, centerFirst
                + w);

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

            boundingBoxes[i] = new Vecs3dCube(xMin1, yMin1, zMin1, xMax1,
                    yMax1, zMax1);
            i++;
        }
    }

    @Override
    public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        super.addCollisionBoxes(mask, list, collidingEntity);
        for (EnumFacing dir : EnumFacing.VALUES) {
            if (connectedSides.containsKey(dir) && mask.intersectsWith(boundingBoxes[Functions.getIntDirFromDirection(dir)].toAABB()))
                list.add(boundingBoxes[Functions.getIntDirFromDirection(dir)].toAABB());
        }
        if (mask.intersectsWith(boundingBoxes[6].toAABB())) {
            list.add(boundingBoxes[6].toAABB());
        }
    }

    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> list) {
        super.addSelectionBoxes(list);
        for (EnumFacing dir : EnumFacing.VALUES) {
            if (connectedSides.containsKey(dir))
                list.add(boundingBoxes[Functions.getIntDirFromDirection(dir)].toAABB());
        }
        list.add(boundingBoxes[6].toAABB());
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
            getWorld().markBlockForUpdate(blockPos);
            CableMultipart part = getPartFromWorld(getWorld(), blockPos, direction);
            if (part != null) {
                part.checkConnectedSides();
            }
        }
    }

    public CableMultipart getPartFromWorld(World world, BlockPos pos, EnumFacing side) {
        IMultipartContainer container = MultipartHelper.getPartContainer(world, pos);
        if (side != null && container != null) {
            ISlottedPart slottedPart = container.getPartInSlot(PartSlot.getFaceSlot(side));
            if (slottedPart instanceof IMicroblock.IFaceMicroblock && !((IMicroblock.IFaceMicroblock) slottedPart).isFaceHollow()) {
                return null;
            }
        }

        if(container == null){
            return null;
        }
        ISlottedPart part = container.getPartInSlot(PartSlot.CENTER);
        if (part instanceof CableMultipart) {
            return (CableMultipart) part;
        }
        return null;
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

        if (!OcclusionHelper.occlusionTest(getContainer().getParts(), this, boundingBoxes[Functions.getIntDirFromDirection(dir)].toAABB())) {
            return false;
        }

        if (getPartFromWorld(getWorld(), getPos().offset(dir), dir.getOpposite()) != null) {
            return true;
        }

        TileEntity tile = getNeighbourTile(dir);

        if (tile instanceof IEnergyInterfaceTile) {
            return true;
        }
        return false;
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

    public double getConductionLoss() {
        switch (this.type) {
            case 0:
                return 0.2D;
            case 1:
                return 0.3D;
            case 2:
                return 0.5D;
            case 3:
                return 0.45D;
            case 4:
                return 0.4D;
            case 5:
                return 1.0D;
            case 6:
                return 0.95D;
            case 7:
                return 0.9D;
            case 8:
                return 0.8D;
            case 9:
                return 0.025D;
            case 10:
                return 0.025D;
            case 11:
                return 0.5D;
            case 12:
                return 0.5D;
            case 13:
            default:
                return 0.025D;
            case 14:
                return 0.2D;
        }
    }

    @Override
    public EnumSet<PartSlot> getSlotMask() {
        return EnumSet.of(PartSlot.CENTER);
    }

    @Override
    public void update() {

    }

    @Override
    public IBlockState getExtendedState(IBlockState state) {
        IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;
        return extendedBlockState
                .withProperty(DOWN, shouldConnectTo(EnumFacing.DOWN))
                .withProperty(UP, shouldConnectTo(EnumFacing.UP))
                .withProperty(NORTH, shouldConnectTo(EnumFacing.NORTH))
                .withProperty(SOUTH, shouldConnectTo(EnumFacing.SOUTH))
                .withProperty(WEST, shouldConnectTo(EnumFacing.WEST))
                .withProperty(EAST, shouldConnectTo(EnumFacing.EAST));
    }

    @Override
    public BlockState createBlockState() {
        return new ExtendedBlockState(MCMultiPartMod.multipart,
                new IProperty[0],
                new IUnlistedProperty[]{
                        DOWN,
                UP,
                NORTH,
                SOUTH,
                WEST,
                EAST});
    }


    @Override
    public String getModelPath() {
        return "techreborn:cable";
    }
}
