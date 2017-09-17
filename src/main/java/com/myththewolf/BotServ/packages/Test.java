package com.myththewolf.BotServ.packages;

import com.myththewolf.BotServ.packages.downloader.Progressbar;



public class Test {
	private static Progressbar pp;
	public static void main(String[] args) throws InterruptedException {
		FileDownloader fd = new FileDownloader("http://70.139.52.7/pluginrepo/repo.json");
		fd.download();
	}
}
