package com.myththewolf.BotServ.packages.downloader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FileDownloader {
	private URL fileURL;
	private File out;
	private List<URL> fileSet = new ArrayList<>();
	private List<File> outs = new ArrayList<>();
	private String text;
	private Progressbar shower;
	private boolean isSet;
	private List<String> stringSet = new ArrayList<>();

	public FileDownloader(String URL, File output, String text2show) throws MalformedURLException {
		fileURL = new URL(URL);
		out = output;
		text = text2show;
		isSet = false;
	}

	public FileDownloader(List<URL> set, List<File> outputs, List<String> texts) {
		this.outs = outputs;
		this.fileSet = set;
		isSet = true;
		stringSet = texts;
	}

	public void run() throws IOException {
		if (isSet) {
			int pos = 0;
			for (URL i : this.fileSet) {
				download(i, outs.get(pos), stringSet.get(pos));
				pos++;
			}
		} else {

			download(fileURL, out, text);

		}
	}

	public static long getFileSize(URL url) {
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("HEAD");
			conn.getInputStream();
			return conn.getContentLength();
		} catch (IOException e) {
			return -1;
		} finally {
			conn.disconnect();
		}
	}

	public void download(URL remote, File file, String info) throws IOException {
		BufferedInputStream bis = null;
		FileOutputStream fis = null;
		try {
			this.shower = new Progressbar(getFileSize(remote), info);

			bis = new BufferedInputStream(remote.openStream());
			fis = new FileOutputStream(file);
			long numBytes = 0;
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = bis.read(buffer, 0, 1024)) != -1) {
				numBytes = numBytes + 1024;
				fis.write(buffer, 0, count);
				this.shower.setVal(numBytes);
				this.shower.printBar(false);
			}
			fis.close();
			bis.close();
		} catch (Exception e) {
			throw e;

		} finally {
			//fis.close();
			//bis.close();
		
		}
	}
}
