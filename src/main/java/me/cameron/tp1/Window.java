package me.cameron.tp1;

import javax.swing.JFrame;

public class Window extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Screen screen;
	
	public Window(){
		
		
		Screen screen = new Screen();
		add(screen);
		this.screen = screen;
		setSize(500, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public void reset() {
//		removeAll();
		remove(screen);
		Screen screen = new Screen();
		this.screen = screen;
		add(screen);
		setSize(500, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	
	}

}
