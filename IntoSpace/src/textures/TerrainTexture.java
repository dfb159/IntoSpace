package textures;

import renderEngine.DisplayManager;

public class TerrainTexture {

	private int textureID;

	public TerrainTexture(int textureID) {
		super();
		this.textureID = textureID;
	}

	public TerrainTexture(String fileName) {
		this(DisplayManager.getLoader().loadTexture(fileName));
	}

	public int getID() {
		return textureID;
	}
}
