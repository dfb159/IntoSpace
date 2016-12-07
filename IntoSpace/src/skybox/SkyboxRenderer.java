package skybox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Fog;
import models.RawModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;

public class SkyboxRenderer {

	private static final float		SIZE			= 500f;

	private static final float[]	VERTICES		= { -SIZE, SIZE, -SIZE,
			-SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE,
			SIZE, -SIZE, -SIZE, SIZE, -SIZE,

			-SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE,
			SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE,

			SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE,
			SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE,

			-SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE,
			SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE,

			-SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE,
			SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE,

			-SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE,
			-SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE };

	private static final String		DEFAULT_DIR		= "res/skybox/";
	private static final String[]	TEXTURE_FILES	= { "right", "left",
			"top", "bottom", "back", "front" };

	private RawModel				cube;
	private int						day, night;
	private SkyboxShader			shader;

	public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix,
			String skyboxDir) {
		cube = loader.loadToVAO(VERTICES, 3);
		String[] textureDay = new String[6];
		String[] textureNight = new String[6];
		for (int i = 0; i < 6; i++) {
			textureDay[i] = skyboxDir + "day/" + TEXTURE_FILES[i];
			textureNight[i] = skyboxDir + "night/" + TEXTURE_FILES[i];
		}
		day = loader.loadCubeMap(textureDay);
		night = loader.loadCubeMap(textureNight);
		shader = new SkyboxShader();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix) {
		this(loader, projectionMatrix, DEFAULT_DIR);
	}

	public SkyboxRenderer(Matrix4f projectionMatrix) {
		this(DisplayManager.getLoader(), projectionMatrix);
	}

	public SkyboxRenderer(String skyboxDir) {
		this(DisplayManager.getLoader(), DisplayManager.getProjectionMatrix(), skyboxDir);
	}

	public SkyboxRenderer() {
		this(DEFAULT_DIR);
	}

	public void render(Camera camera, Fog fog) {
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadFog(fog);
		GL30.glBindVertexArray(cube.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		bindTextures();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	private void bindTextures() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, day);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, night);
		shader.loadBlendFactor(0.6f);
	}

}
