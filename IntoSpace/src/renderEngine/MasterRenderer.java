package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Entity;
import entities.Fog;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrain.Terrain;

public class MasterRenderer {

	private static final float					FOV				= 70;
	private static final float					NEAR_PLANE		= 0.1f;
	private static final float					FAR_PLANE		= 1000;

	private static final float					SKY_COLOR_RED	= 0.5f;
	private static final float					SKY_COLOR_GREEN	= 0.5f;
	private static final float					SKY_COLOR_BLUE	= 0.8f;

	private Matrix4f							projectionMatrix;

	private EntityRenderer						entityRenderer;
	private StaticShader						entityShader;
	private TerrainRenderer						terrainRenderer;
	private TerrainShader						terrainShader	= new TerrainShader();

	private Map<TexturedModel, List<Entity>>	entities		= new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain>						terrains		= new ArrayList<Terrain>();

	public MasterRenderer() {
		enableCulling();
		createProjectionMatrix();
		entityShader = new StaticShader();
		entityRenderer = new EntityRenderer(entityShader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
	}

	public void render(Light light, Camera camera, Fog fog) {
		prepare();

		entityShader.start();
		entityShader.loadFog(fog);
		entityShader.loadLight(light);
		entityShader.loadViewMatrix(camera);
		entityRenderer.render(entities);
		entityShader.stop();

		terrainShader.start();
		terrainShader.loadFog(fog);
		terrainShader.loadLight(light);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();

		entities.clear();
		terrains.clear();
	}

	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public void processEntity(Entity entity) {
		TexturedModel model = entity.getModel();
		List<Entity> batch = entities.get(model);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(model, newBatch);
		}
	}

	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}

	public void cleanUp() {
		entityShader.cleanUp();
		terrainShader.cleanUp();
	}

	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(SKY_COLOR_RED, SKY_COLOR_GREEN, SKY_COLOR_BLUE, 1);
	}

	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth()
				/ (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f)))
				* aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}
}
