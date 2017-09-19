package com.myththewolf.BotServ.packages.downloader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FileDownloader implements Runnable{
	private URL fileURL;
	private File out;
	private URL[] fileSet;
	private File[] outs;
	private Progressbar shower;
	private String text;
	private boolean isSet;
	public FileDownloader(String URL, File output,String text2show) throws MalformedURLException {
		fileURL = new URL(URL);
		out = output;
		text = text2show;
		isSet = false;
	}
	public FileDownloader(URL[] set,File[] outputs) {
		this.outs = outputs;
		this.fileSet = set;
		isSet = true;
	}
	public void run() {
		if(isSet) {
			int pos = 0;
			for(URL i : this.fileSet) {
				try {
					download(i, outs[pos]);
				} catch (IOException e) {
					System.err.println("Could not download/save: " + i.toString());
				}
			}
		}else {
			try {
				download(fileURL, out);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private int getFileSize(URL url) {
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
	public void download(URL remote,File file) throws IOException {
		 BufferedInputStream bis = null;
		 FileOutputStream fis = null;
		try {
			this.shower = new Progressbar(getFileSize(fileURL), text);
			
	        bis = new BufferedInputStream(remote.openStream());
	        fis = new FileOutputStream(file);
	        long numBytes = 0;
	        byte[] buffer = new byte[1024];
	        int count=0;
	        while((count = bis.read(buffer,0,1024)) != -1)
	        {
	        	numBytes = numBytes + 1024;
	            fis.write(buffer, 0, count);
	            this.shower.setVal(numBytes);
	            this.shower.printBar(false);
	        }
	        
	        
			}catch(Exception e) {
				e.printStackTrace();
				
			}finally {
				this.shower.finish();
				fis.close();
				bis.close();
			}
	}
}
