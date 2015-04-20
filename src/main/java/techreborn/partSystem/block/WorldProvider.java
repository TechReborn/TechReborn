/*
 * This file was made by modmuss50. View the licence file to see what licence this is is on. You can always ask me if you would like to use part or all of this file in your project.
 */

package techreborn.partSystem.block;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import techreborn.lib.Location;
import techreborn.lib.vecmath.Vecs3dCube;
import techreborn.partSystem.IPartProvider;
import techreborn.partSystem.ModPart;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by mark on 10/12/14.
 */
public class WorldProvider implements IPartProvider {
	Block blockModPart;

	@Override
	public String modID() {
		return "Minecraft";
	}

	@Override
	public void registerPart() {
//Loads all of the items

		blockModPart = new BlockModPart(Material.ground).setBlockName("modPartBlock");
		GameRegistry.registerBlock(blockModPart, "modPartBlock");

		//registers the tile and renderer
		GameRegistry.registerTileEntity(TileEntityModPart.class, "TileEntityModPart");
	}

	public void clientRegister() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityModPart.class, new RenderModPart());
	}

	@Override
	public boolean checkOcclusion(World world, Location location, Vecs3dCube cube) {
		return true;
	}

	@Override
	public boolean hasPart(World world, Location location, String name) {
		TileEntity tileEntity = world.getTileEntity(location.getX(), location.getY(), location.getZ());
		if (tileEntity instanceof TileEntityModPart) {
			for (ModPart part : ((TileEntityModPart) tileEntity).getParts()) {
				if (part.getName().equals(name)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean placePart(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, ModPart modPart) {
		ForgeDirection forgeDirection = ForgeDirection.getOrientation(side);
		if (world.getBlock(x + forgeDirection.offsetX, y + forgeDirection.offsetY, z + forgeDirection.offsetZ) == Blocks.air) {
			TileEntityModPart modPart1;
			world.setBlock(x + forgeDirection.offsetX, y + forgeDirection.offsetY, z + forgeDirection.offsetZ, blockModPart);
			modPart1 = (TileEntityModPart) world.getTileEntity(x + forgeDirection.offsetX, y + forgeDirection.offsetY, z + forgeDirection.offsetZ);
			//if(modPart1.canAddPart(modPart)){
				modPart1.addPart(modPart);
				return true;
			//}
		}
		//this adds a part to a block
		if (world.getBlock(x, y, z) == blockModPart) {
			TileEntityModPart tileEntityModPart = (TileEntityModPart) world.getTileEntity(x + forgeDirection.offsetX, y + forgeDirection.offsetY, z + forgeDirection.offsetZ);
			if (!tileEntityModPart.getPartsByName().contains(modPart.getName()) && tileEntityModPart.canAddPart(modPart))
				tileEntityModPart.addPart(modPart);
			return true;
		}
		if (world.getBlock(x + forgeDirection.offsetX, y + forgeDirection.offsetY, z + forgeDirection.offsetZ) == blockModPart) {
			TileEntityModPart tileEntityModPart = (TileEntityModPart) world.getTileEntity(x + forgeDirection.offsetX, y + forgeDirection.offsetY, z + forgeDirection.offsetZ);
			if (!tileEntityModPart.getPartsByName().contains(modPart.getName()) && tileEntityModPart.canAddPart(modPart))
				tileEntityModPart.addPart(modPart);
			return true;
		}
		return false;
	}

	@Override
	public boolean isTileFromProvider(TileEntity tileEntity) {
		return tileEntity instanceof TileEntityModPart;
	}
}
