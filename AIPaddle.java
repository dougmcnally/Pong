package pong;

@SuppressWarnings("serial")
public class AIPaddle extends Paddle {
	private long moveCount;

	public AIPaddle(int x, int y, int width, int height) {
		super(x, y, width, height);
		moveCount = 0;
	}
	
	public void move(int v, int y, int windowHeight) {
		if (moveCount % 2 == 0) { // only allow moves every other step
			if (y < this.y || y > this.y + this.height) {
				super.move(v != 0 ? v < 0  : y < this.y, windowHeight);
			}
		}
		moveCount++;
	}
}