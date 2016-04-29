package pong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

@SuppressWarnings("serial")
public class Ball extends Rectangle {

	public static final Color COLOR = Color.WHITE;
	private final int ORIG_X, ORIG_Y;
	private boolean canBounce;
	private int[] v;
	
	public Ball(int x, int y, int width, int height) {
		super(x, y, width, height);
		v = new int[2];
		v[0] = 0; // x-component of velocity
		v[1] = 0; // y-component of velocity
		ORIG_X = x;
		ORIG_Y = y;
		canBounce = true;
	}
	
	public void draw(Graphics g) {
		g.setColor(COLOR);
		g.fillOval(x, y, width, height);
	}
	
	public void reset() {
		this.x = ORIG_X;
		this.y = ORIG_Y;
		v[0] = v[1] = 0;
	}
	
	public void bounce(boolean isSide) {
		/* 
		 * Flip the x-componenet of v if the ball hits a paddle,
		 * otherwise flip the y-componenet if it hits the top/bottom
		 */
		if (isSide && canBounce) {			
			// randomly boost the speed sometimes
			if (Math.random() < 0.25) {
				int i = (int)(Math.random() * 2);
				if (v[i] < 0) v[i]--;
				else v[i]++;
			}
			v[0] = -v[0];
			canBounce = false;
		}
		else v[1] = -v[1];
	}
	
	public void setCanBounce(boolean canBounce) {
		this.canBounce = canBounce;
	}
	
	public void move() {
		this.x += v[0];
		this.y += v[1];
	}
	
	public void setV(int xVel, int yVel) {
		v[0] = xVel;
		v[1] = yVel;
	}
	
	public int getYVelocity() {
		return v[1];
	}
}
