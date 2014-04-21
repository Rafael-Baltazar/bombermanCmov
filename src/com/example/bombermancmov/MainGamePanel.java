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
	private static final int ROUND_TIME = 700;//ms

	private GameLoopThread thread;
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

		// create the enemy droids
		/*Character droid = new Character(1.0f, 2.0f, 0.25f, level.getGrid(),
				this);
		droids = new Character[] { droid };
		DroidAI droidAI = new DroidAI(droids);*/

		// create player
		player = new Character(1.0f, 1.0f, 1.0f, level.getGrid(), this);
		
		// create the game loop thread
		thread = new GameLoopThread(surfaceHolder, this);
		
		// create env thread
		new Thread(new Runnable(){
			@Override
			public void run(){
				while(level.nextRound()){
					try {
						Thread.sleep(ROUND_TIME);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();

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
		/*for (int i = 0; i < droids.length; i++) {
			droids[i].scale();
		}*/
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being created");
		thread.setRunning(true);
		level.scale();
		player.scale();
		/*for (int i = 0; i < droids.length; i++) {
			droids[i].scale();
		}*/
		thread.start();
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
		int motionAction = event.getActionMasked();
		boolean isTouch = motionAction == MotionEvent.ACTION_DOWN;
		if (isTouch) {
			int pointerIndex = event.getActionIndex();
			float pointerX = event.getX(pointerIndex);
			float pointerY = event.getY(pointerIndex);
			Log.d(TAG, "poiX: " + pointerX + " poiY: " + pointerY);
			// Translate the origin (0,0) to the middle of the 
			// player's sprite
			// TODO: Are the values given by getX and getY absolute
			// in screen or surface?
			float bitmapWidth = level.getWallBitMap().getWidth();
			float bitmapHeight = level.getWallBitMap().getHeight();
			float playerX = player.getX() * bitmapWidth + (bitmapWidth / 2);
			float playerY = player.getY() * bitmapHeight + (bitmapHeight / 2);
			Log.d(TAG, "plaX: " + playerX + " plaY: " + playerY);
			pointerX -= playerX;
			pointerY -= playerY;
			float slope = playerY / playerX;
			// Scale the coordinates, so y is negative in the
			// lower part of the screen and positive in the
			// upper part
			pointerY *= -1;
			// Use simple linear functions (f(x) = y = x
			// and f(x) = y = -x) to divide the view in four
			// parts and then determine where to move the player
			boolean upperLeftTriangle = pointerY > slope * pointerX;
			boolean upperRightTriangle = pointerY > -slope * pointerX;
			boolean leftTriangle = upperLeftTriangle && !upperRightTriangle;
			boolean rightTriangle = upperRightTriangle && !upperLeftTriangle;
			if (upperLeftTriangle) {
				if (leftTriangle) {
					Log.d(TAG, "leftTriangle");
					player.moveLeft();
				} else {
					Log.d(TAG, "upperTriangle");
					player.moveUp();
				}
			} else {
				if (rightTriangle) {
					Log.d(TAG, "rightTriangle");
					player.moveRight();
				} else {
					Log.d(TAG, "downTriangle");
					player.moveDown();
				}
			}
		}
		return isTouch;
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
		case KeyEvent.KEYCODE_SPACE:
			level.placeBomb(player.getX(), player.getY());
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Log.d("onDraw", "Using onDraw.");
		drawGameModel(canvas);
	}

	public void drawGameModel(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		level.draw(canvas);
		player.draw(canvas);
		/*for (int i = 0; i < droids.length; i++) {
			droids[i].draw(canvas);
		}*/
	}
}
