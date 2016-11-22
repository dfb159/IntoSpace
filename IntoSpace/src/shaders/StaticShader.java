package shaders;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_SHADER_FILE = "src/shaders/vertexShader";
	private static final String FRAGMENT_SHADER_FILE = "src/shaders/fragmentShader";
	
	public StaticShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}
	
}
