package test;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import entities.Fog;
import entities.Light;
import entities.Player;
import entities.ThirdPersonCamera;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrain.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();

		TerrainTexture backgroundTexture = new TerrainTexture(
				loader.loadTexture("img/grass"));
		TerrainTexture rTexture = new TerrainTexture(
				loader.loadTexture("img/mud"));
		TerrainTexture gTexture = new TerrainTexture(
				loader.loadTexture("img/grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(
				loader.loadTexture("img/path"));
		TerrainTexture blendMap = new TerrainTexture(
				loader.loadTexture("img/blendMap"));
		TerrainTexturePack texturePack = new TerrainTexturePack(
				backgroundTexture, rTexture, gTexture, bTexture);
		Terrain terrain = new Terrain(0, 0, loader, texturePack, blendMap);

		ThirdPersonCamera camera = new ThirdPersonCamera(new Vector3f(50, 1, 50));
		Light light = new Light(new Vector3f(55, 20, 40),
				new Vector3f(1, 0.7f, 0.7f));
		Fog fog = new Fog(0.4f, 0.4f, 0.7f, 0.005f, 1.5f);

		RawModel model3 = OBJLoader.loadObjModel("obj/stall", loader);
		ModelTexture texture3 = new ModelTexture(
				loader.loadTexture("img/stallTexture"));
		texture3.setShineDamper(10);
		texture3.setReflectivity(1);
		TexturedModel staticModel3 = new TexturedModel(model3, texture3);
		Entity entity3 = new Entity(staticModel3, new Vector3f(55, 0, 55), 0, 0,
				0, 0.5f);

		RawModel modelBunny = OBJLoader.loadObjModel("obj/bunny", loader);
		ModelTexture textureBunny = new ModelTexture(
				loader.loadTexture("img/white"));
		textureBunny.setShineDamper(18);
		textureBunny.setReflectivity(0.4f);
		TexturedModel bunny = new TexturedModel(modelBunny, textureBunny);
		Player player = new Player(bunny, new Vector3f(40, 0, 40), 0, 0, 0,
				0.1f);
		
		camera.bindPlayer(player);

		MasterRenderer renderer = new MasterRenderer();
		while (!Display.isCloseRequested()) {
			{// gamelogic
				entity3.increaseRotation(0, 0.5f, 0);
				player.move();
				camera.move();
			}

			renderer.processEntity(player);
			renderer.processEntity(entity3);
			renderer.processTerrain(terrain);

			renderer.render(light, camera, fog);
			DisplayManager.updateDisplay();
		}
		renderer.cleanUp();
		DisplayManager.closeDisplay();
	}

}
