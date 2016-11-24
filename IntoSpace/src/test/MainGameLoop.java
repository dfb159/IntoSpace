package test;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.OBJLoader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);
		Camera camera = new Camera();
		Light light = new Light(new Vector3f(20, 20, -25), new Vector3f(1, 0.7f, 0.7f));
		
		RawModel model3 = OBJLoader.loadObjModel("dragon", loader);
		ModelTexture texture3 = new ModelTexture(loader.loadTexture("stall"));
		texture3.setShineDamper(15);
		texture3.setReflectivity(1);
		TexturedModel staticModel3 = new TexturedModel(model3, texture3);
		Entity entity3 = new Entity(staticModel3, new Vector3f(0, 0, -5), 0, 0, 0, 0.1f);
		
		while(!Display.isCloseRequested()) {
			{//gamelogic
				entity3.increaseRotation(0, 0.5f, 0);
				
				camera.move();
			}
			{//render
				renderer.prepare();
				shader.start();
				shader.loadLight(light);
				shader.loadViewMatrix(camera);
				
				renderer.render(entity3, shader);
				
				shader.stop();
			}
			DisplayManager.updateDisplay();
		}
		
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
