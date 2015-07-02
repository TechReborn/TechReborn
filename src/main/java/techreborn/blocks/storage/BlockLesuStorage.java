package techreborn.blocks.storage;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import techreborn.blocks.BlockMachineBase;
import techreborn.tiles.lesu.TileLesuStorage;

public class BlockLesuStorage extends BlockMachineBase {

	@SideOnly(Side.CLIENT)
	private IIcon iconFront;

	@SideOnly(Side.CLIENT)
	private IIcon iconTop;

	@SideOnly(Side.CLIENT)
	private IIcon iconBottom;

	public BlockLesuStorage(Material material)
	{
		super(material);
		setBlockName("techreborn.lesustorage");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister icon)
	{
		this.blockIcon = icon.registerIcon("techreborn:machine/lesu_block");
		this.iconFront = icon.registerIcon("techreborn:machine/lesu_block");
		this.iconTop = icon.registerIcon("techreborn:machine/lesu_block");
		this.iconBottom = icon.registerIcon("techreborn:machine/lesu_block");
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata)
	{

		return metadata == 0 && side == 3 ? this.iconFront
				: side == 1 ? this.iconTop : 
					side == 0 ? this.iconBottom: (side == 0 ? this.iconTop
						: (side == metadata ? this.iconFront : this.blockIcon));

	}

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemstack) {
        super.onBlockPlacedBy(world, x, y, z, player, itemstack);
        if(world.getTileEntity(x, y, z) instanceof TileLesuStorage){
            ((TileLesuStorage) world.getTileEntity(x, y, z)).rebuildNetwork();
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        if(world.getTileEntity(x, y, z) instanceof TileLesuStorage) {
            ((TileLesuStorage) world.getTileEntity(x, y, z)).removeFromNetwork();
        }
        super.breakBlock(world, x, y, z, block, meta);
    }



    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileLesuStorage();
    }


}
