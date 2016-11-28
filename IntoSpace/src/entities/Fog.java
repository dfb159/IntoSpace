package entities;

import org.lwjgl.util.vector.Vector3f;

public class Fog {

	private float	red;
	private float	green;
	private float	blue;
	private float	density;
	private float	gradient;

	public Fog(float red, float green, float blue, float density,
			float gradient) {
		super();
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.density = density;
		this.gradient = gradient;
	}
	
	public Vector3f getColor() {
		return new Vector3f(red, green, blue);
	}

	public float getRed() {
		return red;
	}

	public void setRed(float red) {
		this.red = red;
	}

	public float getGreen() {
		return green;
	}

	public void setGreen(float green) {
		this.green = green;
	}

	public float getBlue() {
		return blue;
	}

	public void setBlue(float blue) {
		this.blue = blue;
	}

	public float getDensity() {
		return density;
	}

	public void setDensity(float density) {
		this.density = density;
	}

	public float getGradient() {
		return gradient;
	}

	public void setGradient(float gradient) {
		this.gradient = gradient;
	}

}
