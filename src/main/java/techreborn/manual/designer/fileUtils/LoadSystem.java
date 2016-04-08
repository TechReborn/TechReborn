package techreborn.manual.designer.fileUtils;

import javafx.stage.FileChooser;
import techreborn.manual.designer.ManualDesigner;

import java.io.File;

/**
 * Created by Mark on 05/04/2016.
 */
public class LoadSystem {

    public static void load(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open master.json");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Jason File (*.json)", "*.json"));
        fileChooser.setInitialFileName("master.json");
        fileChooser.setInitialDirectory(new File("."));
        File file = fileChooser.showOpenDialog(ManualDesigner.stage);
        if(file != null){
            if(file.getName().endsWith(".json")){
                //Things
            }
        }
    }
}
