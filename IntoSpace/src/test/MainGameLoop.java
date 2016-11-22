package test;

import org.lwjgl.opengl.Display;

import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		StaticShader shader = new StaticShader();
		
		//OpenGL verticles
		float[] verticles = {
			0, 0, 0, //middle v0
			-0.5f, 0.5f, 0, //top left v1
			-0.5f, -0.5f, 0, //top right v2
			0.5f, -0.5f, 0, //bottom right v3
			0.5f, 0.5f, 0 //bottom left v4
		};
		
		//OpenGL texture lineup
		float[] textureCoords = {
			0.5f, 0.5f, //middle v0
			0,0, //top left v1
			0,1, //top right v2
			1,1, //bottom right v3
			1,0 //bottom left v4
		};
		
		//rectangle
		int[] rectangle = {
			0, 1, 4, //left
			0, 2, 1, //top
			0, 3, 2, //right
			0, 4, 3 //bottom
		};
		
		RawModel model = loader.loadToVAO(verticles, textureCoords, rectangle);
		ModelTexture texture = new ModelTexture(loader.loadTexture("stars"));
		TexturedModel texturedModel = new TexturedModel(model, texture);
		
		while(!Display.isCloseRequested()) {
			renderer.prepare();
			shader.start();
			//gamelogic
			//render
			renderer.render(texturedModel);
			shader.stop();
			DisplayManager.updateDisplay();
		}
		
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
