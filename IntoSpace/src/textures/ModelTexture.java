package textures;

import renderEngine.DisplayManager;

public class ModelTexture {

	private int		textureID;
	private float	shineDamper			= 1;
	private float	reflectivity		= 0;

	private boolean	hasTransparency		= false;
	private boolean	useFakeLighting		= false;

	private int		numberOfAtlasRows	= 1;

	public ModelTexture(int id) {
		this.textureID = id;
	}

	public ModelTexture(int id, int atlasRows) {
		this(id);
		setNumberOfAtlasRows(atlasRows);
	}

	public ModelTexture(int id, float damper, float reflectivity) {
		this(id);
		setShineDamper(damper);
		setReflectivity(reflectivity);
	}

	public ModelTexture(int id, float damper, float reflectivity,
			int atlasRows) {
		this(id);
		setShineDamper(damper);
		setReflectivity(reflectivity);
		setNumberOfAtlasRows(atlasRows);
	}

	public ModelTexture(String fileName) {
		this(DisplayManager.getLoader().loadTexture(fileName));
	}

	public ModelTexture(String fileName, int atlasRows) {
		this(fileName);
		setNumberOfAtlasRows(atlasRows);
	}

	public ModelTexture(String fileName, float damper, float reflectivity) {
		this(fileName);
		setShineDamper(damper);
		setReflectivity(reflectivity);
	}

	public ModelTexture(String fileName, float damper, float reflectivity,
			int atlasRows) {
		this(fileName);
		setShineDamper(damper);
		setReflectivity(reflectivity);
		setNumberOfAtlasRows(atlasRows);
	}

	public int getID() {
		return this.textureID;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	public boolean isHasTransparency() {
		return hasTransparency;
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	public int getNumberOfAtlasRows() {
		return numberOfAtlasRows;
	}

	public void setNumberOfAtlasRows(int numberOfAtlasRows) {
		this.numberOfAtlasRows = numberOfAtlasRows;
	}

}
