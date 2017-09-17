package com.myththewolf.BotServ.packages;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.json.JSONException;

import com.myththewolf.BotServ.packages.downloader.DirectDownloader;
import com.myththewolf.BotServ.packages.downloader.DownloadListener;
import com.myththewolf.BotServ.packages.downloader.DownloadTask;
import com.myththewolf.BotServ.packages.downloader.Progressbar;

public class FileDownloader {
	private String down;
	private Progressbar pp;
	private Thread t;
	public FileDownloader(String URL) {
		down = URL;
	}
	public void download() {
		try {
			DirectDownloader dd = new DirectDownloader();
		
			dd.download(new DownloadTask(new URL(
					"http://70.139.52.7/pluginrepo/repo.json"),
					new FileOutputStream("out.rar"), new DownloadListener() {

						@Override
						public void onUpdate(int bytes, int totalDownloaded) {
							pp.setVal(totalDownloaded);
						}

						@Override
						public void onStart(String fname, int fsize) {
							pp = new Progressbar(fsize, "test..");
						}

						@Override
						public void onComplete() {
							pp.finish();
							t.stop();
						}

						@Override
						public void onCancel() {
							// TODO Auto-generated method stub

						}
					}));
			// Start downloading
			 t = new Thread( dd );
			 t.start();
		
			 
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
