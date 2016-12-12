package techreborn.items.tools;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import reborncore.api.power.IEnergyInterfaceTile;
import reborncore.common.powerSystem.PowerSystem;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.items.ItemTRNoDestroy;

/**
 * Created by Mark on 20/03/2016.
 */
public class ItemDebugTool extends ItemTRNoDestroy {

	public ItemDebugTool() {
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		setUnlocalizedName("techreborn.debug");
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos,
	                                  EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof IEnergyInterfaceTile) {
			if (!tile.getWorld().isRemote) {
				playerIn.sendMessage(
					new TextComponentString(TextFormatting.GREEN + "Power" + TextFormatting.BLUE
						+ PowerSystem.getLocaliszedPower(((IEnergyInterfaceTile) tile).getEnergy())));
			}
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.FAIL;
	}
}
