package com.myththewolf.BotServ.packages.downloader;
public class Progressbar {
	private long max;
	private long current;
	private String name;
	private long start;
	private long lastUpdate;

	public Progressbar(long max, String name) {
		this.start = System.currentTimeMillis();
		this.name = name;
		this.max = max;
		System.out.println(this.name + ":");
		this.printBar(false);
	}

	public void setVal(long i) {
		this.current = i;
		if ((System.currentTimeMillis() - this.lastUpdate) > 1000) {
			this.lastUpdate = System.currentTimeMillis();
			this.printBar(false);
		}
	}

	public void finish() {
		this.current = this.max;
		this.printBar(true);
		Thread.currentThread().stop();
	}

	public void printBar(boolean finished) {
		double numbar = Math.floor(20 * (double) current / (double) max);
		String strbar = "";
		int ii = 0;
		for (ii = 0; ii < numbar; ii++) {
			strbar += "=";
		}
		for (ii = (int) numbar; ii < 20; ii++) {
			strbar += " ";
		}
		long elapsed = (System.currentTimeMillis() - this.start);
		int seconds = (int) (elapsed / 1000) % 60;
		int minutes = (int) (elapsed / 1000) / 60;
		String strend = String.format("%02d", minutes) + ":" + String.format("%02d", seconds);

		String strETA = "";
		if (elapsed < 2000) {
			strETA = "--:--";
		} else {
			long timeETA = elapsed * (long) ((double) max / (double) current);
			int ETAseconds = (int) (timeETA / 1000) % 60;
			int ETAminutes = (int) (timeETA / 1000) / 60;
			strETA = String.format("%02d", ETAminutes) + ":" + String.format("%02d", ETAseconds);
		}
		if (finished) {
			strend = "Finished: " + strend + "               ";
		} else {
			strend = "Elapsed: " + strend + " ETA: " + strETA + "   ";
		}
		System.out.print("|" + strbar + "| " + strend);
		if (finished) {
			System.out.print("\n");
		} else {
			System.out.print("\r");
		}
	}

	public static void main(String[] args) {
		Progressbar h = new Progressbar(100000, "k");
		for(int i =0;i<10000;i++) {
			h.setVal(i);
			h.printBar(false);
		}
	}

}