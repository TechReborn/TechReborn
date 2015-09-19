package techreborn.util;

import net.minecraftforge.oredict.OreDictionary;

public class OreUtil {

    public static boolean doesOreExistAndValid(String name){
        if(!OreDictionary.doesOreNameExist(name)){
            return false;
        }
        return OreDictionary.getOres(name).size() >= 1;
    }
}
