package com.example.bombermancmov;

import com.example.bombermancmov.model.Level;
import com.example.bombermancmov.model.Character;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author impaler This is the main surface that handles the ontouch events and
 *         draws the image to the screen.
 */
public class MainGamePanel extends SurfaceView implements
		SurfaceHolder.Callback {

	private static final String TAG = MainGamePanel.class.getSimpleName();

	private MainThread thread;
	private SurfaceHolder surfaceHolder = getHolder();

	/* Game model */
	private Level level;
	private Character player;

	public MainGamePanel(Context context) {
		super(context);
		// adding the callback(this) to the surface holder to intercept events
		surfaceHolder.addCallback(this);

		// create level
		level = new Level(this);

		// create player
		player = new Character(1.0f, 1.0f, 0.25f, level.getGrid(), this);

		// create the game loop thread
		thread = new MainThread(surfaceHolder, this);

		// make the GamePanel focusable so it can handle events
		setFocusable(true);
		
		Log.d(TAG, "constructor");
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d(TAG, "Surface changed");
		level.scale();
		player.scale();

		thread.run();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being created");
		thread.setRunning(true);

		level.scale();
		player.scale();

		thread.run();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		// tell the thread to shut down
		thread.setRunning(false);
		while (true) {
			try {
				thread.join();
				break;
			} catch (InterruptedException e) {
				Log.d(TAG, "Interrupted exception at surface destroyed.");
			}
		}
		Log.d(TAG, "Thread was shut down cleanly");
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		int keyaction = event.getKeyCode();

		switch (keyaction) {
		case KeyEvent.KEYCODE_W:
		case KeyEvent.KEYCODE_DPAD_UP:
			if (player.moveUp())
				Log.d("KEY_DOWN", "Moved Up");
			else
				Log.d("KEY_DOWN", "Collided moving up");
			break;
		case KeyEvent.KEYCODE_S:
		case KeyEvent.KEYCODE_DPAD_DOWN:
			if (player.moveDown())
				Log.d("KEY_DOWN", "Moved Down");
			else
				Log.d("KEY_DOWN", "Collided moving down");
			break;
		case KeyEvent.KEYCODE_A:
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if (player.moveLeft())
				Log.d("KEY_DOWN", "Moved Left");
			else
				Log.d("KEY_DOWN", "Collided moving left");
			break;
		case KeyEvent.KEYCODE_D:
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if (player.moveRight())
				Log.d("KEY_DOWN", "Moved Right");
			else
				Log.d("KEY_DOWN", "Collided moving right");
			break;
		}
		thread.run();
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Log.d("onDraw", "Using onDraw.");
		drawGameModel(canvas);
	}

	private void drawGameModel(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		level.draw(canvas);
		player.draw(canvas);
	}

}
