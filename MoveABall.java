import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.Component;
import java.awt.Dimension;

public class MoveABall extends JFrame implements ActionListener{

	public static final int CANVAS_HEIGHT = 150;
	public static final int CANVAS_WIDTH = 500;
	public static final Color CANVAS_BACKGROUND = Color.CYAN;
	private DrawCanvas canvas;
	private Forage forage;
	private Ball ball;
	private Timer timer;
	private int gameSpeed = 100;
	private String direction = "R";
	/**
	 * Create the frame.
	 */
	public MoveABall() {
		timer = new Timer(gameSpeed, this);
		timer.start();
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

		JPanel mainPanel = new JPanel(new BorderLayout());
		canvas = new DrawCanvas();
		canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
		mainPanel.add(btnPanel, BorderLayout.EAST);
		mainPanel.add(canvas, BorderLayout.CENTER);
		ball = new Ball(CANVAS_WIDTH / 5, CANVAS_HEIGHT / 2, 25, 25, 10, Color.RED);
		forage = new Forage(CANVAS_WIDTH / 4, CANVAS_HEIGHT / 3, 10, 10, Color.GREEN);		
		setContentPane(mainPanel);
		setBounds(new Rectangle(CANVAS_WIDTH, CANVAS_HEIGHT + 50));
		setVisible(true);
		//setFocusable(true);
		requestFocus();
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
	
	public Boolean isHit(){
		if (ball.ball.intersects(forage.x, forage.y, forage.width, forage.height)){
			return true;
		}
		return false;
	}
	
	public Boolean isInside(){
		if (((ball.x > 0) && (ball.x < CANVAS_WIDTH)) && ((ball.y > 0 ) && (ball.y < CANVAS_HEIGHT))){
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
			ball.paint(g);
			forage.paint(g);
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(direction){
			case "U" : 
				ball.y -= ball.moveStep;
				break;
			case "D" : 
				ball.y += ball.moveStep;
				break;
			case "L" : 
				ball.x -= ball.moveStep;
				break;
			case "R" : 
				ball.x += ball.moveStep;
				break;
		}
		repaint();
		if (isHit()) {
			forage.rePlace(CANVAS_WIDTH, CANVAS_HEIGHT);
		}
	}
}
