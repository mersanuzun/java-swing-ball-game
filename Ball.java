import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Ball {
	public int x, y, width, height, moveStep;
	public Color color;
	public Ellipse2D ball;
	public Ball(int x, int y, int width, int height, int moveStep, Color color){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.moveStep = moveStep;
		this.color = color;
		ball = new Ellipse2D.Double(x,y,width,height);
	}
	
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
		ball.setFrame(x, y, width, height);
		g2.fill(ball);
	}
}
