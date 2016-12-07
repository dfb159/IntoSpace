package textures;

public class TerrainTexturePack {

	private static final String[]	TEXTURES	= { "backTexture", "rTexture",
			"gTexture", "bTexture" };
	private static final String		DEFAULT_DIR	= "res/terrain/pack/";

	private TerrainTexture			backgroundTexture;
	private TerrainTexture			rTexture;
	private TerrainTexture			gTexture;
	private TerrainTexture			bTexture;

	public TerrainTexturePack(TerrainTexture backgroundTexture,
			TerrainTexture rTexture, TerrainTexture gTexture,
			TerrainTexture bTexture) {
		super();
		this.backgroundTexture = backgroundTexture;
		this.rTexture = rTexture;
		this.gTexture = gTexture;
		this.bTexture = bTexture;
	}

	public TerrainTexturePack(String backFile, String rFile, String gFile,
			String bFile) {
		this(new TerrainTexture(backFile), new TerrainTexture(rFile),
				new TerrainTexture(gFile), new TerrainTexture(bFile));
	}

	public TerrainTexturePack(String dir) {
		this(dir + TEXTURES[0], dir + TEXTURES[1], dir + TEXTURES[2],
				dir + TEXTURES[3]);
	}

	public TerrainTexturePack() {
		this(DEFAULT_DIR);
	}

	public TerrainTexture getBackgroundTexture() {
		return backgroundTexture;
	}

	public TerrainTexture getrTexture() {
		return rTexture;
	}

	public TerrainTexture getgTexture() {
		return gTexture;
	}

	public TerrainTexture getbTexture() {
		return bTexture;
	}

}
