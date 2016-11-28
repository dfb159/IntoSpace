package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class ThirdPersonCamera extends Camera {

	Player			player				= null;

	private float	distanceFromPlayer	= 50.0f;
	private float	angleAroundPlayer	= 0.0f;
	private float	minDistance			= 1.0f;
	private float	maxDistance			= 50.0f;

	public ThirdPersonCamera() {
		super();
	}

	public ThirdPersonCamera(Vector3f pos) {
		super(pos);
	}

	public ThirdPersonCamera(Player player) {
		super();
		this.player = player;
	}

	public ThirdPersonCamera(Vector3f pos, Player player) {
		super(pos);
		this.player = player;
	}

	public void move() {
		if (player != null) {
			calculateZoom();
			calculateAngle();
			calculatePitch();
			calculatePosition();
		} else {
			super.move();
		}
	}

	public void bindPlayer(Player player) {
		this.player = player;
	}

	private void calculatePosition() {
		super.setYaw(180.0f - player.getRotY() - angleAroundPlayer);
		Vector3f newPos = new Vector3f(0, 0, 0);
		newPos.y = player.getPosition().y + distanceFromPlayer
				* (float) Math.sin(Math.toRadians(getPitch()));
		float hDistance = distanceFromPlayer
				* (float) Math.cos(Math.toRadians(getPitch()));
		newPos.x = player.getPosition().x - hDistance * (float) Math
				.sin(Math.toRadians(player.getRotY() + angleAroundPlayer));
		newPos.z = player.getPosition().z - hDistance * (float) Math
				.cos(Math.toRadians(player.getRotY() + angleAroundPlayer));
		super.setPosition(newPos);
	}

	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel * distanceFromPlayer * 0.001f;
		if (distanceFromPlayer < minDistance)
			distanceFromPlayer = minDistance;
		if (distanceFromPlayer > maxDistance)
			distanceFromPlayer = maxDistance;
	}

	private void calculateAngle() {
		if (Mouse.isButtonDown(1)) {
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}

	private void calculatePitch() {
		if (Mouse.isButtonDown(1)) {
			float pitchChange = Mouse.getDY() * 0.1f;
			super.increasePitch(-pitchChange);
		}
	}
}
