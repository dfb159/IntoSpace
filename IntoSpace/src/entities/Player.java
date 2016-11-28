package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;

public class Player extends Entity {

	private static final float	RUN_SPEED			= 20.0f;
	private static final float	RUN_GAIN			= 50.0f;
	private static final float	RUN_STOP			= 50.0f;
	private static final float	TURN_SPEED			= 160.0f;
	private static final float	TURN_GAIN			= 400.0f;
	private static final float	TURN_STOP			= 400.0f;
	private static final float	GRAVITY				= 9.81f;
	private static final float	JUMP_POWER			= 7.0f;

	private float				currentSpeed		= 0.0f;
	private float				currentTurnSpeed	= 0.0f;
	private float				currentJumpSpeed	= 0.0f;

	private boolean				forward				= false;
	private boolean				back				= false;
	private boolean				right				= false;
	private boolean				left				= false;
	private boolean				jump				= false;
	private boolean				inJump				= false;

	public Player(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}

	public void move() {
		checkInputs();
		checkMovement();
		doMove();
		checkPosition();
	}

	private void checkInputs() {
		forward = Keyboard.isKeyDown(Keyboard.KEY_W);
		back = Keyboard.isKeyDown(Keyboard.KEY_S);
		right = Keyboard.isKeyDown(Keyboard.KEY_D);
		left = Keyboard.isKeyDown(Keyboard.KEY_A);
		jump = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
	}

	private void checkMovement() {
		float delta = DisplayManager.getTimeDelta();
		if (inJump) {
			currentJumpSpeed -= GRAVITY * delta;
		} else {
			if (forward && back) {
				;// keepSpeed
			} else if (forward) {
				currentSpeed += RUN_GAIN * delta;// gainSpeed
			} else if (back) {
				currentSpeed -= RUN_GAIN * delta;// gainBackSpeed
			} else {
				if (currentSpeed > RUN_STOP * delta) {
					currentSpeed -= RUN_STOP * delta;// looseForwardSpeed
				} else if (currentSpeed < -RUN_STOP * delta) {
					currentSpeed += RUN_STOP * delta;// looseBackSpeed
				} else {
					currentSpeed = 0.0f;// loose Rest of Speed
				}
			}

			if (right && left) {
				;// keepSpeed
			} else if (left) {
				currentTurnSpeed += TURN_GAIN * delta;// gainSpeed
			} else if (right) {
				currentTurnSpeed -= TURN_GAIN * delta;// gainBackSpeed
			} else {
				if (currentTurnSpeed > TURN_STOP * delta) {
					currentTurnSpeed -= TURN_STOP * delta;// looseForwardSpeed
				} else if (currentTurnSpeed < -TURN_STOP * delta) {
					currentTurnSpeed += TURN_STOP * delta;// looseBackSpeed
				} else {
					currentTurnSpeed = 0.0f;// loose Rest of Speed
				}
			}

			if (jump) {
				inJump = true;
				currentJumpSpeed = JUMP_POWER;
				currentTurnSpeed = 0.0f;
			}
		}

		// capMaxSpeed
		if (currentSpeed > RUN_SPEED)
			currentSpeed = RUN_SPEED;
		else if (currentSpeed < -RUN_SPEED)
			currentSpeed = -RUN_SPEED;

		// capMaxTurn
		if (currentTurnSpeed > TURN_SPEED)
			currentTurnSpeed = TURN_SPEED;
		if (currentTurnSpeed < -TURN_SPEED)
			currentTurnSpeed = -TURN_SPEED;
	}

	private void doMove() {
		float delta = DisplayManager.getTimeDelta();
		super.increaseRotation(0, currentTurnSpeed * delta, 0);
		float distance = currentSpeed * delta;
		super.increasePosition(
				(float) Math.sin(Math.toRadians(getRotY())) * distance,
				currentJumpSpeed * delta,
				(float) Math.cos(Math.toRadians(getRotY())) * distance);
	}

	private void checkPosition() {
		if (super.getPosition().y < 0.0f) {
			super.getPosition().y = 0.0f;
			inJump = false;
		}
	}

}
