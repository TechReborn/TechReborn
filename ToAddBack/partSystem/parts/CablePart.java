package techreborn.partSystem.parts;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.info.IC2Classic;
import ic2.api.item.IC2Items;
import ic2.api.network.INetworkTileEntityEventListener;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.util.EnumFacing;
import reborncore.common.misc.Functions;
import reborncore.common.misc.Location;
import reborncore.common.misc.vecmath.Vecs3d;
import reborncore.common.misc.vecmath.Vecs3dCube;
import techreborn.client.render.RenderCablePart;
import techreborn.partSystem.IModPart;
import techreborn.partSystem.IPartDesc;
import techreborn.partSystem.ModPart;
import techreborn.partSystem.ModPartUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CablePart extends ModPart implements IEnergyConductor, INetworkTileEntityEventListener, IPartDesc {
    public Vecs3dCube[] boundingBoxes = new Vecs3dCube[14];
    public float center = 0.6F;
    public float offset = 0.10F;
    public Map<EnumFacing, TileEntity> connectedSides;
    public int ticks = 0;
    public boolean addedToEnergyNet = false;
    public ItemStack stack;
    public int type = 0;
    protected EnumFacing[] dirs = EnumFacing.values();
    private boolean[] connections = new boolean[6];
    private boolean hasCheckedSinceStartup;

    public CablePart() {
        connectedSides = new HashMap<EnumFacing, TileEntity>();
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
        for (EnumFacing dir : EnumFacing.VALID_DIRECTIONS) {
            double xMin1 = (dir.offsetX < 0 ? 0.0
                    : (dir.offsetX == 0 ? centerFirst - w : centerFirst + w));
            double xMax1 = (dir.offsetX > 0 ? 1.0
                    : (dir.offsetX == 0 ? centerFirst + w : centerFirst - w));

            double yMin1 = (dir.offsetY < 0 ? 0.0
                    : (dir.offsetY == 0 ? centerFirst - w : centerFirst + w));
            double yMax1 = (dir.offsetY > 0 ? 1.0
                    : (dir.offsetY == 0 ? centerFirst + w : centerFirst - w));

            double zMin1 = (dir.offsetZ < 0 ? 0.0
                    : (dir.offsetZ == 0 ? centerFirst - w : centerFirst + w));
            double zMax1 = (dir.offsetZ > 0 ? 1.0
                    : (dir.offsetZ == 0 ? centerFirst + w : centerFirst - w));

            boundingBoxes[i] = new Vecs3dCube(xMin1, yMin1, zMin1, xMax1,
                    yMax1, zMax1);
            i++;
        }
    }

    @Override
    public void addCollisionBoxesToList(List<Vecs3dCube> boxes, Entity entity) {
        for (EnumFacing dir : EnumFacing.VALID_DIRECTIONS) {
            if (connectedSides.containsKey(dir))
                boxes.add(boundingBoxes[Functions.getIntDirFromDirection(dir)]);
        }
        boxes.add(boundingBoxes[6]);
    }

    @Override
    public List<Vecs3dCube> getSelectionBoxes() {
        List<Vecs3dCube> vec3dCubeList = new ArrayList<Vecs3dCube>();
        for (EnumFacing dir : EnumFacing.VALID_DIRECTIONS) {
            if (connectedSides.containsKey(dir))
                vec3dCubeList.add(boundingBoxes[Functions
                        .getIntDirFromDirection(dir)]);
        }
        vec3dCubeList.add(boundingBoxes[6]);
        return vec3dCubeList;
    }

    @Override
    public List<Vecs3dCube> getOcclusionBoxes() {
        List<Vecs3dCube> vecs3dCubesList = new ArrayList<Vecs3dCube>();
        vecs3dCubesList.add(boundingBoxes[6]);
        return vecs3dCubesList;
    }

    @Override
    public void renderDynamic(Vecs3d translation, double delta) {
    }

    @Override
    public boolean renderStatic(Vecs3d translation, int pass) {
        return RenderCablePart.renderStatic(translation, pass, this);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setInteger("type", type);
        writeConnectedSidesToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        type = tag.getInteger("type");
    }

    @Override
    public String getName() {
        return "Cable." + getNameFromType(type);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getItemTextureName() {
        if (IC2Classic.getLoadedIC2Type() == IC2Classic.IC2Type.SpeigersClassic) {
            return IC2Items.getItem("copperCableBlock").getItem().getIcon(new ItemStack(IC2Items.getItem("copperCableBlock").getItem(), type), 1).getIconName();
        }
        return IC2Items.getItem(getTextureNameFromType(type)).getIconIndex().getIconName();
    }

    @Override
    public void tick() {
        if (!FMLCommonHandler.instance().getEffectiveSide().isClient() && !this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
            nearByChange();
        }
        if (worldObj != null) {
            if (worldObj.getTotalWorldTime() % 80 == 0 || !hasCheckedSinceStartup) {
                checkConnectedSides();
                hasCheckedSinceStartup = true;
            }
        }

    }

    @Override
    public void nearByChange() {
        checkConnectedSides();
        for (EnumFacing direction : EnumFacing.VALID_DIRECTIONS) {
            worldObj.markBlockForUpdate(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
            IModPart part = ModPartUtils.getPartFromWorld(world, new Location(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ), this.getName());
            if (part != null) {
                CablePart cablePart = (CablePart) part;
                cablePart.checkConnectedSides();
            }
        }
    }

    @Override
    public void onAdded() {
        checkConnections(world, getX(), getY(), getZ());
        if (!FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
            nearByChange();
        }
        nearByChange();
    }

    @Override
    public void onRemoved() {
        if (!FMLCommonHandler.instance().getEffectiveSide().isClient() && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            this.addedToEnergyNet = false;
        }
    }

    @Override
    public IModPart copy() {
        CablePart part = new CablePart();
        part.setType(type);
        return part;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(IC2Items.getItem("copperCableItem").getItem(), 1, type);
    }

    public boolean shouldConnectTo(TileEntity entity, EnumFacing dir) {
        if (entity == null) {
            return false;
        } else if (entity instanceof IEnergyTile) {
            return true;
        } else {
            if (ModPartUtils.hasPart(entity.getWorldObj(), entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ(), this.getName())) {
                CablePart otherCable = (CablePart) ModPartUtils.getPartFromWorld(entity.getWorldObj(), new Location(entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ()), this.getName());
                int thisDir = Functions.getIntDirFromDirection(dir);
                int thereDir = Functions.getIntDirFromDirection(dir.getOpposite());
                boolean hasconnection = otherCable.connections[thereDir];

                otherCable.connections[thereDir] = false;

                if (ModPartUtils.checkOcclusion(entity.getWorldObj(), entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ(), boundingBoxes[thereDir])) {
                    otherCable.connections[thereDir] = true;
                    return true;
                }
                otherCable.connections[thereDir] = hasconnection;
            }
            return false;
        }
    }

    public void checkConnectedSides() {
        refreshBounding();
        connectedSides = new HashMap<EnumFacing, TileEntity>();
        for (EnumFacing dir : EnumFacing.VALID_DIRECTIONS) {
            int d = Functions.getIntDirFromDirection(dir);
            if (world == null) {
                return;
            }
            TileEntity te = world.getTileEntity(getX() + dir.offsetX, getY()
                    + dir.offsetY, getZ() + dir.offsetZ);
            if (shouldConnectTo(te, dir)) {
                if (ModPartUtils.checkOcclusion(getWorld(), getX(),
                        getY(), getZ(), boundingBoxes[d])) {
                    connectedSides.put(dir, te);
                }
            }
            if (te != null) {
                te.getWorldObj().markBlockForUpdate(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ());
            }
        }
        checkConnections(world, getX(), getY(), getZ());
        getWorld().markBlockForUpdate(getX(), getY(), getZ());
    }

    public void checkConnections(World world, int x, int y, int z) {
        for (int i = 0; i < 6; i++) {
            EnumFacing dir = dirs[i];
            int dx = x + dir.offsetX;
            int dy = y + dir.offsetY;
            int dz = z + dir.offsetZ;
            connections[i] = shouldConnectTo(world.getTileEntity(dx, dy, dz),
                    dir);
            world.func_147479_m(dx, dy, dz);
        }
        world.func_147479_m(x, y, z);
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

    public double getInsulationEnergyAbsorption() {
        return (double) getMaxCapacity(this.type);
    }

    public double getInsulationBreakdownEnergy() {
        return 9001.0D;
    }

    public double getConductorBreakdownEnergy() {
        return (double) (getMaxCapacity(this.type) + 1);
    }

    @Override
    public void removeInsulation() {

    }

    @Override
    public void removeConductor() {

    }

    @Override
    public boolean acceptsEnergyFrom(TileEntity tileEntity,
                                     EnumFacing forgeDirection) {
        return connectedSides.containsKey(forgeDirection);
    }

    @Override
    public boolean emitsEnergyTo(TileEntity tileEntity,
                                 EnumFacing forgeDirection) {
        return connectedSides.containsKey(forgeDirection);
    }

    @Override
    public void onNetworkEvent(int i) {
        switch (i) {
            case 0:
                this.worldObj.playSoundEffect((double) ((float) this.getPos().getX() + 0.5F), (double) ((float) this.getPos().getY() + 0.5F), (double) ((float) this.getPos().getZ() + 0.5F), "random.fizz", 0.5F, 2.6F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.8F);

                for (int l = 0; l < 8; ++l) {
                    this.worldObj.spawnParticle("largesmoke", (double) this.getPos().getX() + Math.random(), (double) this.getPos().getY() + 1.2D, (double) this.getPos().getZ() + Math.random(), 0.0D, 0.0D, 0.0D);
                }

                return;
            default:

        }
    }

    private void readConnectedSidesFromNBT(NBTTagCompound tagCompound) {

        NBTTagCompound ourCompound = tagCompound.getCompoundTag("connectedSides");

        for (EnumFacing dir : EnumFacing.VALID_DIRECTIONS) {
            connections[dir.ordinal()] = ourCompound.getBoolean(dir.ordinal() + "");
        }
        checkConnectedSides();
    }

    private void writeConnectedSidesToNBT(NBTTagCompound tagCompound) {

        NBTTagCompound ourCompound = new NBTTagCompound();
        int i = 0;
        for (boolean b : connections) {
            ourCompound.setBoolean(i + "", b);
            i++;
        }

        tagCompound.setTag("connectedSides", ourCompound);
    }

    @Override
    public void readDesc(NBTTagCompound tagCompound) {
        readConnectedSidesFromNBT(tagCompound);
    }

    @Override
    public void writeDesc(NBTTagCompound tagCompound) {
        writeConnectedSidesToNBT(tagCompound);
    }

    @Override
    public boolean needsItem() {
        return false;
    }
}