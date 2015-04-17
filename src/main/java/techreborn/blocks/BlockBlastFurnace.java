package techreborn.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileBlastFurnace;
import techreborn.tiles.TileMachineCasing;

public class BlockBlastFurnace extends BlockContainer{

	@SideOnly(Side.CLIENT)
	private IIcon front;
	@SideOnly(Side.CLIENT)
	private IIcon other;
	
	public BlockBlastFurnace(Material material) 
	{
		super(material);
		setCreativeTab(TechRebornCreativeTab.instance);
		setBlockName("techreborn.blastfurnace");
		setHardness(2F);
	}
	
	 @Override
	 public TileEntity createNewTileEntity(World world, int p_149915_2_) {
	        return new TileBlastFurnace();
	 }
	 
	 @Override
	 public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		 if (!player.isSneaking())
			 for(ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS){
				 if(world.getTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ) instanceof TileMachineCasing){
					 TileMachineCasing casing = (TileMachineCasing) world.getTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
					 if(casing.getMultiblockController() != null && casing.getMultiblockController().isAssembled()){
						 player.openGui(Core.INSTANCE, GuiHandler.blastFurnaceID, world, x, y, z);
					 }
				 }
			 }
		 return true;
	    }
	
	 @Override
	 @SideOnly(Side.CLIENT)
	 public void registerBlockIcons(IIconRegister icon) {
		 front = icon.registerIcon("techreborn:machine/industrial_blast_furnace_front_off");
		 other = icon.registerIcon("techreborn:machine/machine_side");
	 }

	 @Override
	 @SideOnly(Side.CLIENT)
	 public IIcon getIcon(int currentSide, int meta) {
	        if (currentSide == 2) {
	            return front;
	        } else {
	            return other;
	        }
	    }

}
