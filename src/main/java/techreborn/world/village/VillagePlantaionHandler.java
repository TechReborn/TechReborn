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

package techreborn.world.village;

import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.VillageGenerator;
import net.minecraft.util.math.Direction;


import java.util.List;
import java.util.Random;

public class VillagePlantaionHandler implements VillagerRegistry.IVillageCreationHandler {

	@Override
	public VillageGenerator.PieceWeight getVillagePieceWeight(Random random, int i) {
		return new VillageGenerator.PieceWeight(VillageComponentRubberPlantaion.class, 5, 1);
	}

	@Override
	public Class<?> getComponentClass() {
		return VillageComponentRubberPlantaion.class;
	}


	@Override
	public VillageGenerator.Piece buildComponent(VillageGenerator.PieceWeight villagePiece, VillageGenerator.Start startPiece, List<StructurePiece> pieces, Random random, int p1, int p2, int p3, Direction facing, int p5) {
		return VillageComponentRubberPlantaion.buildComponent(villagePiece, startPiece, pieces, random, p1, p2, p3, facing, p5);
	}
}
