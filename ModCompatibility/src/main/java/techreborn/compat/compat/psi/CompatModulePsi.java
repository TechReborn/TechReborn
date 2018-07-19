/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

//package techreborn.compat.compat.psi;
//
//import net.minecraftforge.fml.common.event.FMLInitializationEvent;
//import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
//import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
//import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
//import techreborn.compat.compat.ICompatModule;
//import techreborn.compat.compat.psi.spell.trick.block.PieceTrickBlockTap;
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
