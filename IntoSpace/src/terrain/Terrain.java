package terrain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class Terrain {

	private static final float	SIZE			= 800;
	private static final float	MAX_HEIGHT		= 40;
	private static final float	MAX_PIXEL_COLOR	= 256 * 256 * 256;
	private static final String	DEFAULT_DIR		= "res/terrain/";

	private float				x;
	private float				z;
	private RawModel			model;
	private TerrainTexturePack	texturePack;
	private TerrainTexture		blendMap;

	private float[][]			heights;

	public Terrain(int gridX, int gridZ, Loader loader,
			TerrainTexturePack texturePack, TerrainTexture blendMap,
			String heightMap) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.model = generateTerrain(loader, heightMap);
	}
	
	public Terrain(int gridX, int gridZ, TerrainTexturePack pack,
			TerrainTexture blendMap, String heightMap) {
		this(gridX, gridZ, DisplayManager.getLoader(), pack, blendMap,
				heightMap);
	}

	public Terrain(int gridX, int gridZ, TerrainTexturePack pack, String blendMap, String heightMap) {
		this(gridX, gridZ, pack, new TerrainTexture(blendMap), heightMap);
	}
	
	public Terrain(int gridX, int gridZ, String pack, String blendMap,
			String heightMap) {
		this(gridX, gridZ, new TerrainTexturePack(pack), new TerrainTexture(blendMap), heightMap);
	}

	public Terrain(int gridX, int gridZ, String mapDir) {
		this(gridX, gridZ, mapDir + "pack/", mapDir + "map/blendMap",
				mapDir + "map/heightMap");
	}

	public Terrain(int gridX, int gridZ) {
		this(gridX, gridZ, DEFAULT_DIR);
	}

	public Terrain() {
		this(0,0);
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public float getSize() {
		return SIZE;
	}

	public RawModel getModel() {
		return model;
	}

	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}

	public TerrainTexture getBlendMap() {
		return blendMap;
	}

	private RawModel generateTerrain(Loader loader, String heightMap) {

		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(heightMap + ".png"));
		} catch (IOException e) {
			System.err.println(
					"Could not load heightmap: " + heightMap + ".png");
			e.printStackTrace();
		}

		int VERTEX_COUNT = image.getHeight();
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];

		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int vertexPointer = 0;
		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {
				vertices[vertexPointer * 3] = (float) j
						/ ((float) VERTEX_COUNT - 1) * SIZE;
				float height = getHeightOfImage(j, i, image);
				heights[j][i] = height;
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = (float) i
						/ ((float) VERTEX_COUNT - 1) * SIZE;
				Vector3f normal = calculateNormal(j, i, image);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = (float) j
						/ ((float) VERTEX_COUNT - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i
						/ ((float) VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
			for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}

	public float getHightOfTerrain(float worldX, float worldZ) {
		float terrainX = worldX - this.getX();
		float terrainZ = worldZ - this.getZ();
		float gridSquareSize = SIZE / ((float) heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

		if (gridX >= heights.length - 1 || gridZ >= heights.length || gridX < 0
				|| gridZ < 0) {
			return 0;
		}
		float xCoords = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoords = (terrainZ % gridSquareSize) / gridSquareSize;
		if (xCoords <= 1 - zCoords) {
			float h1 = getHeight(gridX, gridZ);
			float h2 = getHeight(gridX + 1, gridZ);
			float h3 = getHeight(gridX, gridZ + 1);
			if (zCoords != 1) {
				float xM = xCoords / (1 - zCoords);
				float hM = h1 + (h2 - h1) * xM;
				float hG = hM + (h3 - hM) * zCoords;
				return hG;
			} else {
				return h3;
			}
		} else {
			float h2 = getHeight(gridX + 1, gridZ);
			float h3 = getHeight(gridX, gridZ + 1);
			float h4 = getHeight(gridX + 1, gridZ + 1);
			if (zCoords != 0) {
				float xM = 1 - ((1 - xCoords) / zCoords);
				float hM = h3 + (h4 - h3) * xM;
				float hG = h2 + (hM - h2) * zCoords;
				return hG;
			} else {
				return h2;
			}
		}
	}

	private float getHeightOfImage(int x, int z, BufferedImage image) {
		if (x < 0 || x >= image.getHeight() || z < 0
				|| z >= image.getHeight()) {
			return 0;
		}
		float height = image.getRGB(x, z);
		height += MAX_PIXEL_COLOR / 2f;
		height /= MAX_PIXEL_COLOR / 2f;
		height *= MAX_HEIGHT;
		return height;
	}

	private float getHeight(int x, int z) {
		return heights[x][z];
	}

	private Vector3f calculateNormal(int x, int z, BufferedImage image) {
		float heightL = getHeightOfImage(x - 1, z, image);
		float heightR = getHeightOfImage(x + 1, z, image);
		float heightD = getHeightOfImage(x, z - 1, image);
		float heightU = getHeightOfImage(x, z + 1, image);

		Vector3f normal = new Vector3f(heightL - heightR, 2f,
				heightD - heightU);
		normal.normalise();
		return normal;
	}

	public Vector3f getEdge00(Vector3f worldPos) {
		float gridSquareSize = SIZE / ((float) heights.length - 1);
		float terrainX = worldPos.x - this.getX();
		float terrainZ = worldPos.z - this.getZ();
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		float gridPosX = gridX * gridSquareSize;
		float gridPosZ = gridZ * gridSquareSize;
		float heightAtPos = getHeight(gridX, gridZ);
		return new Vector3f(gridPosX, heightAtPos, gridPosZ);
	}

	public Vector3f getEdge01(Vector3f worldPos) {
		float gridSquareSize = SIZE / ((float) heights.length - 1);
		float terrainX = worldPos.x - this.getX();
		float terrainZ = worldPos.z - this.getZ();
		int gridX = (int) Math.floor(terrainX / gridSquareSize) + 1;
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		float gridPosX = gridX * gridSquareSize;
		float gridPosZ = gridZ * gridSquareSize;
		float heightAtPos = getHeight(gridX, gridZ);
		return new Vector3f(gridPosX, heightAtPos, gridPosZ);
	}

	public Vector3f getEdge10(Vector3f worldPos) {
		float gridSquareSize = SIZE / ((float) heights.length - 1);
		float terrainX = worldPos.x - this.getX();
		float terrainZ = worldPos.z - this.getZ();
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize) + 1;
		float gridPosX = gridX * gridSquareSize;
		float gridPosZ = gridZ * gridSquareSize;
		float heightAtPos = getHeight(gridX, gridZ);
		return new Vector3f(gridPosX, heightAtPos, gridPosZ);
	}

	public Vector3f getEdge11(Vector3f worldPos) {
		float gridSquareSize = SIZE / ((float) heights.length - 1);
		float terrainX = worldPos.x - this.getX();
		float terrainZ = worldPos.z - this.getZ();
		int gridX = (int) Math.floor(terrainX / gridSquareSize) + 1;
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize) + 1;
		float gridPosX = gridX * gridSquareSize;
		float gridPosZ = gridZ * gridSquareSize;
		float heightAtPos = getHeight(gridX, gridZ);
		return new Vector3f(gridPosX, heightAtPos, gridPosZ);
	}

}
