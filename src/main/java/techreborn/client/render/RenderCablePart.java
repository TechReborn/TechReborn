package techreborn.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import techreborn.client.IconSupplier;
import techreborn.lib.Functions;
import techreborn.lib.vecmath.Vecs3d;
import techreborn.partSystem.QLib.ModLib2QLib;
import techreborn.partSystem.parts.CablePart;
import uk.co.qmunity.lib.client.render.RenderHelper;


public class RenderCablePart {
	@SideOnly(Side.CLIENT)
	public static boolean renderStatic(Vecs3d translation, RenderHelper renderer, int pass, CablePart part) {
		renderer.renderBox(ModLib2QLib.convert(part.boundingBoxes[6]), getIconFromType(part.type));
		if (part.connectedSides != null) {
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				if (part.connectedSides.containsKey(dir))
					renderer.renderBox(ModLib2QLib.convert(part.boundingBoxes[Functions.getIntDirFromDirection(dir)]), getIconFromType(part.type));
			}
		}
		return true;
	}
	@SideOnly(Side.CLIENT)
	public static IIcon getIconFromType(int cableType){
		IIcon p = null;
		switch(cableType) {
			case 0:
				p = IconSupplier.insulatedCopperCable;
				break;
			case 1:
				p = IconSupplier.copperCable;
				break;
			case 2:
				p = IconSupplier.goldCable;
				break;
			case 3:
				p = IconSupplier.insulatedGoldCable;
				break;
			case 4:
				p = IconSupplier.doubleInsulatedGoldCable;
				break;
			case 5:
				p = IconSupplier.ironCable;
				break;
			case 6:
				p = IconSupplier.insulatedIronCable;
				break;
			case 7:
				p = IconSupplier.doubleInsulatedIronCable;
				break;
			case 8:
				p = IconSupplier.trippleInsulatedIronCable;
				break;
			case 9:
				p = IconSupplier.glassFiberCable;
				break;
			case 10:
				p = IconSupplier.tinCable;
				break;
			case 11:
				p = IconSupplier.detectorCableBlock;//Detector
				break;
			case 12:
				p = IconSupplier.splitterCableBlock;// Splitter
				break;
			case 13:
				p = IconSupplier.insulatedtinCableBlock;
				break;
			case 14:
				p = IconSupplier.copperCable; // unused?
		}

		return p;
	}
}
