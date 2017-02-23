/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.manual.loader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import reborncore.RebornCore;
import reborncore.common.util.Unzip;
import techreborn.manual.PageCollection;
import techreborn.manual.Reference;
import techreborn.manual.loader.pages.CategoriesPage;
import techreborn.manual.saveFormat.ManualFormat;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by modmuss50 on 23/04/2016.
 */
public class ManualLoader {

	public static final String MANUAL_VERSION = "0";

	File configDir;

	public static ManualFormat format;

	public ManualLoader(File configDir) {
		this.configDir = configDir;
	}

	VersionsInfo info = null;

	public void load() throws IOException {
		File manualdir = new File(configDir, "manual");
		if (!manualdir.exists()) {
			manualdir.mkdir();
		}

		URL url = new URL(RebornCore.WEB_URL + "techreborn/manual/versions.json");
		URLConnection con = url.openConnection();
		InputStream in = con.getInputStream();
		String encoding = con.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding);
		Gson gson = new Gson();
		info = gson.fromJson(body, new TypeToken<VersionsInfo>() {
		}.getType());

		DownloadablePackageInfo downloadablePackageInfo = null;

		if (info != null) {
			for (DownloadablePackageInfo packageInfo : info.versions) {
				if (packageInfo.packageInfo.version.equals(MANUAL_VERSION)) {
					downloadablePackageInfo = packageInfo;
					break;
				}
			}
		}

		if (downloadablePackageInfo != null) {
			boolean hasIntactZip = false;
			File zipLocation = new File(manualdir, downloadablePackageInfo.fileName);
			if (zipLocation.exists()) {
				String md5 = getMD5(zipLocation);
				if (md5.equals(downloadablePackageInfo.md5)) {
					//Oh look we allready have it!
					hasIntactZip = true;
				}
			} else {
				FileUtils.copyURLToFile(new URL(RebornCore.WEB_URL + "techreborn/manual/packages/" + downloadablePackageInfo.fileName), zipLocation);
				String md5 = getMD5(zipLocation);
				if (md5.equals(downloadablePackageInfo.md5)) {
					//ok the downloaded file is valid
					hasIntactZip = true;
				}
			}
			if (hasIntactZip) {
				File outputDir = new File(manualdir, zipLocation.getName().replace(".zip", ""));
				Unzip.unzip(zipLocation, outputDir);
				File inputData = new File(outputDir, "master.json");
				BufferedReader reader = new BufferedReader(new FileReader(inputData));
				ManualLoader.format = gson.fromJson(reader, ManualFormat.class);
			}

		}

	}

	public static PageCollection getPages() {
		final PageCollection pageCollection = new PageCollection();
		pageCollection.addPage(new CategoriesPage(Reference.pageNames.CONTENTS_PAGE, pageCollection));

		return pageCollection;
	}

	public static String getMD5(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		String md5 = DigestUtils.md5Hex(fis);
		fis.close();
		return md5;
	}

}
