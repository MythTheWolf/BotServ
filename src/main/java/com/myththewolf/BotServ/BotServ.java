package com.myththewolf.BotServ;

import java.util.Arrays;
import java.util.Scanner;

import net.dv8tion.jda.core.JDA;

public class BotServ {
	public static void main(String[] args) {
		Thread server = new Thread(new Driver());
		server.start();
		
	}
	protected static void ready(JDA event) {
		scanAgain();
	}
	private static void scanAgain() {
		System.out.print(">");
		Scanner keyboard = new Scanner(System.in);	
		handleInput(keyboard.nextLine().split(" "));
		keyboard.close();
	}
	private static void handleInput(String[] handle) {
		System.out.println("Got command string: " + Arrays.toString(handle));
	}
}
