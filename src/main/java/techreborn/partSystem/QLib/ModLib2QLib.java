/*
 * This file was made by modmuss50. View the licence file to see what licence this is is on. You can always ask me if you would like to use part or all of this file in your project.
 */

package techreborn.partSystem.QLib;

import reborncore.common.misc.vecmath.Vecs3d;
import reborncore.common.misc.vecmath.Vecs3dCube;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mark on 09/12/14.
 */
public class ModLib2QLib {

    public static Vec3d convert(Vecs3d input) {
        return new Vec3d(input.getX(), input.getY(), input.getZ());
    }

    public static Vec3dCube convert(Vecs3dCube input) {
        return new Vec3dCube(input.toAABB());
    }

    public static Vecs3d convert(Vec3d input) {
        return new Vecs3d(input.getX(), input.getY(), input.getZ());
    }

    public static Vecs3dCube convert(Vec3dCube input) {
        return new Vecs3dCube(input.toAABB());
    }

    public static List<Vecs3dCube> convert(List<Vec3dCube> input) {
        List<Vecs3dCube> list = new ArrayList<Vecs3dCube>();
        for (Vec3dCube cube : input) {
            list.add(new Vecs3dCube(cube.toAABB()));
        }
        return list;
    }

    // Its got to be called becuase of some weird thing see:
    // https://stackoverflow.com/questions/1998544/method-has-the-same-erasure-as-another-method-in-type
    public static List<Vec3dCube> convert2(List<Vecs3dCube> input) {
        List<Vec3dCube> list = new ArrayList<Vec3dCube>();
        for (Vecs3dCube cube : input) {
            list.add(new Vec3dCube(cube.toAABB()));
        }
        return list;
    }

}
