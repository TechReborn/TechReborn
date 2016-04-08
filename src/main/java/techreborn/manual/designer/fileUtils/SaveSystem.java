package techreborn.manual.designer.fileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.control.TreeItem;
import javafx.stage.DirectoryChooser;
import techreborn.lib.ModInfo;
import techreborn.manual.designer.ManualDesigner;
import techreborn.manual.saveFormat.Entry;
import techreborn.manual.saveFormat.ManualFormat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mark on 05/04/2016.
 */
public class SaveSystem {

    public static HashMap<TreeItem, Entry> entries = new HashMap<>();

    public static ManualFormat master;

    static File lastSave = null;

    public static void export(){
        List<Entry> entryList = new ArrayList<>(entries.values());
        if(master == null){
            master = new ManualFormat("TechReborn", ModInfo.MOD_ID, entryList);
        }
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select export location");
        File openLocation = new File(".");
        if(lastSave != null){
            openLocation = lastSave;
        }
        chooser.setInitialDirectory(openLocation);
        File selectedDirectory = chooser.showDialog(ManualDesigner.stage);
        lastSave = selectedDirectory;
        File masterJson = new File(selectedDirectory, "master.json");
        if(masterJson.exists()){
            masterJson.delete();
        }
        if(selectedDirectory != null){
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(master);
            try {
                FileWriter writer = new FileWriter(masterJson);
                writer.write(json);
                writer.close();
            } catch (IOException e) {
                System.out.println("something bad happened");
                e.printStackTrace();
            }
        }

    }


}
