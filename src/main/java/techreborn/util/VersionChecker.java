package techreborn.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import techreborn.lib.ModInfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class VersionChecker {

	public static final String apiAddress = "http://modmuss50.me/api/v1/version.php";

	public String projectName;

	ArrayList<ModifacationVersionInfo> versions;

	public boolean isChecking;

	public VersionChecker(String projectName) {
		this.projectName = projectName;
	}

	public void checkVersion() throws IOException {
		isChecking = true;
		URL url = new URL(apiAddress + "?project=" + projectName);
		URLConnection con = url.openConnection();
		InputStream in = con.getInputStream();
		String encoding = con.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding).replaceAll("<br />", "");

		Gson gson = new Gson();
		versions = gson.fromJson(body,new TypeToken<ArrayList<ModifacationVersionInfo>>(){}.getType());
		isChecking = false;
	}

	public void checkVersionThreaded(){
		class VersionCheckerThread extends Thread{
			public void run(){
				try {
					checkVersion();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		VersionCheckerThread thread = new VersionCheckerThread();
		thread.start();
	}

	public boolean isLatestVersion(){
		if(versions == null || versions.isEmpty()){
			return true;
		}
		return versions.get(0).version.equals(ModInfo.MOD_VERSION);
	}

	public ModifacationVersionInfo getLatestVersion(){
		if(versions == null || versions.isEmpty()){
			return null;
		}
		return versions.get(0);
	}

	public ArrayList<String> getChangeLogSinceCurrentVersion(){
		ArrayList<String> log = new ArrayList<String>();
		if(!isLatestVersion()){
			for(ModifacationVersionInfo version : versions){
				if(version.version.equals(ModInfo.MOD_VERSION)){
					break;
				}
				log.addAll(version.changeLog);
			}
		}
		return log;
	}

	static class ModifacationVersionInfo{
		public String version;

		public String minecraftVersion;

		public ArrayList<String> changeLog;

		public String releaseDate;

		public boolean recommended;

		public ModifacationVersionInfo(String version, String minecraftVersion, ArrayList<String> changeLog, String releaseDate, boolean recommended) {
			this.version = version;
			this.minecraftVersion = minecraftVersion;
			this.changeLog = changeLog;
			this.releaseDate = releaseDate;
			this.recommended = recommended;
		}

		public ModifacationVersionInfo() {
		}
	}

	//use this to make an example json file
	public static void main(String[] args) throws IOException {
		System.out.println("Generating example json file");
		ArrayList<ModifacationVersionInfo> infos = new ArrayList<ModifacationVersionInfo>();
		ArrayList<String> changelog = new ArrayList<String>();
		changelog.add("A change");
		changelog.add("Another change");


		infos.add(new ModifacationVersionInfo("1.1.1", "1.7.10", changelog, "12th July", true));
		infos.add(new ModifacationVersionInfo("1.2.0", "1.7.10", changelog, "28th July", true));

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(infos);
		try {
			FileWriter writer = new FileWriter(new File("master.json"));
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
