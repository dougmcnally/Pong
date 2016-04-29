package pong;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Pong extends JPanel implements ActionListener, KeyListener {
	
	private static final int HEIGHT = 800, WIDTH = 800;
	private static final int BALL_SIZE = 20;
	private static final int PADDLE_HEIGHT = 90, PADDLE_WIDTH = 25;
	private static final int MAX_SCORE = 5;
	private static double ai_difficulty = 0.5; // between 0 and 1, closer to 1 is harder
	private Ball ball;
	private Paddle playerPaddle, aiPaddle;
	private int playerScore, compScore;
	private boolean roundOver;
	private Timer clock;
	
	public Pong() {		
		ball = new Ball((HEIGHT - BALL_SIZE) / 2, (WIDTH - BALL_SIZE) / 2,
				BALL_SIZE, BALL_SIZE);
		playerPaddle = new Paddle(0, (HEIGHT - PADDLE_HEIGHT) / 2,
				PADDLE_WIDTH, PADDLE_HEIGHT);
		aiPaddle = new AIPaddle(WIDTH - PADDLE_WIDTH, (HEIGHT - PADDLE_HEIGHT) / 2,
				PADDLE_WIDTH, PADDLE_HEIGHT);
		clock = new Timer(20, this);
		clock.start();
		roundOver = true;		
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Pong!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		Pong panel = new Pong();
		frame.addKeyListener(panel);
		panel.setPreferredSize(new Dimension(HEIGHT, WIDTH));
		panel.setBackground(Color.BLACK);
		Container c = frame.getContentPane();
		c.add(panel);		
		
		frame.pack();
		frame.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (!roundOver) {
			ball.move();
			if (Math.random() <= ai_difficulty) {
				((AIPaddle)aiPaddle).move(ball.getYVelocity(), ball.y, HEIGHT);
			}
		}		
		
		if (ball.y + ball.height >= HEIGHT || ball.y <= 0) {
			ball.bounce(false);
		}
		if (ball.x + ball.width >= WIDTH) {
			roundOver = true;
			playerScore++;
		}
		else if (ball.x <= 0) {
			roundOver = true;
			compScore++;
		}
		
		if (ball.x > PADDLE_WIDTH && ball.x + ball.width < WIDTH - PADDLE_WIDTH)
			ball.setCanBounce(true);
		
		if (ball.intersects(playerPaddle) || ball.intersects(aiPaddle)) {
			ball.bounce(true);
		}
				
		if (roundOver) {
			ball.reset();
			playerPaddle.reset();
			aiPaddle.reset();
		}
		repaint();		
	}
	
	private void repaint(Graphics g) {
		ball.draw(g);
		aiPaddle.draw(g);
		playerPaddle.draw(g);
		g.setFont(new Font("Arial", 1, 50));
		if (roundOver) {			
			g.drawString("Press Space to Start", 150, 50);
			if ((playerScore == 0 && compScore == 0) || 
					(playerScore >= 5 || compScore >= 5)) {
				g.setFont(new Font("Arial", 1, 20));
				g.drawString("Press d to change difficulty", 10, HEIGHT - 10);
			}			
		}
		g.setFont(new Font("Arial", 1, 20));
		g.drawString("Current Difficulty: " + Double.toString(ai_difficulty), 10, HEIGHT - 35);
		g.setFont(new Font("Arial", 1, 50));
		g.drawString(Integer.toString(playerScore), 100, HEIGHT - 100);
		g.drawString(Integer.toString(compScore), WIDTH - 125, HEIGHT - 100);
		if (playerScore >= MAX_SCORE) {
			g.drawString("Congratulations you win!", 100, HEIGHT - 200);
		}
		else if (compScore >= MAX_SCORE) {
			g.drawString("Computer wins. Try again.", 100, HEIGHT - 200);
		}		
	}
	
	private void startGame() {
		if (compScore >= MAX_SCORE || playerScore >= MAX_SCORE) {			
			playerScore = 0;
			compScore = 0;
		}
		if (roundOver) {			
			roundOver = false;
			// give the ball a random starting speed toward the player
			int posNeg = (int) (Math.random() * 2);
			if (posNeg == 0) posNeg = -1;
			ball.setV((int) -(Math.random() * 4 + 4), posNeg * (int) (Math.random() * 3 + 3));			
		}
	}
	
	private void changeAIDiff() {
		double diff = -1;
		while (diff < 0 || diff > 1) {
			String diffStr = JOptionPane.showInputDialog(this, 
					"Enter a difficulty between 0 and 1\n"
					+ "0 is easiest and 1 is hardest:");
			try {
				diff = Double.parseDouble(diffStr);
			}
			catch (NumberFormatException e) {}
			if (diff < 0 || diff > 1) {
				JOptionPane.showMessageDialog(this, "Invalid value!");
			}			
		}
		ai_difficulty = diff;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.repaint(g);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if (!roundOver) {
			if (arg0.getKeyCode() == KeyEvent.VK_UP ||
					arg0.getKeyCode() == KeyEvent.VK_W)
				playerPaddle.move(true, HEIGHT);
			else if (arg0.getKeyCode() == KeyEvent.VK_DOWN ||
					arg0.getKeyCode() == KeyEvent.VK_S) 
				playerPaddle.move(false, HEIGHT);
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_SPACE) {
			startGame();
		}
		if (arg0.getKeyCode() == KeyEvent.VK_D &&
				roundOver && ((playerScore == 0 && compScore == 0) || 
				(playerScore >= 5 || compScore >= 5))) {
			changeAIDiff();						
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}