package zProject_FlappyBird;

import java.awt.event.*;

public class MyKeys extends KeyAdapter{
	

	public void keyPressed(KeyEvent e) {
	
			Game.birdVel=-20; 
			System.out.println("Pressed");
	
	}
	
}
