package techreborn.dev;

import reborncore.RebornCore;
import reborncore.common.blocks.BlockMachineBase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created by Mark on 24/04/2016.
 */

//TODO DO NOT SHIP THIS!
public class JsonGenerator {

    public void generate(){
        File mcDir = new File(".");
        File exportFolder = new File(mcDir, "export");
        if(!exportFolder.exists()){
            exportFolder.mkdir();
        }
        File assetsFolder = new File(exportFolder, "assets");
        if(!assetsFolder.exists()){
            assetsFolder.mkdir();
        }
        File modFolder = new File(assetsFolder, "techreborn");
        if(!modFolder.exists()){
            modFolder.mkdir();
        }
        File blockstates = new File(modFolder, "blockstates");
        if(!blockstates.exists()){
            blockstates.mkdir();
        }
        File models = new File(modFolder, "models");
        if(!models.exists()){
            models.mkdir();
        }
        File blockModels = new File(models, "block");
        if(!blockModels.exists()){
            blockModels.mkdir();
        }
        File itemModles = new File(models, "item");
        if(!itemModles.exists()){
            itemModles.mkdir();
        }
        for(Object object : RebornCore.jsonDestroyer.objectsToDestroy){
            if(object instanceof BlockMachineBase){
                BlockMachineBase base = (BlockMachineBase) object;
                File state = new File(blockstates, base.getUnlocalizedName() + ".json");
                if(state.exists()){
                    state.delete();
                }
                try {
                    state.createNewFile();
                    FileOutputStream is = new FileOutputStream(state);
                    OutputStreamWriter osw = new OutputStreamWriter(is);
                    Writer w = new BufferedWriter(osw);
                    w.write("{");
                    w.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
