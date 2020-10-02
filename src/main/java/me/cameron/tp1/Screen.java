package me.cameron.tp1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TimerTask;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Screen extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int DELAY = 1;

	HashMap<Integer, LinkedList<Point>> points = new HashMap<>();

//	LinkedList<Point> points = new LinkedList<>();
	LinkedList<Point> points__add = new LinkedList<>();
	boolean pause = false;
	Timer timer;
	Random random;
	boolean useColors = false;
	Color[] colors = new Color[] { Color.PINK, Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE,
			Color.MAGENTA, Color.CYAN, Color.GRAY, Color.YELLOW };
	List<Point> splits = new ArrayList<>();
	int boxsize = 1;
	int offsetAllowance = 3;
	int speed = 3;
	double splitChance = 0.5;
	int ground = -1;
	boolean reset = false;

	public Screen() {
		setBackground(Color.WHITE);
		timer = new Timer(DELAY, this);
		timer.start();
		System.out.println("Loading");
		random = new Random();
		MouseListener mouseListener = new MouseListener();
		addKeyListener(new TAdapter());
		addMouseMotionListener(mouseListener);
		addMouseListener(mouseListener);
		setFocusable(true);
		requestFocusInWindow();
		load();

	}

	public void load() {
		points.put(0, new LinkedList<Point>());
		points.get(0).add(new Point(getWidth() / 2, 0));
	}

	public void unload() {
		points.clear();
		splits.clear();
		ground = -1;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		

		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());

//		reset = false;

		for (Entry<Integer, LinkedList<Point>> e : points.entrySet()) {
			if(useColors) {
				if(e.getKey() > colors.length) {
					g.setColor(colors[e.getKey()-colors.length]);
				} else {
					g.setColor(colors[e.getKey()]);
				}
			} else
				g.setColor(Color.WHITE);
//			g.setColor(colors[e.getKey()]);
			
			if (e.getKey() == ground)
				g.setColor(Color.RED);
			Point lp = null;
			for (int z = 0; z != e.getValue().size(); z++) {

				Point p = e.getValue().get(z);
				g.fillRect((int) p.x, (int) p.y, boxsize, boxsize);
				if (lp != null) {
					if (e.getKey() == ground) {
						for (int x = 0; x != 2; x++) {
							g.drawLine(lp.x + (1 + x), lp.y, p.x + (1 + x), p.y);
							g.drawLine(lp.x - (1 + x), lp.y, p.x - (1 + x), p.y);
						}
					}

					g.drawLine(lp.x, lp.y, p.x, p.y);

				}
				lp = p;
			}
		}

		for (Point point : splits) {
			g.setColor(Color.green);
			g.fillRect(((int) point.x - 2), ((int) point.y - 2), 4, 4);
		}

		if (reset) {
			pause = true;
			unload();
			load();
			pause = false;
			reset = false;
		}
		if (!pause) {
			try {

				int branchSize = points.size() + 0;
				for (int branch = 0; branch != branchSize; branch++) {
					Point lastPoint = points.get(branch).get(points.get(branch).size() - 1);

					Point newPoint = new Point(
							lastPoint.x + ((random.nextInt(offsetAllowance*2) * (random.nextBoolean() ? -1 : 1))),
							(lastPoint.y + speed)
									+ ((random.nextInt(offsetAllowance) * (random.nextBoolean() ? -1 : 1))));

					points.get(branch).add(newPoint);
					int z = newPoint.y;
					if (z >= getHeight()) {
						pause = true;
						ground = branch;

						java.util.Timer timer1 = new java.util.Timer();
						timer1.schedule(new TimerTask() {

							@Override
							public void run() {
								reset = true;
							}
						}, 800);
//						unload();
//						load();
//						pause = false;
					}

				}
				if (random.nextDouble() <= splitChance / 100/* && points.size() < colors.length */) {
					int randomBranchId = random.nextInt(points.size());
					LinkedList<Point> randomBranch = points.get(randomBranchId);
					Point lastPoint = randomBranch.get(randomBranch.size() - 1);

					Point newPoint = new Point(
							lastPoint.x + ((random.nextInt(offsetAllowance) * (random.nextBoolean() ? -1 : 1))),
							lastPoint.y + speed);

					points.put(points.size(), new LinkedList<Point>());
					points.get(points.size() - 1).add(newPoint);
					splits.add(newPoint);

				}

			} catch (NullPointerException ex) {

			}

		}

		// This is all dEbug shitt
		g.setColor(Color.white);
		g.drawString("Frame Delay: " + DELAY, 0, g.getFontMetrics().getHeight());
//		g.drawString("Branch 0 Progress: " + z, 0, g.getFontMetrics().getHeight() * 2);
		for (int b = 0; b != points.size(); b++) {
//			g.setColor(colors[b]);
			g.drawString("Branch " + b, 0, g.getFontMetrics().getHeight() * (b + 3));
		}
//		g.drawString("Numbers: " + numbers, 0, g.getFontMetrics().getHeight() * 2);
//		g.drawString("tSwap: " + tswaps, 0, g.getFontMetrics().getHeight() * 4);
//		g.drawString("Comparisons: " + tcomps, 0, g.getFontMetrics().getHeight() * 5); 
//		g.drawString("Sweeps: " + sweep, 100, g.getFontMetrics().getHeight());

		g.setColor(Color.RED);

		// Content here

		g.dispose();
	}

	private void reset() {
	}

	private class TAdapter extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_R) {
				pause = true;
				unload();
				load();
				pause = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_C) {
				useColors = !useColors;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				pause();
			}
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				reset();
				load();
			}

		}
	}

	private class MouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mouseMoved(MouseEvent e) {

			// mx = e.getX();
			// my = e.getY();

		}
	}

	public void pause() {
		pause = !pause;
	}

}
