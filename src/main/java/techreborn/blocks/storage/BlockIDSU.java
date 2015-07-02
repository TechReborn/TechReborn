package techreborn.blocks.storage;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.blocks.BlockMachineBase;
import techreborn.client.GuiHandler;
import techreborn.packets.PacketHandler;
import techreborn.tiles.idsu.IDSUManager;
import techreborn.tiles.idsu.TileIDSU;

public class BlockIDSU extends BlockMachineBase {

	@SideOnly(Side.CLIENT)
	private IIcon iconFront;

	@SideOnly(Side.CLIENT)
	private IIcon iconTop;

	@SideOnly(Side.CLIENT)
	private IIcon iconBottom;

	public BlockIDSU(Material material)
	{
		super(material);
		setBlockName("techreborn.idsu");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister icon)
	{
		this.blockIcon = icon.registerIcon("techreborn:machine/idsu_side");
		this.iconFront = icon.registerIcon("techreborn:machine/idsu_front");
		this.iconTop = icon.registerIcon("techreborn:machine/idsu_side");
		this.iconBottom = icon.registerIcon("techreborn:machine/idsu_side");
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
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileIDSU(5, 2048, 100000000);
	}


	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
									EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER){
			PacketHandler.sendPacketToPlayer(IDSUManager.INSTANCE.getPacket(world, player), player);
		}
		if (!player.isSneaking())
			player.openGui(Core.INSTANCE, GuiHandler.idsuID, world, x, y,
					z);
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemstack) {
		super.onBlockPlacedBy(world, x, y, z, player, itemstack);
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof TileIDSU){
			((TileIDSU) tile).setFacing((short) world.getBlockMetadata(x, y, z));
		}
	}
}
