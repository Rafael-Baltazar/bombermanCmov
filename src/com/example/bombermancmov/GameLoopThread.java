package com.example.bombermancmov;

import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * The Main thread which contains the game loop. The thread must have access to
 * the surface view and holder to trigger events every game tick.
 */
public class GameLoopThread extends Thread {
	private static final String TAG = GameLoopThread.class.getSimpleName();

	/** Surface holder that can access the physical surface. */
	private SurfaceHolder surfaceHolder;

	/** The actual view that handles inputs and draws to the surface. */
	private MainGamePanel gamePanel;

	/** Flag to hold game state */
	private boolean running;

	/** Frames per second, when game is running and resumed. */
	private static final int RESUME_FPS = 20;

	/** Frames per second, when game is paused. */
	private static final int PAUSE_FPS = 1;

	/** Maximum number of frames per second. */
	private int maxFps = RESUME_FPS;

	/** The duration of a frame in milliseconds. */
	private long frameDuration = 1000 / maxFps;

	public GameLoopThread(SurfaceHolder surfaceHolder, MainGamePanel gamePanel) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;
	}

	/**
	 * Implements the game loop.
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		long beginTime, timeDiff;
		while (running) {
			// draw step
			Canvas canvas = null;
			try {
				// try locking the canvas for exclusive pixel editing
				// in the surface
				canvas = this.surfaceHolder.lockCanvas();
				synchronized (surfaceHolder) {
					beginTime = SystemClock.uptimeMillis();
					// update game state
					// render state to the screen
					// draws the canvas on the panel
					if (canvas != null) {
						this.gamePanel.update(frameDuration);
						this.gamePanel.drawGameModel(canvas);
					}
				}
			} finally {
				// the surface is not left in an inconsistent state
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
			// To get constant frame rate, get the time spent
			// drawing and sleep for the remainder.
			timeDiff = SystemClock.uptimeMillis() - beginTime;
			try {
				// sleep to get constant frame rate
				if (timeDiff < frameDuration) {
					long sleepTime = frameDuration - timeDiff;
					sleep(sleepTime);
				}
			} catch (InterruptedException e) {
				Log.d(TAG, "Interrupted sleep");
			}
		}
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public void unPause() {
		maxFps = RESUME_FPS;
		frameDuration = calculateFrameDuration();
	}

	public void pause() {
		maxFps = PAUSE_FPS;
		frameDuration = calculateFrameDuration();
	}

	private long calculateFrameDuration() {
		return 1000 / maxFps;
	}
	
}
