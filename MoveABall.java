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
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.Component;
import java.awt.Dimension;

public class MoveABall extends JFrame implements ActionListener{

	public static final int CANVAS_HEIGHT = 500;
	public static final int CANVAS_WIDTH = 500;
	public static final Color CANVAS_BACKGROUND = Color.CYAN;
	public static final int BARRIER_HEIGHT = 50;
	public static final int BARRIER_WIDTH = 50;
	public static Color BALL_COLOR = Color.BLUE;
	public static Color FORAGE_COLOR = Color.GREEN;
	public static Color BARRIER_COLOR = Color.BLACK;
	private DrawCanvas canvas;
	private Ellipse2D forage;
	private int forageWidth = 80;
	private int forageHeight = 80;
	private Ellipse2D ball;
	private Rectangle2D barrier;
	private Timer timer;
	private int gameSpeed = 10;
	private int ballMoveStep = 1;
	private String direction = "R";
	private int point = 0;
	private int barrierNumbers = 5;
	private ArrayList<Rectangle2D> barriers = new ArrayList<Rectangle2D>();
	private Random rnd = new Random();
	
	/**
	 * Create the frame.
	 */
	
	public MoveABall() {
		// timer
		timer = new Timer(gameSpeed, this);
		timer.start();
		
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
		canvas = new DrawCanvas();
		canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
		mainPanel.add(btnPanel, BorderLayout.EAST);
		mainPanel.add(canvas, BorderLayout.CENTER);
		
		// creation ball, forage and barriers
		ball = new Ellipse2D.Double(CANVAS_WIDTH / 5, CANVAS_HEIGHT / 2, 25, 25);
		forage = new Ellipse2D.Double(CANVAS_WIDTH / 4, CANVAS_HEIGHT / 3, forageWidth, forageHeight);	
		System.out.println("başladı");
		for (int i = 0; i < barrierNumbers; i++){
			ArrayList<Integer> coor = findBarrierPlace();
			barriers.add(new Rectangle2D.Double(coor.get(0), coor.get(1), BARRIER_WIDTH, BARRIER_HEIGHT));
		}
		System.out.println("bitti");
		// setting window size, visibility, focusable
		setContentPane(mainPanel);
		setBounds(new Rectangle(CANVAS_WIDTH + 200, CANVAS_HEIGHT + 50));
		setVisible(true);
		requestFocus();
		
		// add key listener for controlling the ball with keys
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e){
				if (e.getKeyCode() == KeyEvent.VK_DOWN){
					direction = "D";
				}else if(e.getKeyCode() == KeyEvent.VK_UP){
					direction = "U";
				}else if (e.getKeyCode() == KeyEvent.VK_LEFT){
					direction = "L";
				}else if (e.getKeyCode() == KeyEvent.VK_RIGHT){
					direction = "R";
				}
				System.out.println(isInside());
			}
		});
	    
	}
	
	public ArrayList<Integer> findBarrierPlace(){
		ArrayList<Integer> coor = new ArrayList<Integer>();
		while(true){
			int counter = 0;
			int x = rnd.nextInt(CANVAS_WIDTH - BARRIER_WIDTH) + 1;
			int y = rnd.nextInt(CANVAS_HEIGHT - BARRIER_HEIGHT) + 1;
			if (ball.intersects(x, y, ball.getWidth() , ball.getHeight() )){
				continue;
			}
			for (Rectangle2D b : barriers){
				if ((b.intersects(x, y, BARRIER_WIDTH, BARRIER_HEIGHT))){
					break;
				}
				counter++;
			}
			if (counter == barriers.size()){
				coor.add(x);
				coor.add(y);
				break;
			}
		}
		return coor;
	}
	public ArrayList<Integer> findForagePlace(){
		ArrayList<Integer> coor = new ArrayList<Integer>();
		while(true){
			int counter = 0;
			int x = rnd.nextInt(CANVAS_WIDTH - forageWidth) + 1;
			int y = rnd.nextInt(CANVAS_HEIGHT - forageHeight) + 1;
			if (ball.intersects(x, y, ball.getWidth() , ball.getHeight() )){
				continue;
			}
			for (Rectangle2D b : barriers){
				if ((b.intersects(x, y, forageWidth, forageHeight))){
					break;
				}
				counter++;
			}
			if (counter == barriers.size()){
				coor.add(x);
				coor.add(y);
				break;
			}
		}
		return coor;
	}

	public void forageRePlace(){
		ArrayList<Integer> coor = findForagePlace();
		forage.setFrame(coor.get(0), coor.get(1), forage.getWidth(), forage.getHeight());
	}
	
	public Boolean isHit(){
		if (ball.intersects(forage.getX(), forage.getY(), forage.getWidth(), forage.getHeight())){
			return true;
		}
		return false;
	}
	
	public Boolean ishitBarrier(){
		for (Rectangle2D b : barriers){
			if (ball.intersects(b)){
				return true;
			}
		}
		return false;
	}
	
	public Boolean isInside(){
		if (((ball.getX() > 0) && (ball.getX() + ball.getWidth() < CANVAS_WIDTH)) && ((ball.getY() > 0 ) && (ball.getY() + ball.getHeight() < CANVAS_HEIGHT))){
			return true;
		}
		return false;
	}
	
	class MyListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof JButton){
				if (((JButton)e.getSource()).getText().equalsIgnoreCase("Left")){
					direction = "L";
				}else if (((JButton)e.getSource()).getText().equalsIgnoreCase("right")){
					direction = "R";
				}else if (((JButton)e.getSource()).getText().equalsIgnoreCase("Up")){
					direction = "U";
				}else if (((JButton)e.getSource()).getText().equalsIgnoreCase("down")){
					direction = "D";
				}
			}
			System.out.println(isInside());
		}
	}
	private class DrawCanvas extends JPanel {
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			setBackground(CANVAS_BACKGROUND);
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(Color.blue);
			g2.drawLine(0, 0, CANVAS_WIDTH, 0);
			g2.drawLine(CANVAS_WIDTH, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
			g2.drawLine(CANVAS_WIDTH, CANVAS_HEIGHT, 0, CANVAS_HEIGHT);
			g2.drawLine(0, CANVAS_WIDTH, 0, 0);
			g2.setColor(BALL_COLOR);
			g2.fill(ball);
			g2.setColor(FORAGE_COLOR);
			g2.fill(forage);
			g2.setColor(BARRIER_COLOR);
			for (Rectangle2D b : barriers){
				g2.fill(b);
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(direction){
			case "U" : 
				ball.setFrame(ball.getX(), ball.getY() - ballMoveStep, ball.getWidth(), ball.getHeight());
				break;
			case "D" : 
				ball.setFrame(ball.getX(), ball.getY() + ballMoveStep, ball.getWidth(), ball.getHeight());
				break;
			case "L" : 
				ball.setFrame(ball.getX() - ballMoveStep, ball.getY(), ball.getWidth(), ball.getHeight());
				break;
			case "R" : 
				ball.setFrame(ball.getX() + ballMoveStep, ball.getY(), ball.getWidth(), ball.getHeight());
				break;
		}
		if (isHit()) {
			forageRePlace();
			gameSpeed *=  new Double(1.01);
			System.out.println(gameSpeed);
			point += 1;
		}

		if ((!isInside())){
			timer.stop();
			System.out.println(point);
		}
		if (ishitBarrier()){
			timer.stop();
		}
		repaint();
	}
	
}
