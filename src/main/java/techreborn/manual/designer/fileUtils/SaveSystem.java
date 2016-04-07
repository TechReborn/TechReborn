package techreborn.manual.designer.fileUtils;

import javafx.scene.control.TreeItem;
import techreborn.lib.ModInfo;
import techreborn.manual.saveFormat.Entry;
import techreborn.manual.saveFormat.ManualFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mark on 05/04/2016.
 */
public class SaveSystem {

    public static HashMap<TreeItem, Entry> entries = new HashMap<>();

    public static ManualFormat master;

    public static void export(){

        List<Entry> entrieList = new ArrayList<>();
        for(Object object : entries.entrySet().toArray()){
            Entry entry = (Entry) object;
            entrieList.add(entry);
        }
        System.out.println(entrieList);
        if(master == null){
            master = new ManualFormat("TechReborn", ModInfo.MOD_ID, entrieList);
        }
        //TODO things
    }


}
