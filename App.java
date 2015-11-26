import java.awt.EventQueue;

public class App {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//new MoveALine();
					new MoveABall();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
