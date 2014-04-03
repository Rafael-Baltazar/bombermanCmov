/**
 * 
 */
package com.example.bombermancmov;

import com.example.bombermancmov.model.Level;
import com.example.bombermancmov.model.Character;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author impaler
 * This is the main surface that handles the ontouch events and draws
 * the image to the screen.
 */
public class MainGamePanel extends SurfaceView implements
		SurfaceHolder.Callback {

	private static final String TAG = MainGamePanel.class.getSimpleName();
	
	private MainThread thread;
	private Level level;
	private Character player;
	

	public MainGamePanel(Context context) {
		super(context);
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		// create level
		level = new Level(this);
		
		//create player
		player = new Character(1.0f, 1.0f, 0.25f, level.getGrid(), this);
		
		
		// create the game loop thread
		thread = new MainThread(getHolder(), this);
		
		// make the GamePanel focusable so it can handle events
		setFocusable(true);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		player.scale(width, height);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// at this point the surface is created and
		// we can safely start the game loop
		thread.setRunning(true);
		thread.start();
		int newWidth = this.getWidth() / level.getGrid().getRowSize();
		int newHeigth = this.getHeight() / level.getGrid().getCollSize();
		player.scale(newWidth, newHeigth);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
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
		
		switch(keyaction) {
		case KeyEvent.KEYCODE_W:
		case KeyEvent.KEYCODE_DPAD_UP:
			if(player.moveUp())
				Log.d("KEY_DOWN", "Moved Up");
			else Log.d("KEY_DOWN", "Collided moving up");
			break;
		case KeyEvent.KEYCODE_S:
		case KeyEvent.KEYCODE_DPAD_DOWN:
			if(player.moveDown())
				Log.d("KEY_DOWN", "Moved Down");
			else Log.d("KEY_DOWN", "Collided moving down");
			break;
		case KeyEvent.KEYCODE_A:
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if(player.moveLeft())
				Log.d("KEY_DOWN", "Moved Left");
			else Log.d("KEY_DOWN", "Collided moving left");
			break;
		case KeyEvent.KEYCODE_D:
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if(player.moveRight())
				Log.d("KEY_DOWN", "Moved Right");
			else Log.d("KEY_DOWN", "Collided moving right");
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// fills the canvas with black
		level.draw(canvas);
		player.draw(canvas);
	}

}
