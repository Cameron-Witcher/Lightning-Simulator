package me.cameron.tp1;

public class Main {
	
	public static Window window;

	public static void main(String[] args) {
		window = new Window();
	}

	public static void reset() {
		window.reset();
	}

	public static Window getWindow() {
		return window;
	}

}
