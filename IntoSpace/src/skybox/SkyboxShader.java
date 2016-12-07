package skybox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Fog;
import renderEngine.DisplayManager;
import shader.ShaderProgram;
import toolbox.Maths;

public class SkyboxShader extends ShaderProgram {

	private static final String	VERTEX_FILE		= "src/skybox/skyboxVertexShader";
	private static final String	FRAGMENT_FILE	= "src/skybox/skyboxFragmentShader";

	private static final float	ROTATION_SPEED	= (float) ((1.0 / 360) * Math.PI
			* 2);

	private int					location_projectionMatrix;
	private int					location_viewMatrix;
	private int					location_fogColor;
	private int					location_cubeMap1;
	private int					location_cubeMap2;
	private int					location_blendFactor;

	private float				rotation		= 0;

	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation(
				"projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_fogColor = super.getUniformLocation("fogColor");
		location_blendFactor = super.getUniformLocation("blendFactor");
		location_cubeMap1 = super.getUniformLocation("cubeMap1");
		location_cubeMap2 = super.getUniformLocation("cubeMap2");
		connectTextureUnits();
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}

	public void loadViewMatrix(Camera camera) {
		Matrix4f matrix = Maths.createViewMatrix(camera);
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		rotation = ROTATION_SPEED * DisplayManager.getGameTime();
		Matrix4f.rotate(rotation, new Vector3f(0, 1, 0), matrix, matrix);
		super.loadMatrix(location_viewMatrix, matrix);
	}

	public void loadFog(Fog fog) {
		super.loadVector(location_fogColor, fog.getColor());
	}

	public void loadBlendFactor(float blendFactor) {
		super.loadFloat(location_blendFactor, blendFactor);
	}

	public void connectTextureUnits() {
		super.loadInt(location_cubeMap1, 0);
		super.loadInt(location_cubeMap2, 1);
	}
}
