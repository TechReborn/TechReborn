package techreborn.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileBlastFurnace;
import techreborn.tiles.TileMachineCasing;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBlastFurnace extends BlockMachineBase {

	@SideOnly(Side.CLIENT)
	private IIcon iconFront;

	@SideOnly(Side.CLIENT)
	private IIcon iconTop;

	@SideOnly(Side.CLIENT)
	private IIcon iconBottom;

	public BlockBlastFurnace(Material material)
	{
		super(material);
		setBlockName("techreborn.blastfurnace");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_)
	{
		return new TileBlastFurnace();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		if (!player.isSneaking())
			for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
			{
				if (world.getTileEntity(x + direction.offsetX, y
						+ direction.offsetY, z + direction.offsetZ) instanceof TileMachineCasing)
				{
					TileMachineCasing casing = (TileMachineCasing) world
							.getTileEntity(x + direction.offsetX, y
									+ direction.offsetY, z + direction.offsetZ);
					if (casing.getMultiblockController() != null
							&& casing.getMultiblockController().isAssembled())
					{
						player.openGui(Core.INSTANCE,
								GuiHandler.blastFurnaceID, world, x, y, z);
					}
				}
			}
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister icon)
	{
		this.blockIcon = icon.registerIcon("techreborn:machine/machine_side");
		this.iconFront = icon.registerIcon("techreborn:machine/industrial_blast_furnace_front_off");
		this.iconTop = icon.registerIcon("techreborn:machine/advanced_machine_side");
		this.iconBottom = icon.registerIcon("techreborn:machine/advanced_machine_side");
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata)
	{

		return metadata == 0 && side == 3 ? this.iconFront
				: side == 1 ? this.iconTop : 
					side == 0 ? this.iconBottom: (side == 0 ? this.iconTop
						: (side == metadata ? this.iconFront : this.blockIcon));

	}

}
