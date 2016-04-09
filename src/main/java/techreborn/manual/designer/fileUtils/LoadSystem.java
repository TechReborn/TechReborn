package techreborn.manual.designer.fileUtils;

import com.google.gson.Gson;
import javafx.scene.control.TreeItem;
import javafx.stage.FileChooser;
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open master.json");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Jason File (*.json)", "*.json"));
        fileChooser.setInitialFileName("master.json");
        fileChooser.setInitialDirectory(new File("."));
        File file = fileChooser.showOpenDialog(ManualDesigner.stage);
        if(file != null){
            if(file.getName().endsWith(".json")){
                //Things
                BufferedReader reader = new BufferedReader(new FileReader(file));
                Gson gson = new Gson();
                ManualFormat format;
                format = gson.fromJson(reader, ManualFormat.class);
                System.out.println(format.entries);
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
            }
        }
    }
}
