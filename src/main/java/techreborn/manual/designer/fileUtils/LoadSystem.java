package techreborn.manual.designer.fileUtils;

import com.google.gson.Gson;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.stage.DirectoryChooser;
import techreborn.manual.designer.ManualCatergories;
import techreborn.manual.designer.ManualDesigner;
import techreborn.manual.saveFormat.Entry;
import techreborn.manual.saveFormat.ManualFormat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by Mark on 05/04/2016.
 */
public class LoadSystem {

    public static void load() throws FileNotFoundException {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Open Folder");
        fileChooser.setInitialDirectory(new File("."));
        File folder = fileChooser.showDialog(ManualDesigner.stage);
        if(folder != null){
            File masterJson = new File(folder, "master.json");
            if(!masterJson.exists()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Could not find project");
                alert.setHeaderText("Could not find project!");
                alert.setContentText("Could not find the project in this directory!");
                alert.show();
            } else if(masterJson.getName().endsWith(".json")){
                //Things
                File imageDir = new File(folder, "images");
                if(!imageDir.exists()){
                    imageDir.mkdir();
                }
                BufferedReader reader = new BufferedReader(new FileReader(masterJson));
                Gson gson = new Gson();
                ManualFormat format;
                format = gson.fromJson(reader, ManualFormat.class);
                for(Entry entry : format.entries){
                    System.out.println(entry.type);
                    TreeItem parentTree = null;
                    if(entry.category.equals(ManualCatergories.blocks.getValue())){
                        parentTree = ManualCatergories.blocks;
                    } else if(entry.category.equals(ManualCatergories.items.getValue())){
                        parentTree = ManualCatergories.items;
                    }
                    if(parentTree == null){
                        System.out.println("something bad happened");
                    }
                    TreeItem<String> newItem = new TreeItem<>(entry.name);
                    parentTree.getChildren().add(newItem);
                    parentTree.setExpanded(true);
                    SaveSystem.entries.put(newItem, entry);
                }
                SaveSystem.lastSave = folder;
            }
        }
    }
}
