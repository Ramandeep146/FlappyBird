package zProject_FlappyBird;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.text.AttributeSet.FontAttribute;

public class Game extends Canvas implements Runnable{

	private static final long serialVersionUID = 1L;
	
	static final int screenWidth = 700;
	static final int screenHeight = 550;
	
	static final int gravity=1;
	static final int initialBirdVel = -12;
	static int birdVel;
	
	public static int birdx;
	public static int birdy;
	
	Obstacle[] pipe = new Obstacle[2];

	static BufferedImage image;
		
	static boolean running = false;
	Thread myThread;
	
	Game(){
		
		try {
			image = ImageIO.read(new File("bird.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.addKeyListener(new MyKeyAdapter());
		this.setFocusable(true);
		this.setPreferredSize(new Dimension(screenWidth,screenHeight));
		this.setBackground(new Color(40,40,40));
		
		initialize();
		
		new MyFrame(this);
		
		start();

	}
	
	private void initialize() {
		
		birdx = 200;
		birdy = screenHeight/2 - image.getHeight()/2;
		birdVel = -10;		

		pipe[0] = new Obstacle(screenWidth,screenHeight,screenWidth,0);
		pipe[1] = new Obstacle(screenWidth,screenHeight,screenWidth + screenWidth/2 + Obstacle.getWidth()/2,0);

	}

	public void start() {
		myThread = new Thread(this);
		myThread.start();
		running=true;
	}
	
	

	@Override
	public void run() {
		
		final int framesPerSecond = 60;
		final int updatesPerSecond = 60;
		
		double frameOptimal = 1000000000/framesPerSecond;
		double updateOptimal = 1000000000/updatesPerSecond;
		
		double ftime = 0, utime=0;
		
		long presentTime = System.nanoTime();
		
		while(running) {
			
			long currentTime = System.nanoTime();
			
			long netTime = currentTime - presentTime; 
			
			ftime+=netTime;
			utime+=netTime;
			
			if(utime >= updateOptimal) {
				update();
				utime = utime - updateOptimal;
			}

			if(ftime >= frameOptimal) {
				preDraw();
				ftime = ftime - frameOptimal;
			}
			
			presentTime = currentTime;
			
		}
		
		stop();

	}
	

	void stop() {
		try {
			myThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		preDraw();
	}

	public void preDraw() {
		
		BufferStrategy myBufferStrategy = this.getBufferStrategy();
		
		if(myBufferStrategy == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = myBufferStrategy.getDrawGraphics();

		if(running) {
			draw(g);
		}
		else {
			gameOver(g);
		}
		
		g.dispose();
		
		myBufferStrategy.show();
		
		Toolkit.getDefaultToolkit().sync();
	}

	private void gameOver(Graphics g) {
		
		g.setFont(new Font("Times Roman", Font.ITALIC, 50));
		FontMetrics metrics = getFontMetrics(g.getFont());
		
		g.setColor(new Color(40,40,40));
		g.fillRect(0,0,screenWidth,screenHeight);
		
		g.setColor(Color.RED);
		g.drawString("GAME OVER", screenWidth/2 - metrics.stringWidth("GAME OVER")/2, screenHeight/2);
	}

	public void draw(Graphics g) {
		
		// Drawing the background so the images does not overlap
		g.setColor(new Color(40,40,40));
		g.fillRect(0,0,screenWidth,screenHeight);

		g.drawImage(image,birdx,birdy,null);
		
		pipe[0].draw(g);
		pipe[1].draw(g);	

	}
	
	public void update(){

		postjumpBird();
		checkCollision();
		
		pipe[0].moveObstacle();		
		pipe[1].moveObstacle();
		
		pipe[0].checkCollision(birdx, birdy, image.getWidth(), image.getHeight()-6);
		pipe[1].checkCollision(birdx, birdy, image.getWidth(), image.getHeight()-6);
		
	}



	private void checkCollision() {
		
		if(birdy < 0) {
			birdy = 0;
			birdVel=0;
		}
		
		if(birdy>screenHeight-image.getHeight()+6) {
			birdy=screenHeight-image.getHeight()+6;
			birdVel=0;
		}
		
	}

	private void postjumpBird() {
		birdy = birdy+birdVel;
		
		if(birdVel<=20) {
			birdVel = birdVel+gravity;
		}		
	}
	
	public class MyKeyAdapter extends KeyAdapter{

		public void keyPressed(KeyEvent e) {
			birdVel=initialBirdVel;
		}

	}

}

