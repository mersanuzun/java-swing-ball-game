import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Forage {
	public int x, y, width, height;
	public Color color;
	private Random random;
	public Forage(int x, int y, int width, int height, Color color){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
		random = new Random();
	}
	
	public void rePlace(int width, int height){
		x = random.nextInt(width) + 1;
		y = random.nextInt(height) + 1;
	}
	
	public void paint(Graphics g){
		g.setColor(color);
		g.fillRect(x, y, width, height);
	}
}
