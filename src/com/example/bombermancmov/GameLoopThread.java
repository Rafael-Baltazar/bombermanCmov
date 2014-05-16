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
	
	/** Flag to hold game state */
	private boolean running;

	/** Frames per second, when game is resumed. */
	private static final int RESUME_FPS = 25;

	/** Frames per second, when game is paused. */
	private static final int PAUSE_FPS = 0;

	/** Maximum number of frames per second. */
	private int maxFps = RESUME_FPS;

	/** The duration of a frame in milliseconds. */
	private long frameDuration = 1000 / maxFps;
	
	private GameActivity activity;

	public GameLoopThread(GameActivity activity) {
		super();
		this.activity = activity;
		this.surfaceHolder = this.activity.getmGamePanel().getHolder();
		this.running = true; //for shutdowns
	}

	/**
	 * Implements the game loop.
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		long beginTime, timeDiff;		
		
		while (!this.activity.getGame().isFinished() && running) {
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
					if (canvas != null && frameDuration > 0) {
						this.activity.update(frameDuration);
						this.activity.getmGamePanel().drawGameModel(canvas);
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
				this.running = false;
				Log.d(TAG, "Interrupted sleep");
			}
		}
		this.activity.update(0);
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
		if (maxFps == 0)
			return -1;
		return 1000 / maxFps;
	}	

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

}
