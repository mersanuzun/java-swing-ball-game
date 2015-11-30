import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.Component;
import java.awt.Dimension;

public class MoveABall extends JFrame implements ActionListener {

	public static final int CANVAS_HEIGHT = 500;
	public static final int CANVAS_WIDTH = 500;
	public static final Color CANVAS_BACKGROUND = Color.CYAN;
	public static final int BARRIER_HEIGHT = 50;
	public static final int BARRIER_WIDTH = 50;
	public static Color PEN_COLOR = Color.RED;
	public static Color BALL_COLOR = Color.BLUE;
	public static Color FORAGE_COLOR = Color.GREEN;
	public static Color BARRIER_COLOR = Color.BLACK;
	public static Color WALL_COLOR = Color.RED;
	private DrawCanvas canvas;
	private Ellipse2D forage;
	private int forageWidth = 10;
	private int forageHeight = 10;
	private Ellipse2D ball;
	private Rectangle2D barrier;
	JButton restartButton;
	private Timer timer;
	private int ballMoveStep = 1;
	private String direction = "R";
	private int point = 0;
	private int levelIndex = 0;
	private String gameStatus;
	private ArrayList<Line2D> walls = new ArrayList<Line2D>();
	private ArrayList<Rectangle2D> barriers = new ArrayList<Rectangle2D>();
	private ArrayList<GameLevel> levels = new ArrayList<GameLevel>();
	private Random rnd = new Random();

	/**
	 * Create the frame.
	 */

	public MoveABall() {
		
		prepareLevels();
		// timer
		timer = new Timer((levels.get(levelIndex).getGameSpeed()), this);

		// panel for buttons
		JPanel btnPanel = new JPanel(new BorderLayout());
		MyListener myListener = new MyListener();
		JButton lftButton = new JButton("Left");
		lftButton.addActionListener(myListener);
		btnPanel.add(lftButton, BorderLayout.WEST);

		JButton rghtButton = new JButton("Right");
		rghtButton.addActionListener(myListener);
		btnPanel.add(rghtButton, BorderLayout.EAST);

		JButton upButton = new JButton("Up");
		upButton.addActionListener(myListener);
		btnPanel.add(upButton, BorderLayout.NORTH);

		JButton downButton = new JButton("Down");
		downButton.addActionListener(myListener);
		btnPanel.add(downButton, BorderLayout.SOUTH);

		// main panel, canvas
		JPanel mainPanel = new JPanel(new BorderLayout());
		restartButton = new JButton("Restart");
		restartButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				levelIndex = 0;
				prepareLevels();
				prepareGame();
				requestFocus();
				restartButton.setVisible(false);
			}

		});
		restartButton.setPreferredSize(new Dimension(CANVAS_WIDTH / 4, CANVAS_HEIGHT));
		restartButton.setVisible(false);
		canvas = new DrawCanvas();
		canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
		mainPanel.add(restartButton, BorderLayout.EAST);
		mainPanel.add(canvas, BorderLayout.CENTER);

		// creation ball, forage, walls and barriers
		gameStatus = "GAME_INIT";
		prepareGame();

		// setting window size, visibility, focusable
		setContentPane(mainPanel);
		setBounds(new Rectangle(CANVAS_WIDTH + 200, CANVAS_HEIGHT + 75));
		setVisible(true);
		requestFocus();

		// add key listener for controlling the ball with keys
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if ((gameStatus.equalsIgnoreCase("GAME_STARTED")) || (gameStatus.equalsIgnoreCase("GAME_INIT"))) {
					if (e.getKeyCode() == KeyEvent.VK_DOWN) {
						direction = "D";
						gameStatus = "GAME_STARTED";
						checkTimer();
					} else if (e.getKeyCode() == KeyEvent.VK_UP) {
						direction = "U";
						gameStatus = "GAME_STARTED";
						checkTimer();
					} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
						direction = "L";
						gameStatus = "GAME_STARTED";
						checkTimer();
					} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
						direction = "R";
						gameStatus = "GAME_STARTED";
						checkTimer();
					}
				}
			}
		});
	}
	
	// define levels
	public void prepareLevels() {
		levels.removeAll(levels);
		levels.add(new GameLevel(1, 2, 10)); // level 1
		levels.add(new GameLevel(2, 4, 15)); // level 2
		levels.add(new GameLevel(3, 6, 20)); // level 3
		levels.add(new GameLevel(4, 8, 25)); // level 4
	}

	public void prepareGame() {
		ball = null;
		forage = null;
		barriers.removeAll(barriers);
		walls.removeAll(walls);
		ball = new Ellipse2D.Double(CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2, 25, 25);
		forage = new Ellipse2D.Double(CANVAS_WIDTH / 4, CANVAS_HEIGHT / 3, forageWidth, forageHeight);
		for (int i = 0; i < (levels.get(levelIndex)).getBarrierNumbers(); i++) {
			ArrayList<Integer> coor = findBarrierPlace();
			barriers.add(new Rectangle2D.Double(coor.get(0), coor.get(1), BARRIER_WIDTH, BARRIER_HEIGHT));
		}
		walls.add(new Line2D.Double(0, 0, CANVAS_WIDTH, 0));
		walls.add(new Line2D.Double(CANVAS_WIDTH, 0, CANVAS_WIDTH, CANVAS_HEIGHT));
		walls.add(new Line2D.Double(CANVAS_WIDTH, CANVAS_HEIGHT, 0, CANVAS_HEIGHT));
		walls.add(new Line2D.Double(0, CANVAS_WIDTH, 0, 0));
		repaint();
		gameStatus = "GAME_INIT";
	}

	public void checkTimer() {
		if (!timer.isRunning()) {
			timer.start();
		}
	}

	public ArrayList<Integer> findBarrierPlace() {
		ArrayList<Integer> coor = new ArrayList<Integer>();
		while (true) {
			int counter = 0;
			int x = rnd.nextInt(CANVAS_WIDTH - BARRIER_WIDTH) + 1;
			int y = rnd.nextInt(CANVAS_HEIGHT - BARRIER_HEIGHT) + 1;
			if ((ball.intersects(x, y, BARRIER_WIDTH, BARRIER_HEIGHT)
					|| (forage.intersects(x, y, forage.getWidth(), forage.getHeight())))) {
				continue;
			}
			for (Rectangle2D b : barriers) {
				if ((b.intersects(x, y, BARRIER_WIDTH, BARRIER_HEIGHT))) {
					break;
				}
				counter++;
			}
			if (counter == barriers.size()) {
				coor.add(x);
				coor.add(y);
				break;
			}
		}
		return coor;
	}

	public ArrayList<Integer> findForagePlace() {
		ArrayList<Integer> coor = new ArrayList<Integer>();
		while (true) {
			int counter = 0;
			int x = rnd.nextInt(CANVAS_WIDTH - forageWidth) + 1;
			int y = rnd.nextInt(CANVAS_HEIGHT - forageHeight) + 1;
			if (ball.intersects(x, y, ball.getWidth(), ball.getHeight())) {
				continue;
			}
			for (Rectangle2D b : barriers) {
				if ((b.intersects(x, y, forageWidth, forageHeight))) {
					break;
				}
				counter++;
			}
			if (counter == barriers.size()) {
				coor.add(x);
				coor.add(y);
				break;
			}
		}
		return coor;
	}

	public void forageRePlace() {
		ArrayList<Integer> coor = findForagePlace();
		forage.setFrame(coor.get(0), coor.get(1), forage.getWidth(), forage.getHeight());
	}

	public Boolean isHit() {
		if (ball.intersects(forage.getX(), forage.getY(), forage.getWidth(), forage.getHeight())) {
			return true;
		}
		return false;
	}

	public Boolean ishitBarrier() {
		for (Rectangle2D b : barriers) {
			if (ball.intersects(b)) {
				return true;
			}
		}
		return false;
	}

	public Boolean isInside() {
		if (((ball.getX() > 0) && (ball.getX() + ball.getWidth() < CANVAS_WIDTH))
				&& ((ball.getY() > 0) && (ball.getY() + ball.getHeight() < CANVAS_HEIGHT))) {
			return true;
		}
		return false;
	}

	class MyListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof JButton) {
				if (((JButton) e.getSource()).getText().equalsIgnoreCase("Left")) {
					direction = "L";
					checkTimer();
				} else if (((JButton) e.getSource()).getText().equalsIgnoreCase("right")) {
					direction = "R";
					checkTimer();
				} else if (((JButton) e.getSource()).getText().equalsIgnoreCase("Up")) {
					direction = "U";
					checkTimer();
				} else if (((JButton) e.getSource()).getText().equalsIgnoreCase("down")) {
					direction = "D";
					checkTimer();
				}
			}
		}
	}

	private class DrawCanvas extends JPanel {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			setBackground(CANVAS_BACKGROUND);
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(BALL_COLOR);
			g2.fill(ball);
			g2.setColor(WALL_COLOR);
			for (Line2D w : walls) {
				g2.draw(w);
			}
			g2.setColor(FORAGE_COLOR);
			g2.fill(forage);
			g2.setColor(BARRIER_COLOR);
			for (Rectangle2D b : barriers) {
				g2.fill(b);
			}
			g2.setColor(PEN_COLOR);
			if (gameStatus.equalsIgnoreCase("GAME_OVER")) {
				g2.drawString("Game is over.", CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2);
			}
			if (gameStatus.equalsIgnoreCase("GAME_INIT")) {
				g2.drawString("Press the arrow keys to start", CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (direction) {
		case "U":
			ball.setFrame(ball.getX(), ball.getY() - ballMoveStep, ball.getWidth(), ball.getHeight());
			break;
		case "D":
			ball.setFrame(ball.getX(), ball.getY() + ballMoveStep, ball.getWidth(), ball.getHeight());
			break;
		case "L":
			ball.setFrame(ball.getX() - ballMoveStep, ball.getY(), ball.getWidth(), ball.getHeight());
			break;
		case "R":
			ball.setFrame(ball.getX() + ballMoveStep, ball.getY(), ball.getWidth(), ball.getHeight());
			break;
		}
		if (isHit()) {
			forageRePlace();
			levels.get(levelIndex).setGameSpeed((int) (levels.get(levelIndex).getGameSpeed() * 0.9));
			timer.setDelay(levels.get(levelIndex).getGameSpeed());
			System.out.println(levels.get(levelIndex).getGameSpeed() + " " + levels.get(levelIndex).getLevel());
			levels.get(levelIndex).setForageNumbers(levels.get(levelIndex).getForageNumbers() - 1);
			if (levels.get(levelIndex).getForageNumbers() == 0) {
				gameStatus = "NEXT_LEVEL";
				levelIndex++;
				timer.stop();
				prepareGame();
				timer.setDelay(levels.get(levelIndex).getGameSpeed());

			}
		}

		if ((!isInside())) {
			timer.stop();
			gameStatus = "GAME_OVER";
			restartButton.setVisible(true);
		}
		if (ishitBarrier()) {
			timer.stop();
			gameStatus = "GAME_OVER";
			restartButton.setVisible(true);
		}
		repaint();
	}

}
