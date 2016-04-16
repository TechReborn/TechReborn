package techreborn.manual.designer.exporter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.zeroturnaround.zip.ZipUtil;
import reborncore.shields.json.ShieldJsonLoader;
import techreborn.manual.saveFormat.ManualFormat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Mark on 16/04/2016.
 */
public class Exporter {

    public static void run(String[] arg) throws Exception {
        if(arg.length < 3){
            System.out.println("Error incorrect arguments!");
            System.exit(-1);
        }
        File input = new File(arg[1]);
        File output = new File(arg[2]);
        if(!input.exists()){
            System.out.println("that input file does not exist!");
            System.exit(-1);
        }
        if(!output.exists()){
            System.out.println("that output file does not exist!");
            System.exit(-1);
        }
        File inputMaster = new File(input, "master.json");
        if(!inputMaster.exists()){
            System.out.println("That input is not valid! no master.json file found");
            System.exit(-1);
        }
        File inputData = new File(input, "data.json");
        if(!inputData.exists()){
            System.out.println("That input is not valid! no data.json file found");
            System.exit(-1);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File webJson = new File(output, "versions.json");
        VersionsInfo versions = null;
        if(webJson.exists()){
            BufferedReader reader = new BufferedReader(new FileReader(webJson));
            versions = gson.fromJson(reader, VersionsInfo.class);
        } else {
            versions = new VersionsInfo();
            versions.versions = new ArrayList<>();
            versions.name = "TechReborn";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        versions.lastUpdated = dateFormat.format(new Date());

        BufferedReader reader = new BufferedReader(new FileReader(inputData));
        PackageInfo info = gson.fromJson(reader, PackageInfo.class);

        DownloadablePackageInfo downloadablePackageInfo = new DownloadablePackageInfo();
        downloadablePackageInfo.packageInfo = info;
        File packages = new File(output, "packages");
        if(!packages.exists()){
            packages.mkdir();
        }
        dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss-SSS");

        File outputFile = new File(packages, versions.name + dateFormat.format(new Date()) + ".zip");

        ZipUtil.pack(input, outputFile);
        downloadablePackageInfo.fileName = outputFile.getName();
        downloadablePackageInfo.md5 = ShieldJsonLoader.getMD5(outputFile);

        List<DownloadablePackageInfo> savedVersions = versions.versions;
        versions.versions = new ArrayList<>();
        versions.versions.add(downloadablePackageInfo);
        versions.versions.addAll(savedVersions);

        String json = gson.toJson(versions);
        FileWriter writer = new FileWriter(webJson);
        writer.write(json);
        writer.close();

        System.out.println("Done!");
        System.exit(0);
    }

}
