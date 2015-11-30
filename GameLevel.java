import java.util.ArrayList;

public class GameLevel {
	private int level;
	private int forageNumbers;
	private int barrierNumbers;
	private int gameSpeed = 10;
	public GameLevel(int level, int forageNumbers, int barrierNumbers){
		this.level = level;
		this.forageNumbers = forageNumbers;
		this.barrierNumbers = barrierNumbers;
	}
	public int getForageNumbers() {
		return forageNumbers;
	}
	public void setForageNumbers(int forageNumbers) {
		this.forageNumbers = forageNumbers;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getBarrierNumbers() {
		return barrierNumbers;
	}
	public void setBurrierNumbers(int barrierNumbers) {
		this.barrierNumbers = barrierNumbers;
	}
	public int getGameSpeed() {
		return gameSpeed;
	}
	public void setGameSpeed(int gameSpeed) {
		this.gameSpeed = gameSpeed;
	}
}
