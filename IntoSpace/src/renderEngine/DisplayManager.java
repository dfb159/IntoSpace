package renderEngine;

import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;

public class DisplayManager {

	private static final int		WIDTH		= 1800;
	private static final int		HEIGHT		= 1000;
	private static final int		FPS_CAP		= 120;
	private static long				initTime	= Sys.getTime();
	private static long				lastFrameTime;
	private static float			deltaTime;

	private static Loader			loader;
	private static Camera			camera;
	private static MasterRenderer	renderer;

	public static void createDisplay() {

		ContextAttribs attribs = new ContextAttribs(3, 2)
				.withForwardCompatible(true).withProfileCore(true);

		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("The display");
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.err.println("No window could be created");
			System.exit(-1);
		}

		loader = new Loader();
		camera = new Camera();
		renderer = new MasterRenderer(loader);
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();
		initTime = lastFrameTime;
	}

	public static void updateDisplay() {

		Display.sync(FPS_CAP);
		Display.update();
		long currentFrameTime = getCurrentTime();
		deltaTime = (currentFrameTime - lastFrameTime) / 1000.0f;
		lastFrameTime = currentFrameTime;
	}

	public static void closeDisplay() {
		Display.destroy();
	}
	
	public static void cleanUp() {
		renderer.cleanUp();
		loader.cleanUp();
	}

	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}

	public static float getTimeDelta() {
		return deltaTime;
	}

	public static long getAbsoluteTime() {
		long currentTime = getCurrentTime();
		return currentTime - initTime;
	}

	public static float getGameTime() {
		return getAbsoluteTime() / 1000.0f;
	}

	public static Loader getLoader() {
		return loader;
	}

	public static Loader loader() {
		return getLoader();
	}

	public static void setCamera(Camera cam) {
		camera = cam;
	}

	public static void cam(Camera cam) {
		setCamera(cam);
	}

	public static Camera getCamera() {
		return camera;
	}

	public static Camera cam() {
		return getCamera();
	}

	public static MasterRenderer getRenderer() {
		return renderer;
	}

	public static Matrix4f getProjectionMatrix() {
		return renderer.getProjectionMatrix();
	}
}
