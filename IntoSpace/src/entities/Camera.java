package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	protected Vector3f	position;
	protected float		pitch;
	protected float		yaw;
	protected float		roll;

	public float		speed	= 0.05f;
	public float		turn	= 1;

	public Camera() {
		position = new Vector3f(0, 0, 0);
	}

	public Camera(Vector3f pos) {
		position = pos;
	}

	public void move() {
		Vector3f vecSpeed = new Vector3f(0, 0, 0);
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			vecSpeed.z -= speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			vecSpeed.z += speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			vecSpeed.x -= speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			vecSpeed.x += speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			vecSpeed.y += speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
			vecSpeed.y -= speed;
		}
		movePosition(vecSpeed);

		if (Keyboard.isKeyDown(Keyboard.KEY_I)) {
			pitch -= turn;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_K)) {
			pitch += turn;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_J)) {
			yaw -= turn;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_L)) {
			yaw += turn;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_U)) {
			roll += turn;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_O)) {
			roll -= turn;
		}
	}

	public Vector3f getPosition() {
		return position;
	}

	public void increasePosition(Vector3f dPos) {
		position.x += dPos.x;
		position.y += dPos.y;
		position.z += dPos.z;
	}

	public void setPosition(Vector3f pPos) {
		position = pPos;
	}

	public float getPitch() {
		return pitch;
	}

	public void increasePitch(float dPitch) {
		pitch += dPitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public void increaseYaw(float dYaw) {
		yaw += dYaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getRoll() {
		return roll;
	}

	public void increaseRoll(float dRoll) {
		roll += dRoll;
	}

	private void movePosition(Vector3f speedRel) {
		position.x += Math.cos(Math.toRadians(yaw)) * speedRel.x
				- Math.sin(Math.toRadians(yaw)) * speedRel.z;
		position.y += Math.cos(Math.toRadians(pitch)) * speedRel.y;
		position.z += Math.sin(Math.toRadians(yaw)) * speedRel.x
				+ Math.cos(Math.toRadians(yaw)) * speedRel.z;
	}
}
