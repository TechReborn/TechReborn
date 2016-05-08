//package techreborn.compat.psi;
//
//import net.minecraftforge.fml.common.event.FMLInitializationEvent;
//import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
//import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
//import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
//import techreborn.compat.ICompatModule;
//import techreborn.compat.psi.spell.trick.block.PieceTrickBlockTap;
//import vazkii.psi.api.PsiAPI;
//import vazkii.psi.api.spell.Spell;
//import vazkii.psi.api.spell.SpellPiece;
//import vazkii.psi.common.lib.LibPieceGroups;
//
//public class CompatModulePsi implements ICompatModule {
//	public static PieceContainer trickTreetap;
//	public static final String TRICK_TAP_BLOCK = "trickTapBlock";
//
//	@Override
//	public void preInit(FMLPreInitializationEvent event) {
//		trickTreetap = register(PieceTrickBlockTap.class, TRICK_TAP_BLOCK, LibPieceGroups.BLOCK_WORKS);
//	}
//
//	@Override
//	public void init(FMLInitializationEvent event) {
//
//	}
//
//	@Override
//	public void postInit(FMLPostInitializationEvent event) {
//
//	}
//
//	@Override
//	public void serverStarting(FMLServerStartingEvent event) {
//
//	}
//
//	public static PieceContainer register(Class<? extends SpellPiece> clazz, String name, String group) {
//		return register(clazz, name, group, false);
//	}
//
//	public static PieceContainer register(Class<? extends SpellPiece> clazz, String name, String group, boolean main) {
//		PsiAPI.registerSpellPieceAndTexture(name, clazz);
//		PsiAPI.addPieceToGroup(clazz, group, main);
//		return (Spell s) -> {
//			return SpellPiece.create(clazz, s);
//		};
//	}
//
//	public static interface PieceContainer {
//		public SpellPiece get(Spell s);
//	}
//
//}
