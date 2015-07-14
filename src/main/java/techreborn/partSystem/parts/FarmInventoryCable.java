package techreborn.partSystem.parts;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.energy.tile.IEnergyTile;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import techreborn.client.render.RenderFarmInventoryCable;
import techreborn.lib.Functions;
import techreborn.lib.vecmath.Vecs3d;
import techreborn.lib.vecmath.Vecs3dCube;
import techreborn.partSystem.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FarmInventoryCable extends ModPart implements IPartDesc {
    public Vecs3dCube[] boundingBoxes = new Vecs3dCube[14];
    public float center = 0.6F;
    public float offset = 0.10F;
    public Map<ForgeDirection, TileEntity> connectedSides;
    public int ticks = 0;
    public ItemStack stack;
    protected ForgeDirection[] dirs = ForgeDirection.values();
    private boolean[] connections = new boolean[6];
    private boolean hasCheckedSinceStartup;

    public FarmInventoryCable() {
        connectedSides = new HashMap<ForgeDirection, TileEntity>();
        refreshBounding();
    }

    public void refreshBounding() {
        float centerFirst = center - offset;
        double w = 0.7F / 2;

        boundingBoxes[6] = new Vecs3dCube(centerFirst - w, centerFirst - w,
                centerFirst - w, centerFirst + w, centerFirst + w, centerFirst
                + w);

        w = 0.4F /2;
        int i = 0;
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
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
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if (connectedSides.containsKey(dir))
                boxes.add(boundingBoxes[Functions.getIntDirFromDirection(dir)]);
        }
        boxes.add(boundingBoxes[6]);
    }

    @Override
    public List<Vecs3dCube> getSelectionBoxes() {
        List<Vecs3dCube> vec3dCubeList = new ArrayList<Vecs3dCube>();
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
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

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if (connectedSides.containsKey(dir))
                vecs3dCubesList.add(boundingBoxes[Functions
                        .getIntDirFromDirection(dir)]);
        }
        return vecs3dCubesList;
    }

    @Override
    public void renderDynamic(Vecs3d translation, double delta) {
    }

    @Override
    public boolean renderStatic(Vecs3d translation, int pass) {
        return RenderFarmInventoryCable.renderStatic(translation, pass, this);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        writeConnectedSidesToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
    }

    @Override
    public String getName() {
        return "FarmInventoryCable";
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getItemTextureName() {
        return "";
    }

    @Override
    public void tick() {
        if (worldObj != null) {
            if (worldObj.getTotalWorldTime() % 40 == 0 || hasCheckedSinceStartup == false) {
                checkConnectedSides();
                hasCheckedSinceStartup = true;
            }
        }

    }

    @Override
    public void nearByChange() {
        checkConnectedSides();
    }

    @Override
    public void onAdded() {
        checkConnections(world, getX(), getY(), getZ());

        checkConnectedSides();
    }

    @Override
    public void onRemoved() {
    }

    @Override
    public IModPart copy() {
        FarmInventoryCable part = new FarmInventoryCable();
        return part;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(ModPartRegistry.getItem(getName()));
    }

    public boolean shouldConnectTo(TileEntity entity, ForgeDirection dir) {
        if (entity == null) {
            return false;
        } else if (entity instanceof IEnergyTile) {
            return true;
        } else {
            return ModPartUtils.hasPart(entity.getWorldObj(), entity.xCoord,
                    entity.yCoord, entity.zCoord, this.getName());
        }
    }

    public void checkConnectedSides() {
        refreshBounding();
        connectedSides = new HashMap<ForgeDirection, TileEntity>();
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
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
                te.getWorldObj().markBlockForUpdate(te.xCoord, te.yCoord, te.zCoord);
            }
        }
        checkConnections(world, getX(), getY(), getZ());
        getWorld().markBlockForUpdate(getX(), getY(), getZ());
    }

    public void checkConnections(World world, int x, int y, int z) {
        for (int i = 0; i < 6; i++) {
            ForgeDirection dir = dirs[i];
            int dx = x + dir.offsetX;
            int dy = y + dir.offsetY;
            int dz = z + dir.offsetZ;
            connections[i] = shouldConnectTo(world.getTileEntity(dx, dy, dz),
                    dir);
            world.func_147479_m(dx, dy, dz);
        }
        world.func_147479_m(x, y, z);
    }

    private void readConnectedSidesFromNBT(NBTTagCompound tagCompound) {

        NBTTagCompound ourCompound = tagCompound.getCompoundTag("connectedSides");

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
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
}