package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import entities.Fog;
import entities.Light;
import entities.Player;
import entities.ThirdPersonCamera;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrain.Terrain;
import textures.GuiTexture;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {

		/*
		 * 
		 * https://www.youtube.com/watch?v=PoxDDZmctnU&index=19&list=
		 * PLRIWtICgwaX0u7Rf9zkZhLoLuZVfUksDP
		 * 
		 * 
		 */

		Random random = new Random();

		DisplayManager.createDisplay();

		Terrain terrain = new Terrain();

		ThirdPersonCamera camera = new ThirdPersonCamera(
				new Vector3f(50, 1, 50));

		List<Light> lights = new ArrayList<Light>();
		lights.add(new Light(new Vector3f(500, 1000, 250),
				new Vector3f(0.8f, 0.8f, 0.8f)));
		lights.add(new Light(
				new Vector3f(400, terrain.getHightOfTerrain(400, 400) + 5, 400),
				new Vector3f(0.7f, 0, 0), new Vector3f(1, 0.01f, 0.002f)));
		Fog fog = new Fog(0.4f, 0.4f, 0.7f, 0.005f, 1.5f);

		RawModel model3 = OBJLoader.loadObjModel("res/obj/stall");
		ModelTexture texture3 = new ModelTexture("res/img/stallTexture", 10, 1);
		TexturedModel staticModel3 = new TexturedModel(model3, texture3);
		Entity entity3 = new Entity(staticModel3, new Vector3f(55, 0, 55), 0, 0,
				0, 2);

		Entity[] trees1 = new Entity[500];
		RawModel modelTree1 = OBJLoader.loadObjModel("res/obj/tree");
		ModelTexture textureTree1 = new ModelTexture("res/img/tree", 3, 0.2f);
		TexturedModel TexModelTree1 = new TexturedModel(modelTree1,
				textureTree1);
		for (int i = 0; i < trees1.length; i++) {
			float x = random.nextFloat() * terrain.getSize() + terrain.getX();
			float z = random.nextFloat() * terrain.getSize() + terrain.getZ();
			float y = terrain.getHightOfTerrain(x, z);
			trees1[i] = new Entity(TexModelTree1, new Vector3f(x, y, z), 0,
					random.nextFloat() * 360, 0, random.nextFloat() * 3 + 1);
		}

		Entity[] trees2 = new Entity[500];
		RawModel modelTree2 = OBJLoader.loadObjModel("res/obj/lowPolyTree");
		ModelTexture textureTree2 = new ModelTexture("res/img/lowPolyTree", 3,
				0.2f);
		TexturedModel TexModelTree2 = new TexturedModel(modelTree2,
				textureTree2);
		for (int i = 0; i < trees2.length; i++) {
			float x = random.nextFloat() * terrain.getSize() + terrain.getX();
			float z = random.nextFloat() * terrain.getSize() + terrain.getZ();
			float y = terrain.getHightOfTerrain(x, z);
			trees2[i] = new Entity(TexModelTree2, new Vector3f(x, y, z), 0,
					random.nextFloat() * 360, 0,
					random.nextFloat() * 0.5f + 0.2f);
		}

		Entity[] fern = new Entity[250];
		RawModel modelFern = OBJLoader.loadObjModel("res/obj/fern");
		ModelTexture textureFern = new ModelTexture("res/img/fernAtlas", 3,
				0.3f, 2);
		TexturedModel TexModelFern = new TexturedModel(modelFern, textureFern);
		for (int i = 0; i < fern.length - 100; i++) {
			float x = random.nextFloat() * terrain.getSize() + terrain.getX();
			float z = random.nextFloat() * terrain.getSize() + terrain.getZ();
			float y = terrain.getHightOfTerrain(x, z);
			fern[i] = new Entity(TexModelFern, new Vector3f(x, y, z), 0,
					random.nextFloat() * 360, 0,
					random.nextFloat() * 0.5f + 0.2f, random.nextInt(3));
		}
		for (int i = fern.length - 100; i < fern.length; i += 5) {
			float x = random.nextFloat() * (terrain.getSize() - 10)
					+ terrain.getX();
			float z = random.nextFloat() * (terrain.getSize() - 10)
					+ terrain.getZ();
			for (int j = 0; j < 5; j++) {
				float dx = random.nextFloat() * 10;
				float dz = random.nextFloat() * 10;
				float y = terrain.getHightOfTerrain(x + dx, z + dz);
				fern[i + j] = new Entity(TexModelFern,
						new Vector3f(x + dx, y, z + dz), 0,
						random.nextFloat() * 360, 0,
						random.nextFloat() * 0.5f + 0.2f, random.nextInt(3));
			}
		}

		Entity[] grass = new Entity[1000];
		RawModel modelGrass = OBJLoader.loadObjModel("res/obj/grassModel");
		ModelTexture textureGrass = new ModelTexture("res/img/diffuse", 3, 0.3f,
				4);
		textureGrass.setHasTransparency(true);
		textureGrass.setUseFakeLighting(true);
		TexturedModel TexModelGrass = new TexturedModel(modelGrass,
				textureGrass);
		for (int i = 0; i < grass.length; i += 25) {
			float x = random.nextFloat() * (terrain.getSize() - 15)
					+ terrain.getX();
			float z = random.nextFloat() * (terrain.getSize() - 15)
					+ terrain.getZ();
			for (int j = 0; j < 25; j++) {
				float dx = random.nextFloat() * 15;
				float dz = random.nextFloat() * 15;
				float y = terrain.getHightOfTerrain(x + dx, z + dz);
				grass[i + j] = new Entity(TexModelGrass,
						new Vector3f(x + dx, y, z + dz), 0,
						random.nextFloat() * 360, 0,
						random.nextFloat() * 0.3f + 0.15f, random.nextInt(8));
			}
		}

		RawModel modelPlayer = OBJLoader.loadObjModel("res/obj/person");
		ModelTexture texturePlayer = new ModelTexture("res/img/playerTexture",
				10, 0.8f);
		TexturedModel dragon = new TexturedModel(modelPlayer, texturePlayer);
		float x = random.nextFloat() * 700 + 50,
				z = random.nextFloat() * 700 + 50;
		Player player = new Player(dragon,
				new Vector3f(x, terrain.getHightOfTerrain(x, z), z), 0,
				random.nextFloat() * 360, 0, 0.25f);

		camera.bindPlayer(player);
		camera.setOffset(new Vector3f(0, 1.7f, 0));

		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture gui = new GuiTexture("res/img/socuwan",
				new Vector2f(0.25f, 0.25f), new Vector2f(0.1f, 0.1f));
		guis.add(gui);

		MasterRenderer render = DisplayManager.getRenderer();
		while (!Display.isCloseRequested()) {
			{// gamelogic
				player.move();
				player.checkPosition(terrain.getHightOfTerrain(
						player.getPosition().x, player.getPosition().z));
				camera.move();
				if (Keyboard.isKeyDown(Keyboard.KEY_0)) {
					camera.resetRelativePosition();
				}
				entity3.increaseRotation(0, 0.5f, 0);
			}

			render.processEntity(entity3);
			render.processEntity(player);
			for (Entity tree : trees1) {
				render.processEntity(tree);
			}
			for (Entity tree : trees2) {
				render.processEntity(tree);
			}
			for (Entity oneFern : fern) {
				render.processEntity(oneFern);
			}
			for (Entity oneGrass : grass) {
				render.processEntity(oneGrass);
			}
			render.processTerrain(terrain);

			render.render(lights, camera, fog);
			DisplayManager.updateDisplay();
		}
		DisplayManager.cleanUp();
		DisplayManager.closeDisplay();
	}

}
