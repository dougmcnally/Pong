package pong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

@SuppressWarnings("serial")
public class Paddle extends Rectangle {
	
	public static final Color COLOR = Color.WHITE;
	public static final int MOVE_AMT = 20;
	private final int ORIG_X, ORIG_Y;
	
	public Paddle(int x, int y, int width, int height) {
		super(x, y, width, height);
		ORIG_X = x;
		ORIG_Y = y;
	}
	
	public void draw(Graphics g) {
		g.setColor(COLOR);
		g.fillRect(x, y, width, height);
	}
	
	public void move(boolean up, int windowHeight) {
		if (up && y > 0) {
			y -= MOVE_AMT;
			if (y < 0) {
				y = 0;
			}
		}
		else if (!up && y + height < windowHeight) {
			y += MOVE_AMT;
			if (y + height > windowHeight) {
				y = windowHeight - height;
			}
		}
	}
	
	public void reset() {
		this.x = ORIG_X;
		this.y = ORIG_Y;
	}
}