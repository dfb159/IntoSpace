package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class ThirdPersonCamera extends Camera {

	Player						player				= null;

	private static final float	DEFAULT_DISTANCE	= 10.0f;
	private static final float	DEFAULT_PITCH		= 20.0f;

	private float				distanceFromPlayer	= 10.0f;
	private float				angleAroundPlayer	= 0.0f;
	private float				minDistance			= 1.0f;
	private float				maxDistance			= 50.0f;
	private Vector3f			posOffset			= new Vector3f(0, 0, 0);

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

	public void setDistance(float distance) {
		this.distanceFromPlayer = distance;
	}

	public float getDistance() {
		return distanceFromPlayer;
	}

	public void setAngle(float angle) {
		this.angleAroundPlayer = angle;
	}

	public float getAngle() {
		return angleAroundPlayer;
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
		super.setPitch(20);
	}

	public void setOffset(Vector3f offset) {
		this.posOffset = offset;
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
		Vector3f.add(newPos, posOffset, newPos);
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

	public void resetRelativePosition() {
		super.setPitch(DEFAULT_PITCH);
		this.setDistance(DEFAULT_DISTANCE);
		this.setAngle(0);
	}
}
