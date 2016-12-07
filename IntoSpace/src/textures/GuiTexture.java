package textures;

import org.lwjgl.util.vector.Vector2f;

import renderEngine.DisplayManager;

public class GuiTexture {

	private int			textureID;
	private Vector2f	pos;
	private Vector2f	size;

	public GuiTexture(int textureID, Vector2f pos, Vector2f size) {
		this.textureID = textureID;
		this.pos = pos;
		this.size = size;
	}
	
	public GuiTexture(String fileName, Vector2f pos, Vector2f size) {
		this(DisplayManager.getLoader().loadTexture(fileName), pos, size);
	}

	public int getTextureID() {
		return textureID;
	}

	public Vector2f getPos() {
		return pos;
	}

	public Vector2f getSize() {
		return size;
	}

}
