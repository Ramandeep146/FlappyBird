package zProject_FlappyBird;

import java.util.Random;
import java.awt.*;

public class Obstacle {
	
	private int screenW,screenH;
	
	private int topx;
	private int topy;
	private int topHeight;
	
	private int bottomx;
	private int bottomy;
	private int bottomHeight;
	
	private static int pipeWidth;
	private static int gap;
	
	private int vel;
	
	Random random;
	
	Obstacle(int screenW, int screenH, int topx, int topy){
		
		this.screenW = screenW;
		this.screenH = screenH;
		this.topx = topx;
		this.topy = topy;
		
		random = new Random();
		
		firstInitialize();
	}
	
	private void firstInitialize() {
		vel = 2;
		pipeWidth = 100;
		gap = 200;

		topHeight = random.nextInt(screenH - gap - 10) + 10;
		
		bottomx = topx;
		bottomy = gap + topHeight;
		bottomHeight = screenH - gap - topHeight;
	}

	private void reset() {
		
		
		topx = screenW;
		topy = 0;
		topHeight = random.nextInt(screenH - gap - 10) + 10;
		
		bottomx = topx;
		bottomy = gap + topHeight;
		bottomHeight = screenH - gap - topHeight;
	}
	
	public void moveObstacle() {
		topx-=vel;
		bottomx-=vel;
	}

	
	public void checkCollision(int birdx, int birdy, int imageWidth, int imageHeight) {
		
		// When pipe goes through left wall 
		if (topx+pipeWidth<0) {
			reset();
		}
		
		
		// Bird and Pipe collision cases
		if (   birdx + imageWidth > topx 
			&& birdx + imageWidth < topx + pipeWidth 
			&& birdy > 0 
			&& birdy + 6 < topHeight) {
			
			Game.running=false;
		}
		
		else if (   birdx > topx 
			&& birdx < topx + pipeWidth 
			&& birdy > 0 
			&& birdy + 6 < topHeight) {
			
			Game.running=false;
		}
		
		else if (   birdx + imageWidth > bottomx 
			&& birdx + imageWidth < bottomx + pipeWidth 
			&& birdy + imageHeight > bottomy 
			&& birdy + imageHeight < screenH) {
			
			Game.running=false;
		}
		
		else if (   birdx > bottomx 
			&& birdx < bottomx + pipeWidth 
			&& birdy + imageHeight > bottomy 
			&& birdy + imageHeight < screenH) {
			
			Game.running=false;
		} 

	}

	public void draw(Graphics g) {
		g.setColor(new Color(49,153,33));
		g.fillRect(topx,topy,pipeWidth,topHeight);
		g.fillRect(bottomx, bottomy, pipeWidth, bottomHeight);
	}

	public static int getWidth() {
		return pipeWidth;
	}


}
