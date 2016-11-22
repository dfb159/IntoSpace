package test;

import org.lwjgl.opengl.Display;

import renderEngine.DisplayManager;

public class MainGameLoop {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		while(!Display.isCloseRequested()) {
			//gamelogic
			//render
			DisplayManager.updateDisplay();
		}
		
		DisplayManager.closeDisplay();
	}

}
