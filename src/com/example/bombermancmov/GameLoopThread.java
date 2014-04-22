package com.example.bombermancmov;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * The Main thread which contains the game loop. The thread must have access to
 * the surface view and holder to trigger events every game tick.
 */
public class GameLoopThread extends Thread {

	private static final String TAG = GameLoopThread.class.getSimpleName();

	// Surface holder that can access the physical surface
	private SurfaceHolder surfaceHolder;

	// The actual view that handles inputs
	// and draws to the surface
	private MainGamePanel gamePanel;

	// flag to hold game state
	private boolean running;

	// duration of a frame (in milliseconds)
	private long frameDuration = 100;

	public void setRunning(boolean running) {
		this.running = running;
	}

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
		while (running) {
			long beginTime = System.currentTimeMillis();
			// draw step
			Canvas canvas = null;
			try {
				// try locking the canvas for exclusive pixel editing
				// in the surface
				canvas = this.surfaceHolder.lockCanvas();
				synchronized (surfaceHolder) {
					// update game state
					// render state to the screen
					// draws the canvas on the panel
					if (canvas != null) {
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
			long timeSpent = System.currentTimeMillis() - beginTime;
			try {
				// sleep to get constant frame rate
				if (timeSpent < frameDuration) {
					long sleepTime = frameDuration - timeSpent;
					sleep(sleepTime);
				}
			} catch (InterruptedException e) {
				Log.d(TAG, "Interrupted sleep");
			}
		}

	}
}
